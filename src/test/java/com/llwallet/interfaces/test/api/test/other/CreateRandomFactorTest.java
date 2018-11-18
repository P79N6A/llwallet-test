package com.llwallet.interfaces.test.api.test.other;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.other.CreateRandomFactor;
import com.llwallet.interfaces.dao.DbUtils;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.JavaBeanUtil;
import com.tools.utils.MyConfig;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import com.tools.utils.TelnetUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 创建密码随机因子接口测试
 */

public class CreateRandomFactorTest {

	WebDriver driver;
	DbUtils dbUtils = new DbUtils();

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
		// Environment.set();
		// driver = GetDriver.get("ie");
		// driver.get("http://192.168.110.89:8080/PasswordGuradWeb/");
		// driver.findElement(By.name("oid_partner")).clear();
		// driver.findElement(By.name("oid_partner")).sendKeys("201701120000283007");
		// driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		//
		// String random_key =
		// driver.findElement(By.className("random_key")).getText();
		// System.out.println("random_key" + random_key);
		// driver.findElement(By.id("ocx_1_down")).clear();
		// driver.findElement(By.id("ocx_1_down")).sendKeys("123456");
		// driver.findElement(By.cssSelector("input[value=\"加密\"]")).click();
	}

	@SuppressWarnings("unchecked")
	@Test(description = "创建密码随机因子接口测试", timeOut = 60000, dataProvider = "provider")
	public void test1(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host_redis"),
				Integer.parseInt(Property.get("port_redis")));
		String ipUpdate = dt.get("createRandomFactor").get("ipUpdate");
		String oid_partner = MyConfig.getPartner(dt.get("createRandomFactor").get("oid_partner"));
		String traderStatUpdate = dt.get("createRandomFactor").get("traderStatUpdate");

		try {
			if (!StringUtils.isBlank(ipUpdate)) {
				dbUtils.updateIpRequest(ipUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(traderStatUpdate)) {
				dbUtils.updateTraderStatus(traderStatUpdate, oid_partner);
			}
			Thread.sleep(10);
			if (!StringUtils.isBlank(oid_partner)) {
				StringBuffer command = new StringBuffer();
				command.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"pay_admin_merchantTRADER_PARA_")
						.append(oid_partner).append("\"])");
				telnetUtil.execute(command.toString());
				telnetUtil.disconnect();
			}

			CreateRandomFactor createRandomFactor = new CreateRandomFactor();
			Object reqObj = JavaBeanUtil.convertMap(createRandomFactor.getClass(),
					excel.getParamMap(dt.get("createRandomFactor")));
			Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
			createRandomFactor.setSign(GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map)),
					dt.get("createRandomFactor").get("key")));
			map.put("sign", createRandomFactor.getSign());
			String reqJson = JSON.toJSONString(map);
			String createRandomFactorUrl = Property.get("llwallet.createRandomFactor.url");
			String rsp = HttpRequest.httpPostWithJSON(createRandomFactorUrl, reqJson);
			// excel.writeCurrentCell("createRandomFactor", "actual_return",
			// rsp);
			Reporter.log("创建密码随机因子接口测试： " + dt.get("createRandomFactor").get("comment"), true);
			System.out.println("===============================================");

			// 创建密码随机因子请求返回检查
			assert rsp.contains(dt.get("createRandomFactor").get("expect_return"));
			if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"请求处理成功\"")) {
				assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
						JSONObject.parseObject(rsp).getString("sign"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!StringUtils.isBlank(ipUpdate)) {
				dbUtils.updateIpRequest("*.*.*.*", oid_partner);
			}
			if (!StringUtils.isBlank(traderStatUpdate)) {
				dbUtils.updateTraderStatus("0", oid_partner);
			}
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
