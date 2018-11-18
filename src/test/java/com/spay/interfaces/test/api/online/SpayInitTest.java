package com.spay.interfaces.test.api.online;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.spay.interfaces.bean.PrePay;
import com.spay.interfaces.bean.SpayInit;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.ApiUtils;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 9002接口测试
 */

public class SpayInitTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "9002接口测试", timeOut = 60000, dataProvider = "spayInit")
	public void spayInit(Map<String, String> datadriven) throws Exception {

		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();
		
		// SDK预支付
		PrePay prePay = new PrePay();
		prePay.setOid_partner(datadriven.get("oid_partner"));
		prePay.setPartner_sign_type(datadriven.get("partner_sign_type"));
		prePay.setBusi_partner(datadriven.get("busi_partner"));		
		if ("auto".equals(datadriven.get("no_order"))){
			prePay.setNo_order(no_order);
		}else{
			prePay.setNo_order(datadriven.get("no_order"));
		}
		if ("auto".equals(datadriven.get("dt_order"))){
			prePay.setDt_order(dt_order);
		}else{
			prePay.setDt_order(datadriven.get("dt_order"));
		}
		prePay.setName_goods(datadriven.get("name_goods"));
		prePay.setRisk_item(datadriven.get("risk_item"));
		prePay.setInfo_order(datadriven.get("info_order"));
		prePay.setMoney_order(datadriven.get("money_order"));
		prePay.setValid_order(datadriven.get("valid_order"));
		prePay.setUser_id(datadriven.get("user_id"));
		prePay.setNotify_url(datadriven.get("notify_url"));
		prePay.setIdno(datadriven.get("idno"));
		prePay.setIdtype(datadriven.get("idtype"));
		prePay.setItem_product(datadriven.get("item_product"));
		prePay.setOid_userno(datadriven.get("oid_userno"));
		prePay.setPartner_sign(GenSign.genPartnerSign(JSON.parseObject(JSON.toJSONString(prePay)),datadriven.get("partner_key")));
		System.out.println("partner_sign -----:" + prePay.getPartner_sign());
		
		// 首页初始化
		SpayInit spayInit = new SpayInit();
		spayInit.setOid_partner(datadriven.get("oid_partner"));
		spayInit.setPartner_sign_type(datadriven.get("partner_sign_type"));
		spayInit.setOid_userno(datadriven.get("oid_userno"));
		spayInit.setPay_chnl(datadriven.get("pay_chnl"));
		spayInit.setBusi_partner(datadriven.get("busi_partner"));		
		if ("auto".equals(datadriven.get("no_order"))){
			spayInit.setNo_order(no_order);
		}else{
			spayInit.setNo_order(datadriven.get("no_order"));
		}
		if ("auto".equals(datadriven.get("dt_order"))){
			spayInit.setDt_order(dt_order);
		}else{
			spayInit.setDt_order(datadriven.get("dt_order"));
		}		
		spayInit.setValid_order(datadriven.get("valid_order"));
		spayInit.setName_goods(datadriven.get("name_goods"));
		spayInit.setMoney_order(datadriven.get("money_order"));
		spayInit.setInfo_order(datadriven.get("info_order"));
		spayInit.setNotify_url(datadriven.get("notify_url"));
		spayInit.setItem_product(datadriven.get("item_product"));
		spayInit.setRisk_item(datadriven.get("risk_item"));
		spayInit.setUser_id(datadriven.get("user_id"));
		spayInit.setIdtype(datadriven.get("idtype"));
		spayInit.setIdno(datadriven.get("idno"));
		spayInit.setPartner_sign(prePay.getPartner_sign());
		
		// 基本请求参数
		spayInit.setTranscode(datadriven.get("transcode"));
		spayInit.setImei_request(datadriven.get("imei_request"));
		spayInit.setImsi_request(datadriven.get("imsi_request"));
		spayInit.setMac_request(datadriven.get("mac_request"));
		spayInit.setMachine_id(datadriven.get("machine_id"));
		spayInit.setSign_type(datadriven.get("sign_type"));
		spayInit.setVer_app(datadriven.get("ver_app"));
		spayInit.setFlag_chn(datadriven.get("flag_chn"));
		spayInit.setVer_sdk(datadriven.get("ver_sdk"));
		spayInit.setLatitude(datadriven.get("latitude"));
		spayInit.setLongitude(datadriven.get("longitude"));
		spayInit.setStation_info(datadriven.get("station_info"));
		spayInit.setApp_id(datadriven.get("app_id"));
		spayInit.setApp_name(datadriven.get("app_name"));
		spayInit.setScreen(datadriven.get("screen"));
		spayInit.setManufacturer(datadriven.get("manufacturer"));
		spayInit.setOs_model(datadriven.get("os_model"));
		spayInit.setOs_sdk(datadriven.get("os_sdk"));
		spayInit.setOs_release(datadriven.get("os_release"));
		spayInit.setNet_work(datadriven.get("net_work"));
		spayInit.setNet_type(datadriven.get("net_type"));
		spayInit.setJailbroken(datadriven.get("jailbroken"));		
		spayInit.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(spayInit)), datadriven.get("key")));
		System.out.println("sign -----:" + spayInit.getSign());
		String reqJson = JSON.toJSONString(spayInit);
		reqJson = URLEncoder.encode(reqJson, "UTF-8");
		String spayInitUrl = Property.get("spay.spayInit.url.online");		
		
		// 9002请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(spayInitUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 9002请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("9002接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "spayInit")
	public Iterator<Object[]> data4spayInit() throws IOException {
		return new ExcelProvider(this, "spayInit");
	}

	@AfterClass
	public void afterClass() {

	}
}