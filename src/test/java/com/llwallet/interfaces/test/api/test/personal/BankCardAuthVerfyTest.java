package com.llwallet.interfaces.test.api.test.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.personal.BankCardAuthVerfy;
import com.llwallet.interfaces.bean.personal.BankCardOpenAuth;
import com.llwallet.interfaces.dao.DbUtils;
import com.tools.dataprovider.ExcelProvider;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpFixture;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.JavaBeanUtil;
import com.tools.utils.MyDate;
import com.tools.utils.Property;
import com.tools.utils.SampleFileUtils;
import com.tools.utils.TelnetUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 银行卡绑卡验证接口测试
 */

public class BankCardAuthVerfyTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "银行卡绑卡验证接口测试", timeOut = 60000, dataProvider = "bankCardAuthVerfy")
	public void bankCardAuthVerfy(Map<String, String> datadriven) throws Exception {

		// 清除短信发送15次缓存 BankCardbindActionKEY+{oid_partner} +{bind_mob}
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host"), Integer.parseInt(Property.get("port")));
		StringBuffer command = new StringBuffer();
		if (datadriven.get("result_code").contains("0000")) {
			command.append("delete ").append("BankCardbindActionKEY").append(datadriven.get("oid_partner"))
					.append(datadriven.get("bind_mob"));
			telnetUtil.execute(command.toString());
		}
		telnetUtil.disconnect();

		BankCardOpenAuth bankCardOpenAuth = new BankCardOpenAuth();
		bankCardOpenAuth.setOid_partner(datadriven.get("oid_partner"));
		bankCardOpenAuth.setSign_type(datadriven.get("sign_type"));
		bankCardOpenAuth.setUser_id(datadriven.get("user_id"));
		bankCardOpenAuth.setPay_type(datadriven.get("pay_type"));
		bankCardOpenAuth.setApi_version(datadriven.get("api_version"));
		bankCardOpenAuth.setRisk_item(datadriven.get("risk_item"));
		bankCardOpenAuth.setCard_no(datadriven.get("card_no"));
		bankCardOpenAuth.setBind_mob(datadriven.get("bind_mob"));
		bankCardOpenAuth.setVali_date(datadriven.get("vali_date"));
		bankCardOpenAuth.setCvv2(datadriven.get("cvv2"));
		bankCardOpenAuth.setFlag_auth(datadriven.get("flag_auth"));
		bankCardOpenAuth
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardOpenAuth)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(bankCardOpenAuth);
		String bankCardOpenAuthUrl = Property.get("llwallet.bankCardOpenAuth.url");

		// 银行卡绑卡认证请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(bankCardOpenAuthUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 银行卡绑卡认证请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		Thread.sleep(500);
		DbUtils dbUtils = new DbUtils();
		String token = StringUtils.substringBetween(hf.getResponseBody(), "token\":\"", "\"");
		String verify_code = dbUtils.queryCdSmscd(datadriven.get("bind_mob")).getTno_smscd();

		BankCardAuthVerfy bankCardAuthVerfy = new BankCardAuthVerfy();
		bankCardAuthVerfy.setOid_partner(datadriven.get("oid_partner"));
		bankCardAuthVerfy.setSign_type(datadriven.get("sign_type"));
		bankCardAuthVerfy.setUser_id(datadriven.get("user_id"));
		bankCardAuthVerfy.setVerify_code(verify_code);
		bankCardAuthVerfy.setToken(token);
		bankCardAuthVerfy.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardAuthVerfy)), datadriven.get("key")));
		String reqJson1 = JSON.toJSONString(bankCardAuthVerfy);
		String bankCardAuthVerfyUrl = Property.get("llwallet.bankCardAuthVerfy.url");

		// 银行卡绑卡验证请求提交
		hf.nextRequest();
		hf.setUrl(bankCardAuthVerfyUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 银行卡绑卡验证请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code1"));

		Reporter.log("银行卡绑卡验证接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@SuppressWarnings({ "unchecked" })
	@Test(description = "银行卡绑卡验证接口测试", timeOut = 60000, dataProvider = "provider1")
	public void baseline(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel)
			throws Exception {

		// 银行卡绑卡申请
		BankCardOpenAuth bankCardOpenAuth = new BankCardOpenAuth();
		Object reqObj = JavaBeanUtil.convertMap(bankCardOpenAuth.getClass(), dt.get("baseline"));
		bankCardOpenAuth
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), dt.get("baseline").get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", bankCardOpenAuth.getSign());
		String reqJson = JSON.toJSONString(map);
		String bankCardOpenAuthUrl = Property.get("llwallet.bankCardOpenAuth.url");
		String rsp = HttpRequest.httpPostWithJSON(bankCardOpenAuthUrl, reqJson);
		String token = JSONObject.parseObject(rsp).getString("token");

		// 银行卡绑卡验证
		BankCardAuthVerfy bankCardAuthVerfy = new BankCardAuthVerfy();
		DbUtils dbUtils = new DbUtils();
		String verify_code = dbUtils.queryCdSmscd(dt.get("baseline").get("bind_mob")).getTno_smscd();
		dt.get("baseline").put("token", token);
		dt.get("baseline").put("verify_code", verify_code);
		Object reqObj1 = JavaBeanUtil.convertMap(bankCardAuthVerfy.getClass(), dt.get("baseline"));
		bankCardAuthVerfy
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj1)), dt.get("baseline").get("key")));
		Map<String, String> map1 = JSON.parseObject(JSON.toJSONString(reqObj1), Map.class);
		map1.put("sign", bankCardAuthVerfy.getSign());
		String reqJson1 = JSON.toJSONString(map1);
		String bankCardAuthVerfyUrl = Property.get("llwallet.bankCardAuthVerfy.url");
		String rsp1 = HttpRequest.httpPostWithJSON(bankCardAuthVerfyUrl, reqJson1);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//bankCardAuthVerfy.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//bankCardAuthVerfy.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//bankCardAuthVerfy.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//bankCardAuthVerfy.txt", reqJson1);
		SampleFileUtils.appendLine("D://TA//log//bankCardAuthVerfy.txt", rsp1);
		SampleFileUtils.appendLine("D://TA//log//bankCardAuthVerfy.txt",
				"===============================================");

		Reporter.log("银行卡绑卡验证接口测试： " + dt.get("baseline").get("comment"), true);
		System.out.println("===============================================");

		// 银行卡绑卡验证请求返回检查
		assert rsp1.contains(dt.get("baseline").get("expect_return"));

	}

	@DataProvider(name = "bankCardAuthVerfy")
	public Iterator<Object[]> data4bankCardAuthVerfy() throws IOException {
		return new ExcelProvider(this, "bankCardAuthVerfy", 311);
	}

	@DataProvider(name = "provider1")
	public Iterator<Object[]> provider1() throws IOException {
		return new ExcelProvider2(this, 3);
	}

	@AfterClass
	public void afterClass() {

	}

}
