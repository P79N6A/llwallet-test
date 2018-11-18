package com.llwallet.interfaces.test.api.online.tpay;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.tpay.OpenUser;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.utils.GenSign;
import com.tools.utils.HttpUploadFile;
import com.tools.utils.JavaBeanUtil;
import com.tools.utils.MyDate;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import com.tools.utils.SampleFileUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 消费通开户申请接口测试
 */

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class OpenUserOnlineTest extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings("unchecked")
	@Test(description = "消费通开户申请接口测试", timeOut = 60000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		// 消费通开户申请
		OpenUser openUser = new OpenUser();
		Object reqObj = JavaBeanUtil.convertMap(openUser.getClass(), excel.getParamMap(dt.get("openUser")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		openUser.setSign(
				GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map)), dt.get("openUser").get("key")));
		map.put("sign", openUser.getSign());
		Map<String, String> textMap = new HashMap<String, String>();
		textMap.put("openload", JSON.toJSONString(map));
		Reporter.log("req-------" + JSONObject.toJSON(map).toString(),true);
		String contentType = "image/png";// image/png
		// 设置file的name，路径
		Map<String, String> fileMap = new HashMap<String, String>();
		if (dt.get("openUser").get("front_card").length() > 0) {
			fileMap.put("front_card", dt.get("openUser").get("front_card"));
		}
		if (dt.get("openUser").get("back_card").length() > 0) {
			fileMap.put("back_card", dt.get("openUser").get("back_card"));
		}
		String openUserUrl = Property.get("tpay.openUser.url.online");

		// 消费通开户申请请求提交
		String rsp = HttpUploadFile.formUpload(openUserUrl, textMap, fileMap, contentType);
		Reporter.log("rsp-------" + rsp,true);

		// excel.writeCurrentCell("openUser", "actual_return", rsp);
		Reporter.log("消费通开户申请接口测试： " + dt.get("openUser").get("comment"), true);
		System.out.println("===============================================");
		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//openUser.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//openUser.txt", JSONObject.toJSON(map).toString());
		SampleFileUtils.appendLine("D://TA//log//openUser.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//openUser.txt", "===============================================");

		// 消费通开户申请请求返回检查
		assert rsp.contains(dt.get("openUser").get("expect_return"));
		// 返回验签&数据库检查
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