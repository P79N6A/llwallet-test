package com.tools.webdriver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class JQuery {
	private static JQuery jquery = null;

	private String strJqueryMin = "";

	private JQuery() throws IOException {
		InputStream inStream = this.getClass().getResourceAsStream(
				"/shelper/webdriver/jquery-1.7.2.min.js");
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[1000];
		int rc = 0;
		while ((rc = inStream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		strJqueryMin = new String(swapStream.toByteArray(), "UTF-8");
	}

	private void initJQuery(JavascriptExecutor driver) {
		if (!jQueryLoaded(driver)) {
			driver.executeScript(strJqueryMin);
			driver.executeScript("window.jQuery=jQuery.noConflict();");
		}
	}

	/**
	 * 判断当前页面是否加载了JQuery
	 * 
	 * @param driver
	 * @return
	 */
	public Boolean jQueryLoaded(JavascriptExecutor driver) {
		Boolean loaded;
		try {
			loaded = (Boolean) driver
					.executeScript("if(typeof jQuery==\"undefined\"){return false;}else{return true;}");
		} catch (WebDriverException e) {
			loaded = false;
		}
		return loaded;
	}

	public static JQuery getInstance() throws IOException {
		if (jquery == null) {
			jquery = new JQuery();
		}
		return jquery;
	}

	/**
	 * 执行JS脚本
	 * 
	 * @param driver
	 * @param script
	 * @param args
	 * @return
	 */
	public Object runJs(WebDriver driver, String script, Object... args) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		initJQuery(js);
		return js.executeScript(script, args);
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {
		WebDriver driver = new InternetExplorerDriver();
		driver.get("http://www.baidu.com");
		JQuery.getInstance().runJs(driver, "jQuery('#kw').val('软件测试');");
		JQuery.getInstance().runJs(driver, "jQuery('#su').click();");
		Thread.sleep(5000);
		driver.quit();
	}

}
