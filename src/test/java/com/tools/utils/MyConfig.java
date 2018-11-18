package com.tools.utils;

import java.util.Random;
import java.util.UUID;

import com.llwallet.interfaces.dao.DbUtils;

/** 
 * 配置数据类 
 * @cfg   
 * @version 1.0 
 */

import com.tools.utils.Property;

public class MyConfig {

	/**
	 * 获取所属商户号
	 * 
	 * @return返回
	 */
	public static String getPartner(String cfg) {

		String partner;
		switch (cfg) {
		case "get_cfg_oid_partner":
			partner = Property.get("oid_partner");
			break;
		case "get_cfg_oid_partner_pause":
			partner = Property.get("oid_partner_pause");
			break;
		default:
			partner = cfg;
		}
		return partner;
	}

	/**
	 * 获取主收款商户号
	 * 
	 * @return返回
	 */
	public static String getColPartner(String cfg) {

		String partner;
		switch (cfg) {
		case "get_cfg_col_partner":
			partner = Property.get("col_partner");
			break;
		case "get_cfg_col_partner_pause":
			partner = Property.get("col_partner_pause");
			break;
		default:
			partner = cfg;
		}
		return partner;
	}

	/**
	 * 获取担保商户号
	 * 
	 * @return返回
	 */
	public static String getSecuredPartner(String cfg) {

		String partner;
		switch (cfg) {
		case "get_cfg_secured_partner":
			partner = Property.get("secured_partner");
			break;
		case "get_cfg_secured_partner_pause":
			partner = Property.get("secured_partner_pause");
			break;
		default:
			partner = cfg;
		}
		return partner;
	}

	/**
	 * 获取优惠券商户号
	 * 
	 * @return返回
	 */
	public static String getCouponPartner(String cfg) {

		String partner;
		switch (cfg) {
		case "get_cfg_coupon_partner":
			partner = Property.get("coupon_partner");
			break;
		case "get_cfg_coupon_partner_pause":
			partner = Property.get("coupon_partner_pause");
			break;
		default:
			partner = cfg;
		}
		return partner;
	}

	/**
	 * 获取用户登录号
	 * 
	 * @return返回
	 */
	public static String getUser(String cfg) {

		String user_id;
		switch (cfg) {
		case "get_cfg_user_id_personal":
			user_id = Property.get("user_id_personal");
			break;
		case "get_cfg_user_id_personal1":
			user_id = Property.get("user_id_personal1");
			break;
		case "get_cfg_user_id_personal_unreal":
			user_id = Property.get("user_id_personal_unreal");
			break;
		case "get_cfg_user_id_personal_pause":
			user_id = Property.get("user_id_personal_pause");
			break;
		case "get_cfg_user_id_personal_unactive":
			user_id = Property.get("user_id_personal_unactive");
			break;
		case "get_cfg_user_id_personal_acct_unactive":
			user_id = Property.get("user_id_personal_acct_unactive");
			break;
		case "get_cfg_user_id_personal_acct_cancel":
			user_id = Property.get("user_id_personal_acct_cancel");
			break;
		case "get_cfg_user_id_personal_acct_loss":
			user_id = Property.get("user_id_personal_acct_loss");
			break;
		case "get_cfg_user_id_personal_balance_large":
			user_id = Property.get("user_id_personal_balance_large");
			break;
		case "get_cfg_user_id_individual":
			user_id = Property.get("user_id_individual");
			break;
		case "get_cfg_user_id_individual_unreal":
			user_id = Property.get("user_id_individual_unreal");
			break;
		case "get_cfg_user_id_enterprise":
			user_id = Property.get("user_id_enterprise");
			break;
		case "get_cfg_user_id_enterprise_unreal":
			user_id = Property.get("user_id_enterprise_unreal");
			break;
		case "get_cfg_user_id_enterprise_pause":
			user_id = Property.get("user_id_enterprise_pause");
			break;
		case "get_cfg_user_id_enterprise_unactive":
			user_id = Property.get("user_id_enterprise_unactive");
			break;
		case "get_cfg_user_id_enterprise_acct_unactive":
			user_id = Property.get("user_id_enterprise_acct_unactive");
			break;
		case "get_cfg_user_id_enterprise_balance_large":
			user_id = Property.get("user_id_enterprise_balance_large");
			break;
		case "get_user_id":
			user_id = ApiUtils.getCurrentDateMillisecondStr();
			break;
		case "get_user_id_random":
			user_id = ApiUtils.getCurrentDateMillisecondStr() + "@anonymous";
			break;
		case "get_user_id_uuid":
			user_id = getUuid();
			break;
		case "get_user_id_random33":
			user_id = getRandomString(33);
			break;
		default:
			user_id = cfg;
		}
		return user_id;
	}

	/**
	 * 获取订单号
	 * 
	 * @return返回
	 */
	public static String getNoOrder(String cfg) {

		String no_order;
		switch (cfg) {
		case "get_no_order":
			no_order = ApiUtils.getCurrentDateMillisecondStr();
			break;
		case "get_no_order_random33":
			no_order = getRandomString(33);
			break;
		case "get_no_order_random32":
			no_order = getRandomString(32);
			break;
		case "get_no_order_random41":
			no_order = getRandomString(41);
			break;
		case "get_no_order_random40":
			no_order = getRandomString(40);
			break;
		case "get_no_order_repeat_success":
			DbUtils dbUtils = new DbUtils();
			no_order = dbUtils.queryNoOrder("0", getPartner("get_cfg_oid_partner")).getNo_order();
			break;
		case "get_no_order_repeat_waiting":
			DbUtils dbUtils1 = new DbUtils();
			no_order = dbUtils1.queryNoOrder("1", getPartner("get_cfg_oid_partner")).getNo_order();
			break;
		default:
			no_order = cfg;
		}
		return no_order;
	}

	/**
	 * 获取订单时间
	 * 
	 * @return返回
	 */
	public static String getDtOrder(String cfg) {

		String dt_order;
		switch (cfg) {
		case "get_dt_order":
			dt_order = ApiUtils.getCurrentDateSecondStr();
			break;
		case "get_dt_order_yyyymmdd":
			dt_order = ApiUtils.getCurrentDateStr();
			break;
		case "get_dt_order_yyyyMMdd HH:mm:ss":
			dt_order = ApiUtils.getCurrentDateSecondStr1();
			break;
		default:
			dt_order = cfg;
		}
		return dt_order;
	}

	
	/**
	 * 商户确认订单号
	 * 
	 * @return返回
	 */
	public static String getNoConfirm(String cfg) {

		String no_confirm;
		switch (cfg) {
		case "get_no_confirm":
			no_confirm = ApiUtils.getCurrentDateMillisecondStr();
			break;
		case "get_no_confirm_random33":
			no_confirm = getRandomString(33);
			break;
		default:
			no_confirm = cfg;
		}
		return no_confirm;
	}

	/**
	 * 商户确认时间
	 * 
	 * @return返回
	 */
	public static String getDtConfirm(String cfg) {

		String dt_confirm;
		switch (cfg) {
		case "get_dt_confirm":
			dt_confirm = ApiUtils.getCurrentDateSecondStr();
			break;
		case "get_dt_confirm_yyyymmdd":
			dt_confirm = ApiUtils.getCurrentDateStr();
			break;
		default:
			dt_confirm = cfg;
		}
		return dt_confirm;
	}
	
	/**
	 * 获取时间戳
	 * 
	 * @return返回
	 */
	public static String getTimestamp(String cfg) {

		String timestamp;
		switch (cfg) {
		case "get_timestamp":
			timestamp = ApiUtils.getCurrentDateSecondStr();
			break;
		case "get_timestamp_yyyymmdd":
			timestamp = ApiUtils.getCurrentDateStr();
			break;
		default:
			timestamp = cfg;
		}
		return timestamp;
	}
	
	/**
	 * 获取日期
	 * 
	 * @return返回
	 */
	public static String getDate(String cfg) {

		String date;
		switch (cfg) {
		case "get_date":
			date = ApiUtils.getCurrentDateStr();
			break;
		case "get_date+1":
			date = ApiUtils.getTomorrowStr();
			break;
		case "get_date-1":
			date = ApiUtils.getYesterdayStr();
			break;
		default:
			date = cfg;
		}
		return date;
	}
	
	/**
	 * 获取手机号
	 * 
	 * @return返回
	 */
	public static String getMobBind(String cfg) {

		String mob_bind;
		switch (cfg) {
		case "get_mob_bind":
			mob_bind = ApiUtils.getMobStr();
			break;
		default:
			mob_bind = cfg;
		}
		return mob_bind;
	}
	
	/**
	 * 获取身份证号
	 * 
	 * @return返回
	 */
	public static String getNoIdcard(String cfg) {

		String no_idcard;		
		IdCardGenerator idCardGenerator = new IdCardGenerator();
		
		switch (cfg) {
		case "get_no_idcard":
			no_idcard = idCardGenerator.generate();
			break;
		default:
			no_idcard = cfg;
		}
		return no_idcard;
	}
	
	/**
	 * 获取随机字符串
	 * 
	 * @return返回指定长度随机字符串
	 */
	public static String getRandomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 获取32位唯一标识
	 * 
	 * @return返回唯一标识uuid
	 */
	public static String getUuid() {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return uuid.toString();
	}

	public static void main(String[] args) throws Exception {
		Property.set();
		try {
			// System.out.println(getPartner("get_cfg_oid_partner"));
			// System.out.println(getUser("get_cfg_user_id_personal"));
			// System.out.println(getUuid());
			// System.out.println(getRandomString(6));

			System.out.println(getDtOrder("get_dt_order_yyyymmdd"));
		} catch (Exception e) {
			throw new Exception();
		}
	}
}
