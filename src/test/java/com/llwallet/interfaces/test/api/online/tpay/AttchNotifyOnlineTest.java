package com.llwallet.interfaces.test.api.online.tpay;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.tpay.AttchNotify;
import com.llwallet.interfaces.bean.tpay.RiskInfo;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.MyConfig;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 附加交易成功通知接口测试
 */

public class AttchNotifyOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings({"rawtypes" })
	@Test(description = "附加交易成功通知接口测试", timeOut = 60000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		AttchNotify attchNotify = new AttchNotify();	
		RiskInfo riskInfo = new RiskInfo();
		Map<String, String> params = new HashMap<String, String>();
		Iterator it = dt.get("attchNotify").entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (!entry.getValue().equals("") && StringUtils.startsWith((String) entry.getKey(), "riskInfo.")) {
				params.put(((String) entry.getKey()).split("\\.")[1], (String) entry.getValue());
			}
		}
		BeanUtils.populate(riskInfo, params);
		attchNotify.setRiskInfo(riskInfo);
		attchNotify.setSign_type(dt.get("attchNotify").get("sign_type"));
		attchNotify.setOid_partner(MyConfig.getPartner(dt.get("attchNotify").get("oid_partner")));
		attchNotify.setVersion(dt.get("attchNotify").get("version"));
		attchNotify.setNo_order(MyConfig.getNoOrder(dt.get("attchNotify").get("no_order")));
		attchNotify.setUser_id(dt.get("attchNotify").get("user_id"));
		attchNotify.setQr_no(dt.get("attchNotify").get("qr_no"));
		attchNotify.setResult_check(dt.get("attchNotify").get("result_check"));		
		attchNotify.setTimestamp(MyConfig.getTimestamp(dt.get("attchNotify").get("timestamp")));
		attchNotify.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(attchNotify)), dt.get("attchNotify").get("key")));
		String attchNotifyUrl = Property.get("tpay.attchNotify.url.online");
		String reqJson = JSON.toJSONString(attchNotify);		

		// 附加交易成功通知请求提交
		String rsp = HttpRequest.httpPostWithJSON(attchNotifyUrl, reqJson);
		// excel.writeCurrentCell("attchNotify", "actual_return",rsp);
		Reporter.log("附加交易成功通知接口测试： " + dt.get("attchNotify").get("comment"), true);
		System.out.println("===============================================");
		// 附加交易成功通知请求返回检查
		assert rsp.contains(dt.get("attchNotify").get("expect_return"));
		// 返回验签
		if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")){
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)), JSONObject.parseObject(rsp).getString("sign"));
		}
	}

	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
		return new ExcelProvider2(this,3);
	}

	@AfterClass
	public void afterClass() {

	}
}