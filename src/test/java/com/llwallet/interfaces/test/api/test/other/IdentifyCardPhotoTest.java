package com.llwallet.interfaces.test.api.test.other;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.other.IdentifyCardPhoto;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.utils.GenSign;
import com.tools.utils.HttpUploadFile;
import com.tools.utils.JavaBeanUtil;
import com.tools.utils.MyConfig;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 身份证照片识别接口测试
 */

public class IdentifyCardPhotoTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings("unchecked")
	@Test(description = "身份证照片识别接口测试", timeOut = 60000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {
		long starttime = System.currentTimeMillis();
		IdentifyCardPhoto identifyCardPhoto = new IdentifyCardPhoto();

		Object reqObj = JavaBeanUtil.convertMap(identifyCardPhoto.getClass(),
				excel.getParamMap(dt.get("identifyCardPhoto")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		identifyCardPhoto.setSign(GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map)),
				dt.get("identifyCardPhoto").get("key")));

		Map<String, String> textMap = new HashMap<String, String>();
		// 设置input的key，value
		textMap.put("oid_partner", MyConfig.getPartner(dt.get("identifyCardPhoto").get("oid_partner")));
		textMap.put("user_id", MyConfig.getUser(dt.get("identifyCardPhoto").get("user_id")));
		textMap.put("sign_type", dt.get("identifyCardPhoto").get("sign_type"));
		textMap.put("sign", identifyCardPhoto.getSign());
		// 设置file的name，路径
		Map<String, String> fileMap = new HashMap<String, String>();
		if (dt.get("identifyCardPhoto").get("front_card").length() > 0) {
			fileMap.put("front_card", dt.get("identifyCardPhoto").get("front_card"));
		}
		if (dt.get("identifyCardPhoto").get("back_card").length() > 0) {
			fileMap.put("back_card", dt.get("identifyCardPhoto").get("back_card"));
		}

		String contentType = "";
		String identifyCardPhotoUrl = Property.get("llwallet.identifyCardPhoto.url");
		String rsp = HttpUploadFile.formUpload(identifyCardPhotoUrl, textMap, fileMap, contentType);
		// excel.writeCurrentCell("identifyCardPhoto", "actual_return", rsp);
		Reporter.log("接口返回： " + rsp, true);
		Reporter.log("身份证照片识别接口测试： " + dt.get("identifyCardPhoto").get("comment"), true);
		long endtime = System.currentTimeMillis();
		long t = endtime - starttime;
		System.out.println("总耗时：" + t + "ms");
		System.out.println("===============================================");
		// 身份证照片识别请求返回检查
		assert rsp.contains(dt.get("identifyCardPhoto").get("expect_return"));
		assert rsp.contains(dt.get("identifyCardPhoto").get("expect_return1"));
		assert rsp.contains(dt.get("identifyCardPhoto").get("expect_return2"));
		assert rsp.contains(dt.get("identifyCardPhoto").get("expect_return3"));
	}

	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
//		return new ExcelProvider2(this,11);
		ExcelProvider2 excel =new ExcelProvider2(this,1,3);
//		excel.maxRow=100;
		return excel;
		
	}

	@AfterClass
	public void afterClass() {

	}

}