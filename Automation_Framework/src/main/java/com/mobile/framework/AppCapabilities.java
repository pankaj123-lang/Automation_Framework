package com.mobile.framework;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Fillo;
import com.web.framework.Run_Automation;

import io.appium.java_client.remote.MobileCapabilityType;

public class AppCapabilities extends RunMobAutomation {
	private static String srNO_Cap;
	private static String runStatusCap;
	private static String application_Name_Cap;
	private static String appPackage;
	private static String appActivity;
	private static String appReset;
	private static String platformName;
	private static String platformVersion;
	private static String appiumUrl;
	private static String applicationType;
	private static String appPath;
	private static String deviceName;
	private static String udid;

	protected static DesiredCapabilities GetApplicationCapabilitiesFromSheet() {
		String queryForController = "select * from MobileCapabilities where RunStatus='Y' and Application_Name='"
				+ RunMobAutomation.getApplication_Name() + "'";
		Run_Automation.setHomedir(System.getProperty("user.dir"));
		Fillo fillo = new Fillo();
		try {
			Run_Automation
					.setCon(fillo.getConnection(Run_Automation.getHomedir() + "/Resources/Controller_Mobile.xlsx"));
			Run_Automation.setRsController(Run_Automation.getCon().executeQuery(queryForController));
			while (Run_Automation.getRsController().next()) {
				srNO_Cap = Run_Automation.getRsController().getField("SrNo");
				runStatusCap = Run_Automation.getRsController().getField("RunStatus");
				application_Name_Cap = Run_Automation.getRsController().getField("Application_Name");
				appPackage = Run_Automation.getRsController().getField("AppPackage");
				appActivity = Run_Automation.getRsController().getField("AppActivity");
				appReset = Run_Automation.getRsController().getField("AppReset");
				platformName = Run_Automation.getRsController().getField("PlatformName");
				platformVersion = Run_Automation.getRsController().getField("PlatformVersion");
				appiumUrl = Run_Automation.getRsController().getField("AppiumUrl");
				applicationType = Run_Automation.getRsController().getField("ApplicationType");
				appPath = Run_Automation.getRsController().getField("AppPath");
				deviceName = Run_Automation.getRsController().getField("DeviceName");
				udid = Run_Automation.getRsController().getField("Udid");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (FilloException e) {
			e.printStackTrace();
		}
		DesiredCapabilities capability = new DesiredCapabilities();

		if (appReset.equalsIgnoreCase("Yes")) {
			System.out.println("App Reset is : " + appReset);
			capability.setCapability("noReset", false);
			capability.setCapability("fullReset", true);
		} else {
			System.out.println("App Reset is : " + appReset);
			capability.setCapability("noReset", true);
			capability.setCapability("fullReset", false);
		}
		if (applicationType.equalsIgnoreCase("firefox")) {
			System.out.println("firefox");
			String os = System.getProperty("os.name");
			if (os.startsWith("Windows")) {
				os = "Windows";
			} else if (os.startsWith("Mac")) {
				os = "Mac";
			} else {
				os = "Linux";
			}
			capability = new DesiredCapabilities();
			capability.setCapability(MobileCapabilityType.DEVICE_NAME,deviceName );
			capability.setCapability(MobileCapabilityType.AUTOMATION_NAME, "Gecko");
			capability.setCapability(MobileCapabilityType.BROWSER_NAME, "firefox");
			capability.setCapability(MobileCapabilityType.PLATFORM_NAME, os);
			capability.setCapability(MobileCapabilityType.UDID, udid);
			capability.setCapability("newCommandTimeout", 180);
			Map<String, Object> firefoxOptions = new HashMap<>();
			firefoxOptions.put("androidPackage", "org.mozilla.firefox");
			capability.setCapability("-marionette", true);
			capability.setCapability("marionettePort", "2828");
			capability.setCapability("verbosity", "debug");
			capability.setCapability("moz:firefoxOptions", firefoxOptions);
			capability.setCapability("acceptInsecureCerts", true);
			final Map<String, Object> firefoxOptions1 = new HashMap<>();
			firefoxOptions1.put("level", "trace");
			capability.setCapability("log", firefoxOptions1);

		} else if (applicationType.equalsIgnoreCase("chrome")) {
			System.out.println("chrome capabilities");
			capability = new DesiredCapabilities();
			capability.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
			capability.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
			capability.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
			capability.setCapability(MobileCapabilityType.UDID, udid);
			capability.setCapability("newCommandTimeout", 180);
		} else if (applicationType.equalsIgnoreCase("safari")) {
			System.out.println("safari capabilities");
			capability = new DesiredCapabilities();
			capability.setCapability("platformName", platformName);
			capability.setCapability("platformVersion", platformVersion);
			capability.setCapability("automationName", "XCUITest");
			capability.setCapability("autoWebview", true);
			capability.setCapability("nativeWebTap", true);
			capability.setCapability("browserName", "Safari");
			capability.setCapability("deviceName", deviceName);
			capability.setCapability("udid", udid);

		}
		else if (applicationType.equalsIgnoreCase("Native_IOS")) {
			capability.setCapability("VERSION", platformVersion);
			capability.setCapability("deviceName", deviceName);
			capability.setCapability("platformName", platformName);
			capability.setCapability(MobileCapabilityType.UDID, udid);
			capability.setCapability("automationName", "XCUITest");
			capability.setCapability("clearSystemFiles", true);
			capability.setCapability("newCommandTimeout", 180);
			capability.setCapability("app", appPackage);
			
		}
		else if (applicationType.equalsIgnoreCase("IOS")) {
			capability.setCapability("udid", udid);
            System.out.println("UDID : " + udid);
            
		}
		else if (applicationType.equalsIgnoreCase("Native")){
			capability.setCapability("VERSION", platformVersion);
			capability.setCapability("deviceName", deviceName);
			capability.setCapability("platformName", platformName);
			capability.setCapability(MobileCapabilityType.UDID, udid);
			capability.setCapability("appPackage", appPackage);
			capability.setCapability("appActivity", appActivity);
			capability.setCapability("automationName", "uiautomator2");
			capability.setCapability("clearSystemFiles", true);
			capability.setCapability("newCommandTimeout", 180);
		}else {
			System.out.println("Application type is blank, Please provide application type in Configuration.");
		}
		return capability;

	}

	public static String getDeviceName() {
		return deviceName;
	}

	public static void setDeviceName(String deviceName) {
		AppCapabilities.deviceName = deviceName;
	}

	public static String getUdid() {
		return udid;
	}

	public static void setUdid(String udid) {
		AppCapabilities.udid = udid;
	}

	public static String getAppPath() {
		return appPath;
	}

	public static void setAppPath(String appPath) {
		AppCapabilities.appPath = appPath;
	}

	public static String getSrNO_Cap() {
		return srNO_Cap;
	}

	public static void setSrNO_Cap(String srNO_Cap) {
		AppCapabilities.srNO_Cap = srNO_Cap;
	}

	public static String getRunStatusCap() {
		return runStatusCap;
	}

	public static void setRunStatusCap(String runStatusCap) {
		AppCapabilities.runStatusCap = runStatusCap;
	}

	public static String getApplication_Name_Cap() {
		return application_Name_Cap;
	}

	public static void setApplication_Name_Cap(String application_Name_Cap) {
		AppCapabilities.application_Name_Cap = application_Name_Cap;
	}

	public static String getAppPackage() {
		return appPackage;
	}

	public static void setAppPackage(String appPackage) {
		AppCapabilities.appPackage = appPackage;
	}

	public static String getAppActivity() {
		return appActivity;
	}

	public static void setAppActivity(String appActivity) {
		AppCapabilities.appActivity = appActivity;
	}

	public static String getAppReset() {
		return appReset;
	}

	public static void setAppReset(String appReset) {
		AppCapabilities.appReset = appReset;
	}

	public static String getPlatformName() {
		return platformName;
	}

	public static void setPlatformName(String platformName) {
		AppCapabilities.platformName = platformName;
	}

	public static String getPlatformVersion() {
		return platformVersion;
	}

	public static void setPlatformVersion(String platformVersion) {
		AppCapabilities.platformVersion = platformVersion;
	}

	public static String getAppiumUrl() {
		return appiumUrl;
	}

	public static void setAppiumUrl(String appiumUrl) {
		AppCapabilities.appiumUrl = appiumUrl;
	}

	public static String getApplicationType() {
		return applicationType;
	}

	public static void setApplicationType(String applicationType) {
		AppCapabilities.applicationType = applicationType;
	}

}
