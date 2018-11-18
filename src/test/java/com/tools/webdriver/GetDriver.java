/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tools.webdriver;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;


/**
 * 
 * @author Administrator
 */
public class GetDriver {
	public static WebDriver get(String drivername) {
		if (drivername.equals("ie")) {
			WebDriver dr = new InternetExplorerDriver();
			// 设置识别元素的超时时间
			dr.manage()
					.timeouts()
					.implicitlyWait(
							Integer.parseInt(Environment
									.get("Selenium.waittime")),
							TimeUnit.SECONDS);
			// 设置页面加载的超时时间
			dr.manage()
					.timeouts()
					.pageLoadTimeout(
							Integer.parseInt(Environment
									.get("Selenium.waittime")),
							TimeUnit.SECONDS);
			// 窗口最大化
			dr.manage().window().maximize();
			return dr;
		} else if (drivername.equals("ie8")) {
			DesiredCapabilities ieCapabilities = DesiredCapabilities
					.internetExplorer();
			ieCapabilities
					.setCapability(
							InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
							true);
			WebDriver dr = new InternetExplorerDriver(ieCapabilities);
			// 设置识别元素的超时时间
			dr.manage()
					.timeouts()
					.implicitlyWait(
							Integer.parseInt(Environment
									.get("Selenium.waittime")),
							TimeUnit.SECONDS);
			// 设置页面加载的超时时间
			dr.manage()
					.timeouts()
					.pageLoadTimeout(
							Integer.parseInt(Environment
									.get("Selenium.waittime")),
							TimeUnit.SECONDS);
			// 窗口最大化
			dr.manage().window().maximize();
			return dr;
		} else if (drivername.equals("firefox")) {
			WebDriver dr = new org.openqa.selenium.firefox.FirefoxDriver();
			dr.manage()
					.timeouts()
					.implicitlyWait(
							Integer.parseInt(Environment
									.get("Selenium.waittime")),
							TimeUnit.SECONDS);
			return dr;
		} else {
			throw new IllegalStateException(
					"selected explorer is not supported");
		}
	}

	public static void quit(WebDriver driver) {
		Autoit3.run("closeallwindows.au3");
		Set<String> windows = driver.getWindowHandles();
		for (String a : windows) {
			driver.switchTo().window(a).close();
		}
		Environment.stopStartTag();
		try {
			Thread.sleep(15);
		} catch (InterruptedException ex) {
			Logger.getLogger(GetDriver.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}
}
