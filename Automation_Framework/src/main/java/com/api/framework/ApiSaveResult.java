package com.api.framework;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.web.framework.Run_Automation;

public class ApiSaveResult {
	private static XSSFSheet sheet;
	private static XSSFWorkbook workbook;
	static int count = 1;
	private static String logsPathApi;
	private static String logFilePathApi;
	private static FileOutputStream fos;
	private static String jsonPath;

	public static void ApiReports(String srNo, String apiName, String apiDescription) {
		System.out.println("-----------------------Saving Logs--------------------------------");
		sheet = workbook.getSheet("Sheet1");
		int lastRow = sheet.getLastRowNum();
		XSSFRow row = sheet.createRow(lastRow + 1);
		count++;
		System.out.println("Count==" + lastRow);
		XSSFCell cell = row.createCell(0);
		cell.setCellValue(apiName);
		cell = row.createCell(1);
		cell.setCellValue(apiDescription);
		cell = row.createCell(2);
		cell.setCellType(Cell.CELL_TYPE_FORMULA);
		cell.setCellFormula("HYPERLINK(\"" +jsonPath  + "\", \"View Response\")");
		cell = row.createCell(3);
		cell.setCellValue(srNo);
		cell = row.createCell(4);
		
		
	}


	public static void generateLogFolderAndFile() {
		// TODO Auto-generated method stub
		File file;
		Date now = new Date();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		String monthName = new SimpleDateFormat("MMM").format(now);
		int monthday = Calendar.getInstance().get(Calendar.DATE);
		logsPathApi = System.getProperty("user.dir") + "/Reports/API/ExcelReport_" + monthday + " " + monthName + " "
				+ year + "/";
		
		System.out.println("logs path :- " + logsPathApi);
		
		new File(logsPathApi).mkdirs();
		
		logFilePathApi = logsPathApi  + "API_TEST_ExecutionTime_" + Run_Automation.getStartTimeForLogs() + ".xlsx";

		file = new File(logFilePathApi);
		
		if (!file.exists()) {
			try {
				workbook = new XSSFWorkbook();
				Font headerFont = workbook.createFont();
				headerFont.setBold(true);
				headerFont.setColor(IndexedColors.BLACK.index);

				XSSFCellStyle style = workbook.createCellStyle();
				style.setFont(headerFont);
				style.setFillBackgroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
				style.setFillPattern(FillPatternType.DIAMONDS);
				style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
				style.setFillPattern(FillPatternType.FINE_DOTS);
				fos = new FileOutputStream(logFilePathApi, true);
				sheet = workbook.createSheet("Sheet1");
				System.out.println(logFilePathApi);
				file.createNewFile();
				String[] columnHeadings = { "Api Name", "Api Description", "Api Response","Step No" };
				Row headerRow = sheet.createRow(0);
				for (int i = 0; i < columnHeadings.length; i++) {
					Cell cell = headerRow.createCell(i);
					cell.setCellValue(columnHeadings[i]);
					cell.setCellStyle(style);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void createJsonResponseFile(String string, String apiName) {
		new File(logsPathApi+"/Response/").mkdirs();
		jsonPath =logsPathApi+"/Response/"+apiName+"_"+ new SimpleDateFormat("dd-MMM-yyyy HH mm ss").format(new Date());
		try {
			FileWriter file = new FileWriter(jsonPath);
			file.write(string);
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static XSSFSheet getSheet() {
		return sheet;
	}

	public static void setSheet(XSSFSheet sheet) {
		ApiSaveResult.sheet = sheet;
	}

	public static XSSFWorkbook getWorkbook() {
		return workbook;
	}

	public static void setWorkbook(XSSFWorkbook workbook) {
		ApiSaveResult.workbook = workbook;
	}

	public static int getCount() {
		return count;
	}

	public static void setCount(int count) {
		ApiSaveResult.count = count;
	}

	public static String getLogsPathApi() {
		return logsPathApi;
	}

	public static void setLogsPathApi(String logsPathApi) {
		ApiSaveResult.logsPathApi = logsPathApi;
	}

	public static String getLogFilePathApi() {
		return logFilePathApi;
	}

	public static void setLogFilePathApi(String logFilePathApi) {
		ApiSaveResult.logFilePathApi = logFilePathApi;
	}

	public static FileOutputStream getFos() {
		return fos;
	}

	public static void setFos(FileOutputStream fos) {
		ApiSaveResult.fos = fos;
	}

	public static String getJsonPath() {
		return jsonPath;
	}

	public static void setJsonPath(String jsonPath) {
		ApiSaveResult.jsonPath = jsonPath;
	}


}
