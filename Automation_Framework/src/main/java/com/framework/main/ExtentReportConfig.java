package com.framework.main;

import java.util.HashMap;

import com.aventstack.extentreports.Status;

//import com.relevantcodes.extentreports.LogStatus;

public class ExtentReportConfig extends SaveResult {

	private static boolean startTest;
	private static String screenshotfileLocation;

	public static void startExtentTest(HashMap<String, String> newHashMap) {
		try {
			if (!ReadFromDatasheet.getTestCase().equalsIgnoreCase(newHashMap.get("testCase_IdDS"))) {
				ReadFromDatasheet.setTestCase(ReadFromDatasheet.getTestCase_IdDS());
//				if(ReadFromDatasheet.getCountTest()>0) {
//					SaveResult.extentrpt.removeTest(SaveResult.extent);
//					SaveResult.extentrpt.flush();
//				}
//				ReadFromDatasheet.countTest++;

				if (ReadFromDatasheet.getTestCase_typeDS().equalsIgnoreCase("Positive")) {

					extent = extentrpt.createTest("<font color=\"Blue\"><b>" + newHashMap.get("scenario_IdDS")
							+ "</b></font> - <font color=\"Brown\"><b>" + newHashMap.get("scenario_DescriptionDS")
							+ "</b></font> - <font color=\"Green\"><b>" + newHashMap.get("testCase_IdDS")
							+ "</b></font>(" + newHashMap.get("testCase_DescriptionDS") + ")");
				} else if (ReadFromDatasheet.getTestCase_typeDS().equalsIgnoreCase("Negative")) {
					extent = extentrpt.createTest("<font color=\"Blue\"><b>" + newHashMap.get("scenario_IdDS")
							+ "</b></font> - <font color=\"Brown\"><b>" + newHashMap.get("scenario_DescriptionDS")
							+ "</b></font> - <font color=\"Red\"><b>" + newHashMap.get("testCase_IdDS") + "</b></font>("
							+ newHashMap.get("testCase_DescriptionDS") + ")");
				} else {
					extent = extentrpt.createTest("<font color=\"Blue\"><b>" + newHashMap.get("scenario_IdDS")
							+ "</b></font> - <font color=\"Brown\"><b>" + newHashMap.get("scenario_DescriptionDS")
							+ "</b></font> - <font color=\"Green\"><b>" + newHashMap.get("testCase_IdDS")
							+ "</b></font>(" + newHashMap.get("testCase_DescriptionDS") + ")");
				}

				startTest = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addStepsToExtentReport(HashMap<String, String> newHashMap) {
		extent.log(Status.INFO,
				"<font color=\"Blue\"><b>Module - </b></font>" + newHashMap.get("moduleName")
						+ " <font color=\"maroon\"><b>Step - </b></font>" + newHashMap.get("srNO_DS") + " "
						+ "<font color=\"Red\"><b>Values - </b></font>" + newHashMap.get("valuesDS")
						+ " <font color=\"Green\"><b>Performs - </b></font>" + newHashMap.get("performDS")
						+ "<b> Step Description - </b>" + newHashMap.get("step_Description"));
		extentrpt.flush();
	}

	public static void addPassStep(HashMap<String, String> newHashMap, String expectedValue, String actualValue) {
		extent.log(Status.PASS,
				"<br><font color=\"Red\"><b> Expected Result is - </b></font>" + expectedValue
						+ " <br> <font color=\"Red\"><b>Actual Result is - </b></font>" + actualValue + "<br>"
						+ extent.addScreenCaptureFromPath(Screenshot.getScreenshotFileLocation()) + "<br>"
						+ "<font color=\"Blue\"><b>Module - </b></font>" + newHashMap.get("moduleName")
						+ " <font color=\"maroon\"><b>Step - </b></font>" + newHashMap.get("srNO_DS") + " "
						+ "<font color=\"Red\"><b>Values - </b></font>" + newHashMap.get("valuesDS")
						+ " <font color=\"Green\"><b>Performs - </b></font>" + newHashMap.get("performDS")
						+ "<b> Step Description - </b>" + newHashMap.get("step_Description"));
		ReadFromDatasheet.setExtentLogcount(1);
		startTest = false;
		extentrpt.flush();
	}

	public static void addFailStep(HashMap<String, String> newHashMap, String expectedValue, String actualValue) {
		extent.log(Status.FAIL,
				"<br><font color=\"Red\"><b> Expected Result is - </b></font>" + expectedValue
						+ " <br> <font color=\"Red\"><b>Actual Result is - </b></font>" + actualValue + "<br>"
						+ extent.addScreenCaptureFromPath(Screenshot.getScreenshotFileLocation()) + "<br>"
						+ "<font color=\"Blue\"><b>Module - </b></font>" + newHashMap.get("moduleName")
						+ " <font color=\"maroon\"><b>Step - </b></font>" + newHashMap.get("srNO_DS") + " "
						+ "<font color=\"Red\"><b>Values - </b></font>" + newHashMap.get("valuesDS")
						+ " <font color=\"Green\"><b>Performs - </b></font>" + newHashMap.get("performDS")
						+ "<b> Step Description - </b>" + newHashMap.get("step_Description"));
		ReadFromDatasheet.setExtentLogcount(1);
		startTest = false;
		extentrpt.flush();
	}

	public static boolean isStartTest() {
		return startTest;
	}

	public static void setStartTest(boolean startTest) {
		ExtentReportConfig.startTest = startTest;
	}

	public static String getScreenshotfileLocation() {
		return screenshotfileLocation;
	}

	public static void setScreenshotfileLocation(String screenshotfileLocation) {
		ExtentReportConfig.screenshotfileLocation = screenshotfileLocation;
	}

	public static void main(String[] args) {

	}
}
