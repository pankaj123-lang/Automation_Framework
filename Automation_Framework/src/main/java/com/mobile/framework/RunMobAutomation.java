package com.mobile.framework;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.framework.main.DatabaseOperations;
import com.framework.main.ReadFromDatasheet;
import com.framework.main.RunFramework;
import com.framework.main.SaveResult;
import com.web.framework.Run_Automation;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class RunMobAutomation {

	private static String srNO_Controller;
	private static String runStatusController;
	private static String application_Name;
	private static String dataSheet_Name;
	private static long maxExplicitWait_Time;
	private static long maxImplicitWait_Time;
	private static String dB_Connection="";
	private static String email;
	private static String sMS;
	private static boolean saveResult = false;
	private static AppiumDriver driver;
	private static String homedir;
	private static Recordset rsDB;
	private static String dBUserName;
	private static String dBPassword;
	private static String dbDriver;
	private static String dbUrl;
	protected static Connection con;
	private static int logCount;

	public static void getDataFromMobController() {

		Run_Automation.setStartTimeForLogs(new SimpleDateFormat("yyyy-MM-dd HH mm ss").format(new Date()));
		System.out.println("Inside Mobile Controller");
		String queryForController = "select * from MobileController where RunStatus='Y'";
		Run_Automation.setHomedir(System.getProperty("user.dir"));
		Fillo fillo = new Fillo();
		try {
			Run_Automation
					.setCon(fillo.getConnection(Run_Automation.getHomedir() + "/Resources/Controller_Mobile.xlsx"));
			Run_Automation.setRsController(Run_Automation.getCon().executeQuery(queryForController));
			while (Run_Automation.getRsController().next()) {
				srNO_Controller = Run_Automation.getRsController().getField("SrNO");
				runStatusController = Run_Automation.getRsController().getField("RunStatus");
				application_Name = Run_Automation.getRsController().getField("Application_Name");
				dataSheet_Name = Run_Automation.getRsController().getField("DataSheet_Name");
				maxExplicitWait_Time = Long
						.parseLong(Run_Automation.getRsController().getField("MaxExplicitWait_Time"));
				maxImplicitWait_Time = Long
						.parseLong(Run_Automation.getRsController().getField("MaxImplicitWait_Time"));
				dB_Connection = Run_Automation.getRsController().getField("DB_Connection");
				Run_Automation.setdB_Connection(dB_Connection);
				email = Run_Automation.getRsController().getField("Email");
				sMS = Run_Automation.getRsController().getField("SMS");
				Run_Automation.setMultipleTestData(Run_Automation.getRsController().getField("MultipleTestData"));

				if (email.equals("Y")) {
					getDataFromEmailConfigMobile();

				}
				if (dB_Connection.equals("Y")) {
					getDataFromDBPropertiesMobile();
					DatabaseOperations.RunDuration("started");
				}
				//////////////////////////// APPLICATION
				//////////////////////////// CONFIGURATION/////////////////////////////
				DesiredCapabilities capability = AppCapabilities.GetApplicationCapabilitiesFromSheet();
				////////////////////////// RESULT FILE CREATION////////////////////////////////
				SaveResult.generateResultFolderAndFiles(application_Name);
				//////////////////////////// READ DATASHEET/////////////////////////////////////
				if (AppCapabilities.getPlatformName().equalsIgnoreCase("Android")) {
					try {
						driver = new AndroidDriver(new URL(AppCapabilities.getAppiumUrl()), capability);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				} else if (AppCapabilities.getPlatformName().equalsIgnoreCase("IOS")) {
					try {
						driver = new IOSDriver(new URL(AppCapabilities.getAppiumUrl()), capability);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				}
				ReadFromDatasheet.getDataFromDataSheet();
				//////////////// Save result for Application //////////////////
				if (RunFramework.getAutomationType().equalsIgnoreCase("Automation")) {
					try {
						if (dB_Connection.equals("Y")) {
							getDataFromDBPropertiesMobile();
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

		} catch (FilloException e) {
			System.out.println("Unable to Connect to Controller..........");
			e.printStackTrace();
		} finally {
//			try {
//				SaveResult.getWorkbook().write(SaveResult.getFos());
//				SaveResult.getExtentrpt().flush();
//				Run_Automation.setSaveResult(true);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			if (RunFramework.getAutomationType().equalsIgnoreCase("Monitoring")) {
				driver.quit();
			} else {
				System.exit(0);
			}
		}

	}

	public static String getHomedir() {
		return homedir;
	}

	public static void setHomedir(String homedir) {
		RunMobAutomation.homedir = homedir;
	}

	public static Recordset getRsDB() {
		return rsDB;
	}

	public static void setRsDB(Recordset rsDB) {
		RunMobAutomation.rsDB = rsDB;
	}

	public static String getdBUserName() {
		return dBUserName;
	}

	public static void setdBUserName(String dBUserName) {
		RunMobAutomation.dBUserName = dBUserName;
	}

	public static String getdBPassword() {
		return dBPassword;
	}

	public static void setdBPassword(String dBPassword) {
		RunMobAutomation.dBPassword = dBPassword;
	}

	public static String getDbDriver() {
		return dbDriver;
	}

	public static void setDbDriver(String dbDriver) {
		RunMobAutomation.dbDriver = dbDriver;
	}

	public static String getDbUrl() {
		return dbUrl;
	}

	public static void setDbUrl(String dbUrl) {
		RunMobAutomation.dbUrl = dbUrl;
	}

	public static Connection getCon() {
		return con;
	}

	public static void setCon(Connection con) {
		RunMobAutomation.con = con;
	}

	public static int getLogCount() {
		return logCount;
	}

	public static void setLogCount(int logCount) {
		RunMobAutomation.logCount = logCount;
	}

	private static void getDataFromDBPropertiesMobile() {
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

	private static void getDataFromEmailConfigMobile() {
		// TODO Auto-generated method stub

	}

	public static String getSrNO_Controller() {
		return srNO_Controller;
	}

	public static void setSrNO_Controller(String srNO_Controller) {
		RunMobAutomation.srNO_Controller = srNO_Controller;
	}

	public static String getRunStatusController() {
		return runStatusController;
	}

	public static void setRunStatusController(String runStatusController) {
		RunMobAutomation.runStatusController = runStatusController;
	}

	public static String getApplication_Name() {
		return application_Name;
	}

	public static void setApplication_Name(String application_Name) {
		RunMobAutomation.application_Name = application_Name;
	}

	public static String getDataSheet_Name() {
		return dataSheet_Name;
	}

	public static void setDataSheet_Name(String dataSheet_Name) {
		RunMobAutomation.dataSheet_Name = dataSheet_Name;
	}

	public static long getMaxExplicitWait_Time() {
		return maxExplicitWait_Time;
	}

	public static void setMaxExplicitWait_Time(long maxExplicitWait_Time) {
		RunMobAutomation.maxExplicitWait_Time = maxExplicitWait_Time;
	}

	public static long getMaxImplicitWait_Time() {
		return maxImplicitWait_Time;
	}

	public static void setMaxImplicitWait_Time(long maxImplicitWait_Time) {
		RunMobAutomation.maxImplicitWait_Time = maxImplicitWait_Time;
	}

	public static String getdB_Connection() {
		return dB_Connection;
	}

	public static void setdB_Connection(String dB_Connection) {
		RunMobAutomation.dB_Connection = dB_Connection;
	}

	public static String getEmail() {
		return email;
	}

	public static void setEmail(String email) {
		RunMobAutomation.email = email;
	}

	public static String getsMS() {
		return sMS;
	}

	public static void setsMS(String sMS) {
		RunMobAutomation.sMS = sMS;
	}

	public static boolean isSaveResult() {
		return saveResult;
	}

	public static void setSaveResult(boolean saveResult) {
		RunMobAutomation.saveResult = saveResult;
	}

	public static AppiumDriver getDriver() {
		return driver;
	}

	public static void setDriver(AppiumDriver driver) {
		RunMobAutomation.driver = driver;
	}

	public static void main(String[] args) {
		getDataFromMobController();
	}
}
