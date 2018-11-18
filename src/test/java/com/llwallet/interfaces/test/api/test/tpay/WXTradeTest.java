package com.llwallet.interfaces.test.api.test.tpay;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedHashMap;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.dao.DbUtils;
import com.llwallet.interfaces.dao.TaAcctInfo;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpRequest;
import com.tools.utils.MyConfig;
import com.tools.utils.MyDate;
import com.tools.utils.Property;
import com.tools.utils.SampleFileUtils;
import com.tools.utils.TelnetUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 消费（微信）测试
 */

public class WXTradeTest {

	DbUtils dbUtils = new DbUtils();

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "消费（微信）测试", timeOut = 60000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host_redis"),
				Integer.parseInt(Property.get("port_redis")));
		String chnlUpdate = dt.get("wXTrade").get("chnlUpdate");
		String traderStatUpdate = dt.get("wXTrade").get("traderStatUpdate");
		String userStatUpdate = dt.get("wXTrade").get("userStatUpdate");
		String userBusiStatUpdate = dt.get("wXTrade").get("userBusiStatUpdate");
		String userBusiAcctStatUpdate = dt.get("wXTrade").get("userBusiAcctStatUpdate");
		String oid_partner = MyConfig.getPartner(dt.get("wXTrade").get("oid_partner"));
		String col_partner = MyConfig.getPartner(dt.get("wXTrade").get("col_partner"));
		String oid_biz = dt.get("wXTrade").get("oid_biz");
		String user_id = MyConfig.getUser(dt.get("wXTrade").get("user_id"));
		String oid_userno = dbUtils.queryTPayAcct(user_id).getOid_userno();
		String payerAcc = dt.get("wXTrade").get("payerAcc");
		String balanceUpdate = dt.get("wXTrade").get("balanceUpdate");
		String authUpdate = dt.get("wXTrade").get("authUpdate");

		String wXTradeUrl = Property.get("tpay.wXTrade.url");

		try {
			JSONObject json = new JSONObject();
			json.put("version", dt.get("wXTrade").get("version"));
			json.put("orderId", MyConfig.getNoOrder(dt.get("wXTrade").get("orderId")));
			json.put("clearDate", MyConfig.getDtOrder(dt.get("wXTrade").get("clearDate")));
			json.put("date", MyConfig.getDtOrder(dt.get("wXTrade").get("date")));
			json.put("amount", dt.get("wXTrade").get("amount"));
			json.put("currency", dt.get("wXTrade").get("currency"));
			json.put("transType", dt.get("wXTrade").get("transType"));
			json.put("payerAcc", dt.get("wXTrade").get("payerAcc"));
			json.put("mchNo", dt.get("wXTrade").get("mchNo"));
			json.put("mchName", dt.get("wXTrade").get("mchName"));
			json.put("mchType", dt.get("wXTrade").get("mchType"));
			json.put("mchCertType", dt.get("wXTrade").get("mchCertType"));
			json.put("mchCertId", dt.get("wXTrade").get("mchCertId"));
			json.put("mchCtgyCode", dt.get("wXTrade").get("mchCtgyCode"));
			json.put("orderDesc", dt.get("wXTrade").get("orderDesc"));
			json.put("extension", dt.get("wXTrade").get("extension"));
			json.put("signData", dt.get("wXTrade").get("signData"));

			if (!StringUtils.isBlank(chnlUpdate)) {
				String result[] = chnlUpdate.split(",");
				dbUtils.updateChnlBankState(result[0], result[1], result[2]);
			}
			if (!StringUtils.isBlank(traderStatUpdate)) {
				String result1[] = traderStatUpdate.split(",");
				dbUtils.updateTraderStatus(result1[0], result1[1]);
			}
			if (!StringUtils.isBlank(userStatUpdate)) {
				dbUtils.updateUserStat(userStatUpdate, user_id);
			}
			if (!StringUtils.isBlank(userBusiStatUpdate)) {
				dbUtils.updateUserBusiStat(userBusiStatUpdate, user_id);
			}
			if (!StringUtils.isBlank(userBusiAcctStatUpdate)) {
				dbUtils.updateUserBusiAcctStat(userBusiAcctStatUpdate, payerAcc);
			}
			if (!StringUtils.isBlank(balanceUpdate)) {
				String result2[] = balanceUpdate.split(",");
				dbUtils.updateAcctBalance(result2[0], result2[1]);
			}
			if (!StringUtils.isBlank(authUpdate)) {
				dbUtils.updateMerchantAuth(authUpdate);
			}
			Thread.sleep(50);
			if ((!StringUtils.isBlank(oid_partner)) && (!StringUtils.isBlank(user_id))) {
				StringBuffer command1 = new StringBuffer();
				StringBuffer command2 = new StringBuffer();
				StringBuffer command3 = new StringBuffer();
				StringBuffer command4 = new StringBuffer();
				command1.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"pay_admin_merchantTRADER_PARA_")
						.append(oid_partner).append("\"])");
				command2.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"TRADER_").append(oid_userno)
						.append(oid_partner).append("llwalltet_userbase\"])");
				command3.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"ConsumptionThrough_PAY")
						.append(col_partner).append(payerAcc).append("\"])");
				command4.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"TPAY_RELATION_KEY")
						.append(oid_partner).append("\"])");
				telnetUtil.execute(command1.toString());
				telnetUtil.execute(command2.toString());
				telnetUtil.execute(command3.toString());
				telnetUtil.execute(command4.toString());
				telnetUtil.disconnect();
			}

			// 收款商户业务账户消费前余额
			String payeeAcctNo = dbUtils.queryTraderBusinessAcct(col_partner, oid_biz).trim();
			TaAcctInfo taAcctInfo = new TaAcctInfo();
			taAcctInfo = dbUtils.queryTraderAcctInfo(payeeAcctNo);
			String amtBalCur = taAcctInfo.getAmt_balcur();
			String amtBalAval = taAcctInfo.getAmt_balaval();
			String amtBalFrz = taAcctInfo.getAmt_balfrz();
			Reporter.log(
					"消费前账户： " + payeeAcctNo + "	总余额：" + amtBalCur + "	可用余额：" + amtBalAval + "	冻结余额：" + amtBalFrz,
					true);

			// 消费（微信）请求提交
			String rsp = HttpRequest.httpPostWithJSON(wXTradeUrl, json.toString());
			String respCode = JSONObject.parseObject(rsp).getString("respCode");
			String orderId = JSONObject.parseObject(rsp).getString("orderId");			

			// 收款商户业务账户消费后余额
			TaAcctInfo taAcctInfo1 = new TaAcctInfo();
			taAcctInfo1 = dbUtils.queryTraderAcctInfo(payeeAcctNo);
			String amtBalCur1 = taAcctInfo1.getAmt_balcur();
			String amtBalAval1 = taAcctInfo1.getAmt_balaval();
			String amtBalFrz1 = taAcctInfo1.getAmt_balfrz();
			Reporter.log(
					"消费后账户： " + payeeAcctNo + "	总余额：" + amtBalCur1 + "	可用余额：" + amtBalAval1 + "	冻结余额：" + amtBalFrz1,
					true);

			BigDecimal amt1 = new BigDecimal(amtBalCur1).subtract(new BigDecimal(amtBalCur));
			BigDecimal amt2 = new BigDecimal(amtBalAval1).subtract(new BigDecimal(amtBalAval));
			BigDecimal amt3 = new BigDecimal(amtBalFrz1).subtract(new BigDecimal(amtBalFrz));
			Reporter.log("消费后账户： " + payeeAcctNo + "	总余额变化：" + amt1 + "	可用余额变化：" + amt2 + "	冻结余额变化：" + amt3, true);

			Reporter.log("消费（微信）测试： " + dt.get("wXTrade").get("comment"), true);
			System.out.println("===============================================");

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//log//wXTrade.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//log//wXTrade.txt", json.toString());
			SampleFileUtils.appendLine("D://TA//log//wXTrade.txt", rsp);
			SampleFileUtils.appendLine("D://TA//log//wXTrade.txt", "===============================================");

			// 消费（微信）请求返回检查
			assert rsp.contains(dt.get("wXTrade").get("expect_return"));
			if (("00000").equals(respCode)) {
				String statBill = dbUtils.queryTpayStatBill(orderId);
				// 收款商户账户余额校验
				assert amt1.divide(new BigDecimal(10)).toString().equals(dt.get("wXTrade").get("amount"));
				assert amt2.divide(new BigDecimal(10)).toString().equals(dt.get("wXTrade").get("amount"));
				// 支付单状态校验
				assert "0".equals(statBill);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!StringUtils.isBlank(chnlUpdate)) {
				String result[] = chnlUpdate.split(",");
				dbUtils.updateChnlBankState("0", result[1], result[2]);
			}
			if (!StringUtils.isBlank(traderStatUpdate)) {
				String result1[] = traderStatUpdate.split(",");
				dbUtils.updateTraderStatus("0", result1[1]);
			}
			if (!StringUtils.isBlank(userStatUpdate)) {
				dbUtils.updateUserStat("0", user_id);
			}
			if (!StringUtils.isBlank(userBusiStatUpdate)) {
				dbUtils.updateUserBusiStat("1", user_id);
			}
			if (!StringUtils.isBlank(userBusiAcctStatUpdate)) {
				dbUtils.updateUserBusiAcctStat("0", payerAcc);
			}
			if (!StringUtils.isBlank(balanceUpdate)) {
				String result2[] = balanceUpdate.split(",");
				dbUtils.updateAcctBalance(result2[0], "100000");
			}
			if (!StringUtils.isBlank(authUpdate)) {
				dbUtils.updateMerchantAuth(
						"{\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\",\"sign_type\":\"RSA\",\"money_order\":\"${get}\",\"oid_paybill\":\"${get}\",\"dt_order\":\"${get}\",\"no_order\":\"${get}\",\"paymentInfo\":[{\"amount\": \"2\",\"identification\": \"201801150000741050\", \"method\": \"cp\"},{\"amount\": \"1\",\"identification\": \"201801150000741044\",\"method\": \"coupon\"}]}");
			}
		}
	}

	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
		return new ExcelProvider2(this);
	}

	@AfterClass
	public void afterClass() {

	}
}