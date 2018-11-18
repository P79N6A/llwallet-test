package com.llwallet.interfaces.test.api.online.qrcode;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.qrcode.CreateQrcode;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 生成二维码加密串接口测试
 */

public class CreateQrcodeOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "生成二维码加密串接口测试", timeOut = 60000, dataProvider = "createQrcode")
	public void createQrcode(Map<String, String> datadriven) throws Exception {
		
		CreateQrcode createQrcode = new CreateQrcode();
		createQrcode.setOid_partner(datadriven.get("oid_partner"));
		createQrcode.setSign_type(datadriven.get("sign_type"));
		createQrcode.setUser_id(datadriven.get("user_id"));
		createQrcode.setType_user(datadriven.get("type_user"));
		createQrcode.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(createQrcode)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(createQrcode);
		String createQrcodeUrl = Property.get("llwallet.createQrcode.url.online");

		// 生成二维码加密串请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(createQrcodeUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		CreateQrcode rsp=JSON.parseObject(hf.getResponseBody(), CreateQrcode.class);
		String qrcode_data = rsp.getQrcode_data();
		System.out.println("二维码加密数据:"+qrcode_data);
		
		// 生成二维码加密串请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("生成二维码加密串接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "createQrcode")
	public Iterator<Object[]> data4createQrcode() throws IOException {
		return new ExcelProvider(this, "createQrcode");
	}

	@AfterClass
	public void afterClass() {

	}

}
