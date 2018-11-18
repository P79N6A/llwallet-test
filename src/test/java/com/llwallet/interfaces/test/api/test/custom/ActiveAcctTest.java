package com.llwallet.interfaces.test.api.test.custom;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.custom.ActiveAcct;
import com.llwallet.interfaces.bean.custom.OpenUnactivedAcct;
import com.llwallet.interfaces.dao.DbUtils;
import com.tools.dataprovider.ExcelProvider;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.JavaBeanUtil;
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

public class ActiveAcctTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings("unchecked")
	@Test(description = "钱包账户激活接口测试", timeOut = 60000, dataProvider = "activeAcct")
	public void activeAcct(Map<String, String> datadriven) throws Exception {

		ActiveAcct activeAcct = new ActiveAcct();
		Object reqObj = JavaBeanUtil.convertMap(activeAcct.getClass(), datadriven);
		activeAcct.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), datadriven.get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", activeAcct.getSign());
		String reqJson = JSON.toJSONString(map);
		String activeAcctUrl = Property.get("llwallet.activeAcct.url");

		// 钱包账户激活请求提交
		String rsp = HttpRequest.httpPostWithJSON(activeAcctUrl, reqJson);

		Reporter.log("钱包账户激活接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// 钱包账户激活请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test(description = "钱包账户激活接口测试", timeOut = 60000, dataProvider = "provider1")
	public void baseline(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel)
			throws Exception {

		// 账户待激活开户
		OpenUnactivedAcct openUnactivedAcct = new OpenUnactivedAcct();
		Object reqObj = JavaBeanUtil.convertMap(openUnactivedAcct.getClass(), dt.get("baseline"));
		openUnactivedAcct
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), dt.get("baseline").get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", openUnactivedAcct.getSign());
		String reqJson = JSON.toJSONString(map);
		String openUnactivedAcctUrl = Property.get("llwallet.openUnactivedAcct.url");
		String rsp = HttpRequest.httpPostWithJSON(openUnactivedAcctUrl, reqJson);

		String user_id = JSONObject.parseObject(rsp).getString("user_id");

		// 账户激活
		ActiveAcct activeAcct = new ActiveAcct();
		Map<String, String> params = new HashMap<String, String>();
		Iterator it = dt.get("baseline").entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (!entry.getValue().equals("") && StringUtils.startsWith((String) entry.getKey(), "activeacct.")) {
				params.put(((String) entry.getKey()).split("\\.")[1], (String) entry.getValue());
			}
		}
		BeanUtils.populate(activeAcct, params);
		if ("get".equals(dt.get("baseline").get("activeacct.user_id"))) {
			activeAcct.setUser_id(user_id);
		}
		activeAcct.setSign(GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(activeAcct)),
				dt.get("baseline").get("activeacct.key")));
		String reqJson1 = JSON.toJSONString(activeAcct);
		String activeAcctUrl = Property.get("llwallet.activeAcct.url");
		String rsp1 = HttpRequest.httpPostWithJSON(activeAcctUrl, reqJson1);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//activeAcct.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//activeAcct.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//activeAcct.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//activeAcct.txt", reqJson1);
		SampleFileUtils.appendLine("D://TA//log//activeAcct.txt", rsp1);
		SampleFileUtils.appendLine("D://TA//log//activeAcct.txt", "===============================================");

		// 开户成功数据检查
		if (rsp1.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			Thread.sleep(500);
			String oid_userno_new = JSONObject.parseObject(rsp1).getString("oid_userno");
			DbUtils dbUtils = new DbUtils();
			String stat_acct = dbUtils.queryAcctStat(oid_userno_new).getStat_acct();
			Reporter.log("stat_acct： " + stat_acct, true);
			assert dt.get("baseline").get("expect_acctstat").equals(stat_acct);
		}

		Reporter.log("个人用户开户接口测试： " + dt.get("baseline").get("comment"), true);
		System.out.println("===============================================");

		// 个人用户开户请求返回检查
		assert rsp1.contains(dt.get("baseline").get("expect_return"));

	}

	@DataProvider(name = "activeAcct")
	public Iterator<Object[]> data4activeAcct() throws IOException {
		return new ExcelProvider(this, "activeAcct", 1);
	}

	@DataProvider(name = "provider1")
	public Iterator<Object[]> provider1() throws IOException {
		return new ExcelProvider2(this, 3);
	}

	@AfterClass
	public void afterClass() {

	}

}
