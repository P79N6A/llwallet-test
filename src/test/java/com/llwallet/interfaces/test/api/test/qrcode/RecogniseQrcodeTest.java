package com.llwallet.interfaces.test.api.test.qrcode;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.qrcode.CreateQrcode;
import com.llwallet.interfaces.bean.qrcode.RecogniseQrcode;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 识别二维码加密串接口测试
 */

public class RecogniseQrcodeTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "识别二维码加密串接口测试", timeOut = 60000, dataProvider = "recogniseQrcode")
	public void recogniseQrcode(Map<String, String> datadriven) throws Exception {
		
		CreateQrcode createQrcode = new CreateQrcode();
		createQrcode.setOid_partner(datadriven.get("oid_partner"));
		createQrcode.setSign_type(datadriven.get("sign_type"));
		createQrcode.setUser_id(datadriven.get("user_id"));
		createQrcode.setType_user(datadriven.get("type_user"));
		createQrcode.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(createQrcode)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(createQrcode);
		String createQrcodeUrl = Property.get("llwallet.createQrcode.url");

		// 生成二维码加密串请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(createQrcodeUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		CreateQrcode rsp=JSON.parseObject(hf.getResponseBody(), CreateQrcode.class);
		String qrcode_data = rsp.getQrcode_data();
//		String qrcode_data = StringUtils.substringBetween(hf.getResponseBody(), "qrcode_data\":\"", "\"");
		System.out.println("二维码加密数据:"+qrcode_data);

		RecogniseQrcode recogniseQrcode = new RecogniseQrcode();
		recogniseQrcode.setOid_partner(datadriven.get("oid_partner1"));
		recogniseQrcode.setSign_type(datadriven.get("sign_type1"));
		if ("auto".equals(datadriven.get("qrcode_data"))){
			recogniseQrcode.setQrcode_data(qrcode_data);
		}else{
			recogniseQrcode.setQrcode_data(datadriven.get("qrcode_data"));
		}
		recogniseQrcode.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(recogniseQrcode)),datadriven.get("key")));
		String reqJson1 = JSON.toJSONString(recogniseQrcode);
		String recogniseQrcodeUrl = Property.get("llwallet.recogniseQrcode.url");

		// 识别二维码加密串请求提交
		hf.nextRequest();
		hf.setUrl(recogniseQrcodeUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 识别二维码加密串请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("识别二维码加密串接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "recogniseQrcode")
	public Iterator<Object[]> data4recogniseQrcode() throws IOException {
		return new ExcelProvider(this, "recogniseQrcode");
	}

	@AfterClass
	public void afterClass() {

	}

}
