package com.lianpay.interfaces.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.lianpay.interfaces.bean.ModifyUnitUser;
import com.lianpay.tools.dataprovider.ExcelProvider;
import com.lianpay.tools.http.HttpFixture;
import com.lianpay.tools.utils.GenSign;
import com.lianpay.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 企业用户基本信息修改接口测试
 */

public class ModifyUnitUserTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "企业用户基本信息修改接口测试", timeOut = 60000, dataProvider = "modifyUnitUser")
	public void modifyUnitUser(Map<String, String> datadriven) throws Exception {

		ModifyUnitUser modifyUnitUser = new ModifyUnitUser();
		modifyUnitUser.setOid_partner(datadriven.get("oid_partner"));
		modifyUnitUser.setSign_type(datadriven.get("sign_type"));
		modifyUnitUser.setToken(datadriven.get("token"));
		modifyUnitUser.setUser_id(datadriven.get("user_id"));
		modifyUnitUser.setMob_bind(datadriven.get("mob_bind"));
		modifyUnitUser.setName_user(datadriven.get("name_user"));
		modifyUnitUser.setType_idcard(datadriven.get("type_idcard"));
		modifyUnitUser.setNo_idcard(datadriven.get("no_idcard"));
		modifyUnitUser.setEml_bind(datadriven.get("eml_bind"));
		modifyUnitUser.setAddr_unit(datadriven.get("addr_unit"));
		modifyUnitUser.setAddr_pro(datadriven.get("addr_pro"));
		modifyUnitUser.setAddr_city(datadriven.get("addr_city"));
		modifyUnitUser.setAddr_dist(datadriven.get("addr_dist"));
		modifyUnitUser.setExp_idcard(datadriven.get("exp_idcard"));
		modifyUnitUser.setBusi_user(datadriven.get("busi_user"));
		modifyUnitUser.setName_unit(datadriven.get("name_unit"));
		modifyUnitUser.setNum_license(datadriven.get("num_license"));
		modifyUnitUser.setNum_taxreg(datadriven.get("num_taxreg"));
		modifyUnitUser.setExp_taxreg(datadriven.get("exp_taxreg"));
		modifyUnitUser.setOrg_code(datadriven.get("org_code"));
		modifyUnitUser.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(modifyUnitUser)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(modifyUnitUser);
		String modifyUnitUserUrl = Property.get("llwallet.modifyUnitUser.url");
				
		// 企业用户基本信息修改请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(modifyUnitUserUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 企业用户基本信息修改请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("企业用户基本信息修改接口测试： " + datadriven.get("comment"), true);
		Reporter.log("===============================================", true);
	}
   
	@DataProvider(name = "modifyUnitUser")
	public Iterator<Object[]> data4modifyUnitUser() throws IOException {
		return new ExcelProvider(this, "modifyUnitUser", 2);
	}

	@AfterClass
	public void afterClass() {

	}

}
