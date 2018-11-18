package com.llwallet.interfaces.test.api.test.other;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lianpay.chnl.domain.notify.PaymentProcessReq;
import com.lianpay.chnl.service.notify.PaymentResultNotifyService;
import com.llwallet.interfaces.bean.other.CombinePay;
import com.llwallet.interfaces.dao.DbUtils;
import com.llwallet.interfaces.dao.TaAcctInfo;
import com.tools.dataprovider.ExcelProvider;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.JavaBeanUtil;
import com.tools.utils.MyConfig;
import com.tools.utils.MyDate;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import com.tools.utils.SampleFileUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 微信支付宝支付接口测试
 */

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class CombinePayTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private PaymentResultNotifyService paymentResultNotifyService;

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings("unchecked")
	@Test(description = "微信支付宝支付测试", timeOut = 60000, dataProvider = "combinePay")
	public void combinePay(Map<String, String> datadriven) throws Exception {

		CombinePay combinePay = new CombinePay();

		Object reqObj = JavaBeanUtil.convertMap(combinePay.getClass(), datadriven);
		combinePay.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), datadriven.get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", combinePay.getSign());
		String reqJson = JSON.toJSONString(map);
		String combinePayUrl = Property.get("llwallet.combinePay.url");

		// 微信支付宝支付处理请求提交
		String rsp = HttpRequest.httpPostWithJSON(combinePayUrl, reqJson);
		Reporter.log("支付单号： " + JSONObject.parseObject(rsp).getString("oid_paybill"), true);
		Reporter.log("支付内容： " + JSONObject.parseObject(rsp).getString("payLoad"), true);

		Reporter.log("微信支付宝支付接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// 微信支付宝支付处理请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));
	}

	@SuppressWarnings("unchecked")
	@Test(description = "微信支付宝支付接口测试", timeOut = 60000, dataProvider = "provider1")
	public void baseline(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel)
			throws Exception {

		// 支付前收款方余额
		DbUtils dbUtils = new DbUtils();
		String oid_partner = MyConfig.getPartner(dt.get("baseline").get("oid_partner"));
		String secured_partner = MyConfig.getSecuredPartner(dt.get("baseline").get("secured_partner"));
		String col_oidpartner = MyConfig.getColPartner(dt.get("baseline").get("col_oidpartner"));
		String oid_traderno = null, oid_acctno = null;
		if (!StringUtils.isBlank(secured_partner)) {
			oid_traderno = secured_partner;
			oid_acctno = dbUtils.queryTraderBusinessAcct(oid_traderno, dt.get("baseline").get("busi_partner")).trim();
		} else if (!StringUtils.isBlank(col_oidpartner)) {
			oid_traderno = col_oidpartner;
			oid_acctno = dbUtils.queryTraderBusinessAcct(oid_traderno, dt.get("baseline").get("busi_partner")).trim();
		}
		TaAcctInfo taAcctInfo = new TaAcctInfo();
		String amtBalCur, amtBalAval, amtBalFrz;
		if (StringUtils.isBlank(col_oidpartner) && StringUtils.isBlank(secured_partner)) {
			taAcctInfo = dbUtils.queryUserAcctInfo(dt.get("baseline").get("col_userid"), oid_partner);
			amtBalCur = taAcctInfo.getAmt_balcur();
			amtBalAval = taAcctInfo.getAmt_balaval();
			amtBalFrz = taAcctInfo.getAmt_balfrz();
			Reporter.log("支付前用户： " + dt.get("baseline").get("col_userid") + "	总余额：" + amtBalCur + "	可用余额："
					+ amtBalAval + "	冻结余额：" + amtBalFrz, true);
		} else {
			taAcctInfo = dbUtils.queryTraderAcctInfo(oid_acctno);
			amtBalCur = taAcctInfo.getAmt_balcur();
			amtBalAval = taAcctInfo.getAmt_balaval();
			amtBalFrz = taAcctInfo.getAmt_balfrz();
			Reporter.log(
					"支付前商户： " + oid_traderno + "	总余额：" + amtBalCur + "	可用余额：" + amtBalAval + "	冻结余额：" + amtBalFrz,
					true);
		}

		// 微信支付宝支付申请
		CombinePay combinePay = new CombinePay();
		Object reqObj = JavaBeanUtil.convertMap(combinePay.getClass(), dt.get("baseline"));
		combinePay.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), dt.get("baseline").get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", combinePay.getSign());
		String reqJson = JSON.toJSONString(map);
		String combinePayUrl = Property.get("llwallet.combinePay.url");
		// 微信支付宝支付处理请求提交
		String rsp = HttpRequest.httpPostWithJSON(combinePayUrl, reqJson);
		String money_order = dt.get("baseline").get("money_order");
		String no_order = JSONObject.parseObject(rsp).getString("no_order");
		Reporter.log("支付单号： " + JSONObject.parseObject(rsp).getString("oid_paybill"), true);
		Reporter.log("支付内容： " + JSONObject.parseObject(rsp).getString("payLoad"), true);

		// 网银异步回调处理成功
		String pay_type = dt.get("baseline").get("pay_type");
		if (("0000".equals(JSONObject.parseObject(rsp).getString("ret_code"))
				&& ("I".equals(pay_type) || "L".equals(pay_type) || "Y".equals(pay_type) || "W".equals(pay_type)))) {
			String oid_rungroupid = null;
			oid_rungroupid = dbUtils.queryPayBillRunGroupId(no_order);
			String amount = new BigDecimal(money_order).multiply(new BigDecimal(100)).toBigInteger().toString();
			System.out.println("amount：" + amount);
			PaymentProcessReq paymentprocessreq = new PaymentProcessReq();
			paymentprocessreq.setStatus("00");
			paymentprocessreq.setPayRungroupid(oid_rungroupid);
			paymentprocessreq.setSettleDate("20180503");
			paymentprocessreq.setBankSerial(no_order);
			paymentprocessreq.setPayAmount(amount);
			System.out.println("paymentprocessreq： " + JSON.toJSONString(paymentprocessreq));
			String rsp2 = JSON.toJSONString(paymentResultNotifyService.processAfterNotify(paymentprocessreq));
			System.out.println("异步回调返回：" + rsp2);
		}

		// 请求返回检查
		assert rsp.contains(dt.get("baseline").get("expect_return"));
		if (rsp.contains("\"ret_code\":\"0000\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
					JSONObject.parseObject(rsp).getString("sign"));
		}
		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//combinePay.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//combinePay.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//combinePay.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//combinePay.txt", "===============================================");

		// 微信支付宝支付后余额
		TaAcctInfo taAcctInfo1 = new TaAcctInfo();
		String amtBalCur1, amtBalAval1, amtBalFrz1;
		BigDecimal amt1, amt2, amt3;
		if (StringUtils.isBlank(col_oidpartner) && StringUtils.isBlank(secured_partner)) {
			taAcctInfo1 = dbUtils.queryUserAcctInfo(dt.get("baseline").get("col_userid"), oid_partner);
			amtBalCur1 = taAcctInfo1.getAmt_balcur();
			amtBalAval1 = taAcctInfo1.getAmt_balaval();
			amtBalFrz1 = taAcctInfo1.getAmt_balfrz();
			Reporter.log("支付后用户： " + dt.get("baseline").get("col_userid") + "	总余额：" + amtBalCur + "	可用余额："
					+ amtBalAval + "	冻结余额：" + amtBalFrz, true);

			amt1 = new BigDecimal(amtBalCur1).subtract(new BigDecimal(amtBalCur));
			amt2 = new BigDecimal(amtBalAval1).subtract(new BigDecimal(amtBalAval));
			amt3 = new BigDecimal(amtBalFrz1).subtract(new BigDecimal(amtBalFrz));
			Reporter.log("支付后用户： " + dt.get("baseline").get("col_userid") + "	总余额增加：" + amt1 + "	可用余额增加：" + amt2
					+ "	冻结余额增加：" + amt3, true);
		} else {
			taAcctInfo1 = dbUtils.queryTraderAcctInfo(oid_acctno);
			amtBalCur1 = taAcctInfo1.getAmt_balcur();
			amtBalAval1 = taAcctInfo1.getAmt_balaval();
			amtBalFrz1 = taAcctInfo1.getAmt_balfrz();
			Reporter.log("支付后商户： " + oid_traderno + "	总余额：" + amtBalCur1 + "	可用余额：" + amtBalAval1 + "	冻结余额："
					+ amtBalFrz1, true);

			amt1 = new BigDecimal(amtBalCur1).subtract(new BigDecimal(amtBalCur));
			amt2 = new BigDecimal(amtBalAval1).subtract(new BigDecimal(amtBalAval));
			amt3 = new BigDecimal(amtBalFrz1).subtract(new BigDecimal(amtBalFrz));
			Reporter.log("支付后商户： " + oid_traderno + "	总余额增加：" + amt1 + "	可用余额增加：" + amt2 + "	冻结余额增加：" + amt3, true);
		}

		Reporter.log("微信支付宝支付接口测试： " + dt.get("baseline").get("comment"), true);
		System.out.println("===============================================");

		// 微信支付宝支付处理请求返回检查
		assert rsp.contains(dt.get("baseline").get("expect_return"));
		// 支付单状态校验
		BigDecimal AMT = new BigDecimal(String.valueOf(money_order));
		assert "0".equals(dbUtils.queryPayBillStat(no_order, AMT.multiply(new BigDecimal(1000)).toString()));
		// 收款商户余额校验
		assert amt1.divide(new BigDecimal(1000)).toString().equals(dt.get("baseline").get("money_order2"));
		assert amt2.divide(new BigDecimal(1000)).toString().equals(dt.get("baseline").get("money_order2"));

	}

	@DataProvider(name = "combinePay")
	public Iterator<Object[]> data4combinePay() throws IOException {
		return new ExcelProvider(this, "combinePay", 3111);
	}

	@DataProvider(name = "provider1")
	public Iterator<Object[]> provider1() throws IOException {
		return new ExcelProvider2(this, 3, 12);
	}

	@AfterClass
	public void afterClass() {

	}

}