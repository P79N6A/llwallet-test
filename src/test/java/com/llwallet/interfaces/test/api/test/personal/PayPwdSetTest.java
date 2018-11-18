package com.llwallet.interfaces.test.api.test.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.personal.BankPayPwdSet;
import com.llwallet.interfaces.bean.personal.PayPwdSet;
import com.llwallet.interfaces.bean.personal.PayPwdVerify;
import com.llwallet.interfaces.dao.DbUtils;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.JavaBeanUtil;
import com.tools.utils.MyDate;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import com.tools.utils.SampleFileUtils;
import com.tools.utils.TelnetUtil;

import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 支付密码找回接口测试
 */

public class PayPwdSetTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "支付密码找回接口测试", timeOut = 60000, dataProvider = "payPwdSet")
	public void payPwdSet(Map<String, String> datadriven) throws Exception {

		// 清除短信发送次数限制
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host_redis"),
				Integer.parseInt(Property.get("port_redis")));
		StringBuffer command = new StringBuffer();
		command.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"BankpaypwdsetKEY").append(datadriven.get("oid_partner"))
		.append(datadriven.get("bind_mob")).append("\"])");
		telnetUtil.execute(command.toString());
		telnetUtil.disconnect();
		
		PayPwdSet payPwdSet = new PayPwdSet();
		payPwdSet.setOid_partner(datadriven.get("oid_partner"));
		payPwdSet.setSign_type(datadriven.get("sign_type"));
		payPwdSet.setUser_id(datadriven.get("user_id"));
		if (datadriven.get("pwd_pay_new").length() != 0 && datadriven.get("pwd_pay_new").length() < 50) {
			payPwdSet.setPwd_pay_new(RSAUtil.encrypt(datadriven.get("pwd_pay_new"), Property.get("rsa_pub_key")));
		} else {
			payPwdSet.setPwd_pay_new(datadriven.get("pwd_pay_new"));
		}
		payPwdSet.setToken(datadriven.get("token"));
		payPwdSet.setRisk_item(datadriven.get("risk_item"));
		payPwdSet.setRandom_key(datadriven.get("random_key"));
		payPwdSet.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(payPwdSet)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(payPwdSet);
		String payPwdSetUrl = Property.get("llwallet.payPwdSet.url");
		String rsp = HttpRequest.httpPostWithJSON(payPwdSetUrl, reqJson);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//payPwdSet.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//payPwdSet.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//payPwdSet.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//payPwdSet.txt", "===============================================");

		Reporter.log("支付密码找回接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
		// 支付密码找回请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));
	}

	@SuppressWarnings("unchecked")
	@Test(description = "银行卡支付密码找回接口测试", timeOut = 60000, dataProvider = "payPwdSet1")
	public void payPwdSet1(Map<String, String> datadriven) throws Exception {

		// 清除短信发送次数限制
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host_redis"),
				Integer.parseInt(Property.get("port_redis")));
		StringBuffer command = new StringBuffer();
		command.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"BankpaypwdsetKEY").append(datadriven.get("oid_partner"))
		.append(datadriven.get("bind_mob")).append("\"])");
		telnetUtil.execute(command.toString());
		telnetUtil.disconnect();
		
		// 银行卡绑卡授权申请
		BankPayPwdSet bankPayPwdSet = new BankPayPwdSet();
		Object reqObj = JavaBeanUtil.convertMap(bankPayPwdSet.getClass(), datadriven);
		bankPayPwdSet.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), datadriven.get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", bankPayPwdSet.getSign());
		String reqJson = JSON.toJSONString(map);
		String bankPayPwdSetUrl = Property.get("llwallet.bankPayPwdSet.url");
		String rsp = HttpRequest.httpPostWithJSON(bankPayPwdSetUrl, reqJson);
		String token = JSONObject.parseObject(rsp).getString("token");
		DbUtils dbUtils = new DbUtils();
		String verify_code = dbUtils.queryCdSmscd(map.get("bind_mob")).getTno_smscd();

		// 银行卡绑卡授权验证
		PayPwdVerify payPwdVerify = new PayPwdVerify();
		payPwdVerify.setOid_partner(datadriven.get("paypwdverify.oid_partner"));
		payPwdVerify.setSign_type(datadriven.get("paypwdverify.sign_type"));
		payPwdVerify.setUser_id(datadriven.get("paypwdverify.user_id"));
		if ("get".equals(datadriven.get("paypwdverify.token"))) {
			payPwdVerify.setToken(token);
		} else {
			payPwdVerify.setToken(datadriven.get("paypwdverify.token"));
		}
		if ("get".equals(datadriven.get("paypwdverify.verify_code"))) {
			payPwdVerify.setVerify_code(verify_code);
		} else {
			payPwdVerify.setVerify_code(datadriven.get("paypwdverify.verify_code"));
		}
		payPwdVerify.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(payPwdVerify)), datadriven.get("paypwdverify.key")));
		String reqJson1 = JSON.toJSONString(payPwdVerify);
		String payPwdVerifyUrl = Property.get("llwallet.payPwdVerify.url");
		String rsp1 = HttpRequest.httpPostWithJSON(payPwdVerifyUrl, reqJson1);
		String token1 = JSONObject.parseObject(rsp1).getString("token");

		// 银行卡支付密码找回
		PayPwdSet payPwdSet = new PayPwdSet();
		payPwdSet.setOid_partner(datadriven.get("paypwdset.oid_partner"));
		payPwdSet.setSign_type(datadriven.get("paypwdset.sign_type"));
		payPwdSet.setUser_id(datadriven.get("paypwdset.user_id"));
		if (datadriven.get("paypwdset.pwd_pay_new").length() != 0
				&& datadriven.get("paypwdset.pwd_pay_new").length() < 50) {
			payPwdSet.setPwd_pay_new(
					RSAUtil.encrypt(datadriven.get("paypwdset.pwd_pay_new"), Property.get("rsa_pub_key")));
		} else {
			payPwdSet.setPwd_pay_new(datadriven.get("paypwdset.pwd_pay_new"));
		}
		if ("get".equals(datadriven.get("paypwdset.token"))) {
			payPwdSet.setToken(token1);
		} else {
			payPwdSet.setToken(datadriven.get("paypwdset.token"));
		}
		payPwdSet.setRisk_item(datadriven.get("paypwdset.risk_item"));
		payPwdSet.setRandom_key(datadriven.get("paypwdset.random_key"));
		payPwdSet.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(payPwdSet)), datadriven.get("paypwdset.key")));
		String reqJson2 = JSON.toJSONString(payPwdSet);
		String payPwdSetUrl = Property.get("llwallet.payPwdSet.url");
		String rsp2 = HttpRequest.httpPostWithJSON(payPwdSetUrl, reqJson2);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//payPwdSet.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//payPwdSet.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//payPwdSet.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//payPwdSet.txt", reqJson1);
		SampleFileUtils.appendLine("D://TA//log//payPwdSet.txt", rsp1);
		SampleFileUtils.appendLine("D://TA//log//payPwdSet.txt", reqJson2);
		SampleFileUtils.appendLine("D://TA//log//payPwdSet.txt", rsp2);
		SampleFileUtils.appendLine("D://TA//log//payPwdSet.txt", "===============================================");

		Reporter.log("银行卡支付密码找回接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// 银行卡支付密码找回请求返回检查
		assert rsp2.contains(datadriven.get("expect_return"));
	}

	@DataProvider(name = "payPwdSet")
	public Iterator<Object[]> data4payPwdSet() throws IOException {
		return new ExcelProvider(this, "payPwdSet", 11);
	}

	@DataProvider(name = "payPwdSet1")
	public Iterator<Object[]> data4payPwdSet1() throws IOException {
		return new ExcelProvider(this, "payPwdSet1", 4,6);
	}

	@AfterClass
	public void afterClass() {

	}

}
