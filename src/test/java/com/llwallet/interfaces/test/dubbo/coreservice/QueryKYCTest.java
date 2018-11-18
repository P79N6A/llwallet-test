package com.llwallet.interfaces.test.dubbo.coreservice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import com.alibaba.fastjson.JSON;
import com.tools.dataprovider.ExcelProvider;
import com.tools.utils.ApiUtils;
import com.tools.utils.Property;
import com.yintong.llwallet.domain.TrustAuth;
import com.yintong.llwallet.service.ILLWalletCoreService;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.Reporter;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class QueryKYCTest extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Autowired
	private ILLWalletCoreService llwalletCoreService;

	@Test(description = "查询KYC等级接口测试", timeOut = 60000, dataProvider = "queryKYC")
	public void queryKYC(Map<String, String> datadriven) throws Exception {

		String correlationID = ApiUtils.getCurrentDateMillisecondStr();

		TrustAuth trustAuth = new TrustAuth();
		trustAuth.setCorrelationID(correlationID);
		trustAuth.setOid_partner(datadriven.get("oid_partner"));
		trustAuth.setOid_userno(datadriven.get("oid_userno"));
		trustAuth.setUser_id(datadriven.get("user_id"));
		trustAuth.setMoney_order(datadriven.get("money_order"));
		trustAuth.setRet_code(datadriven.get("ret_code"));
		trustAuth.setRet_msg(datadriven.get("ret_msg"));
		trustAuth.setRisk_item(datadriven.get("risk_item"));
		trustAuth.setNo_idcard(datadriven.get("no_idcard"));
		trustAuth.setType_idcard(datadriven.get("type_idcard"));
		trustAuth.setName_user(datadriven.get("name_user"));
		trustAuth.setBind_mob(datadriven.get("bind_mob"));
		trustAuth.setLevel_auth(datadriven.get("level_auth"));
		trustAuth.setStat_user(datadriven.get("stat_user"));
		trustAuth.setAuth_chnl(datadriven.get("auth_chnl"));
		trustAuth.setAuth_detail(datadriven.get("auth_detail"));
		trustAuth.setId_expiration_time(datadriven.get("id_expiration_time"));
		trustAuth.setKyc_stat(datadriven.get("kyc_stat"));
		trustAuth.setAmt_kyc_year(datadriven.get("amt_kyc_year"));
		trustAuth.setBack_card(new ByteArrayInputStream(datadriven.get("back_card").getBytes()));
		trustAuth.setBank_openlicense(new ByteArrayInputStream(datadriven.get("bank_openlicense").getBytes()));
		trustAuth.setCopy_license(new ByteArrayInputStream(datadriven.get("copy_license").getBytes()));
		trustAuth.setCopy_org(new ByteArrayInputStream(datadriven.get("copy_org").getBytes()));
		trustAuth.setFront_card(new ByteArrayInputStream(datadriven.get("front_card").getBytes()));

		System.out.println("trustAuth： " + JSON.toJSONString(trustAuth));
		String rsp = JSON.toJSONString(llwalletCoreService.queryKYC(trustAuth));
		System.out.println("查询KYC等级返回：" + rsp);
		Reporter.log("查询KYC等级返回：" + rsp);
		// 查询KYC等级请求返回检查
		assert rsp.contains(datadriven.get("result_code"));

		Reporter.log("查询KYC等级接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "queryKYC")
	public Iterator<Object[]> data4queryKYC() throws IOException {
		return new ExcelProvider(this, "queryKYC");
	}

	@AfterClass
	public void afterClass() {

	}
}