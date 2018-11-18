package com.llwallet.interfaces.test.api.online.custom;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.custom.SecuredPrepayModify;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.ApiUtils;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 担保交易修改接口测试
 */

public class SecuredPrepayModifyOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "担保交易修改接口测试", timeOut = 60000, dataProvider = "securedPrepayModify1")
	public void securedPrepayModify1(Map<String, String> datadriven) throws Exception {

		Thread.sleep(1000);
		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();

		SecuredPrepayModify securedPrepayModify = new SecuredPrepayModify();
		securedPrepayModify.setOid_partner(datadriven.get("oid_partner1"));
		securedPrepayModify.setSign_type(datadriven.get("sign_type1"));
		securedPrepayModify.setApi_version(datadriven.get("api_version1"));		
		if ("auto".equals(datadriven.get("no_order1"))){
			securedPrepayModify.setNo_order(no_order);
		}else{
			securedPrepayModify.setNo_order(datadriven.get("no_order1"));
		}
		if ("auto".equals(datadriven.get("dt_order1"))){
			securedPrepayModify.setDt_order(dt_order);
		}else{
			securedPrepayModify.setDt_order(datadriven.get("dt_order1"));
		}
		securedPrepayModify.setOid_paybill(datadriven.get("oid_paybill1"));
		securedPrepayModify.setOrign_col_custtype(datadriven.get("orign_col_custtype1"));
		securedPrepayModify.setOrign_col_custid(datadriven.get("orign_col_custid1"));
		securedPrepayModify.setOrign_col_biz(datadriven.get("orign_col_biz1"));
		securedPrepayModify.setDest_col_custtype(datadriven.get("dest_col_custtype1"));
		securedPrepayModify.setDest_col_custid(datadriven.get("dest_col_custid1"));
		securedPrepayModify.setDest_col_biz(datadriven.get("dest_col_biz1"));
		securedPrepayModify.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(securedPrepayModify)),datadriven.get("key1")));
		String reqJson1 = JSON.toJSONString(securedPrepayModify);
		String securedPrepayModifyUrl = Property.get("llwallet.securedPrepayModify.url.online");

		// 担保交易修改请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.addHeaderValue("Content-Type", "application/json");
		hf.nextRequest();
		hf.setUrl(securedPrepayModifyUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 担保交易修改请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("担保交易修改接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
	
	@DataProvider(name = "securedPrepayModify1")
	public Iterator<Object[]> data4securedPrepayModify1() throws IOException {
		return new ExcelProvider(this, "securedPrepayModify1",2);
	}
	
	@AfterClass
	public void afterClass() {

	}

}