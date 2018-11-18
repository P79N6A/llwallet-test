package com.llwallet.interfaces.test.api.online.custom;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.custom.ActiveAcct;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.MyConfig;
import com.tools.utils.MyDate;
import com.tools.utils.Property;
import com.tools.utils.SampleFileUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 钱包账户激活接口测试
 */

public class ActiveAcctOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "钱包账户激活接口测试", timeOut = 60000, dataProvider = "activeAcct")
	public void activeAcct(Map<String, String> datadriven) throws Exception {

		ActiveAcct activeAcct = new ActiveAcct();
		activeAcct.setOid_partner(MyConfig.getPartner(datadriven.get("oid_partner")));
		activeAcct.setSign_type(datadriven.get("sign_type"));
		activeAcct.setUser_id(MyConfig.getUser(datadriven.get("user_id")));
		activeAcct.setAddr_city(datadriven.get("addr_city"));
		activeAcct.setAddr_conn(datadriven.get("addr_conn"));
		activeAcct.setAddr_dist(datadriven.get("addr_dist"));
		activeAcct.setAddr_pro(datadriven.get("addr_pro"));
		activeAcct.setConsent_agreement(datadriven.get("consent_agreement"));
		activeAcct.setEml_bind(datadriven.get("eml_bind"));
		activeAcct.setMob_bind(datadriven.get("mob_bind"));
		activeAcct.setOid_job(datadriven.get("oid_job"));
		activeAcct.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(activeAcct)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(activeAcct);
		String activeAcctUrl = Property.get("llwallet.activeAcct.url.online");

		// 钱包账户激活请求提交
		String rsp = HttpRequest.httpPostWithJSON(activeAcctUrl, reqJson);

		Reporter.log("钱包账户激活接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
		
		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//activeAcct.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//activeAcct.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//activeAcct.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//activeAcct.txt", "===============================================");
		
		// 钱包账户激活请求返回检查
		assert rsp.contains(datadriven.get("result_code"));
	}
   
	@DataProvider(name = "activeAcct")
	public Iterator<Object[]> data4activeAcct() throws IOException {
		return new ExcelProvider(this, "activeAcct",7);
	}

	@AfterClass
	public void afterClass() {

	}

}
