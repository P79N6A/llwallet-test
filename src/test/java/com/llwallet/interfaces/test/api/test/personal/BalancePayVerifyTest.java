package com.llwallet.interfaces.test.api.test.personal;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.personal.BalancePayVerify;
import com.llwallet.interfaces.bean.personal.BalancePwdPay;
import com.llwallet.interfaces.dao.DbUtils;
import com.llwallet.interfaces.dao.TaAcctInfo;
import com.tools.dataprovider.ExcelProvider;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpFixture;
import com.tools.http.HttpRequest;
import com.tools.utils.ApiUtils;
import com.tools.utils.GenSign;
import com.tools.utils.JavaBeanUtil;
import com.tools.utils.MyConfig;
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
 * 余额支付短信验证接口测试
 */

public class BalancePayVerifyTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "余额支付短信验证接口测试", timeOut = 60000, dataProvider = "balancePayVerify")
	public void balancePayVerify(Map<String, String> datadriven) throws Exception {

		// 清除短信发送15次缓存 BanlacepayActionKEY+{oid_partner} +{mob_bind}
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host_redis"),
				Integer.parseInt(Property.get("port_redis")));
		StringBuffer command = new StringBuffer();
		StringBuffer command1 = new StringBuffer();
		if (datadriven.get("result_code").contains("0000")) {
			DbUtils dbUtils = new DbUtils();
			dbUtils.updateTraderBusiCtl(datadriven.get("oid_partner"));
			String mob_bind = dbUtils.queryMob(datadriven.get("user_id")).getMob_bind().trim();
			command.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"BanlacepayActionKEY")
					.append(datadriven.get("oid_partner")).append(mob_bind).append("\"])");
			command1.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"")
					.append(datadriven.get("oid_partner")).append("_walletTraderBusiParam\"])");
			telnetUtil.execute(command.toString());
			telnetUtil.execute(command1.toString());
		}
		telnetUtil.disconnect();

		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();

		BalancePwdPay balancePwdPay = new BalancePwdPay();
		balancePwdPay.setOid_partner(datadriven.get("oid_partner"));
		balancePwdPay.setSign_type(datadriven.get("sign_type"));
		balancePwdPay.setUser_id(datadriven.get("user_id"));
		if ("auto".equals(datadriven.get("no_order"))) {
			balancePwdPay.setNo_order(no_order);
		} else {
			balancePwdPay.setNo_order(datadriven.get("no_order"));
		}
		if ("auto".equals(datadriven.get("dt_order"))) {
			balancePwdPay.setDt_order(dt_order);
		} else {
			balancePwdPay.setDt_order(datadriven.get("dt_order"));
		}
		balancePwdPay.setBusi_partner(datadriven.get("busi_partner"));
		balancePwdPay.setName_goods(datadriven.get("name_goods"));
		balancePwdPay.setMoney_order(datadriven.get("money_order"));
		balancePwdPay.setInfo_order(datadriven.get("info_order"));
		if (datadriven.get("pwd_pay").length() != 0) {
			balancePwdPay.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {

		}
		balancePwdPay.setNotify_url(datadriven.get("notify_url"));
		balancePwdPay.setRisk_item(datadriven.get("risk_item"));
		balancePwdPay.setShareing_data(datadriven.get("shareing_data"));
		balancePwdPay.setCol_oidpartner(datadriven.get("col_oidpartner"));
		balancePwdPay.setCol_userid(datadriven.get("col_userid"));
		balancePwdPay.setSecured_partner(datadriven.get("secured_partner"));
		if (datadriven.get("buyer_confirm_valid").length() > 0) {
			balancePwdPay.setBuyer_confirm_valid(Integer.parseInt(datadriven.get("buyer_confirm_valid")));
		}
		if (datadriven.get("seller_send_valid").length() > 0) {
			balancePwdPay.setSeller_send_valid(Integer.parseInt(datadriven.get("seller_send_valid")));
		}
		balancePwdPay
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(balancePwdPay)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(balancePwdPay);
		String balancePwdPayUrl = Property.get("llwallet.balancePwdPay.url");

		// 余额支付请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(balancePwdPayUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();

		Thread.sleep(100);
		DbUtils dbUtils = new DbUtils();
		String token = JSONObject.parseObject(hf.getResponseBody()).getString("token");
		String mob_bind = dbUtils.queryMob(datadriven.get("user_id")).getMob_bind();
		String verify_code = dbUtils.queryCdSmscd(mob_bind).getTno_smscd();

		BalancePayVerify balancePayVerify = new BalancePayVerify();
		balancePayVerify.setOid_partner(datadriven.get("oid_partner1"));
		balancePayVerify.setSign_type(datadriven.get("sign_type1"));
		balancePayVerify.setUser_id(datadriven.get("user_id1"));
		if ("auto".equals(datadriven.get("no_order1"))) {
			balancePayVerify.setNo_order(no_order);
		} else {
			balancePayVerify.setNo_order(datadriven.get("no_order1"));
		}
		if ("auto".equals(datadriven.get("token1"))) {
			balancePayVerify.setToken(token);
		} else {
			balancePayVerify.setToken(datadriven.get("token1"));
		}
		balancePayVerify.setMoney_order(datadriven.get("money_order1"));
		if ("auto".equals(datadriven.get("verify_code1"))) {
			balancePayVerify.setVerify_code(verify_code);
		} else {
			balancePayVerify.setVerify_code(datadriven.get("verify_code1"));
		}
		balancePayVerify.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(balancePayVerify)), datadriven.get("key1")));
		String reqJson1 = JSON.toJSONString(balancePayVerify);
		String balancePayVerifyUrl = Property.get("llwallet.balancePayVerify.url");

		// 余额支付短信验证请求提交
		hf.nextRequest();
		hf.setUrl(balancePayVerifyUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 余额支付短信验证请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("expect_return"));

		Reporter.log("余额支付短信验证接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@SuppressWarnings({ "unchecked" })
	@Test(description = "余额支付短信验证接口测试", timeOut = 60000, dataProvider = "provider1")
	public void baseline(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel)
			throws Exception {
		// 清除短信发送15次缓存 BanlacepayActionKEY+{oid_partner} +{mob_bind}
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host_redis"),
				Integer.parseInt(Property.get("port_redis")));
		StringBuffer command = new StringBuffer();
		StringBuffer command1 = new StringBuffer();
		if (dt.get("baseline").get("expect_return").contains("0000")) {
			DbUtils dbUtils = new DbUtils();
			dbUtils.updateTraderBusiCtl(dt.get("baseline").get("oid_partner"));
			String mob_bind = dbUtils.queryMob(dt.get("baseline").get("user_id")).getMob_bind().trim();
			command.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"BanlacepayActionKEY")
					.append(dt.get("baseline").get("oid_partner")).append(mob_bind).append("\"])");
			command1.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"")
					.append(dt.get("baseline").get("oid_partner")).append("_walletTraderBusiParam\"])");
			telnetUtil.execute(command.toString());
			telnetUtil.execute(command1.toString());
		}
		telnetUtil.disconnect();

		DbUtils dbUtils = new DbUtils();
		String oid_partner = MyConfig.getPartner(dt.get("baseline").get("oid_partner"));
		String user_id = MyConfig.getUser(dt.get("baseline").get("user_id"));
		// 付款用户支付账户支付前余额
		TaAcctInfo taAcctInfo = new TaAcctInfo();
		taAcctInfo = dbUtils.queryUserAcctInfo(user_id, oid_partner);
		String amtBalCur = taAcctInfo.getAmt_balcur();
		String amtBalAval = taAcctInfo.getAmt_balaval();
		String amtBalFrz = taAcctInfo.getAmt_balfrz();
		Reporter.log("支付前用户： " + user_id + "	总余额：" + amtBalCur + "	可用余额：" + amtBalAval + "	冻结余额：" + amtBalFrz,
				true);

		// 余额支付申请
		BalancePwdPay balancePwdPay = new BalancePwdPay();
		Object reqObj = JavaBeanUtil.convertMap(balancePwdPay.getClass(), dt.get("baseline"));
		balancePwdPay
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), dt.get("baseline").get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", balancePwdPay.getSign());
		String reqJson = JSON.toJSONString(map);
		String balancePwdPayUrl = Property.get("llwallet.balancePwdPay.url");
		String rsp = HttpRequest.httpPostWithJSON(balancePwdPayUrl, reqJson);
		String token = JSONObject.parseObject(rsp).getString("token");
		String money_order = JSONObject.parseObject(rsp).getString("money_order");
		String no_order = JSONObject.parseObject(rsp).getString("no_order");
		Thread.sleep(100);
		String mob_bind = dbUtils.queryMob(dt.get("baseline").get("user_id")).getMob_bind();
		String verify_code = dbUtils.queryCdSmscd(mob_bind).getTno_smscd();

		if ("8888".equals(JSONObject.parseObject(rsp).getString("ret_code"))) {
			// 余额支付验证
			BalancePayVerify balancePayVerify = new BalancePayVerify();
			balancePayVerify.setOid_partner(dt.get("baseline").get("oid_partner1"));
			balancePayVerify.setSign_type(dt.get("baseline").get("sign_type1"));
			balancePayVerify.setUser_id(dt.get("baseline").get("user_id1"));
			if ("get".equals(dt.get("baseline").get("no_order1"))) {
				balancePayVerify.setNo_order(no_order);
			} else {
				balancePayVerify.setNo_order(dt.get("baseline").get("no_order1"));
			}
			if ("get".equals(dt.get("baseline").get("token1"))) {
				balancePayVerify.setToken(token);
			} else {
				balancePayVerify.setToken(dt.get("baseline").get("token1"));
			}
			balancePayVerify.setMoney_order(dt.get("baseline").get("money_order1"));
			if ("get".equals(dt.get("baseline").get("verify_code1"))) {
				balancePayVerify.setVerify_code(verify_code);
			} else {
				balancePayVerify.setVerify_code(dt.get("baseline").get("verify_code1"));
			}
			balancePayVerify.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(balancePayVerify)),
					dt.get("baseline").get("key1")));
			String reqJson1 = JSON.toJSONString(balancePayVerify);
			String balancePayVerifyUrl = Property.get("llwallet.balancePayVerify.url");
			String rsp1 = HttpRequest.httpPostWithJSON(balancePayVerifyUrl, reqJson1);

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//log//balancePayVerify.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//log//balancePayVerify.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//log//balancePayVerify.txt", rsp);
			SampleFileUtils.appendLine("D://TA//log//balancePayVerify.txt", reqJson1);
			SampleFileUtils.appendLine("D://TA//log//balancePayVerify.txt", rsp1);
			SampleFileUtils.appendLine("D://TA//log//balancePayVerify.txt",
					"===============================================");
			// 余额支付短信验证请求返回检查
			assert rsp1.contains(dt.get("baseline").get("expect_return"));
			if (rsp1.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")){
				assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp1)), JSONObject.parseObject(rsp1).getString("sign"));
			}
		} else {
			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//log//balancePayVerify.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//log//balancePayVerify.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//log//balancePayVerify.txt", rsp);
			SampleFileUtils.appendLine("D://TA//log//balancePayVerify.txt",
					"===============================================");
			// 余额支付短信验证请求返回检查
			assert rsp.contains(dt.get("baseline").get("expect_return"));
			if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")){
				assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)), JSONObject.parseObject(rsp).getString("sign"));
			}
		}

		// 付款用户支付账户支付后余额
		TaAcctInfo taAcctInfo1 = new TaAcctInfo();
		taAcctInfo1 = dbUtils.queryUserAcctInfo(user_id, oid_partner);
		String amtBalCur1 = taAcctInfo1.getAmt_balcur();
		String amtBalAval1 = taAcctInfo1.getAmt_balaval();
		String amtBalFrz1 = taAcctInfo1.getAmt_balfrz();
		Reporter.log("支付后用户： " + user_id + "	总余额：" + amtBalCur1 + "	可用余额：" + amtBalAval1 + "	冻结余额：" + amtBalFrz1,
				true);
		BigDecimal amt1 = new BigDecimal(amtBalCur).subtract(new BigDecimal(amtBalCur1));
		BigDecimal amt2 = new BigDecimal(amtBalAval).subtract(new BigDecimal(amtBalAval1));
		BigDecimal amt3 = new BigDecimal(amtBalFrz).subtract(new BigDecimal(amtBalFrz1));
		Reporter.log("支付后用户： " + user_id + "	总余额减少：" + amt1 + "	可用余额减少：" + amt2 + "	冻结余额减少：" + amt3, true);

		Reporter.log("余额支付短信验证接口测试： " + dt.get("baseline").get("comment"), true);
		System.out.println("===============================================");

		// 支付单状态校验
		BigDecimal AMT = new BigDecimal(String.valueOf(money_order));
		assert "0".equals(dbUtils.queryPayBillStat(no_order, AMT.multiply(new BigDecimal(1000)).toString()));
		// 支付用户余额校验
		assert amt1.divide(new BigDecimal(1000)).toString().equals(dt.get("baseline").get("money_order"));
		assert amt2.divide(new BigDecimal(1000)).toString().equals(dt.get("baseline").get("money_order"));
	}

	@DataProvider(name = "balancePayVerify")
	public Iterator<Object[]> data4balancePayVerify() throws IOException {
		return new ExcelProvider(this, "balancePayVerify", 111);
	}

	@DataProvider(name = "provider1")
	public Iterator<Object[]> provider1() throws IOException {
		return new ExcelProvider2(this, 3,31);
	}

	@AfterClass
	public void afterClass() {

	}

}
