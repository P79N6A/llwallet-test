package com.llwallet.interfaces.test.api.test.tpay;

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
import com.llwallet.interfaces.bean.tpay.CreateQr;
import com.llwallet.interfaces.bean.tpay.RiskInfo;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
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
 * 消费通二维码生成接口测试
 */

public class CreateQrTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings({"rawtypes" })
	@Test(description = "消费通二维码生成接口测试", timeOut = 60000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		if ("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"".equals(dt.get("createQr").get("expect_return"))){
			Thread.sleep(1000);
		}
		CreateQr createQr = new CreateQr();		
		RiskInfo riskInfo = new RiskInfo();
		Map<String, String> params = new HashMap<String, String>();
		Iterator it = dt.get("createQr").entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (!entry.getValue().equals("") && StringUtils.startsWith((String) entry.getKey(), "riskInfo.")) {
				params.put(((String) entry.getKey()).split("\\.")[1], (String) entry.getValue());
			}
		}
		BeanUtils.populate(riskInfo, params);
		createQr.setRiskInfo(riskInfo);
		createQr.setSign_type(dt.get("createQr").get("sign_type"));
		createQr.setOid_partner(MyConfig.getPartner(dt.get("createQr").get("oid_partner")));
		createQr.setVersion(dt.get("createQr").get("version"));
		createQr.setUser_id(dt.get("createQr").get("user_id"));
		createQr.setTimestamp(MyConfig.getTimestamp(dt.get("createQr").get("timestamp")));
		createQr.setAuth_flag(dt.get("createQr").get("auth_flag"));
		createQr.setAuth_amt(dt.get("createQr").get("auth_amt"));
		createQr.setDt_order(MyConfig.getDtOrder(dt.get("createQr").get("dt_order")));
		createQr.setNo_order(MyConfig.getNoOrder(dt.get("createQr").get("no_order")));
		createQr.setPay_partner(dt.get("createQr").get("pay_partner"));
		createQr.setQr_validtime(dt.get("createQr").get("qr_validtime"));
		createQr.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(createQr)), dt.get("createQr").get("key")));
		String createQrUrl = Property.get("tpay.createQr.url");
		String reqJson = JSON.toJSONString(createQr);
		
		// 消费通二维码生成请求提交
		String rsp = HttpRequest.httpPostWithJSON(createQrUrl, reqJson);
		// excel.writeCurrentCell("createQr", "actual_return", rsp);
		Reporter.log("消费通二维码生成接口测试： " + dt.get("createQr").get("comment"), true);
		System.out.println("===============================================");
		
		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//createQr.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//createQr.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//createQr.txt", rsp);		
		SampleFileUtils.appendLine("D://TA//log//createQr.txt","===============================================");
		
		// 消费通二维码生成请求返回检查
		assert rsp.contains(dt.get("createQr").get("expect_return"));
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