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
import com.llwallet.interfaces.bean.personal.TraderPayment;
import com.llwallet.interfaces.dao.DbUtils;
import com.llwallet.interfaces.dao.TaAcctInfo;
import com.tools.dataprovider.ExcelProvider;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpFixture;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.JavaBeanUtil;
import com.tools.utils.MyConfig;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 商户付款钱包用户接口测试
 */

public class TraderPaymentTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "商户付款接口测试", timeOut = 60000, dataProvider = "traderPayment")
	public void traderPayment(Map<String, String> datadriven) throws Exception {

		TraderPayment traderPayment = new TraderPayment();
		traderPayment.setOid_partner(datadriven.get("oid_partner"));
		traderPayment.setSign_type(datadriven.get("sign_type"));
		traderPayment.setCol_userid(datadriven.get("col_userid"));
		traderPayment.setBusi_partner(datadriven.get("busi_partner"));
		traderPayment.setRisk_item(datadriven.get("risk_item"));
		traderPayment.setInfo_order(datadriven.get("info_order"));
		traderPayment.setProc_src(datadriven.get("proc_src"));
		traderPayment.setName_goods(datadriven.get("name_goods"));
		traderPayment.setFlag_account(datadriven.get("flag_account"));
		traderPayment.setNo_order(MyConfig.getNoOrder(datadriven.get("no_order")));
		traderPayment.setDt_order(MyConfig.getDtOrder(datadriven.get("dt_order")));
		if ("".equals(datadriven.get("money_order"))) {
			traderPayment.setMoney_order(null);
		} else {
			traderPayment.setMoney_order(datadriven.get("money_order"));
		}

		traderPayment.setName_goods(datadriven.get("name_goods"));
		traderPayment
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(traderPayment)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(traderPayment);
		String traderPaymentUrl = Property.get("llwallet.traderPayment.url");

		// 商户付款钱包用户请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(traderPaymentUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();

		// 商户付款钱包用户请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("商户付款钱包用户接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@SuppressWarnings({ "unchecked" })
	@Test(description = "商户付款接口测试", timeOut = 60000, dataProvider = "provider1")
	public void baseline(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel)
			throws Exception {

		DbUtils dbUtils = new DbUtils();
		String oid_partner = MyConfig.getPartner(dt.get("baseline").get("oid_partner"));
		String col_userid = dt.get("baseline").get("col_userid");
		// 收款用户支付账户支付前余额
		TaAcctInfo taAcctInfo = new TaAcctInfo();
		taAcctInfo = dbUtils.queryUserAcctInfo(col_userid, oid_partner);
		String amtBalCur = taAcctInfo.getAmt_balcur();
		String amtBalAval = taAcctInfo.getAmt_balaval();
		String amtBalFrz = taAcctInfo.getAmt_balfrz();
		Reporter.log("支付前收款用户： " + col_userid + "	总余额：" + amtBalCur + "	可用余额：" + amtBalAval + "	冻结余额：" + amtBalFrz,
				true);

		TraderPayment traderPayment = new TraderPayment();
		Object reqObj = JavaBeanUtil.convertMap(traderPayment.getClass(), dt.get("baseline"));
		traderPayment
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), dt.get("baseline").get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", traderPayment.getSign());
		String reqJson = JSON.toJSONString(map);
		String traderPaymentUrl = Property.get("llwallet.traderPayment.url");
		String rsp = HttpRequest.httpPostWithJSON(traderPaymentUrl, reqJson);
		String no_order = JSONObject.parseObject(rsp).getString("no_order");
		String money_order = JSONObject.parseObject(rsp).getString("money_order");

		assert rsp.contains(dt.get("baseline").get("expect_return"));
		if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
					JSONObject.parseObject(rsp).getString("sign"));
		}

		// 收款用户支付账户支付后余额
		TaAcctInfo taAcctInfo1 = new TaAcctInfo();
		taAcctInfo1 = dbUtils.queryUserAcctInfo(col_userid, oid_partner);
		String amtBalCur1 = taAcctInfo1.getAmt_balcur();
		String amtBalAval1 = taAcctInfo1.getAmt_balaval();
		String amtBalFrz1 = taAcctInfo1.getAmt_balfrz();
		Reporter.log(
				"支付后收款用户： " + col_userid + "	总余额：" + amtBalCur1 + "	可用余额：" + amtBalAval1 + "	冻结余额：" + amtBalFrz1,
				true);
		BigDecimal amt1 = new BigDecimal(amtBalCur1).subtract(new BigDecimal(amtBalCur));
		BigDecimal amt2 = new BigDecimal(amtBalAval1).subtract(new BigDecimal(amtBalAval));
		BigDecimal amt3 = new BigDecimal(amtBalFrz1).subtract(new BigDecimal(amtBalFrz));
		Reporter.log("支付后用户： " + col_userid + "	总余额增加：" + amt1 + "	可用余额增加：" + amt2 + "	冻结余额增加：" + amt3, true);

		Reporter.log("商户付款钱包用户接口测试： " + dt.get("baseline").get("comment"), true);
		System.out.println("===============================================");

		// 支付单状态校验
		BigDecimal AMT = new BigDecimal(String.valueOf(money_order));
		assert "0".equals(dbUtils.queryPayBillStat(no_order, AMT.multiply(new BigDecimal(1000)).toString()));
		// 支付用户余额校验
		assert amt1.divide(new BigDecimal(1000)).toString().equals(dt.get("baseline").get("money_order"));
		assert amt2.divide(new BigDecimal(1000)).toString().equals(dt.get("baseline").get("money_order"));
	}

	@DataProvider(name = "traderPayment")
	public Iterator<Object[]> data4traderPayment() throws IOException {
		return new ExcelProvider(this, "traderPayment", 1);
	}

	@DataProvider(name = "provider1")
	public Iterator<Object[]> provider1() throws IOException {
		return new ExcelProvider2(this, 3, 5);
	}

	@AfterClass
	public void afterClass() {

	}
}