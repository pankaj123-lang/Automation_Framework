package com.web.framework;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.openqa.selenium.WebDriver;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.framework.main.DatabaseOperations;
import com.framework.main.ReadFromDatasheet;
import com.framework.main.RunFramework;
import com.framework.main.SaveResult;

public class Run_Automation {

	public Run_Automation() {
		super();

	}

	protected static String homedir;
	protected static Connection con;
	private static Recordset rsController;
	protected static Recordset rsDatasheet, rsValues;
	private static Recordset rsEmail;
	protected static String application_Name;
	private static Recordset rsDB;
	protected static WebDriver driver;
	private static String dBUserName;
	private static String dBPassword;
	private static String email_Host;
	private static String email_Port;
	private static String from_Mail;
	private static String to_Mail;
	private static String srNO_Controller;
	private static String runStatusController;
	protected static String dataSheet_Name;
	protected static String browser;
	protected static long maxExplicitWait_Time;
	private static long maxImplicitWait_Time;
	private static String dB_Connection;
	private static String email;
	private static String sMS;
	private static String dbDriver;
	protected static double startTime;
	private static Object startTimeForLogs;
	private static boolean saveResult = false;
	private static int logCount = 0;
	private static int appCount;
	private static String multipleTestData;
	private static String testDataId;
	private static String dbUrl;

	public static int getLogCount() {
		return logCount;
	}

	public static void setLogCount(int logCount) {
		Run_Automation.logCount = logCount;
	}

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if (isSaveResult() == false) {
					try {
						SaveResult.getWorkbook().write(SaveResult.getFos());
						SaveResult.getExtentrpt().flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		});
		int i = 0;
		while (i <= 3) {
			getDataFromController();
			i++;
		}

		System.out.println(srNO_Controller);
		System.out.println(runStatusController);
		System.out.println(to_Mail);
		System.out.println(from_Mail);
		System.out.println(email_Port);
		System.out.println(email_Host);
		System.out.println(dBPassword);
		System.out.println(dBUserName);
		System.out.println(sMS);
		System.out.println(dbDriver);
		System.out.println(email);
		System.out.println(dB_Connection);
		System.out.println(maxImplicitWait_Time);
		System.out.println(maxExplicitWait_Time);
		System.out.println(browser);
		System.out.println(dataSheet_Name);
		System.out.println(dbUrl);

	}

	public static void getDataFromController() {
		startTimeForLogs = new SimpleDateFormat("dd-MMM-yyyy HH mm ss").format(new Date());
		System.out.println("Inside Controller");
		String queryForController = "select * from Controller where RunStatus='Y'";
		homedir = System.getProperty("user.dir");
		Fillo fillo = new Fillo();
		try {
			con = fillo.getConnection(homedir + "/Resources/Controller_Web.xlsx");
			rsController = con.executeQuery("SELECT count(*) FROM Controller where RunStatus='Y'");
			if (rsController != null) {
				appCount = rsController.getCount();
			}
			System.out.println("Number of Application to execute : " + appCount);
			rsController = con.executeQuery(queryForController);

			while (rsController.next()) {
				srNO_Controller = rsController.getField("SrNO");
				runStatusController = rsController.getField("RunStatus");
				application_Name = rsController.getField("Application_Name");
				dataSheet_Name = rsController.getField("DataSheet_Name");
				browser = rsController.getField("Browser");
				maxExplicitWait_Time = Long.parseLong(rsController.getField("MaxExplicitWait_Time"));
				maxImplicitWait_Time = Long.parseLong(rsController.getField("MaxImplicitWait_Time"));
				dB_Connection = rsController.getField("DB_Connection");
				email = rsController.getField("Email");
				sMS = rsController.getField("SMS");
				multipleTestData = rsController.getField("MultipleTestData");
				System.out.println("Application Name ------------> " + application_Name);
				if (email.equals("Y")) {
					getDataFromEmailConfig();

				}
				if (dB_Connection.equals("Y")) {
					getDataFromDBProperties();
					DatabaseOperations.RunDuration("started");
				}
				//////////////////////////// BROWSER CONFIGURATION/////////////////////////////
				BrowserConfiguration.configureBrowser();
				////////////////////////// RESULT FILE CREATION////////////////////////////////
				SaveResult.generateResultFolderAndFiles(application_Name);
				//////////////////////////// READ DATASHEET/////////////////////////////////////
				ReadFromDatasheet.getDataFromDataSheet();

				//////////////// Save result for Application //////////////////
				if (RunFramework.getAutomationType().equalsIgnoreCase("Automation")) {
					try {
						if (dB_Connection.equals("Y")) {
							getDataFromDBProperties();
							DatabaseOperations.RunDuration("completed");
							DatabaseOperations.RunDuration("total");
						}
						SaveResult.getWorkbook().write(SaveResult.getFos());
						SaveResult.getExtentrpt().flush();
//						setSaveResult(true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				logCount++;
			}

		} catch (Exception e) {
			System.out.println("Unable to Connect to Controller..........");
			e.printStackTrace();
		} finally {
			if (RunFramework.getAutomationType().equalsIgnoreCase("Monitoring")) {
				driver.quit();
			} else {
				System.exit(0);
			}
		}
	}

	private static void getDataFromDBProperties() {
		System.out.println("Inside DB Properties========");
		String queryForDB = "select * from DB_Properties where RunStatus='Y' and ApplicationName='" + application_Name
				+ "'";
		homedir = System.getProperty("user.dir");
		try {
			rsDB = con.executeQuery(queryForDB);
			while (rsDB.next()) {
				dbDriver = rsDB.getField("Driver");
				dbUrl = rsDB.getField("Url");
				dBUserName = rsDB.getField("UserName");
				dBPassword = rsDB.getField("Password");
			}
		} catch (FilloException e) {
			System.out.println("Unable to Connect to DB_Properties..........");
		}
	}

	private static ArrayList<String> getDataFromEmailConfig() {
		ArrayList<String> emailConfigData = new ArrayList<String>();
		System.out.println("Inside Email Configuration");
		String queryForController1 = "select * from Email_Config where RunStatus='Y' and Application_Name='"
				+ application_Name + "'";
		homedir = System.getProperty("user.dir");
		try {
			rsEmail = con.executeQuery(queryForController1);
			while (rsEmail.next()) {
				email_Host = rsEmail.getField("email_Host");
				email_Port = rsEmail.getField("email_Port");
				from_Mail = rsEmail.getField("from_Mail");
				to_Mail = rsEmail.getField("to_Mail");
				Collections.addAll(emailConfigData, email_Host, email_Port, from_Mail, to_Mail);
				break;
			}
		} catch (FilloException e) {
//			e.printStackTrace();
			System.out.println("Unable to Connect to Email_Config..........");
		}
		return emailConfigData;
	}
//////////////////////////////////////////////////////getter setter////////////////////////////////

	public static double getStartTime() {
		return startTime;
	}

	public static void setStartTime(double startTime) {
		Run_Automation.startTime = startTime;
	}

	public static String getHomedir() {
		return homedir;
	}

	public static void setHomedir(String homedir) {
		Run_Automation.homedir = homedir;
	}

	public static Connection getCon() {
		return con;
	}

	public static void setCon(Connection con) {
		Run_Automation.con = con;
	}

	public static Recordset getRsController() {
		return rsController;
	}

	public static void setRsController(Recordset rsController) {
		Run_Automation.rsController = rsController;
	}

	public static Recordset getRsDatasheet() {
		return rsDatasheet;
	}

	public static void setRsDatasheet(Recordset rsDatasheet) {
		Run_Automation.rsDatasheet = rsDatasheet;
	}

	public static Recordset getRsValues() {
		return rsValues;
	}

	public static void setRsValues(Recordset rsValues) {
		Run_Automation.rsValues = rsValues;
	}

	public static Recordset getRsEmail() {
		return rsEmail;
	}

	public static void setRsEmail(Recordset rsEmail) {
		Run_Automation.rsEmail = rsEmail;
	}

	public static String getApplication_Name() {
		return application_Name;
	}

	public static void setApplication_Name(String application_Name) {
		Run_Automation.application_Name = application_Name;
	}

	public static Recordset getRsDB() {
		return rsDB;
	}

	public static void setRsDB(Recordset rsDB) {
		Run_Automation.rsDB = rsDB;
	}

	public static WebDriver getDriver() {
		return driver;
	}

	public static void setDriver(WebDriver driver) {
		Run_Automation.driver = driver;
	}

	public static String getdBUserName() {
		return dBUserName;
	}

	public static void setdBUserName(String dBUserName) {
		Run_Automation.dBUserName = dBUserName;
	}

	public static String getdBPassword() {
		return dBPassword;
	}

	public static void setdBPassword(String dBPassword) {
		Run_Automation.dBPassword = dBPassword;
	}

	public static String getEmail_Host() {
		return email_Host;
	}

	public static void setEmail_Host(String email_Host) {
		Run_Automation.email_Host = email_Host;
	}

	public static String getEmail_Port() {
		return email_Port;
	}

	public static void setEmail_Port(String email_Port) {
		Run_Automation.email_Port = email_Port;
	}

	public static String getFrom_Mail() {
		return from_Mail;
	}

	public static void setFrom_Mail(String from_Mail) {
		Run_Automation.from_Mail = from_Mail;
	}

	public static String getTo_Mail() {
		return to_Mail;
	}

	public static void setTo_Mail(String to_Mail) {
		Run_Automation.to_Mail = to_Mail;
	}

	public static String getSrNO_Controller() {
		return srNO_Controller;
	}

	public static void setSrNO_Controller(String srNO_Controller) {
		Run_Automation.srNO_Controller = srNO_Controller;
	}

	public static String getRunStatusController() {
		return runStatusController;
	}

	public static void setRunStatusController(String runStatusController) {
		Run_Automation.runStatusController = runStatusController;
	}

	public static String getDataSheet_Name() {
		return dataSheet_Name;
	}

	public static void setDataSheet_Name(String dataSheet_Name) {
		Run_Automation.dataSheet_Name = dataSheet_Name;
	}

	public static String getBrowser() {
		return browser;
	}

	public static void setBrowser(String browser) {
		Run_Automation.browser = browser;
	}

	public static long getMaxExplicitWait_Time() {
		return maxExplicitWait_Time;
	}

	public static void setMaxExplicitWait_Time(long maxExplicitWait_Time) {
		Run_Automation.maxExplicitWait_Time = maxExplicitWait_Time;
	}

	public static long getMaxImplicitWait_Time() {
		return maxImplicitWait_Time;
	}

	public static void setMaxImplicitWait_Time(long maxImplicitWait_Time) {
		Run_Automation.maxImplicitWait_Time = maxImplicitWait_Time;
	}

	public static String getdB_Connection() {
		return dB_Connection;
	}

	public static void setdB_Connection(String dB_Connection) {
		Run_Automation.dB_Connection = dB_Connection;
	}

	public static String getEmail() {
		return email;
	}

	public static void setEmail(String email) {
		Run_Automation.email = email;
	}

	public static String getsMS() {
		return sMS;
	}

	public static void setsMS(String sMS) {
		Run_Automation.sMS = sMS;
	}

	public static String getDbDriver() {
		return dbDriver;
	}

	public static void setDbDriver(String dbDriver) {
		Run_Automation.dbDriver = dbDriver;
	}

	public static Object getStartTimeForLogs() {
		return startTimeForLogs;
	}

	public static void setStartTimeForLogs(Object startTimeForLogs) {
		Run_Automation.startTimeForLogs = startTimeForLogs;
	}

	public static boolean isSaveResult() {
		return saveResult;
	}

	public static void setSaveResult(boolean saveResult) {
		Run_Automation.saveResult = saveResult;
	}

	public static String getDbUrl() {
		return dbUrl;
	}

	public static void setDbUrl(String dbUrl) {
		Run_Automation.dbUrl = dbUrl;
	}

	public static String getMultipleTestData() {
		return multipleTestData;
	}

	public static void setMultipleTestData(String multipleTestData) {
		Run_Automation.multipleTestData = multipleTestData;
	}

	public static String getTestDataId() {
		return testDataId;
	}

	public static void setTestDataId(String testDataId) {
		Run_Automation.testDataId = testDataId;
	}

	public static int getAppCount() {
		return appCount;
	}

	public static void setAppCount(int appCount) {
		Run_Automation.appCount = appCount;
	}
}
