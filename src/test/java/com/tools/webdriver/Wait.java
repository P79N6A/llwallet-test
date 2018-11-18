package com.tools.webdriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class Wait {

	/**
	 * 等待页面元素显示
	 * 
	 * @param webelement
	 * @return
	 */
	public static boolean waitDisplay(WebElement webelement) {
		for (int i = 0; i < Integer.parseInt(Environment
				.get("Selenium.waittime")); i++) {
			try {
				if (webelement.isDisplayed())
					return true;
				else
					try {
						Thread.sleep(1000L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			} catch (Exception e) {
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		}
		return webelement.isDisplayed();
	}

	/**
	 * 等待Select控件异步加载
	 * 
	 * @param webelement
	 * @param visibleText
	 * @return
	 */
	public static boolean waitOptionByVisibleText(WebElement selectElement,
			String visibleText) {
		boolean flag = true;
		for (int i = 0; i < Integer.parseInt(Environment
				.get("Selenium.waittime")); i++) {
			try {
				(new Select(selectElement)).selectByVisibleText(visibleText);
				flag = true;
			} catch (Exception ex) {
				flag = false;
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (flag)
				break;
		}
		return flag;
	}

}
