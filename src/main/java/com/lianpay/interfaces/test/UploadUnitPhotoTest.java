package com.lianpay.interfaces.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.lianpay.interfaces.bean.UploadUnitPhoto;
import com.lianpay.tools.dataprovider.ExcelProvider;
import com.lianpay.tools.utils.GenSign;
import com.lianpay.tools.utils.HttpUploadFile;
import com.lianpay.tools.utils.Property;
import com.lianpay.tools.utils.TelnetUtil;

import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 企业用户上传照片接口测试
 */

public class UploadUnitPhotoTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "企业用户上传照片接口测试", timeOut = 60000, dataProvider = "uploadUnitPhoto")
	public void uploadUnitPhoto(Map<String, String> datadriven) throws Exception {

		// 清除用户缓存  同一商户同一执照号一天最大8次 uploadEnterpriseCert_times=8
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host"), Integer.parseInt(Property.get("port")));
		StringBuffer command = new StringBuffer();
		command.append("flush_all");
		telnetUtil.execute(command.toString());
		telnetUtil.disconnect();
		
		UploadUnitPhoto uploadUnitPhoto = new UploadUnitPhoto();
		uploadUnitPhoto.setOid_partner(datadriven.get("oid_partner"));
		uploadUnitPhoto.setSign_type(datadriven.get("sign_type"));
		uploadUnitPhoto.setUser_id(datadriven.get("user_id"));
		String sign = GenSign.genSign(JSON.parseObject(JSON.toJSONString(uploadUnitPhoto)), datadriven.get("key"));

		System.out.println("sign--：" + sign);
		String uploadUnitPhotoUrl = Property.get("llwallet.uploadUnitPhoto.url");
		Map<String, String> textMap = new HashMap<String, String>();
		// 设置input的key，value
		textMap.put("oid_partner", datadriven.get("oid_partner"));
		textMap.put("user_id", datadriven.get("user_id"));
		textMap.put("sign_type", datadriven.get("sign_type"));
		textMap.put("sign", sign);
		// 设置file的name，路径
		Map<String, String> fileMap = new HashMap<String, String>();
		if (datadriven.get("front_card").length()>0) {
			fileMap.put("front_card", datadriven.get("front_card"));
		}
		if (datadriven.get("back_card").length()>0) {
			fileMap.put("back_card", datadriven.get("back_card"));
		}
		if (datadriven.get("copy_license").length()>0) {
			fileMap.put("copy_license", datadriven.get("copy_license"));
		}
		if (datadriven.get("copy_org").length()>0) {
			fileMap.put("copy_org", datadriven.get("copy_org"));
		}
		if (datadriven.get("bank_openlicense").length()>0) {
			fileMap.put("bank_openlicense", datadriven.get("bank_openlicense"));
		}
		if (datadriven.get("agent_front_card").length()>0) {
			fileMap.put("agent_front_card", datadriven.get("agent_front_card"));
		}
		if (datadriven.get("agent_back_card").length()>0) {
			fileMap.put("agent_back_card", datadriven.get("agent_back_card"));
		}

		String contentType = "";
		String res = HttpUploadFile.formUpload(uploadUnitPhotoUrl, textMap, fileMap, contentType);
		System.out.println(res);
		Reporter.log("接口返回： " + res, true);
		// 企业用户上传照片请求返回检查
		assert res.contains(datadriven.get("result_code"));

		Reporter.log("企业用户上传照片接口测试： " + datadriven.get("comment"), true);
		Reporter.log("===============================================", true);
	}

	@DataProvider(name = "uploadUnitPhoto")
	public Iterator<Object[]> data4uploadUnitPhoto() throws IOException {
		return new ExcelProvider(this, "uploadUnitPhoto");
	}

	@AfterClass
	public void afterClass() {

	}

}
