package com.tools.dataprovider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * <h1>读取所有sheet页的provider</h1>
 * <p>
 * 表格开始不能包含空的列 ◑︿◐
 * 
 * @author huangfucf
 *
 */
public class ExcelProvider2 implements Iterator<Object[]> {
	public LinkedHashMap<String, LinkedList<LinkedHashMap<String, String>>> sheetMap = new LinkedHashMap<String, LinkedList<LinkedHashMap<String, String>>>();// 存放sheet和sheet数据的hashmap
	public LinkedList<String> sheetList = new LinkedList<String>(); // 存放excel所有sheet名字的list
	public LinkedHashMap<String, Integer> sheetKeyRowMap = new LinkedHashMap<String, Integer>(); // 存放excel第一行数据和列数的map
	public int numberindex;// 当前执行的行数，可以控制是否返回excel前两行的参数名和参数的注释
	public int line2;// 获取数据的行数范围
	public int paramLine1 = 0;//excel表格中，接口参数的起始列数
	public int maxRow;// 最大行数
	public String flag;// 标记执行的行数flag，single单行 multi多行full全部执行
	public String excel;// excel路径
	public Workbook wb;
	public FileInputStream fis;

	// 自定义main方法
	@SuppressWarnings("unchecked")
	public static void main(String args[]) throws IOException {
		ExcelProvider2 t = new ExcelProvider2();

		while (t.hasNext()) {
			Object[] a = t.next();
			LinkedHashMap<String, LinkedHashMap<String, String>> b = (LinkedHashMap<String, LinkedHashMap<String, String>>) a[0];
			System.out.println(b.get("userInfo").get("name"));
			System.out.println(b.get("getJob").get("job"));
		}
		System.out.println("##loop over...");
	}

	public boolean hasNext() {
		/* System.out.println("hasNext..."); */
		if (this.flag.equals("full")) {
			if (this.numberindex < this.maxRow) {
				return true;
			}
		} else if (this.flag.equals("multi")) {
			if (this.numberindex <= this.line2) {
				return true;
			}
		} else if (this.flag.equals("single")) {
			if (this.numberindex < this.line2) {
				return true;
			}
		}
		return false;
	}

	public Object[] next() {
		 System.out.println("numberindex:" + this.numberindex);
		if (this.flag.equals("full")) {
			// 获取多个sheet中的参数
			LinkedHashMap<String, LinkedHashMap<String, String>> result = new LinkedHashMap<String, LinkedHashMap<String, String>>();
			for (String sheetName : this.sheetList) {
				result.put(sheetName, this.sheetMap.get(sheetName).get(this.numberindex));
			}
			// 返回object对象
			Object[] objresult = new Object[2];
			objresult[0] = result;
			objresult[1] = this;
			this.numberindex += 1;
			return objresult;
		} else if (this.flag.equals("single")) {
			// 获取多个sheet中的参数
			LinkedHashMap<String, LinkedHashMap<String, String>> result = new LinkedHashMap<String, LinkedHashMap<String, String>>();
			for (String sheetName : this.sheetList) {
				result.put(sheetName, this.sheetMap.get(sheetName).get(this.numberindex));
			}
			// 返回object对象
			Object[] objresult = new Object[2];
			objresult[0] = result;
			objresult[1] = this;
			this.numberindex += 1;
			return objresult;
		} else if (this.flag.equals("multi")) {
			// 获取多个sheet中的参数
			LinkedHashMap<String, LinkedHashMap<String, String>> result = new LinkedHashMap<String, LinkedHashMap<String, String>>();
			for (String sheetName : this.sheetList) {
				result.put(sheetName, this.sheetMap.get(sheetName).get(this.numberindex));
			}
			// 返回object对象
			Object[] objresult = new Object[2];
			objresult[0] = result;
			objresult[1] = this;
			this.numberindex += 1;
			return objresult;
		}
		return null;
	}

	public void remove() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap getParamMap(LinkedHashMap<String, String> lmap) {
		HashMap map = new HashMap();
		int i = 0;
		for (String key : lmap.keySet()) {
			i++;
			if (i >= this.paramLine1) {
				map.put(key, lmap.get(key));
			}
		}
		return map;
	}

	public void appendCurrenCell(String sheetName,String key,String value) throws IOException {
		String con=this.wb.getSheet(sheetName).getRow(this.numberindex-1).getCell(this.sheetKeyRowMap.get(key)).getStringCellValue();
		if (con.equals("N/A")) {
			this.wb.getSheet(sheetName).getRow(this.numberindex-1).getCell(this.sheetKeyRowMap.get(key)).setCellValue(value);
		} else {
			this.wb.getSheet(sheetName).getRow(this.numberindex-1).getCell(this.sheetKeyRowMap.get(key)).setCellValue(con+"::"+value);
		}
		this.fis.close();
		FileOutputStream fos = new FileOutputStream(this.excel);
		this.wb.write(fos);
		fos.close();
	}
	
	public void writeCurrentCell(String sheetName, String key, String value) throws IOException, InterruptedException {
		this.wb.getSheet(sheetName).getRow(this.numberindex - 1).getCell(this.sheetKeyRowMap.get(key))
				.setCellValue(value);
		this.fis.close();
		FileOutputStream fos = new FileOutputStream(this.excel);
		this.wb.write(fos);
		fos.close();
	}

	public void genKeyRowMap(String sheetName, int maxColumn) {
		for (int i = 0; i < maxColumn; i++) {
//			 System.out.println(this.wb.getSheet(sheetName).getRow(0).getCell(i));
			this.sheetKeyRowMap.put(this.wb.getSheet(sheetName).getRow(0).getCell(i).toString(), i);
		}
	}

	/**
	 * 获取excel表格中的所有数据
	 * 
	 * @author huangfucf
	 * @throws IOException
	 */
	public void getExcelData(String excel) throws IOException {
		this.fis = new FileInputStream(excel);
		try {
			this.wb = new HSSFWorkbook(this.fis);// 读取.xls
		} catch (Exception e) {
			// e.printStackTrace();
			this.fis = new FileInputStream(excel);
			this.wb = new XSSFWorkbook(this.fis); // 读取.xlsx
		}
		// 获取第一页sheet最大行数
		this.maxRow = this.wb.getSheetAt(0).getPhysicalNumberOfRows();

		// 获取所有的sheet名字
		int sheetNum = this.wb.getNumberOfSheets();
		for (int i = 0; i < sheetNum; i++) {
			this.sheetList.add(wb.getSheetName(i));
		}
		// 获取所有的数据
		for (String sheet : sheetList) {
			sheetMap.put(sheet, this.getSheetDate(wb, sheet));
		}
		/*
		 * System.out.println(sheetMap.get("userInfo").get(0).get("order_id").
		 * toString());
		 * //System.out.println(sheetMap.get("userInfo").get(1).get("order_id").
		 * toString());
		 * System.out.println(sheetMap.get("userInfo").get(2).get("order_id").
		 * toString());
		 * System.out.println(sheetMap.get("userInfo").get(3).get("order_id").
		 * toString());
		 * System.out.println(sheetMap.get("userInfo").get(3).get("amt_paybill")
		 * .toString());
		 * System.out.println(sheetMap.get("getJob").get(3).get("job").toString(
		 * )); System.out.println(sheetMap.get("getJob").get(3).get("address").
		 * toString());
		 */ }

	/**
	 * 获取Excel表格中的所有数据
	 * @param workbook
	 * @param sheetName
	 * @return
	 */
	public LinkedList<LinkedHashMap<String, String>> getSheetDate(Workbook workbook,String sheetName) {
		Sheet sheet=workbook.getSheet(sheetName);
		LinkedList <LinkedHashMap<String,String>>list = new LinkedList <LinkedHashMap<String,String>>();
		//获取最大行数，列数
		int maxRow=sheet.getPhysicalNumberOfRows();
		int maxColumn=sheet.getRow(0).getPhysicalNumberOfCells();
		this.genKeyRowMap(sheetName, maxColumn);
		int intColumn=0;//设置读取json参数初始化列数
		//读取参数
		for (int i=0;i<maxRow;i++) {
			//初始化一个map用来存储每一行的hashmap数据
			LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
			Row row=sheet.getRow(i);
			int Column;
			Column=intColumn;
			while(Column<maxColumn) {
				String key=sheet.getRow(0).getCell(Column).toString();
				String value = "";
				try{
					value=row.getCell(Column).toString();	
				}
				catch (Exception e){
					value = "N/A";
				}
				map.put(key, value);
				Column++;
			}
			list.add(map);
		}
		return list;
	}

	public ExcelProvider2() throws IOException {
		this.flag = "full";
		this.numberindex = 2;
		this.excel = System.getProperty("user.dir") + File.separator + "Test.xls";
		System.out.println(this.excel);
		this.getExcelData(this.excel);
	}

	/**
	 * 获取所有sheet内的内容方法 ( ´◔ ‸◔`) <br>
	 * 全部执行
	 * 
	 * @author huangfucf
	 * @param aimob
	 * @throws IOException
	 */
	public ExcelProvider2(Object aimob) throws IOException {
		this.flag = "full";
		this.numberindex = 2;
		this.excel = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator
				+ "resources" + File.separator + "data" + File.separator + "excel" + File.separator
				+ aimob.getClass().getPackage().getName().replace(".", File.separator) + File.separator
				+ aimob.getClass().getSimpleName() + ".xls";

		System.out.println(this.excel);
		this.getExcelData(this.excel);
	}

	/**
	 * 单行执行用例 (o◕∀◕)ﾉ
	 * 
	 * @param aimob
	 * @param line1
	 * @param line2
	 * @throws IOException
	 */
	public ExcelProvider2(Object aimob, int line1) throws IOException {
		this(aimob);
		this.flag = "single";
		this.numberindex = line1 - 1;
		this.line2 = line1;
	}

	/**
	 * 执行从line1到line2之间的用例 (｀◕‸◕´+)
	 * 
	 * @param aimob
	 * @param line1
	 * @param line2
	 * @throws IOException
	 */
	public ExcelProvider2(Object aimob, int line1, int line2) throws IOException {
		this(aimob);
		this.flag = "multi";
		this.numberindex = line1 - 1;
		this.line2 = line2 - 1;
	}
}