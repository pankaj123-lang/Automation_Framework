package com.framework.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.web.framework.Run_Automation;
import com.mobile.framework.RunMobAutomation;

public class SaveResult {

	private static FileOutputStream fos;
	private static FileInputStream fin;
	private static XSSFWorkbook workbook;
	private static XSSFSheet sheet;
	private static ExtentSparkReporter sparkReport;
	protected static ExtentReports extentrpt;
	protected static ExtentTest extent;
	static int count = 1;
	private static String logsPath;
	private static String extentPath;

	private static String screenshotPath;
	private static String errorScreenshotID;
	private static File csvfile;

	public static void main(String[] args) {

	}

	public static void Reports(String actualValue, String expectedValue) {
		System.out.println("-----------------------Saving Logs--------------------------------");
		Date now = new Date();
		Calendar calender = Calendar.getInstance();
		double responseTime = (calender.getTimeInMillis()) - (Run_Automation.getStartTime());
		double tTotalTime = responseTime / 1000;
		String totalTimeRounded = String.format("%.2f", tTotalTime);
		calender.setTime(now);
		int iMonth = calender.get(Calendar.MONTH) + 1;
		String currentTimeDB = Calendar.getInstance().get(Calendar.YEAR) + "-" + iMonth + "-"
				+ calender.get(Calendar.DATE) + " " + calender.get(Calendar.HOUR_OF_DAY) + ":"
				+ calender.get(Calendar.MINUTE) + ":" + calender.get(Calendar.SECOND);
		String tSystemTime = calender.get(Calendar.HOUR_OF_DAY) + ":" + calender.get(Calendar.MINUTE) + ":"
				+ calender.get(Calendar.SECOND) + " ";
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		String date = format.format(now);
		String status;
		if (expectedValue.equalsIgnoreCase(actualValue)) {
			status = "PASS";

		} else {
			status = "FAIL";
		}
		if (RunFramework.getAutomationType().equalsIgnoreCase("Monitoring")) {
			try {
				FileWriter fileWritter = new FileWriter(csvfile, true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				totalTimeRounded = String.format("%.2f", tTotalTime);

				bufferWritter.write(ReadFromDatasheet.getScenario_DescriptionDS() + ","
						+ ReadFromDatasheet.getScenario_IdDS() + "," + ReadFromDatasheet.getTestCase_DescriptionDS()
						+ "," + ReadFromDatasheet.getTestCase_IdDS() + "," + ReadFromDatasheet.getTestCase_typeDS()
						+ "," + actualValue + "," + expectedValue + "," + status + "," + "=HYPERLINK(\""
						+ Screenshot.getScreenshotFileLocation1() + "\")" + ","
						+ ReadFromDatasheet.getStep_Description() + "," + ReadFromDatasheet.getSrNO_DS() + ","
						+ tSystemTime + "," + tSystemTime + "\n");
				bufferWritter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (Run_Automation.getdB_Connection().equalsIgnoreCase("Y")) {
				System.out.println("-----------------------Saving Logs In Database--------------------------------");
				SaveScreenshotIntoDb(ReadFromDatasheet.getPageNameDS());
				try {
					Class.forName(Run_Automation.getDbDriver());
					Connection connection = DriverManager.getConnection(Run_Automation.getDbUrl(),
							Run_Automation.getdBUserName(), Run_Automation.getdBPassword());
					String query = "insert into mont_execution_data(Application_Name,Page_Name,Actual_Result,Expected_Result,Status,Time_Taken,Timestamp,Step_No,Step_Description,screenshot_id) values(?,?,?,?,?,?,?,?,?,?)";

					try (PreparedStatement pstmt = connection.prepareStatement(query);) {
						pstmt.setString(1, ReadFromDatasheet.getApplication_Name_DS());
						pstmt.setString(2, ReadFromDatasheet.getPageNameDS());
						pstmt.setString(3, actualValue);
						pstmt.setString(4, expectedValue);
						pstmt.setString(5, status);
						pstmt.setString(6, totalTimeRounded);
						pstmt.setString(7, currentTimeDB);
						pstmt.setString(8, ReadFromDatasheet.getSrNO_DS());
						pstmt.setString(9, ReadFromDatasheet.getStep_Description());
						pstmt.setString(10, DatabaseOperations.getScreenshotFileLocation_ID());
						pstmt.executeUpdate();

					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				if (ExtentReportConfig.isStartTest() != true) {
					ExtentReportConfig.startExtentTest(ReadFromDatasheet.getNewHashMap());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Style

			try {
				sheet = workbook.getSheet("Sheet1");
				int lastRow = sheet.getLastRowNum();
				XSSFRow row = sheet.createRow(lastRow + 1);
				count++;
				System.out.println("Count==" + lastRow);
				XSSFCell cell = row.createCell(0);
				cell.setCellValue(ReadFromDatasheet.getScenario_DescriptionDS());
				cell = row.createCell(1);
				cell.setCellValue(ReadFromDatasheet.getScenario_IdDS());
				cell = row.createCell(2);
				cell.setCellValue(ReadFromDatasheet.getTestCase_DescriptionDS());
				cell = row.createCell(3);
				cell.setCellValue(ReadFromDatasheet.getTestCase_IdDS());
				cell = row.createCell(4);
				cell.setCellValue(ReadFromDatasheet.getTestCase_typeDS());
				cell = row.createCell(5);
				cell.setCellValue(actualValue);
				cell = row.createCell(6);
				cell.setCellValue(expectedValue);
				cell = row.createCell(7);

				if (status.equalsIgnoreCase("PASS")) {
					XSSFCellStyle style1 = workbook.createCellStyle();
					style1.setFillBackgroundColor(IndexedColors.DARK_GREEN.getIndex());
					style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					style1.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
					style1.setFillPattern(FillPatternType.FINE_DOTS);
					XSSFFont font1 = workbook.createFont();
					font1.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
					style1.setFont(font1);
					cell.setCellValue(status);
					cell.setCellStyle(style1);

					ExtentReportConfig.addPassStep(ReadFromDatasheet.getNewHashMap(), expectedValue, actualValue);

				} else {
					XSSFCellStyle style1 = workbook.createCellStyle();
					style1.setFillBackgroundColor(IndexedColors.RED.getIndex());
					style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					style1.setFillForegroundColor(IndexedColors.RED.getIndex());
					style1.setFillPattern(FillPatternType.FINE_DOTS);
					XSSFFont font1 = workbook.createFont();
					font1.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
					style1.setFont(font1);
					cell.setCellValue(status);
					cell.setCellStyle(style1);

					ExtentReportConfig.addFailStep(ReadFromDatasheet.getNewHashMap(), expectedValue, actualValue);

				}
				cell = row.createCell(8);
				cell.setCellType(Cell.CELL_TYPE_FORMULA);
				cell.setCellFormula(
						"HYPERLINK(\"" + Screenshot.getScreenshotFileLocation1() + "\", \"View Scrennshot\")");
				cell = row.createCell(9);
				cell.setCellValue(ReadFromDatasheet.getStep_Description());
				cell = row.createCell(10);
				cell.setCellValue(ReadFromDatasheet.getSrNO_DS());
				cell = row.createCell(11);
				cell.setCellValue(date);
				cell = row.createCell(12);
				cell.setCellValue(tSystemTime);

			} catch (Exception e) {
				e.printStackTrace();
			}
			if (Run_Automation.getdB_Connection().equalsIgnoreCase("Y")
					|| RunMobAutomation.getdB_Connection().equalsIgnoreCase("Y")) {
				System.out.println("-----------------------Saving Logs In Database--------------------------------");
				SaveScreenshotIntoDb(ReadFromDatasheet.getPageNameDS());
				try {
					Class.forName(Run_Automation.getDbDriver());
					Connection connection = DriverManager.getConnection(Run_Automation.getDbUrl(),
							Run_Automation.getdBUserName(), Run_Automation.getdBPassword());
					String query = "insert into result_logs(RUN_ID,Application_Name,Scenario_Id,Scenario_Description,Testcase_Id,Testcase_Description,"
							+ "Testcase_type,Actual_Result,Expected_Result,Status,Time_Taken,Timestamp,Step_No,Step_Description,ScreenshotId) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

					try (PreparedStatement pstmt = connection.prepareStatement(query);) {
						pstmt.setInt(1, DatabaseOperations.getRun_Id());
						pstmt.setString(2, ReadFromDatasheet.getApplication_Name_DS());
						pstmt.setString(3, ReadFromDatasheet.getScenario_IdDS());
						pstmt.setString(4, ReadFromDatasheet.getScenario_DescriptionDS());
						pstmt.setString(5, ReadFromDatasheet.getTestCase_IdDS());
						pstmt.setString(6, ReadFromDatasheet.getTestCase_DescriptionDS());
						pstmt.setString(7, ReadFromDatasheet.getTestCase_typeDS());
						pstmt.setString(8, actualValue);
						pstmt.setString(9, expectedValue);
						pstmt.setString(10, status);
						pstmt.setString(11, totalTimeRounded);
						pstmt.setString(12, currentTimeDB);
						pstmt.setString(13, ReadFromDatasheet.getSrNO_DS());
						pstmt.setString(14, ReadFromDatasheet.getStep_Description());
						pstmt.setString(15, DatabaseOperations.getScreenshotFileLocation_ID());
						pstmt.executeUpdate();

					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			}
			System.out.println("-------------logs entered successfully-------------");
		}

	}

	private static void SaveScreenshotIntoDb(String pagename) {
		System.out.println("-------------------------Save Screenshot into database-------------------------");
		String id = null;
		Connection conn = null;
		Statement statement = null;
		FileInputStream fis = null;
		try {
			String imagepath = Screenshot.getScreenshotFileLocation1();
			conn = DatabaseOperations.Localconnection_propoties();
			statement = conn.createStatement();
			File file = new File(imagepath);
			fis = new FileInputStream(file);
			System.out.println(imagepath);
			System.out.println(file.length());
			Date nowScreen = new Date();
			Calendar calender = Calendar.getInstance();
			calender.setTime(nowScreen);

			String screenShotTime = pagename + "_" + calender.get(Calendar.HOUR_OF_DAY) + "_"
					+ calender.get(Calendar.MINUTE) + "_" + calender.get(Calendar.SECOND);
			String getCurrentID = "SELECT max(id) as id FROM screenshots";
			try (PreparedStatement ps = conn
					.prepareStatement("insert into screenshots(image_name, image_value) values (?, ?)");) {

				ps.setString(1, screenShotTime + ".png");
				ps.setBinaryStream(2, fis, (int) file.length());
				ps.executeUpdate();
				ResultSet rs = statement.executeQuery(getCurrentID);
				while (rs.next()) {
					id = rs.getString("id");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				conn.close();
				statement.close();
			} catch (IOException | SQLException e) {
				e.printStackTrace();
			}

		}
		DatabaseOperations.setScreenshotFileLocation_ID(id);
		if (id != null)
			if ((id != null) || (!id.equals(""))) {
				errorScreenshotID = id;
				System.out.println("Screenshot id : " + errorScreenshotID);
			}
	}

	public static void generateResultFolderAndFiles(String application_Name) {
		String logFilePath;
		String extentFilePath = null;
//		File file;
		if (ReadDataFromConfigFile.getAutomationType().equalsIgnoreCase("Web")) {
			Date now = new Date();
			int year = Calendar.getInstance().get(Calendar.YEAR);
			String monthName = new SimpleDateFormat("MMM").format(now);
			int monthday = Calendar.getInstance().get(Calendar.DATE);
			if (RunFramework.getAutomationType().equalsIgnoreCase("Monitoring")) {
				logsPath = Run_Automation.getHomedir() + "/Reports/Web/ExcelReport_" + monthday + " " + monthName + " "
						+ year + "/" + application_Name + "/";
				screenshotPath = Run_Automation.getHomedir() + "/Reports/Web/ExcelReport_" + monthday + " " + monthName
						+ " " + year + "/" + application_Name + "/Screenshots/";
				System.out.println("logs path :- " + logsPath);
				System.out.println("Scrennshot path :- " + screenshotPath);
				new File(logsPath).mkdirs();
				new File(screenshotPath).mkdirs();
				logFilePath = logsPath + application_Name + ".csv";
				csvfile = new File(logFilePath);
			} else {
				logsPath = Run_Automation.getHomedir() + "/Reports/Web/ExcelReport_" + monthday + " " + monthName + " "
						+ year + "/" + application_Name + "/";
				extentPath = Run_Automation.getHomedir() + "/Reports/Web/HtmlReport_" + monthday + " " + monthName + " "
						+ year + "/" + application_Name;
				screenshotPath = Run_Automation.getHomedir() + "/Reports/Web/HtmlReport_" + monthday + " " + monthName
						+ " " + year + "/" + application_Name + "/Screenshots/";
				System.out.println("logs path :- " + logsPath);
				System.out.println("Extent report path :- " + extentPath);
				System.out.println("Scrennshot path :- " + screenshotPath);
				new File(logsPath).mkdirs();
				new File(extentPath).mkdirs();
				new File(screenshotPath).mkdirs();
				logFilePath = logsPath + application_Name + "_" + Run_Automation.getStartTimeForLogs() + ".xlsx";
				extentFilePath = extentPath + "/" + application_Name + "_" + Run_Automation.getStartTimeForLogs()
						+ ".html";
				csvfile = new File(logFilePath);
			}
		} else {
			Date now = new Date();
			int year = Calendar.getInstance().get(Calendar.YEAR);
			String monthName = new SimpleDateFormat("MMM").format(now);
			int monthday = Calendar.getInstance().get(Calendar.DATE);
			if (RunFramework.getAutomationType().equalsIgnoreCase("Monitoring")) {
				logsPath = Run_Automation.getHomedir() + "/Reports/Mobile/ExcelReport_" + monthday + " " + monthName
						+ " " + year + "/" + application_Name + "/";
				screenshotPath = Run_Automation.getHomedir() + "/Reports/Mobile/ExcelReport_" + monthday + " "
						+ monthName + " " + year + "/" + application_Name + "/Screenshots/";
				System.out.println("logs path :- " + logsPath);
				System.out.println("Scrennshot path :- " + screenshotPath);
				new File(logsPath).mkdirs();
				new File(screenshotPath).mkdirs();
				logFilePath = logsPath + application_Name + ".csv";

				csvfile = new File(logFilePath);
			} else {
				logsPath = Run_Automation.getHomedir() + "/Reports/Mobile/ExcelReport_" + monthday + " " + monthName
						+ " " + year + "/" + application_Name + "/";
				extentPath = Run_Automation.getHomedir() + "/Reports/Mobile/HtmlReport_" + monthday + " " + monthName
						+ " " + year + "/" + application_Name;
				screenshotPath = Run_Automation.getHomedir() + "/Reports/Mobile/HtmlReport_" + monthday + " "
						+ monthName + " " + year + "/" + application_Name + "/Screenshots/";
				System.out.println("logs path :- " + logsPath);
				System.out.println("Extent report path :- " + extentPath);
				System.out.println("Scrennshot path :- " + screenshotPath);
				new File(logsPath).mkdirs();
				new File(extentPath).mkdirs();
				new File(screenshotPath).mkdirs();
				logFilePath = logsPath + application_Name + "_" + Run_Automation.getStartTimeForLogs() + ".xlsx";
				extentFilePath = extentPath + "/" + application_Name + "_" + Run_Automation.getStartTimeForLogs()
						+ ".html";

				csvfile = new File(logFilePath);
			}
		}
		if (RunFramework.getAutomationType().equalsIgnoreCase("Automation")) {
			try {
//				new File(extentFilePath).createNewFile();
				extentrpt = new ExtentReports();
				sparkReport = new ExtentSparkReporter(extentFilePath);
				extentrpt.attachReporter(sparkReport);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		if (!csvfile.exists()) {
			try {
				if (RunFramework.getAutomationType().equalsIgnoreCase("Monitoring")) {
					System.out.println("----------------- Writting Header in CSV --------------------");
					csvfile.createNewFile();
					FileWriter fileWritter = new FileWriter(csvfile, true);
					BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
					bufferWritter.write(
							"Scenario Description, Scenario ID, Testcase Description,Testcase ID, TestCase Type, Actual Result, Expected Result, Status, Screenshot,Step_Description,Step No, Date, Time_Value\n");
					bufferWritter.close();

				} else {

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
					fos = new FileOutputStream(logFilePath, true);
					sheet = workbook.createSheet("Sheet1");
					System.out.println(logFilePath);
					csvfile.createNewFile();
					String[] columnHeadings = { "Scenario Description", "Scenario ID", "Testcase Description",
							"Testcase ID", "TestCase Type", "Actual Result", "Expected Result", "Status", "Screenshot",
							"Step_Description", "Step No", "Date", "Time_Value" };
					Row headerRow = sheet.createRow(0);
					for (int i = 0; i < columnHeadings.length; i++) {
						Cell cell = headerRow.createCell(i);
						cell.setCellValue(columnHeadings[i]);
						cell.setCellStyle(style);
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static FileOutputStream getFos() {
		return fos;
	}

	public static void setFos(FileOutputStream fos) {
		SaveResult.fos = fos;
	}

	public static FileInputStream getFin() {
		return fin;
	}

	public static void setFin(FileInputStream fin) {
		SaveResult.fin = fin;
	}

	public static XSSFWorkbook getWorkbook() {
		return workbook;
	}

	public static void setWorkbook(XSSFWorkbook workbook) {
		SaveResult.workbook = workbook;
	}

	public static XSSFSheet getSheet() {
		return sheet;
	}

	public static void setSheet(XSSFSheet sheet) {
		SaveResult.sheet = sheet;
	}

	public static int getCount() {
		return count;
	}

	public static void setCount(int count) {
		SaveResult.count = count;
	}

	public static ExtentSparkReporter getSparkReport() {
		return sparkReport;
	}

	public static void setSparkReport(ExtentSparkReporter sparkReport) {
		SaveResult.sparkReport = sparkReport;
	}

	public static ExtentReports getExtentrpt() {
		return extentrpt;
	}

	public static void setExtentrpt(ExtentReports extentrpt) {
		SaveResult.extentrpt = extentrpt;
	}

	public static ExtentTest getExtent() {
		return extent;
	}

	public static void setExtent(ExtentTest extent) {
		SaveResult.extent = extent;
	}

	public static String getLogsPath() {
		return logsPath;
	}

	public static void setLogsPath(String logsPath) {
		SaveResult.logsPath = logsPath;
	}

	public static String getExtentPath() {
		return extentPath;
	}

	public static void setExtentPath(String extentPath) {
		SaveResult.extentPath = extentPath;
	}

	public static String getScreenshotPath() {
		return screenshotPath;
	}

	public static void setScreenshotPath(String screenshotPath) {
		SaveResult.screenshotPath = screenshotPath;
	}

}
