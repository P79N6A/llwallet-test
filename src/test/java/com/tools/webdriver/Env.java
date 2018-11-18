package com.tools.webdriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class Env {

	private static Env uniqueInstance = null;

	/**
	 * 测试大环境的ID
	 */
	public final String stageIdParameterName = "STAGE_ID";

	/**
	 * 机组的ID
	 */
	public final String groupIdParameterName = "GROUP_ID";

	/**
	 * 参数文件根目录
	 */
	private final String propertiesRootPath = "/properties";

	/**
	 * 属性文件后缀
	 */
	private final String propertyFileSuffix = ".properties";

	/**
	 * 默认的环境参数文件
	 */
	private final String defaultEnvFilepath = propertiesRootPath + "/env"
			+ propertyFileSuffix;

	/**
	 * 默认参数文件
	 */
	private final String defaultPropertyFilepath = propertiesRootPath
			+ "/ta_base" + propertyFileSuffix;

	/**
	 * 构造函数
	 * 
	 * @throws IOException
	 */
	private Env() throws IOException {
		this.setDefaultProperties();
		this.setCurrentStageProperties();
	}

	/**
	 * 单例模式
	 * 
	 * @return
	 * @throws IOException
	 */
	public synchronized static Env getInstance() throws IOException {
		if (uniqueInstance == null) {
			uniqueInstance = new Env();
		}
		return uniqueInstance;
	}

	/**
	 * 设置当前Stage相关的参数
	 * 
	 * @throws IOException
	 */
	private void setCurrentStageProperties() throws IOException {
		// 根据STAGE_ID、GROUP_ID拼接路径
		StringBuilder propertiesPath = new StringBuilder();
		propertiesPath.append(propertiesRootPath);
		if (System.getProperty(this.stageIdParameterName) != null) {
			propertiesPath.append("/");
			propertiesPath
					.append(System.getProperty(this.stageIdParameterName));
		}
		if (System.getProperty(this.groupIdParameterName) != null) {
			propertiesPath.append("/");
			propertiesPath
					.append(System.getProperty(this.groupIdParameterName));
		}
		propertiesPath.append("/*");
		propertiesPath.append(propertyFileSuffix);

		// 利用spring的功能拿到全部资源文件的列表
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources(propertiesPath.toString());

		// 加载到System Property
		for (Resource resource : resources) {
			System.out.println(resource);
			System.out.println(resource.getURI());
			setSystemProperties(setProperties(resource.getURI().toString()));
		}
	}

	private void setDefaultProperties() throws IOException {
		// 拿到特殊的环境属性
		String stageId = System.getProperty(this.stageIdParameterName);
		String groupId = System.getProperty(this.groupIdParameterName);

		// 加载默认属性
		this.setSystemProperties(this
				.setProperties(this.defaultPropertyFilepath));

		// 加载默认环境属性
		this.setSystemProperties(this.setProperties(this.defaultEnvFilepath));

		// 覆盖特殊的环境属性
		if (stageId != null)
			System.setProperty(this.stageIdParameterName, stageId);
		if (groupId != null)
			System.setProperty(this.groupIdParameterName, groupId);
	}

	/**
	 * 从文件中获取属性
	 * 
	 * @param filepath
	 * @return
	 * @throws IOException
	 */
	private Properties setProperties(String filepath) throws IOException {
		// System.out.println("**********************");
		// System.out.println(filepath);
		// System.out.println("**********************");
		Properties p = new Properties();
		if (filepath.indexOf("file:/") > -1) {
			// FileInputStream propFile = new FileInputStream(
			// filepath.substring(filepath.indexOf("/")));
			// p.load(propFile);

			InputStream inStream = Env.class.getResourceAsStream(filepath
					.substring(filepath.indexOf("!") + 1));
			p.load(inStream);
		} else {
			InputStream inStream = Env.class.getResourceAsStream(filepath);
			p.load(inStream);
		}
		return p;
	}

	/**
	 * 设置系统属性
	 * 
	 * @param p
	 */
	private void setSystemProperties(Properties p) {
		Enumeration<Object> enums = p.keys();
		while (enums.hasMoreElements()) {
			Object key = enums.nextElement();
			if (key != null) {
				if (key.toString().trim().length() > 0) {
					System.setProperty(key.toString(), p.get(key).toString());
				}
			}
		}
	}

	/**
	 * 获得名字为key的环境变量
	 * 
	 * @param key
	 *            要获得环境变量的名字
	 * @return 获取的名字为key的环境变量的值
	 * @throws UnsupportedEncodingException
	 */
	public static String get(String key) throws UnsupportedEncodingException {
		String result = "";
		String tmp1 = System.getProperty(key);
		if (tmp1 == null || tmp1.equals("")) {
			return null;
		}
		byte[] temp2 = tmp1.getBytes("ISO-8859-1");
		result = new String(temp2, "utf-8");
		return result;
	}

	/**
	 * 设置环境变量
	 * 
	 * @param key
	 *            变量名
	 * @param value
	 *            变量值
	 */
	public static void set(String key, String value) {
		System.setProperty(key, value);
	}
}
