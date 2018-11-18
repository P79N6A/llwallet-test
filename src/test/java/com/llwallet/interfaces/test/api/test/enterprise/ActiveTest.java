package com.llwallet.interfaces.test.api.test.enterprise;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.enterprise.Active;
import com.tools.dataprovider.ExcelProvider;
import com.tools.utils.GenSign;
import com.tools.utils.HttpUploadFile;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 用户激活测试
 */

public class ActiveTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "用户激活测试", timeOut = 60000, dataProvider = "active")
	public void active(Map<String, String> datadriven) throws Exception {

		String user_id = datadriven.get("user_id");
		String oid_partner = datadriven.get("oid_partner");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		Date date = cal.getTime();
		String timestamp = sdf.format(date);

		Active active = new Active();
		active.setOid_partner(oid_partner);
		active.setSign_type(datadriven.get("sign_type"));
		active.setUser_id(datadriven.get("user_id"));
		active.setRisk_item(datadriven.get("risk_item"));
		active.setTimestamp(timestamp);
		active.setUrl_return(datadriven.get("url_return"));	
		String sign = GenSign.genSign(JSON.parseObject(JSON.toJSONString(active)), datadriven.get("key"));

		System.out.println("sign----：" + sign);
		String activeUrl = Property.get("llwalletActive.url");
		Map<String, String> textMap = new HashMap<String, String>();
		// 设置input的key，value
		textMap.put("oid_partner", oid_partner);
		textMap.put("user_id", user_id);
		textMap.put("sign_type", datadriven.get("sign_type"));
		textMap.put("timestamp", timestamp);
		textMap.put("risk_item", datadriven.get("risk_item"));
		textMap.put("url_return", datadriven.get("url_return"));
		textMap.put("sign", sign);

		String contentType = "";
		String res = HttpUploadFile.formUpload(activeUrl, textMap, null, contentType);
		System.out.println("req----:" + JSONObject.toJSON(textMap));
		Thread.sleep(1000);
//		System.out.println(res);
		Reporter.log("接口返回： " + res, true);
		// 用户激活请求返回检查
		assert res.contains(datadriven.get("result_code"));

		Reporter.log("用户激活测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
	
	@DataProvider(name = "active")
	public Iterator<Object[]> data4active() throws IOException {
		return new ExcelProvider(this, "active",1);
	}
	
	@AfterClass
	public void afterClass() {

	}

}
