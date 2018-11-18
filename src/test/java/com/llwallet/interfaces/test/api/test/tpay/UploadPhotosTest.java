package com.llwallet.interfaces.test.api.test.tpay;

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
import com.llwallet.interfaces.dao.DbUtils;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.utils.GenSign;
import com.tools.utils.HttpUploadFile;
import com.tools.utils.JavaBeanUtil;
import com.tools.utils.MyConfig;
import com.tools.utils.MyDate;
import com.tools.utils.Property;
import com.tools.utils.SampleFileUtils;
import com.tools.utils.TelnetUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 消费通上传照片接口测试
 */

public class UploadPhotosTest {

	DbUtils dbUtils = new DbUtils();

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings("unchecked")
	@Test(description = "消费通上传照片接口测试", timeOut = 60000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host_redis"),
				Integer.parseInt(Property.get("port_redis")));
		String ipUpdate = dt.get("uploadPhotos").get("ipUpdate");
		String traderStatUpdate = dt.get("uploadPhotos").get("traderStatUpdate");
		String userStatUpdate = dt.get("uploadPhotos").get("userStatUpdate");
		String userBusiStatUpdate = dt.get("uploadPhotos").get("userBusiStatUpdate");
		String oid_partner = MyConfig.getPartner(dt.get("uploadPhotos").get("oid_partner"));
		String user_id = MyConfig.getUser(dt.get("uploadPhotos").get("user_id"));
		String no_idcard = dt.get("uploadPhotos").get("no_idcard");
		String uploadPhotosUrl = Property.get("tpay.uploadPhotos.url");

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
			if (!StringUtils.isBlank(ipUpdate)) {
				dbUtils.updateIpRequest(ipUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(traderStatUpdate)) {
				dbUtils.updateTraderStatus(traderStatUpdate, oid_partner);
			}
			if (!StringUtils.isBlank(userStatUpdate)) {
				dbUtils.updateUserStat(userStatUpdate, user_id);
			}
			if (!StringUtils.isBlank(userBusiStatUpdate)) {
				dbUtils.updateUserBusiStat(userBusiStatUpdate, user_id);
			}
			Thread.sleep(50);
			if ((!StringUtils.isBlank(oid_partner)) && (!StringUtils.isBlank(user_id))) {
				StringBuffer command1 = new StringBuffer();
				StringBuffer command2 = new StringBuffer();
				StringBuffer command3 = new StringBuffer();
				StringBuffer command4 = new StringBuffer();
				command1.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"tpayapi_").append(oid_partner)
						.append("_").append(user_id).append("_tpay_suffix_upload_person").append("\"])");
				command2.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"tpayapi_").append(oid_partner)
						.append("_").append(no_idcard).append("_tpay_suffix_upload_person").append("\"])");
				command3.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"pay_admin_merchantTRADER_PARA_")
						.append(oid_partner).append("\"])");
				command4.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"TRADER_").append(user_id)
						.append(oid_partner).append("llwalltet_userbase\"])");
				telnetUtil.execute(command1.toString());
				telnetUtil.execute(command2.toString());
				telnetUtil.execute(command3.toString());
				telnetUtil.execute(command4.toString());
				telnetUtil.disconnect();
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
			if (!StringUtils.isBlank(ipUpdate)) {
				dbUtils.updateIpRequest("*.*.*.*", oid_partner);
			}
			if (!StringUtils.isBlank(traderStatUpdate)) {
				dbUtils.updateTraderStatus("0", oid_partner);
			}
			if (!StringUtils.isBlank(userStatUpdate)) {
				dbUtils.updateUserStat("0", user_id);
			}
			if (!StringUtils.isBlank(userBusiStatUpdate)) {
				dbUtils.updateUserBusiStat("1", user_id);
			}
		}
	}

	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
		return new ExcelProvider2(this);
	}

	@AfterClass
	public void afterClass() {

	}
}