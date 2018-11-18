package com.llwallet.interfaces.test.api.online.tpay;

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
import com.llwallet.interfaces.bean.tpay.UploadPhotos;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.utils.GenSign;
import com.tools.utils.HttpUploadFile;
import com.tools.utils.JavaBeanUtil;
import com.tools.utils.MyDate;
import com.tools.utils.Property;
import com.tools.utils.SampleFileUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 消费通上传照片接口测试
 */

public class UploadPhotosOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings("unchecked")
	@Test(description = "消费通上传照片接口测试", timeOut = 60000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		String uploadPhotosUrl = Property.get("tpay.uploadPhotos.url.online");

		try {
			UploadPhotos uploadPhotos = new UploadPhotos();
			Object reqObj = JavaBeanUtil.convertMap(uploadPhotos.getClass(), excel.getParamMap(dt.get("uploadPhotos")));
			Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
			uploadPhotos.setSign(
					GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map)), dt.get("uploadPhotos").get("key")));
			map.put("sign", uploadPhotos.getSign());
			Map<String, String> textMap = new HashMap<String, String>();
			textMap.put("openload", JSON.toJSONString(map));
			Reporter.log("req-------" + JSONObject.toJSON(map).toString(), true);
			String contentType = "image/png";// image/png
			// 设置file的name，路径
			Map<String, String> fileMap = new HashMap<String, String>();

			if (!StringUtils.isBlank(dt.get("uploadPhotos").get("front_card"))) {
				fileMap.put("front_card", dt.get("uploadPhotos").get("front_card"));
			}
			if (!StringUtils.isBlank(dt.get("uploadPhotos").get("back_card"))) {
				fileMap.put("back_card", dt.get("uploadPhotos").get("back_card"));
			}

			// 消费通上传照片请求提交
			String rsp = HttpUploadFile.formUpload(uploadPhotosUrl, textMap, fileMap, contentType);
			Reporter.log("rsp-------" + rsp, true);

			// excel.writeCurrentCell("uploadPhotos", "actual_return", rsp);
			Reporter.log("消费通上传照片接口测试： " + dt.get("uploadPhotos").get("comment"), true);
			System.out.println("===============================================");
			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//log//uploadPhotos.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//log//uploadPhotos.txt", JSONObject.toJSON(map).toString());
			SampleFileUtils.appendLine("D://TA//log//uploadPhotos.txt", rsp);
			SampleFileUtils.appendLine("D://TA//log//uploadPhotos.txt",
					"===============================================");

			// 消费通上传照片请求返回检查
			assert rsp.contains(dt.get("uploadPhotos").get("expect_return"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
		return new ExcelProvider2(this, 4);
	}

	@AfterClass
	public void afterClass() {

	}
}