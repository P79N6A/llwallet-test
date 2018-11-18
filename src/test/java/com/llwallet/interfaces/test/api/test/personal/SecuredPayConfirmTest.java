package com.llwallet.interfaces.test.api.test.personal;

import java.io.IOException;
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
import com.lianlian.crypt.service.IAESCryptService;
import com.llwallet.interfaces.bean.personal.BankCardPay;
import com.llwallet.interfaces.bean.personal.BankCardPrepay;
import com.llwallet.interfaces.bean.personal.SecuredPayConfirm;
import com.llwallet.interfaces.dao.DbUtils;
import com.tools.dataprovider.ExcelProvider;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.JavaBeanUtil;
import com.tools.utils.MyConfig;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 担保交易确认交易接口测试
 */

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class SecuredPayConfirmTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private IAESCryptService aesCryptService;

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings({ "unchecked" })
	@Test(description = "担保交易确认交易接口测试", timeOut = 60000, dataProvider = "securedPayConfirm")
	public void securedPayConfirm(Map<String, String> datadriven) throws Exception {

		// 删除卡签约信息
		DbUtils dbUtils = new DbUtils();
		String cardSignUpdate = datadriven.get("cardSignUpdate");
		if (!StringUtils.isBlank(cardSignUpdate)) {
			dbUtils.deleteCardSigned(aesCryptService, datadriven.get("card_no"));
		}

		// 银行卡支付申请
		BankCardPrepay bankCardPrepay = new BankCardPrepay();
		Object reqObj = JavaBeanUtil.convertMap(bankCardPrepay.getClass(), datadriven);
		bankCardPrepay.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), datadriven.get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", bankCardPrepay.getSign());
		String reqJson = JSON.toJSONString(map);
		String bankCardPrepayUrl = Property.get("llwallet.bankCardPrepay.url");
		String rsp = HttpRequest.httpPostWithJSON(bankCardPrepayUrl, reqJson);
		String token = JSONObject.parseObject(rsp).getString("token");
		String no_order = JSONObject.parseObject(rsp).getString("no_order");
		String dt_order = JSONObject.parseObject(rsp).getString("dt_order");
		String oid_paybill = JSONObject.parseObject(rsp).getString("oid_paybill");

		// 支付验证
		if ("8888".equals(JSONObject.parseObject(rsp).getString("ret_code"))) {
			Thread.sleep(10);
			String verify_code = dbUtils.queryCdSmscd(datadriven.get("bind_mob")).getTno_smscd();
			BankCardPay bankCardPay = new BankCardPay();
			bankCardPay.setMoney_order(datadriven.get("money_order"));
			bankCardPay.setCol_oidpartner(datadriven.get("col_oidpartner"));
			bankCardPay.setOid_partner(datadriven.get("oid_partner"));
			bankCardPay.setSign_type(datadriven.get("sign_type"));
			bankCardPay.setNo_order(no_order);
			bankCardPay.setToken(token);
			if ("get".equals(datadriven.get("verify_code1"))) {
				bankCardPay.setVerify_code(verify_code);
			} else {
				bankCardPay.setVerify_code(datadriven.get("verify_code1"));
			}
			bankCardPay
					.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardPay)), datadriven.get("key")));
			String reqJson1 = JSON.toJSONString(bankCardPay);
			String bankCardPayUrl = Property.get("llwallet.bankCardPay.url");
			HttpRequest.httpPostWithJSON(bankCardPayUrl, reqJson1);
		}

		// 担保确认
		SecuredPayConfirm securedPayConfirm = new SecuredPayConfirm();
		securedPayConfirm.setOid_partner(datadriven.get("oid_partner2"));
		securedPayConfirm.setSecured_partner(datadriven.get("secured_partner2"));
		securedPayConfirm.setSign_type(datadriven.get("sign_type2"));
		securedPayConfirm.setApi_version(datadriven.get("api_version2"));
		securedPayConfirm.setNo_confirm(MyConfig.getNoConfirm(datadriven.get("no_confirm2")));
		securedPayConfirm.setDt_confirm(MyConfig.getDtConfirm(datadriven.get("dt_confirm2")));
		securedPayConfirm.setMoney_order(datadriven.get("money_order2"));
		if ("get".equals(datadriven.get("no_order2"))) {
			securedPayConfirm.setNo_order(no_order);
		} else {
			securedPayConfirm.setNo_order(datadriven.get("no_order2"));
		}
		if ("get".equals(datadriven.get("dt_order2"))) {
			securedPayConfirm.setDt_order(dt_order);
		} else {
			securedPayConfirm.setDt_order(datadriven.get("dt_order2"));
		}
		if ("get".equals(datadriven.get("oid_paybill2"))) {
			securedPayConfirm.setOid_paybill(oid_paybill);
		} else {
			securedPayConfirm.setOid_paybill(datadriven.get("oid_paybill2"));
		}
		if (datadriven.get("col_custid2").length() > 0) {
			securedPayConfirm.setCol_custid(datadriven.get("col_custid2"));
		}
		if (datadriven.get("col_custtype2").length() > 0) {
			securedPayConfirm.setCol_custtype(datadriven.get("col_custtype2"));
		}
		securedPayConfirm.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(securedPayConfirm)), datadriven.get("key2")));
		String reqJson2 = JSON.toJSONString(securedPayConfirm);
		String securedPayConfirmUrl = Property.get("llwallet.securedPayConfirm.url");
		String rsp2 = HttpRequest.httpPostWithJSON(securedPayConfirmUrl, reqJson2);

		Reporter.log("担保交易确认交易接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
		// 担保交易确认交易请求返回检查
		assert rsp2.contains(datadriven.get("expect_return"));
	}

	@SuppressWarnings("unchecked")
	@Test(description = "担保交易确认交易接口测试", timeOut = 60000, dataProvider = "provider1")
	public void test1(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		SecuredPayConfirm securedPayConfirm = new SecuredPayConfirm();

		Object reqObj = JavaBeanUtil.convertMap(securedPayConfirm.getClass(),
				excel.getParamMap(dt.get("securedPayConfirm1")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		securedPayConfirm.setSign(GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map)),
				dt.get("securedPayConfirm1").get("key")));
		map.put("sign", securedPayConfirm.getSign());
		String reqJson1 = JSON.toJSONString(map);
		String securedPayConfirmUrl = Property.get("llwallet.securedPayConfirm.url");

		// 担保交易确认交易请求提交
		String rsp = HttpRequest.httpPostWithJSON(securedPayConfirmUrl, reqJson1);
		// excel.writeCurrentCell("orderQuery", "actual_return", rsp);
		Reporter.log("担保交易确认交易接口测试： " + dt.get("securedPayConfirm1").get("comment"), true);
		System.out.println("===============================================");
		// 担保交易确认交易返回检查
		assert rsp.contains(dt.get("securedPayConfirm1").get("expect_return"));
	}

	@DataProvider(name = "securedPayConfirm")
	public Iterator<Object[]> data4securedPayConfirm() throws IOException {
		return new ExcelProvider(this, "securedPayConfirm",999);
	}

	@DataProvider(name = "provider1")
	public Iterator<Object[]> provider1() throws IOException {
		// return new ExcelProvider2(this);
		ExcelProvider2 excel = new ExcelProvider2(this, 3);
		excel.maxRow = 41;
		return excel;
	}

	@AfterClass
	public void afterClass() {

	}

}
