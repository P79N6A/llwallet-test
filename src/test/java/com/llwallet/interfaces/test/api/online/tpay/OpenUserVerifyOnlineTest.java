package com.llwallet.interfaces.test.api.online.tpay;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.tpay.OpenUserVerify;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.MyDate;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import com.tools.utils.SampleFileUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 消费通开户验证接口测试
 */

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class OpenUserVerifyOnlineTest extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings({ "rawtypes" })
	@Test(description = "消费通开户申请-验证接口测试", timeOut = 6000000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		// 消费通开户验证
		OpenUserVerify openUserVerify = new OpenUserVerify();
		Map<String, String> params = new HashMap<String, String>();
		Iterator it = dt.get("openUserVerify").entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (!entry.getValue().equals("") && StringUtils.startsWith((String) entry.getKey(), "openUserVerify.")) {
				params.put(((String) entry.getKey()).split("\\.")[1], (String) entry.getValue());
			}
		}
		BeanUtils.populate(openUserVerify, params);
		openUserVerify.setSign(GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(openUserVerify)),
				dt.get("openUserVerify").get("openUserVerify.key")));
		String reqJson1 = JSON.toJSONString(openUserVerify);
		String openUserVerifyUrl = Property.get("tpay.openUserVerify.url.online");
		// 消费通开户验证请求提交
		String rsp1 = HttpRequest.httpPostWithJSON(openUserVerifyUrl, reqJson1);
		// excel.writeCurrentCell("openUserVerify", "actual_return", rsp1);
		Reporter.log("消费通开户验证接口测试： " + dt.get("openUserVerify").get("comment"), true);
		System.out.println("===============================================");
		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//openUserVerify.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//openUserVerify.txt", reqJson1);
		SampleFileUtils.appendLine("D://TA//log//openUserVerify.txt", rsp1);
		SampleFileUtils.appendLine("D://TA//log//openUserVerify.txt","===============================================");
		
		// 消费通开户验证请求返回检查
		assert rsp1.contains(dt.get("openUserVerify").get("expect_return"));
		// 返回验签&数据库检查
		if (rsp1.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")){
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp1)), JSONObject.parseObject(rsp1).getString("sign"));
		}		
	}

	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
		return new ExcelProvider2(this,4);
	}
	
	@AfterClass
	public void afterClass() {

	}
}