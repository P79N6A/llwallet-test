package com.tools.utils;

import java.io.File;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelUtil {

	/**
	 * @author huangfucf
	 * @param o
	 * @param sheetName
	 *            sheet名字
	 * @param retmsg_column
	 *            结果的列数
	 * @param retmsg
	 *            结果的内容
	 * @throws Exception
	 */
	public void writeResult2Excel(Object o, String sheetName, int retmsg_column, String retmsg) throws Exception {
		Workbook book = null;
		File file = new File("src/test/resources/data/excel/" + o.getClass().getName().replaceAll("\\.", "/") + ".xls");

		book = Workbook.getWorkbook(file);
		Sheet sheet = book.getSheet(sheetName);
		int flag_column = 0; // 指定标志列
		int totalRows = getRightRows(sheet);
		int currentRow = 0;
		WritableWorkbook workbook = Workbook.createWorkbook(file, book);
		WritableSheet writesheet = workbook.getSheet(sheetName);
		Label labelCurrnt, labelRetMsg;

		try {
			for (int i = 0; i < totalRows; i++) {
				labelCurrnt = new Label(flag_column, i, "Y");

				if (("").equals(sheet.getCell(0, i).getContents())
						&& ("Y").equals(sheet.getCell(0, i - 1).getContents())) {
					currentRow = labelCurrnt.getRow();
					labelRetMsg = new Label(retmsg_column, currentRow, retmsg);
					writesheet.addCell(labelRetMsg);
					writesheet.addCell(labelCurrnt);
				}

				else {
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workbook.write();
			workbook.close();
			book.close();
		}
	}

	/**
	 * 获取excel真实行数，去掉空行
	 * 
	 * @author wanglin002
	 */
	private int getRightRows(Sheet sheet) {
		int rsCols = sheet.getColumns(); // 列数
		int rsRows = sheet.getRows(); // 行数
		int nullCellNum;
		int afterRows = rsRows;
		for (int i = 1; i < rsRows; i++) { // 统计行中为空的单元格数
			nullCellNum = 0;
			for (int j = 0; j < rsCols; j++) {
				String val = sheet.getCell(j, i).getContents().trim();
				val = StringUtils.trimToEmpty(val);
				if (StringUtils.isBlank(val))
					nullCellNum++;
			}
			if (nullCellNum >= rsCols) { // 如果nullCellNum大于或等于总的列数
				{
					afterRows--; // 行数减一
				}
			}
		}
		return afterRows;
	}

	/**
	 * * 设置excel初始格式，在BeforeClass中调用 * @author wanglin002
	 * 
	 * @throws IOException
	 * @throws WriteException
	 */
	public void prepare4Excel(Object o, String sheetName) throws Exception {
		Workbook book = null;
		WritableWorkbook workbook = null;
		try {
			File file = new File(
					"src/test/resources/data/excel/" + "/" + o.getClass().getName().replaceAll("\\.", "/") + ".xls");
			book = Workbook.getWorkbook(file);
			Sheet sheet = book.getSheet(sheetName);
			int totalRows = getRightRows(sheet);
			workbook = Workbook.createWorkbook(file, book);
			WritableSheet writesheet = workbook.getSheet(sheetName);
			for (int j = 1; j < totalRows;) {
				writesheet.addCell(new Label(0, j, ""));
				j++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				workbook.write();
				workbook.close();
			}
			if (book != null) {
				book.close();
			}
		}
	}

	public static void main(String[] args) throws Exception {

	}
}
