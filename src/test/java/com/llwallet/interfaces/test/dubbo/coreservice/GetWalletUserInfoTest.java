package com.llwallet.interfaces.test.dubbo.coreservice;

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
import com.yintong.llwallet.domain.LLWalletUserInfo;
import com.yintong.llwallet.service.ILLWalletCoreService;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.Reporter;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class GetWalletUserInfoTest extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Autowired
	private ILLWalletCoreService llwalletCoreService;

	@Test(description = "获取钱包用户基本信息接口测试", timeOut = 60000, dataProvider = "getWalletUserInfo")
	public void getWalletUserInfo(Map<String, String> datadriven) throws Exception {

		String correlationID = ApiUtils.getCurrentDateMillisecondStr();
		
		LLWalletUserInfo walletUserInfo = new LLWalletUserInfo();
		walletUserInfo.setCorrelationID(correlationID);
		walletUserInfo.setOid_partner(datadriven.get("oid_partner"));
		walletUserInfo.setOid_userno(datadriven.get("oid_userno"));
		walletUserInfo.setUser_id(datadriven.get("user_id"));
		walletUserInfo.setMoney_order(datadriven.get("money_order"));
		walletUserInfo.setRet_code(datadriven.get("ret_code"));
		walletUserInfo.setRet_msg(datadriven.get("ret_msg"));
		walletUserInfo.setRisk_item(datadriven.get("risk_item"));
		walletUserInfo.setBusi_level(datadriven.get("busi_level"));
		walletUserInfo.setNeadCalculateBusiLevelBykycStatus(
				Boolean.parseBoolean(datadriven.get("neadCalculateBusiLevelBykycStatus")));
		walletUserInfo.setUser_login(datadriven.get("user_login"));
		walletUserInfo.setName(datadriven.get("name"));
		walletUserInfo.setId_no(datadriven.get("id_no"));
		walletUserInfo.setId_type(datadriven.get("id_type"));
		walletUserInfo.setFailReason(datadriven.get("failReason"));
		walletUserInfo.setMemo(datadriven.get("memo"));
		walletUserInfo.setKycStatus(datadriven.get("kycStatus"));
		walletUserInfo.setUserType(datadriven.get("userType"));
		walletUserInfo.setEnterpriseType(datadriven.get("enterpriseType"));

		System.out.println("walletUserInfo： " + JSON.toJSONString(walletUserInfo));
		String rsp = JSON.toJSONString(llwalletCoreService.getWalletUserInfo(walletUserInfo));
		System.out.println("获取钱包用户基本信息返回：" + rsp);
		Reporter.log("获取钱包用户基本信息返回：" + rsp);
		// 获取钱包用户基本信息请求返回检查
		assert rsp.contains(datadriven.get("result_code"));

		Reporter.log("获取钱包用户基本信息接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "getWalletUserInfo")
	public Iterator<Object[]> data4getWalletUserInfo() throws IOException {
		return new ExcelProvider(this, "getWalletUserInfo");
	}

	@AfterClass
	public void afterClass() {

	}
}