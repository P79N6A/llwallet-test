package com.llwallet.interfaces.test.api.test.custom;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.custom.Balance2OtherPublicBankCard;
import com.llwallet.interfaces.dao.DbUtils;
import com.llwallet.interfaces.dao.TaAcctInfo;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpRequest;
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
 * 用户余额到他人对公银行卡接口测试
 */

public class Balance2OtherPublicBankCardTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings("unchecked")
	@Test(description = "余额到他人对公银行卡接口测试", timeOut = 60000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		// 清除支付密码3次错误缓存
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host_redis"),
				Integer.parseInt(Property.get("port_redis")));
		StringBuffer command = new StringBuffer();
		if (dt.get("balance2OtherPublicBankCard").get("expect_return").contains("支付密码错误")) {
			DbUtils dbUtils = new DbUtils();
			String oid_userno = dbUtils
					.queryOidAcctNo(MyConfig.getPartner(dt.get("balance2OtherPublicBankCard").get("oid_partner")),
							MyConfig.getUser(dt.get("balance2OtherPublicBankCard").get("user_id")))
					.getOid_acctno().trim();

			command.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"").append(oid_userno)
					.append("_payPwdWrongTimesCachedKey\"])");
			System.out.println("清缓存：" + command);
			telnetUtil.execute(command.toString());
		}
		telnetUtil.disconnect();
		Balance2OtherPublicBankCard balance2OtherPublicBankCard = new Balance2OtherPublicBankCard();
		Object reqObj = JavaBeanUtil.convertMap(balance2OtherPublicBankCard.getClass(),
				excel.getParamMap(dt.get("balance2OtherPublicBankCard")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		balance2OtherPublicBankCard.setSign(GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map)),
				dt.get("balance2OtherPublicBankCard").get("key")));
		map.put("sign", balance2OtherPublicBankCard.getSign());
		String reqJson = JSON.toJSONString(map);
		String balance2OtherPublicBankCardUrl = Property.get("llwallet.balance2OtherPublicBankCard.url");
		// 用户余额到他人对公银行卡请求提交
		String rsp = HttpRequest.httpPostWithJSON(balance2OtherPublicBankCardUrl, reqJson);
		// excel.writeCurrentCell("balance2OtherPublicBankCard",
		// "actual_return", rsp);
		Reporter.log("用户余额到他人对公银行卡接口测试： " + dt.get("balance2OtherPublicBankCard").get("comment"), true);
		System.out.println("===============================================");
		// 用户余额到他人对公银行卡请求返回检查
		assert rsp.contains(dt.get("balance2OtherPublicBankCard").get("expect_return"));
	}

	@SuppressWarnings("unchecked")
	@Test(description = "余额到他人对公银行卡接口测试", timeOut = 60000, dataProvider = "provider1")
	public void baseline(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel)
			throws Exception {

		// 清除支付密码3次错误缓存
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host_redis"),
				Integer.parseInt(Property.get("port_redis")));
		StringBuffer command = new StringBuffer();
		if (dt.get("baseline").get("expect_return").contains("支付密码错误")) {
			DbUtils dbUtils = new DbUtils();
			String oid_userno = dbUtils.queryOidAcctNo(MyConfig.getPartner(dt.get("baseline").get("oid_partner")),
					MyConfig.getUser(dt.get("baseline").get("user_id"))).getOid_acctno().trim();

			command.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"").append(oid_userno)
					.append("_payPwdWrongTimesCachedKey\"])");
			System.out.println("清缓存：" + command);
			telnetUtil.execute(command.toString());
		}
		telnetUtil.disconnect();

		// 余额到他人对公卡付款前用户余额
		String oid_partner = MyConfig.getPartner(dt.get("baseline").get("oid_partner"));
		String user_id = MyConfig.getUser(dt.get("baseline").get("user_id"));
		TaAcctInfo taAcctInfo = new TaAcctInfo();
		String amtBalCur, amtBalAval, amtBalFrz;
		DbUtils dbUtils = new DbUtils();
		taAcctInfo = dbUtils.queryUserAcctInfo(user_id, oid_partner);
		amtBalCur = taAcctInfo.getAmt_balcur();
		amtBalAval = taAcctInfo.getAmt_balaval();
		amtBalFrz = taAcctInfo.getAmt_balfrz();
		Reporter.log("付款前用户账户： " + user_id + "	总余额：" + amtBalCur + "	可用余额：" + amtBalAval + "	冻结余额：" + amtBalFrz,
				true);

		// 余额到他人对公卡
		Balance2OtherPublicBankCard balance2OtherPublicBankCard = new Balance2OtherPublicBankCard();
		Object reqObj = JavaBeanUtil.convertMap(balance2OtherPublicBankCard.getClass(),
				excel.getParamMap(dt.get("baseline")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		balance2OtherPublicBankCard.setSign(
				GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map)), dt.get("baseline").get("key")));
		map.put("sign", balance2OtherPublicBankCard.getSign());
		String reqJson = JSON.toJSONString(map);
		String balance2OtherPublicBankCardUrl = Property.get("llwallet.balance2OtherPublicBankCard.url");
		// 用户余额到他人对公银行卡请求提交
		String rsp = HttpRequest.httpPostWithJSON(balance2OtherPublicBankCardUrl, reqJson);
		// excel.writeCurrentCell("balance2OtherPublicBankCard",
		// "actual_return", rsp);
		String money_order = dt.get("baseline").get("money_order");
		String oid_paybill = JSONObject.parseObject(rsp).getString("oid_paybill");
		SampleFileUtils.appendLine("D://TA//log//balance2OtherPublicBankCard.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//balance2OtherPublicBankCard.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//balance2OtherPublicBankCard.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//balance2OtherPublicBankCard.txt",
				"===============================================");

		// 支付请求返回检查
		assert rsp.contains(dt.get("baseline").get("expect_return"));
		if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
					JSONObject.parseObject(rsp).getString("sign"));
		}

		// 余额到他人对公卡付款后用户余额
		TaAcctInfo taAcctInfo1 = new TaAcctInfo();
		String amtBalCur1, amtBalAval1, amtBalFrz1;
		taAcctInfo1 = dbUtils.queryUserAcctInfo(user_id, oid_partner);
		amtBalCur1 = taAcctInfo1.getAmt_balcur();
		amtBalAval1 = taAcctInfo1.getAmt_balaval();
		amtBalFrz1 = taAcctInfo1.getAmt_balfrz();
		Reporter.log("付款后用户账户： " + user_id + "	总余额：" + amtBalCur1 + "	可用余额：" + amtBalAval1 + "	冻结余额：" + amtBalFrz1,
				true);
		BigDecimal amt1 = new BigDecimal(amtBalCur).subtract(new BigDecimal(amtBalCur1));
		BigDecimal amt2 = new BigDecimal(amtBalAval).subtract(new BigDecimal(amtBalAval1));
		BigDecimal amt3 = new BigDecimal(amtBalFrz1).subtract(new BigDecimal(amtBalFrz));
		Reporter.log("付款后用户账户： " + user_id + "	总余额减少：" + amt1 + "	可用余额减少：" + amt2 + "	冻结余额增加：" + amt3, true);

		Reporter.log("用户余额到他人对公银行卡接口测试： " + dt.get("baseline").get("comment"), true);
		System.out.println("===============================================");

		if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			// 支付单状态校验
			BigDecimal AMT = new BigDecimal(String.valueOf(money_order));
			String pay_timetype = null;
			if (StringUtils.isBlank(dt.get("baseline").get("paytime_type"))) {
				pay_timetype = "1";
			} else {
				pay_timetype = dt.get("baseline").get("paytime_type");
			}
			String col_accttype = "6";// 银行帐号
			assert "6".equals(dbUtils.queryPayBillStat1(oid_paybill, AMT.multiply(new BigDecimal(1000)).toString(),
					pay_timetype, col_accttype));
			// 付款用户余额校验
			// assert amt1.divide(new BigDecimal(1000)).toString().equals("0");
			assert amt2.divide(new BigDecimal(1000)).toString().equals(money_order);
			// assert amt3.divide(new
			// BigDecimal(1000)).toString().equals(money_order);
		}
	}

	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
		return new ExcelProvider2(this, 7,9);
	}

	@DataProvider(name = "provider1")
	public Iterator<Object[]> provider1() throws IOException {
		return new ExcelProvider2(this, 318);
	}

	@AfterClass
	public void afterClass() {

	}
}