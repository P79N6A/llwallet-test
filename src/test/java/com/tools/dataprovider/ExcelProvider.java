package com.tools.dataprovider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * 数据驱动
 * 
 * @author maotouying
 * 
 */
public class ExcelProvider implements Iterator<Object[]> {
	private String excel;
	private String sheetname;
	private FileInputStream fis;
	private POIFSFileSystem fs;
	private HSSFWorkbook wb;
	private HSSFSheet sheet;
	private int linenumber;
	private int rownumber;
	private int numberindex;
	private String[] rowname;
	private int singleline;
	private boolean singletag;
	private List<Integer> removearray;
	public LinkedHashMap<String, Integer> sheetKeyRowMap = new LinkedHashMap<String, Integer>(); // 存放excel第一行数据和列数的map

	/**
	 * 执行全部
	 * 
	 * @param aimob
	 *            测试类
	 * @param aimmethod
	 *            方法名
	 * @param linenumber
	 *            执行的行号
	 * @throws IOException
	 */
	public ExcelProvider(Object aimob, String aimmethod) {
		this.rownumber = 0;

		this.numberindex = 0;

		this.singleline = 0;

		this.singletag = false;

		this.removearray = new ArrayList<Integer>();

		getInfo(aimob, aimmethod);

		openSheet();

		getRowname();

	}

	/**
	 * 单行调试模式
	 * 
	 * @param aimob
	 *            测试类
	 * @param aimmethod
	 *            方法名
	 */
	public ExcelProvider(Object aimob, String aimmethod, int linenumber) throws IOException {
		this(aimob, aimmethod);
		// this.singleline = linenumber;
		this.numberindex = linenumber;
		this.linenumber = linenumber + 1;

	}

	/**
	 * 执行第M行到第N行
	 * 
	 * @param aimob
	 *            测试类
	 * @param aimmethod
	 *            方法名
	 * @param linenumber1
	 *            执行的起始行号M
	 * @param linenumber2
	 *            执行的结束行号N
	 * @throws IOException
	 */
	public ExcelProvider(Object aimob, String aimmethod, int linenumber1, int linenumber2) throws IOException {
		this(aimob, aimmethod);
		// this.singleline = linenumber;
		this.numberindex = linenumber1;
		this.linenumber = linenumber2 + 1;
	}

	/**
	 * 不执行指定的行
	 * 
	 * @param aimob
	 *            测试类
	 * @param aimmethod
	 *            方法名
	 * @param linenumber
	 *            不执行的行号列表
	 * @throws IOException
	 */
	public ExcelProvider(Object aimob, String aimmethod, String removestring) throws IOException {
		this(aimob, aimmethod);
		String[] removestringarray = removestring.split(",");
		for (int i = 0; i < removestringarray.length; ++i)
			this.removearray.add(Integer.valueOf(Integer.parseInt(removestringarray[i])));
	}

	private void getRowname() {
		HSSFRow row = this.sheet.getRow(this.numberindex);
		while (row.getCell(this.rownumber) != null) {
			this.rownumber += 1;
		}
		this.rowname = new String[this.rownumber];
		for (int i = 0; i < this.rownumber; ++i) {
			this.rowname[i] = StringUtils.remove(row.getCell(i).toString(), "\n");
		}
		this.numberindex += 1;
	}

	public boolean hasNext() {

		if (this.singleline == 0) {
			while (this.removearray.contains(Integer.valueOf(this.numberindex))) {
				this.numberindex += 1;
			}

			if (this.numberindex < this.linenumber)
				return true;
			try {
				this.fis.close();
			} catch (IOException ex) {
				Logger.getLogger(ExcelProvider.class.getName()).log(Level.SEVERE, null, ex);
			}
			return false;
		}

		if (!(this.singletag)) {
			this.numberindex = this.singleline;

			if ((0 > this.numberindex) || (this.numberindex >= this.linenumber)) {
				try {
					this.fis.close();
				} catch (IOException ex) {
					Logger.getLogger(ExcelProvider.class.getName()).log(Level.SEVERE, null, ex);
				}
				return false;
			} else {
				this.singletag = true;
				return true;
			}
			//
			// this.singletag = true;
			// return true;
		}
		try {
			this.fis.close();
		} catch (IOException ex) {
			Logger.getLogger(ExcelProvider.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	public Object[] next() {

		HSSFRow row = this.sheet.getRow(this.numberindex);
		Map<String, String> result = new HashMap<String, String>();
		for (int j = 0; j < this.rownumber; j++) {
			String temp = "";
			if (null != row.getCell(j)) {
				temp = row.getCell(j).toString();
			}
			result.put(this.rowname[j], temp);
		}
		Object[] objresult = new Object[1];
		objresult[0] = result;
		this.numberindex += 1;
		return objresult;
	}

	public void remove() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private void getInfo(Object aimob, String aimmethod) {
		DataDriven dd = (DataDriven) locateTestMethod(aimob, aimmethod).getAnnotation(DataDriven.class);
		if (dd == null) {
			this.excel = modifydata(aimob, "auto");
			this.sheetname = modifysheet(aimmethod, "auto");
			modifyType("excel");
		} else {
			this.excel = modifydata(aimob, dd.excel());
			this.sheetname = modifysheet(aimmethod, dd.sheet());
			modifyType(dd.type());
		}

		DPRow dpr = (DPRow) locateTestMethod(aimob, aimmethod).getAnnotation(DPRow.class);
		if (dpr != null) {
			this.singleline = dpr.includeThisRow();
			String[] removestringarray = dpr.excludeTheseRows().split(",");
			for (int i = 0; i < removestringarray.length; ++i)
				if (removestringarray[i].trim().length() > 0) {
					this.removearray.add(Integer.valueOf(Integer.parseInt(removestringarray[i])));
				}

		}

	}

	private void openSheet() {
		try {
			this.fis = new FileInputStream(this.excel);
			this.fs = new POIFSFileSystem(this.fis);
			this.wb = new HSSFWorkbook(this.fs);
			this.sheet = this.wb.getSheet(this.sheetname);
			this.linenumber = this.sheet.getPhysicalNumberOfRows();
		} catch (IOException ex) {
			Logger.getLogger(ExcelProvider.class.getName()).log(Level.SEVERE, null, ex);
			try {
				this.fis.close();
			} catch (IOException ex1) {
				Logger.getLogger(ExcelProvider.class.getName()).log(Level.SEVERE, null, ex1);
			}
		}
	}

	private String modifysheet(String method, String sheetname) {
		if (sheetname.equals("auto")) {
			return method;
		}
		return sheetname;
	}

	private String modifydata(Object o, String data) {
		try {
			if (data.equals("auto")) {
//				String datadrivenRoot = "src/main/resources/data/excel/";
//				String filename = datadrivenRoot + o.getClass().getName().replaceAll("\\.", "/") + ".xls";
				String filename = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator
						+ "resources" + File.separator + "data" + File.separator + "excel" + File.separator + o.getClass().getPackage().getName().replace(".", File.separator)
						+ File.separator + o.getClass().getSimpleName() + ".xls";
				// System.out.println("filename--" + filename);
				return filename;
			}
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Method locateTestMethod(Object objectname, String methodname) {
		try {
			// System.out.println(objectname.getClass());
			Method[] arrayOfMethod1 = objectname.getClass().getDeclaredMethods();
			Method[] arrayOfMethod2 = arrayOfMethod1;
			int j = arrayOfMethod2.length;
			int k = 0;
			while (k < j) {
				Method localMethod = arrayOfMethod2[k];

				if (localMethod.getName().equals(methodname)) {
					return localMethod;
				}
				++k;
			}
		} catch (Throwable ex) {
			Logger.getLogger(ExcelProvider.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	private String modifyType(String type) {
		return type;
	}
}