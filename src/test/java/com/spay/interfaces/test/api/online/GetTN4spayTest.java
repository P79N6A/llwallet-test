package com.spay.interfaces.test.api.online;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.spay.interfaces.bean.ConfirmPay;
import com.spay.interfaces.bean.GetTN4spay;
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
 * 9003接口测试
 */

public class GetTN4spayTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "9003接口测试", timeOut = 60000, dataProvider = "getTN4spay")
	public void getTN4spay(Map<String, String> datadriven) throws Exception {
		
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
	    String ret_code = hf.getResponseBody().substring(hf.getResponseBody().indexOf("ret_code\":\"")+11,hf.getResponseBody().indexOf("\",\"ret_msg"));
	    
	    String token = "";
	    String oid_userno = "";
	    if ("000000".equals(ret_code)||"0000".equals(ret_code)){
		    token = hf.getResponseBody().substring(hf.getResponseBody().indexOf("token\":\"")+8,hf.getResponseBody().indexOf("\",\"transcode"));
			oid_userno = hf.getResponseBody().substring(hf.getResponseBody().indexOf("oid_userno\":\"")+13,hf.getResponseBody().indexOf("\",\"ret_code"));
	    }
		System.out.println("ret_code---------------" + ret_code);
		System.out.println("token---------------" + token);
		System.out.println("oid_userno---------------" + oid_userno);
		
		// SDK确认支付
		ConfirmPay confirmPay = new ConfirmPay();
		confirmPay.setOid_partner(datadriven.get("oid_partner1"));
		confirmPay.setPartner_sign_type(datadriven.get("partner_sign_type1"));
		if ("auto".equals(datadriven.get("no_order1"))){
			confirmPay.setNo_order(no_order);
		}else{
			confirmPay.setNo_order(datadriven.get("no_order1"));
		}
		
		confirmPay.setMoney_final(datadriven.get("money_final"));
		confirmPay.setUser_id(datadriven.get("user_id"));
		confirmPay.setReturn_url(datadriven.get("return_url"));	
		confirmPay.setPartner_sign(GenSign.genPartnerSign(JSON.parseObject(JSON.toJSONString(confirmPay)),datadriven.get("partner_key1")));
		System.out.println("partner_sign -----:" + confirmPay.getPartner_sign());
		
		// 获取银联支付TN
		GetTN4spay getTN4spay = new GetTN4spay();
		getTN4spay.setOid_partner(datadriven.get("oid_partner1"));
//		if ("auto".equals(datadriven.get("oid_userno"))){
			getTN4spay.setOid_userno(oid_userno);
//		}else{
//			getTN4spay.setOid_userno(datadriven.get("oid_userno"));
//		}
		
		getTN4spay.setPartner_sign_type(datadriven.get("partner_sign_type1"));
		if ("auto".equals(datadriven.get("no_order1"))){
			getTN4spay.setNo_order(no_order);
		}else{
			getTN4spay.setNo_order(datadriven.get("no_order1"));
		}
		
		getTN4spay.setMoney_final(datadriven.get("money_final"));
		getTN4spay.setUser_id(datadriven.get("user_id"));
		if ("auto".equals(datadriven.get("token"))){
			getTN4spay.setToken(token);
		}else{
			getTN4spay.setToken(datadriven.get("token"));
		}
		
		getTN4spay.setUser_login(datadriven.get("user_login"));
		getTN4spay.setPay_chnl(datadriven.get("pay_chnl1"));
		getTN4spay.setPay_type(datadriven.get("pay_type"));
		getTN4spay.setReturn_url(datadriven.get("return_url"));
		getTN4spay.setPartner_sign(confirmPay.getPartner_sign());

		// 基本请求参数
		getTN4spay.setSign_type(datadriven.get("sign_type1"));
		getTN4spay.setTranscode(datadriven.get("transcode1"));
		getTN4spay.setImei_request(datadriven.get("imei_request"));
		getTN4spay.setImsi_request(datadriven.get("imsi_request"));
		getTN4spay.setMac_request(datadriven.get("mac_request"));
		getTN4spay.setMachine_id(datadriven.get("machine_id"));
		getTN4spay.setVer_app(datadriven.get("ver_app"));
		getTN4spay.setFlag_chn(datadriven.get("flag_chn"));
		getTN4spay.setVer_sdk(datadriven.get("ver_sdk"));
		getTN4spay.setLatitude(datadriven.get("latitude"));
		getTN4spay.setLongitude(datadriven.get("longitude"));
		getTN4spay.setStation_info(datadriven.get("station_info"));
		getTN4spay.setApp_id(datadriven.get("app_id"));
		getTN4spay.setApp_name(datadriven.get("app_name"));
		getTN4spay.setScreen(datadriven.get("screen"));
		getTN4spay.setManufacturer(datadriven.get("manufacturer"));
		getTN4spay.setOs_model(datadriven.get("os_model"));
		getTN4spay.setOs_sdk(datadriven.get("os_sdk"));
		getTN4spay.setOs_release(datadriven.get("os_release"));
		getTN4spay.setNet_work(datadriven.get("net_work"));
		getTN4spay.setNet_type(datadriven.get("net_type"));
		getTN4spay.setJailbroken(datadriven.get("jailbroken"));
		getTN4spay.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(getTN4spay)), datadriven.get("key")));
		System.out.println("sign -----:" + getTN4spay.getSign());
		String reqJson1 = JSON.toJSONString(getTN4spay);
		reqJson1 = URLEncoder.encode(reqJson1, "UTF-8");
		String getTN4spayUrl = Property.get("spay.getTN4spay.url.online");
		
		// 9003请求提交
		hf.nextRequest();
		hf.setUrl(getTN4spayUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 9003返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("9003接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "getTN4spay")
	public Iterator<Object[]> data4getTN4spay() throws IOException {
		return new ExcelProvider(this, "getTN4spay",3);
	}

	@AfterClass
	public void afterClass() {

	}
}