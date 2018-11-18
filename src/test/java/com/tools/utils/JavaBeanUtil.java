package com.tools.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

public class JavaBeanUtil {

	@SuppressWarnings("rawtypes")
	public static Object convertMap(Class type, Map map) throws Exception {
		Object obj = type.newInstance(); // 创建 JavaBean 对象
		// 给 JavaBean 对象的属性赋值
		Map<String, String> params = new HashMap<String, String>();
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			if (!entry.getValue().equals("")) {
//				if ("pwd_pay".equals(key)) {
				if ("pwd_pay".equals(key) && value.length() < 50) {
					try {
						value = RSAUtil.encrypt(value, Property.get("rsa_pub_key"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if ("pwd_pay_new".equals(key) && value.length() < 50) {
					try {
						value = RSAUtil.encrypt(value, Property.get("rsa_pub_key"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if ("pwd_login".equals(key) && value.length() < 50 ) {
					try {
						value = RSAUtil.encrypt(value, Property.get("rsa_pub_key"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if ("oid_partner".equals(key)) {
					value = MyConfig.getPartner(value);
				}else if ("col_oidpartner".equals(key)) {
					value = MyConfig.getColPartner(value);
				} else if ("user_id".equals(key)) {
					value = MyConfig.getUser(value);
				} else if ("mob_bind".equals(key)) {
					value = MyConfig.getMobBind(value);
				}else if ("bind_mob".equals(key)) {
					value = MyConfig.getMobBind(value);
				}else if ("no_idcard".equals(key)) {
					value = MyConfig.getNoIdcard(value);
				} else if ("no_order".equals(key)) {
					value = MyConfig.getNoOrder(value);
				} else if ("dt_order".equals(key)) {
					value = MyConfig.getDtOrder(value);
				} else if ("timestamp".equals(key)) {
					value = MyConfig.getTimestamp(value);
				} else if ("no_confirm".equals(key)) {
					value = MyConfig.getNoConfirm(value);
				} else if ("dt_confirm".equals(key)) {
					value = MyConfig.getDtConfirm(value);
				}else if ("exp_idcard".equals(key)) {
					value = MyConfig.getDate(value);
				}
				params.put(key, value);
			}
		}
		BeanUtils.populate(obj, params);
		return obj;
	}

	@SuppressWarnings("rawtypes")
	public static Object convertMap1(Class type, Map map)
			throws IntrospectionException, IllegalAccessException, InstantiationException, InvocationTargetException {
		BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
		Object obj = type.newInstance(); // 创建 JavaBean 对象

		// 给 JavaBean 对象的属性赋值
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();

			if (map.containsKey(propertyName)) {
				// 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
				Object value = map.get(propertyName);

				Object[] args = new Object[1];
				args[0] = value;

				descriptor.getWriteMethod().invoke(obj, args);
			}
		}
		return obj;
	}

}