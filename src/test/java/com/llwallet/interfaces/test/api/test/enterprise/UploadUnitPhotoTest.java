package com.llwallet.interfaces.test.api.test.enterprise;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.enterprise.UploadUnitPhoto;
import com.tools.dataprovider.ExcelProvider;
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
 * 企业用户上传照片接口测试
 */

public class UploadUnitPhotoTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	/**
	 * @param datadriven
	 * @throws Exception
	 */
	@Test(description = "企业用户上传照片接口测试", timeOut = 60000, dataProvider = "uploadUnitPhoto")
	public void uploadUnitPhoto(Map<String, String> datadriven) throws Exception {

		String user_id = MyConfig.getUser(datadriven.get("user_id"));
		String oid_partner = MyConfig.getPartner(datadriven.get("oid_partner"));
		// 更新用户测试数据
//		DbUtils dbUtils = new DbUtils();
//		String oid_userno = dbUtils.queryOidUserNo(oid_partner, user_id).getOid_userno().trim();
//		dbUtils.updatePhoto(oid_userno);
//
		// 清除用户缓存
//		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host"), Integer.parseInt(Property.get("port")));
//		StringBuffer command = new StringBuffer();
//		command.append("delete ").append(datadriven.get("oid_partner")).append(datadriven.get("user_id")).append("userBusiInfo");
//		telnetUtil.execute(command.toString());
//		telnetUtil.disconnect();

		UploadUnitPhoto uploadUnitPhoto = new UploadUnitPhoto();
		uploadUnitPhoto.setOid_partner(oid_partner);
		uploadUnitPhoto.setSign_type(datadriven.get("sign_type"));
		uploadUnitPhoto.setUser_id(user_id);
		uploadUnitPhoto.setNotify_url(datadriven.get("notify_url"));
		String sign = GenSign.genSign(JSON.parseObject(JSON.toJSONString(uploadUnitPhoto)), datadriven.get("key"));

		System.out.println("sign-----：" + sign);
		String uploadUnitPhotoUrl = Property.get("llwallet.uploadUnitPhoto.url");
		Map<String, String> textMap = new HashMap<String, String>();
		// 设置input的key，value
		textMap.put("oid_partner", oid_partner);
		textMap.put("user_id", user_id);
		textMap.put("sign_type", datadriven.get("sign_type"));
		textMap.put("sign", sign);
		textMap.put("notify_url", datadriven.get("notify_url"));
		// 设置file的name，路径
		Map<String, String> fileMap = new HashMap<String, String>();
		if (datadriven.get("front_card").length() > 0) {
			fileMap.put("front_card", datadriven.get("front_card"));
		}
		if (datadriven.get("back_card").length() > 0) {
			fileMap.put("back_card", datadriven.get("back_card"));
		}
		if (datadriven.get("copy_license").length() > 0) {
			fileMap.put("copy_license", datadriven.get("copy_license"));
		}
		if (datadriven.get("copy_org").length() > 0) {
			fileMap.put("copy_org", datadriven.get("copy_org"));
		}
		if (datadriven.get("bank_openlicense").length() > 0) {
			fileMap.put("bank_openlicense", datadriven.get("bank_openlicense"));
		}
		if (datadriven.get("agent_front_card").length() > 0) {
			fileMap.put("agent_front_card", datadriven.get("agent_front_card"));
		}
		if (datadriven.get("agent_back_card").length() > 0) {
			fileMap.put("agent_back_card", datadriven.get("agent_back_card"));
		}

		String contentType = "";
		String rsp = HttpUploadFile.formUpload(uploadUnitPhotoUrl, textMap, fileMap, contentType);
		System.out.println("req-----:" + JSONObject.toJSON(textMap));
		Thread.sleep(100);
		System.out.println(rsp);
		Reporter.log("接口返回： " + rsp, true);
		// 企业用户上传照片请求返回检查
		assert rsp.contains(datadriven.get("result_code"));

		Reporter.log("企业用户上传照片接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
	
	@SuppressWarnings("unchecked")
	@Test(description = "企业用户上传照片接口测试", timeOut = 60000, dataProvider = "provider1")
	public void baseline(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel)
			throws Exception {

		UploadUnitPhoto uploadUnitPhoto = new UploadUnitPhoto();
		
		Object reqObj = JavaBeanUtil.convertMap(uploadUnitPhoto.getClass(), excel.getParamMap(dt.get("baseline")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		uploadUnitPhoto.setSign(
				GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map)), dt.get("baseline").get("key")));

		String uploadUnitPhotoUrl = Property.get("llwallet.uploadUnitPhoto.url");
		Map<String, String> textMap = new HashMap<String, String>();
		// 设置input的key，value
		textMap.put("oid_partner", MyConfig.getPartner(dt.get("baseline").get("oid_partner")));
		textMap.put("user_id", MyConfig.getUser(dt.get("baseline").get("user_id")));
		textMap.put("sign_type", dt.get("baseline").get("sign_type"));
		textMap.put("notify_url", dt.get("baseline").get("notify_url"));
		textMap.put("sign", uploadUnitPhoto.getSign());
		
		// 设置file的name，路径
		Map<String, String> fileMap = new HashMap<String, String>();
		if (!StringUtils.isBlank(dt.get("baseline").get("front_card"))) {
			fileMap.put("front_card", dt.get("baseline").get("front_card"));
		}
		if (!StringUtils.isBlank(dt.get("baseline").get("back_card"))) {
			fileMap.put("back_card", dt.get("baseline").get("back_card"));
		}
		if (!StringUtils.isBlank(dt.get("baseline").get("copy_license"))) {
			fileMap.put("copy_license", dt.get("baseline").get("copy_license"));
		}
		if (!StringUtils.isBlank(dt.get("baseline").get("copy_org"))) {
			fileMap.put("copy_org", dt.get("baseline").get("copy_org"));
		}		
		if (!StringUtils.isBlank(dt.get("baseline").get("bank_openlicense"))) {
			fileMap.put("bank_openlicense", dt.get("baseline").get("bank_openlicense"));
		}		
		if (!StringUtils.isBlank(dt.get("baseline").get("agent_front_card"))) {
			fileMap.put("agent_front_card", dt.get("baseline").get("agent_front_card"));
		}		
		if (!StringUtils.isBlank(dt.get("baseline").get("agent_back_card"))) {
			fileMap.put("agent_back_card", dt.get("baseline").get("agent_back_card"));
		}

		String contentType = "image/png";
		String rsp = HttpUploadFile.formUpload(uploadUnitPhotoUrl, textMap, fileMap, contentType);
		System.out.println(rsp);
		Reporter.log("接口返回： " + rsp, true);

		Reporter.log("企业用户上传照片接口测试： " + dt.get("baseline").get("comment"), true);
		System.out.println("===============================================");

		// 企业用户上传照片请求返回检查
		assert rsp.contains(dt.get("baseline").get("expect_return"));

	}
	
	@DataProvider(name = "uploadUnitPhoto")
	public Iterator<Object[]> data4uploadUnitPhoto() throws IOException {
		return new ExcelProvider(this, "uploadUnitPhoto",411);
	}

	@DataProvider(name = "provider1")
	public Iterator<Object[]> provider1() throws IOException {
		return new ExcelProvider2(this, 3,4);
	}
	
	@AfterClass
	public void afterClass() {

	}

}