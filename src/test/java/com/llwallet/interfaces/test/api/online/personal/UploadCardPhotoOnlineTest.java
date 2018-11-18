package com.llwallet.interfaces.test.api.online.personal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.UploadCardPhoto;
import com.tools.dataprovider.ExcelProvider;
import com.tools.utils.GenSign;
import com.tools.utils.HttpUploadFile;
import com.tools.utils.MyConfig;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 个人用户上传照片接口测试
 */

public class UploadCardPhotoOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "个人用户上传照片接口测试", timeOut = 60000, dataProvider = "uploadCardPhoto")
	public void uploadCardPhoto(Map<String, String> datadriven) throws Exception {

		UploadCardPhoto uploadCardPhoto = new UploadCardPhoto();
		uploadCardPhoto.setOid_partner(MyConfig.getPartner(datadriven.get("oid_partner")));
		uploadCardPhoto.setSign_type(datadriven.get("sign_type"));
		uploadCardPhoto.setUser_id(MyConfig.getUser(datadriven.get("user_id")));
		uploadCardPhoto.setNotify_url(datadriven.get("notify_url"));
		String sign = GenSign.genSign(JSON.parseObject(JSON.toJSONString(uploadCardPhoto)), datadriven.get("key"));

		System.out.println("sign--：" + sign);
		String uploadCardPhotoUrl = Property.get("llwallet.uploadCardPhoto.url.online");
		Map<String, String> textMap = new HashMap<String, String>();
		// 设置input的key，value
		textMap.put("oid_partner", MyConfig.getPartner(datadriven.get("oid_partner")));
		textMap.put("user_id", MyConfig.getUser(datadriven.get("user_id")));
		textMap.put("sign_type", datadriven.get("sign_type"));
		textMap.put("notify_url", datadriven.get("notify_url"));
		textMap.put("sign", sign);
		// 设置file的name，路径
		Map<String, String> fileMap = new HashMap<String, String>();
		if (datadriven.get("front_card").length()>0) {
			fileMap.put("front_card", datadriven.get("front_card"));
		}
		if (datadriven.get("back_card").length()>0) {
			fileMap.put("back_card", datadriven.get("back_card"));
		}

		String contentType = "";
		String res = HttpUploadFile.formUpload(uploadCardPhotoUrl, textMap, fileMap, contentType);
		System.out.println(res);
		Reporter.log("接口返回： " + res, true);
		// 个人用户上传照片请求返回检查
		assert res.contains(datadriven.get("result_code"));

		Reporter.log("个人用户上传照片接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "uploadCardPhoto")
	public Iterator<Object[]> data4uploadCardPhoto() throws IOException {
		return new ExcelProvider(this, "uploadCardPhoto",1);
	}

	
	@AfterClass
	public void afterClass() {

	}

}
