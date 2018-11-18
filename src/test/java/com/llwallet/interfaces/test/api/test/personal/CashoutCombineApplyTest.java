package com.llwallet.interfaces.test.api.test.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.personal.CashoutCombineApply;
import com.llwallet.interfaces.dao.DbUtils;
import com.tools.dataprovider.ExcelProvider;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
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
 * 用户银行卡提现申请接口测试
 */

public class CashoutCombineApplyTest {

	DbUtils dbUtils = new DbUtils();

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "用户银行卡提现申请接口测试", timeOut = 60000, dataProvider = "cashoutCombineApply")
	public void cashoutCombineApply(Map<String, String> datadriven) throws Exception {

		// 清除支付密码3次错误缓存
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host_redis"),
				Integer.parseInt(Property.get("port_redis")));
		StringBuffer command = new StringBuffer();
		if (datadriven.get("expect_return").contains("7701")) {
			String oid_userno = dbUtils.queryOidUserNo(datadriven.get("oid_partner"), datadriven.get("user_id"))
					.getOid_userno().trim();
			String oid_acctno = dbUtils.queryAcctStat(oid_userno).getOid_acctno();
			command.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"").append(oid_acctno)
					.append("_payPwdWrongTimesCachedKey\"])");
			telnetUtil.execute(command.toString());
		}
		telnetUtil.disconnect();

		dbUtils.updateTraderBusiCtl(datadriven.get("oid_partner"));
		String balanceUpdate = datadriven.get("balanceUpdate");

		String cashoutCombineApplyUrl = Property.get("llwallet.cashoutCombineApply.url");

		try {
			CashoutCombineApply cashoutCombineApply = new CashoutCombineApply();
			cashoutCombineApply.setOid_partner(datadriven.get("oid_partner"));
			cashoutCombineApply.setSign_type(datadriven.get("sign_type"));
			cashoutCombineApply.setUser_id(datadriven.get("user_id"));
			cashoutCombineApply.setNo_order(MyConfig.getNoOrder(datadriven.get("no_order")));
			cashoutCombineApply.setDt_order(MyConfig.getDtOrder(datadriven.get("dt_order")));
			if ("".equals(datadriven.get("money_order"))) {
				cashoutCombineApply.setMoney_order(null);
			} else {
				cashoutCombineApply.setMoney_order(datadriven.get("money_order"));
			}
			cashoutCombineApply.setCard_no(datadriven.get("card_no"));
			if (datadriven.get("pwd_pay").length() != 0 && datadriven.get("pwd_pay").length() < 50) {
				cashoutCombineApply.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
			} else {
				cashoutCombineApply.setPwd_pay(datadriven.get("pwd_pay"));
			}
			if ("auto".equals(datadriven.get("no_agree"))) {
				DbUtils dbUtils = new DbUtils();
				String no_agree = dbUtils.querySigned(datadriven.get("user_id")).getAgreementno();
				cashoutCombineApply.setNo_agree(no_agree);
			} else {
				cashoutCombineApply.setNo_agree(datadriven.get("no_agree"));
			}
			cashoutCombineApply.setCity_code(datadriven.get("city_code"));
			cashoutCombineApply.setBrabank_name(datadriven.get("brabank_name"));
			cashoutCombineApply.setInfo_order(datadriven.get("info_order"));
			cashoutCombineApply.setNotify_url(datadriven.get("notify_url"));
			cashoutCombineApply.setApi_version(datadriven.get("api_version"));
			cashoutCombineApply.setPrcptcd(datadriven.get("prcptcd"));
			cashoutCombineApply.setRisk_item(datadriven.get("risk_item"));
			cashoutCombineApply.setFee_data(datadriven.get("fee_data"));
			cashoutCombineApply.setInfo_order(datadriven.get("info_order"));
			cashoutCombineApply.setPaytime_type(datadriven.get("paytime_type"));
			cashoutCombineApply.setPay_type(datadriven.get("pay_type"));
			cashoutCombineApply.setRandom_key(datadriven.get("random_key"));
			cashoutCombineApply.setSign(
					GenSign.genSign(JSON.parseObject(JSON.toJSONString(cashoutCombineApply)), datadriven.get("key")));
			String reqJson = JSON.toJSONString(cashoutCombineApply);

			if (!StringUtils.isBlank(balanceUpdate)) {
				String result[] = balanceUpdate.split(",");
				dbUtils.updateAcctBalance(result[0], result[1]);
			}

			String rsp = HttpRequest.httpPostWithJSON(cashoutCombineApplyUrl, reqJson);

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//log//cashoutCombineApply.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//log//cashoutCombineApply.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//log//cashoutCombineApply.txt", rsp);
			SampleFileUtils.appendLine("D://TA//log//cashoutCombineApply.txt",
					"===============================================");

			Reporter.log("用户银行卡提现申请接口测试： " + datadriven.get("comment"), true);
			System.out.println("===============================================");

			// 用户银行卡提现申请请求返回检查
			assert rsp.contains(datadriven.get("expect_return"));
			if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
				assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
						JSONObject.parseObject(rsp).getString("sign"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!StringUtils.isBlank(balanceUpdate)) {
				String result[] = balanceUpdate.split(",");
				dbUtils.updateAcctBalance(result[0], "1000000000");
			}
		}
	}

	@Test(description = "企业用户提现测试", timeOut = 60000, dataProvider = "cashoutCombineApply1")
	public void cashoutCombineApply1(Map<String, String> datadriven) throws Exception {

		// 清除支付密码3次错误缓存
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host_redis"),
				Integer.parseInt(Property.get("port_redis")));
		StringBuffer command = new StringBuffer();
		if (datadriven.get("expect_return").contains("7701")) {
			String oid_userno = dbUtils.queryOidUserNo(datadriven.get("oid_partner"), datadriven.get("user_id"))
					.getOid_userno().trim();
			String oid_acctno = dbUtils.queryAcctStat(oid_userno).getOid_acctno();
			command.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"").append(oid_acctno)
					.append("_payPwdWrongTimesCachedKey\"])");
			telnetUtil.execute(command.toString());
		}
		telnetUtil.disconnect();

		dbUtils.updateTraderBusiCtl(datadriven.get("oid_partner"));
		String balanceUpdate = datadriven.get("balanceUpdate");

		String cashoutCombineApplyUrl = Property.get("llwallet.cashoutCombineApply.url");

		try {
			CashoutCombineApply cashoutCombineApply = new CashoutCombineApply();
			cashoutCombineApply.setOid_partner(datadriven.get("oid_partner"));
			cashoutCombineApply.setSign_type(datadriven.get("sign_type"));
			cashoutCombineApply.setUser_id(datadriven.get("user_id"));
			cashoutCombineApply.setNo_order(MyConfig.getNoOrder(datadriven.get("no_order")));
			cashoutCombineApply.setDt_order(MyConfig.getDtOrder(datadriven.get("dt_order")));
			if ("".equals(datadriven.get("money_order"))) {
				cashoutCombineApply.setMoney_order(null);
			} else {
				cashoutCombineApply.setMoney_order(datadriven.get("money_order"));
			}
			if (datadriven.get("pwd_pay").length() != 0 && datadriven.get("pwd_pay").length() < 50) {
				cashoutCombineApply.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
			} else {
				cashoutCombineApply.setPwd_pay(datadriven.get("pwd_pay"));
			}
			cashoutCombineApply.setNotify_url(datadriven.get("notify_url"));
			cashoutCombineApply.setRisk_item(datadriven.get("risk_item"));
			cashoutCombineApply.setFee_data(datadriven.get("fee_data"));
			cashoutCombineApply.setPaytime_type(datadriven.get("paytime_type"));
			cashoutCombineApply.setPay_type(datadriven.get("pay_type"));
			cashoutCombineApply.setRandom_key(datadriven.get("random_key"));
			cashoutCombineApply.setSign(
					GenSign.genSign(JSON.parseObject(JSON.toJSONString(cashoutCombineApply)), datadriven.get("key")));
			String reqJson = JSON.toJSONString(cashoutCombineApply);

			if (!StringUtils.isBlank(balanceUpdate)) {
				String result[] = balanceUpdate.split(",");
				dbUtils.updateAcctBalance(result[0], result[1]);
			}

			String rsp = HttpRequest.httpPostWithJSON(cashoutCombineApplyUrl, reqJson);

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//log//cashoutCombineApply.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//log//cashoutCombineApply.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//log//cashoutCombineApply.txt", rsp);
			SampleFileUtils.appendLine("D://TA//log//cashoutCombineApply.txt",
					"===============================================");

			Reporter.log("用户银行卡提现申请接口测试： " + datadriven.get("comment"), true);
			System.out.println("===============================================");

			// 用户银行卡提现申请请求返回检查
			assert rsp.contains(datadriven.get("expect_return"));
			if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
				assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
						JSONObject.parseObject(rsp).getString("sign"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!StringUtils.isBlank(balanceUpdate)) {
				String result[] = balanceUpdate.split(",");
				dbUtils.updateAcctBalance(result[0], "1000000000");
			}
		}
	}

	@Test(description = "用户银行卡提现申请接口测试", timeOut = 60000, dataProvider = "provider")
	public void baseline(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel)
			throws Exception {

		// 清除支付密码3次错误缓存
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host_redis"),
				Integer.parseInt(Property.get("port_redis")));
		StringBuffer command = new StringBuffer();
		if (dt.get("baseline").get("expect_return").contains("7701")) {
			String oid_userno = dbUtils
					.queryOidUserNo(dt.get("baseline").get("oid_partner"), dt.get("baseline").get("user_id"))
					.getOid_userno().trim();
			String oid_acctno = dbUtils.queryAcctStat(oid_userno).getOid_acctno();
			command.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"").append(oid_acctno)
					.append("_payPwdWrongTimesCachedKey\"])");
			telnetUtil.execute(command.toString());
		}
		telnetUtil.disconnect();

		dbUtils.updateTraderBusiCtl(dt.get("baseline").get("oid_partner"));
		String balanceUpdate = dt.get("baseline").get("balanceUpdate");

		String cashoutCombineApplyUrl = Property.get("llwallet.cashoutCombineApply.url");

		try {
			CashoutCombineApply cashoutCombineApply = new CashoutCombineApply();
			cashoutCombineApply.setOid_partner(dt.get("baseline").get("oid_partner"));
			cashoutCombineApply.setSign_type(dt.get("baseline").get("sign_type"));
			cashoutCombineApply.setUser_id(dt.get("baseline").get("user_id"));
			cashoutCombineApply.setNo_order(MyConfig.getNoOrder(dt.get("baseline").get("no_order")));
			cashoutCombineApply.setDt_order(MyConfig.getDtOrder(dt.get("baseline").get("dt_order")));
			if ("".equals(dt.get("baseline").get("money_order"))) {
				cashoutCombineApply.setMoney_order(null);
			} else {
				cashoutCombineApply.setMoney_order(dt.get("baseline").get("money_order"));
			}
			cashoutCombineApply.setCard_no(dt.get("baseline").get("card_no"));
			if (dt.get("baseline").get("pwd_pay").length() != 0 && dt.get("baseline").get("pwd_pay").length() < 50) {
				cashoutCombineApply
						.setPwd_pay(RSAUtil.encrypt(dt.get("baseline").get("pwd_pay"), Property.get("rsa_pub_key")));
			} else {
				cashoutCombineApply.setPwd_pay(dt.get("baseline").get("pwd_pay"));
			}
			if ("auto".equals(dt.get("baseline").get("no_agree"))) {
				DbUtils dbUtils = new DbUtils();
				String no_agree = dbUtils.querySigned(dt.get("baseline").get("user_id")).getAgreementno();
				cashoutCombineApply.setNo_agree(no_agree);
			} else {
				cashoutCombineApply.setNo_agree(dt.get("baseline").get("no_agree"));
			}
			cashoutCombineApply.setCity_code(dt.get("baseline").get("city_code"));
			cashoutCombineApply.setBrabank_name(dt.get("baseline").get("brabank_name"));
			cashoutCombineApply.setInfo_order(dt.get("baseline").get("info_order"));
			cashoutCombineApply.setNotify_url(dt.get("baseline").get("notify_url"));
			cashoutCombineApply.setApi_version(dt.get("baseline").get("api_version"));
			cashoutCombineApply.setPrcptcd(dt.get("baseline").get("prcptcd"));
			cashoutCombineApply.setRisk_item(dt.get("baseline").get("risk_item"));
			cashoutCombineApply.setFee_data(dt.get("baseline").get("fee_data"));
			cashoutCombineApply.setInfo_order(dt.get("baseline").get("info_order"));
			cashoutCombineApply.setPaytime_type(dt.get("baseline").get("paytime_type"));
			cashoutCombineApply.setPay_type(dt.get("baseline").get("pay_type"));
			cashoutCombineApply.setRandom_key(dt.get("baseline").get("random_key"));
			cashoutCombineApply.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(cashoutCombineApply)),
					dt.get("baseline").get("key")));
			String reqJson = JSON.toJSONString(cashoutCombineApply);

			if (!StringUtils.isBlank(balanceUpdate)) {
				String result[] = balanceUpdate.split(",");
				dbUtils.updateAcctBalance(result[0], result[1]);
			}

			String rsp = HttpRequest.httpPostWithJSON(cashoutCombineApplyUrl, reqJson);

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//log//cashoutCombineApply.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//log//cashoutCombineApply.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//log//cashoutCombineApply.txt", rsp);
			SampleFileUtils.appendLine("D://TA//log//cashoutCombineApply.txt",
					"===============================================");

			Reporter.log("用户银行卡提现申请接口测试： " + dt.get("baseline").get("comment"), true);
			System.out.println("===============================================");

			// 用户银行卡提现申请请求返回检查
			assert rsp.contains(dt.get("baseline").get("expect_return"));
			if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
				assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
						JSONObject.parseObject(rsp).getString("sign"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!StringUtils.isBlank(balanceUpdate)) {
				String result[] = balanceUpdate.split(",");
				dbUtils.updateAcctBalance(result[0], "1000000000");
			}
		}
	}

	@DataProvider(name = "cashoutCombineApply")
	public Iterator<Object[]> data4cashoutCombineApply() throws IOException {
		return new ExcelProvider(this, "cashoutCombineApply", 1,2);
	}

	@DataProvider(name = "cashoutCombineApply1")
	public Iterator<Object[]> data4cashoutCombineApply1() throws IOException {
		return new ExcelProvider(this, "cashoutCombineApply1", 111);
	}

	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
		return new ExcelProvider2(this, 311);
	}

	@AfterClass
	public void afterClass() {

	}
}