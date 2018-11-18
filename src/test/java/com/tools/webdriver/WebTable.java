package com.tools.webdriver;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WebTable {

	private boolean __hasHead__ = true;
	private boolean __hasFoot__ = true;
	private WebElement __webTab__ = null;
	// private WebDriver __driver__ = null;
	private int __rowNum__ = 0;
	private List<List<String>> __listTab__ = new ArrayList<List<String>>();
	private List<String> __listTabHead__ = new ArrayList<String>();
	private List<String> __listTabFoot__ = new ArrayList<String>();

	private void init(WebDriver dr, By by) {
		WebElement we = null;
		try {
			we = dr.findElement(by);
		} catch (Exception ex) {
			ex.getStackTrace();
			new Exception("无法获取到WebElement对象");
		}
		init(dr, we);
	}

	private void init(WebDriver dr, WebElement we) {
		if (!we.getTagName().equals("table")) {
			new Exception("获取到的WebElement不是Table");
		}
		// this.__driver__ = dr;
		this.__webTab__ = we;
		int rowNum = we.findElements(By.tagName("tr")).size();

		if (this.__hasHead__)
			rowNum--;
		if (this.__hasFoot__)
			rowNum--;
		if (rowNum < 0)
			__rowNum__ = 0;
		else
			__rowNum__ = rowNum;
		System.out.println("rowNum == " + rowNum);
		long t1 = System.currentTimeMillis();
		List<WebElement> trElems = __webTab__.findElements(By.tagName("tr"));
		for (int i = 0; i < trElems.size(); i++) {
			List<WebElement> tdElems = trElems.get(i).findElements(
					By.tagName("td"));
			List<String> listTd = new ArrayList<String>();
			for (int j = 0; j < tdElems.size(); j++)
				listTd.add(tdElems.get(j).getText());
			if (this.__hasHead__ && i == 0) {
				__listTabHead__ = listTd;
				continue;
			}
			if (this.__hasFoot__ && i == trElems.size() - 1) {
				__listTabFoot__ = listTd;
				break;
			}
			__listTab__.add(listTd);
		}
		long t2 = System.currentTimeMillis() - t1;
		System.out.println("从界面遍历数据并填充到List花费时间:" + t2);
	}

	public WebTable(WebDriver dr, By by) {
		this.init(dr, by);
	}

	public WebTable(WebDriver dr, By by, Boolean hasHead, Boolean hasFoot) {
		this.__hasHead__ = hasHead;
		this.__hasFoot__ = hasFoot;
		this.init(dr, by);
	}

	public WebTable(WebDriver dr, WebElement we) {
		this.init(dr, we);
	}

	public WebTable(WebDriver dr, WebElement we, Boolean hasHead,
			Boolean hasFoot) {
		this.init(dr, we);
		this.__hasHead__ = hasHead;
		this.__hasFoot__ = hasFoot;
	}

	public boolean hasHead() {
		return this.__hasHead__;
	}

	public boolean hasFoot() {
		return this.__hasFoot__;
	}

	public int getRowNum() {
		return __rowNum__;
	}

	/**
	 * 获取WebTable
	 * 
	 * @return 返回WebElement对象，TagName为table
	 */
	public WebElement getAllTable() {
		return this.__webTab__;
	}

	/**
	 * 按行列获取TD 行列ID从0开始计算 处理速度较慢，不适合做遍历
	 * 
	 * @param rowid
	 *            行号
	 * @param columnid
	 *            列号
	 * @return 返回WebElement对象，TagName为td
	 * @throws Exception
	 */
	public WebElement getTd(int rowid, int columnid) throws Exception {
		if (rowid - 1 > this.__rowNum__)
			throw new Exception("行号溢出");
		if (__hasHead__)
			rowid++;
		return __webTab__.findElements(By.tagName("tr")).get(rowid)
				.findElements(By.tagName("td")).get(columnid);
	}

	public List<List<String>> getListTable() {
		return this.__listTab__;
	}

	public List<String> getListTabHead() {
		return this.__listTabHead__;
	}

	public List<String> getListTabFoot() {
		return this.__listTabFoot__;
	}

	public String getText() {
		return this.__webTab__.getText();
	}
}
