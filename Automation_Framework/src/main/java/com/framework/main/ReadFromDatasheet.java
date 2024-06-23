package com.framework.main;

import java.util.ArrayList;
import java.util.HashMap;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.mobile.framework.RunMobAutomation;
import com.web.framework.Run_Automation;

public class ReadFromDatasheet extends Run_Automation {

	private static String srNO_DS;
	private static String application_Name_DS;
	private static String pageNameDS;
	private static String scenario_IdDS;
	private static String scenario_DescriptionDS;
	private static String testCase_IdDS;
	private static String testCase_DescriptionDS;
	private static String testCase_typeDS;
	private static String runStatusDS;
	private static String controlDS;
	private static String objectTypeDS;
	private static String objectDS;
	private static String performDS;
	private static String valuesDS;
	private static ArrayList<String> datasheetListValue;
	protected static HashMap<String, String> newHashMap;
	protected static String testCase = "";
	private static int extentLogcount;
	private static String moduleName;
	protected static int countTest;
	private static String datasheetPath;
	private static String optionsDS;
	private static String step_Description;

	public static int getCountTest() {
		return countTest;
	}

	public static void setCountTest(int countTest) {
		ReadFromDatasheet.countTest = countTest;
	}

	public static void main(String[] args) {
		getDataFromDataSheet();
	}

	public static void getDataFromDataSheet() {
		System.out.println("Inside Datasheet");

		Fillo fillo = new Fillo();
		if (Run_Automation.getMultipleTestData().equalsIgnoreCase("y")) {

			try {
				if (ReadDataFromConfigFile.getAutomationType().equalsIgnoreCase("Web")) {
					datasheetPath = Run_Automation.homedir + "/Resources/DataSheet/" + Run_Automation.dataSheet_Name;

				} else {
					datasheetPath = Run_Automation.homedir + "/Resources/DataSheet/"
							+ RunMobAutomation.getDataSheet_Name();
				}
				System.out.println("Datasource path : " + datasheetPath);
				con = fillo.getConnection(datasheetPath);
				Recordset rsSheet2 = con.executeQuery("select TestDataId from Sheet2 where RunStatus='Y'");
				System.out.println(rsSheet2.getCount());
				while (rsSheet2.next()) {
					String testDataIdSheet2 = rsSheet2.getField("TestDataId");
					String queryForDatasheet;
					if (ReadDataFromConfigFile.getAutomationType().equalsIgnoreCase("Web")) {
						queryForDatasheet = "select * from Sheet1 where RunStatus='Y' and Application_Name='"
								+ application_Name + "'";

					} else {
						queryForDatasheet = "select * from Sheet1 where RunStatus='Y' and Application_Name='"
								+ RunMobAutomation.getApplication_Name() + "'";
					}

					System.out.println("Data Get Query : " + queryForDatasheet);
					Run_Automation.rsDatasheet = con.executeQuery(queryForDatasheet);
					while (Run_Automation.rsDatasheet.next()) {
						srNO_DS = Run_Automation.rsDatasheet.getField("SrNo");
						application_Name_DS = Run_Automation.rsDatasheet.getField("Application_Name");
						moduleName = Run_Automation.rsDatasheet.getField("Module");
						pageNameDS = Run_Automation.rsDatasheet.getField("PageName");
						scenario_IdDS = Run_Automation.rsDatasheet.getField("Scenario_Id");
						scenario_DescriptionDS = Run_Automation.rsDatasheet.getField("Scenario_Description");
						testCase_IdDS = Run_Automation.rsDatasheet.getField("TestCase_Id");
						testCase_DescriptionDS = Run_Automation.rsDatasheet.getField("TestCase_Description");
						testCase_typeDS = Run_Automation.rsDatasheet.getField("TestCase_type");
						step_Description = Run_Automation.rsDatasheet.getField("Step_Description");
						runStatusDS = Run_Automation.rsDatasheet.getField("RunStatus");
						controlDS = Run_Automation.rsDatasheet.getField("Control");
						objectTypeDS = Run_Automation.rsDatasheet.getField("ObjectType");
						objectDS = Run_Automation.rsDatasheet.getField("Object");
						performDS = Run_Automation.rsDatasheet.getField("Perform");
						valuesDS = Run_Automation.rsDatasheet.getField("DataValues");
						optionsDS = Run_Automation.rsDatasheet.getField("Options");

						System.out.println(
								"------------------------------------------------------------------------------------------------------------------");
						if (!valuesDS.equalsIgnoreCase("") && valuesDS != null) {
							if (Run_Automation.getMultipleTestData().equalsIgnoreCase("y")) {
								String queryForDatasheet2 = "select " + valuesDS
										+ " from Sheet2 where RunStatus='Y' and TestDataId='" + testDataIdSheet2 + "'";
								System.out.println(queryForDatasheet2);
								Recordset rs = con.executeQuery(queryForDatasheet2);
								String sh2Value = null;
								while (rs.next()) {
									sh2Value = rs.getField(valuesDS);
									break;
								}
								valuesDS = sh2Value;
							}
						}
//						System.out.println(objectDS);
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("srNO_DS", srNO_DS);
						map.put("application_Name_DS", application_Name_DS);
						map.put("moduleName", moduleName);
						map.put("pageNameDS", pageNameDS);
						map.put("scenario_IdDS", scenario_IdDS);
						map.put("scenario_DescriptionDS", scenario_DescriptionDS);
						map.put("testCase_IdDS", testCase_IdDS);
						map.put("testCase_DescriptionDS", testCase_DescriptionDS);
						map.put("testCase_typeDS", testCase_typeDS);
						map.put("runStatusDS", runStatusDS);
						map.put("controlDS", controlDS);
						map.put("objectTypeDS", objectTypeDS);
						map.put("objectDS", objectDS);
						map.put("performDS", performDS);
						map.put("valuesDS", valuesDS);
						map.put("optionsDS", optionsDS);
						map.put("step_Description", step_Description);
						newHashMap = new HashMap<String, String>();
						newHashMap.putAll(map);
						System.out.println(newHashMap);

						Validator.validateObjects(newHashMap);

						if (RunFramework.getAutomationType().equalsIgnoreCase("Automation")) {
							if (Validator.getExtentStatus() == 1) {
								if (!testCase.equalsIgnoreCase(testCase_IdDS)) {
									testCase = testCase_IdDS;
									if (ReadFromDatasheet.getTestCase_typeDS().equalsIgnoreCase("Positive")) {

										SaveResult.setExtent(SaveResult.getExtentrpt()
												.createTest("<font color=\"Blue\"><b>" + newHashMap.get("scenario_IdDS")
														+ "</b></font> - <font color=\"Brown\"><b>"
														+ newHashMap.get("scenario_DescriptionDS")
														+ "</b></font> - <font color=\"Green\"><b>"
														+ newHashMap.get("testCase_IdDS") + "</b></font>("
														+ newHashMap.get("testCase_DescriptionDS") + ")"));
									} else if (ReadFromDatasheet.getTestCase_typeDS().equalsIgnoreCase("Negative")) {
										SaveResult.setExtent(SaveResult.getExtentrpt()
												.createTest("<font color=\"Blue\"><b>" + newHashMap.get("scenario_IdDS")
														+ "</b></font> - <font color=\"Brown\"><b>"
														+ newHashMap.get("scenario_DescriptionDS")
														+ "</b></font> - <font color=\"Red\"><b>"
														+ newHashMap.get("testCase_IdDS") + "</b></font>("
														+ newHashMap.get("testCase_DescriptionDS") + ")"));
									} else {
										SaveResult.setExtent(SaveResult.getExtentrpt()
												.createTest("<font color=\"Blue\"><b>" + newHashMap.get("scenario_IdDS")
														+ "</b></font> - <font color=\"Brown\"><b>"
														+ newHashMap.get("scenario_DescriptionDS")
														+ "</b></font> - <font color=\"Green\"><b>"
														+ newHashMap.get("testCase_IdDS") + "</b></font>("
														+ newHashMap.get("testCase_DescriptionDS") + ")"));
									}

									ExtentReportConfig.setStartTest(true);
								}
								if (extentLogcount == 0) {
									ExtentReportConfig.addStepsToExtentReport(newHashMap);
								}
								extentLogcount = 0;
							}
						}
					}
				}
			} catch (FilloException e) {
				e.printStackTrace();
			}
		} else {
			try {
				String queryForDatasheet;
				if (ReadDataFromConfigFile.getAutomationType().equalsIgnoreCase("Web")) {
					queryForDatasheet = "select * from Sheet1 where RunStatus='Y' and Application_Name='"
							+ application_Name + "'";
					datasheetPath = Run_Automation.homedir + "/Resources/DataSheet/" + Run_Automation.dataSheet_Name;
					con = fillo.getConnection(datasheetPath);
				} else {
					queryForDatasheet = "select * from Sheet1 where RunStatus='Y' and Application_Name='"
							+ RunMobAutomation.getApplication_Name() + "'";
					datasheetPath = Run_Automation.homedir + "/Resources/DataSheet/"
							+ RunMobAutomation.getDataSheet_Name();
					con = fillo.getConnection(datasheetPath);
				}
				System.out.println("Datasource path : " + datasheetPath);
				System.out.println("Data Get Query : " + queryForDatasheet);
				Run_Automation.rsDatasheet = con.executeQuery(queryForDatasheet);
				while (Run_Automation.rsDatasheet.next()) {
					srNO_DS = Run_Automation.rsDatasheet.getField("SrNo");
					application_Name_DS = Run_Automation.rsDatasheet.getField("Application_Name");
					moduleName = Run_Automation.rsDatasheet.getField("Module");
					pageNameDS = Run_Automation.rsDatasheet.getField("PageName");
					scenario_IdDS = Run_Automation.rsDatasheet.getField("Scenario_Id");
					scenario_DescriptionDS = Run_Automation.rsDatasheet.getField("Scenario_Description");
					testCase_IdDS = Run_Automation.rsDatasheet.getField("TestCase_Id");
					testCase_DescriptionDS = Run_Automation.rsDatasheet.getField("TestCase_Description");
					testCase_typeDS = Run_Automation.rsDatasheet.getField("TestCase_type");
					step_Description = Run_Automation.rsDatasheet.getField("Step_Description");
					runStatusDS = Run_Automation.rsDatasheet.getField("RunStatus");
					controlDS = Run_Automation.rsDatasheet.getField("Control");
					objectTypeDS = Run_Automation.rsDatasheet.getField("ObjectType");
					objectDS = Run_Automation.rsDatasheet.getField("Object");
					performDS = Run_Automation.rsDatasheet.getField("Perform");
					valuesDS = Run_Automation.rsDatasheet.getField("DataValues");
					optionsDS = Run_Automation.rsDatasheet.getField("Options");
					if (!valuesDS.equalsIgnoreCase("") && valuesDS != null) {
						if (Run_Automation.getMultipleTestData().equalsIgnoreCase("y")) {
							String queryForDatasheet2 = "select " + valuesDS
									+ " from Sheet2 where RunStatus='Y' and TestDataId='"
									+ Run_Automation.getTestDataId() + "'";
							System.out.println(queryForDatasheet2);
							Recordset rs = con.executeQuery(queryForDatasheet2);
							String sh2Value = null;
							while (rs.next()) {
								sh2Value = rs.getField(valuesDS);
								break;
							}
							valuesDS = sh2Value;
						}
					}
					System.out.println(objectDS);
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("srNO_DS", srNO_DS);
					map.put("application_Name_DS", application_Name_DS);
					map.put("moduleName", moduleName);
					map.put("pageNameDS", pageNameDS);
					map.put("scenario_IdDS", scenario_IdDS);
					map.put("scenario_DescriptionDS", scenario_DescriptionDS);
					map.put("testCase_IdDS", testCase_IdDS);
					map.put("testCase_DescriptionDS", testCase_DescriptionDS);
					map.put("testCase_typeDS", testCase_typeDS);
					map.put("runStatusDS", runStatusDS);
					map.put("controlDS", controlDS);
					map.put("objectTypeDS", objectTypeDS);
					map.put("objectDS", objectDS);
					map.put("performDS", performDS);
					map.put("valuesDS", valuesDS);
					map.put("optionsDS", optionsDS);
					map.put("step_Description", step_Description);
					newHashMap = new HashMap<String, String>();
					newHashMap.putAll(map);
					System.out.println(newHashMap);

					Validator.validateObjects(newHashMap);

					if (RunFramework.getAutomationType().equalsIgnoreCase("Automation")) {
						if (Validator.getExtentStatus() == 1) {
							if (!testCase.equalsIgnoreCase(testCase_IdDS)) {
								testCase = testCase_IdDS;
								if (ReadFromDatasheet.getTestCase_typeDS().equalsIgnoreCase("Positive")) {

									SaveResult.setExtent(SaveResult.getExtentrpt()
											.createTest("<font color=\"Blue\"><b>" + newHashMap.get("scenario_IdDS")
													+ "</b></font> - <font color=\"Brown\"><b>"
													+ newHashMap.get("scenario_DescriptionDS")
													+ "</b></font> - <font color=\"Green\"><b>"
													+ newHashMap.get("testCase_IdDS") + "</b></font>("
													+ newHashMap.get("testCase_DescriptionDS") + ")"));
								} else if (ReadFromDatasheet.getTestCase_typeDS().equalsIgnoreCase("Negative")) {
									SaveResult.setExtent(SaveResult.getExtentrpt()
											.createTest("<font color=\"Blue\"><b>" + newHashMap.get("scenario_IdDS")
													+ "</b></font> - <font color=\"Brown\"><b>"
													+ newHashMap.get("scenario_DescriptionDS")
													+ "</b></font> - <font color=\"Red\"><b>"
													+ newHashMap.get("testCase_IdDS") + "</b></font>("
													+ newHashMap.get("testCase_DescriptionDS") + ")"));
								} else {
									SaveResult.setExtent(SaveResult.getExtentrpt()
											.createTest("<font color=\"Blue\"><b>" + newHashMap.get("scenario_IdDS")
													+ "</b></font> - <font color=\"Brown\"><b>"
													+ newHashMap.get("scenario_DescriptionDS")
													+ "</b></font> - <font color=\"Green\"><b>"
													+ newHashMap.get("testCase_IdDS") + "</b></font>("
													+ newHashMap.get("testCase_DescriptionDS") + ")"));
								}

								ExtentReportConfig.setStartTest(true);
							}
							if (extentLogcount == 0) {
								ExtentReportConfig.addStepsToExtentReport(newHashMap);
							}
							extentLogcount = 0;
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Unable to connect to DataSource----------");
			}
		}

	}

	public static String getStep_Description() {
		return step_Description;
	}

	public static void setStep_Description(String step_Description) {
		ReadFromDatasheet.step_Description = step_Description;
	}

	public static String getOptionsDS() {
		return optionsDS;
	}

	public static void setOptionsDS(String optionsDS) {
		ReadFromDatasheet.optionsDS = optionsDS;
	}

	public static String getDatasheetPath() {
		return datasheetPath;
	}

	public static void setDatasheetPath(String datasheetPath) {
		ReadFromDatasheet.datasheetPath = datasheetPath;
	}

	public static HashMap<String, String> getNewHashMap() {
		return newHashMap;
	}

	public static void setNewHashMap(HashMap<String, String> newHashMap) {
		ReadFromDatasheet.newHashMap = newHashMap;
	}

	public static String getTestCase() {
		return testCase;
	}

	public static void setTestCase(String testCase) {
		ReadFromDatasheet.testCase = testCase;
	}

	public static int getExtentLogcount() {
		return extentLogcount;
	}

	public static void setExtentLogcount(int extentLogcount) {
		ReadFromDatasheet.extentLogcount = extentLogcount;
	}

	public static String getModuleName() {
		return moduleName;
	}

	public static void setModuleName(String moduleName) {
		ReadFromDatasheet.moduleName = moduleName;
	}

	public static String getSrNO_DS() {
		return srNO_DS;
	}

	public static void setSrNO_DS(String srNO_DS) {
		ReadFromDatasheet.srNO_DS = srNO_DS;
	}

	public static String getApplication_Name_DS() {
		return application_Name_DS;
	}

	public static void setApplication_Name_DS(String application_Name_DS) {
		ReadFromDatasheet.application_Name_DS = application_Name_DS;
	}

	public static String getPageNameDS() {
		return pageNameDS;
	}

	public static void setPageNameDS(String pageNameDS) {
		ReadFromDatasheet.pageNameDS = pageNameDS;
	}

	public static String getScenario_IdDS() {
		return scenario_IdDS;
	}

	public static void setScenario_IdDS(String scenario_IdDS) {
		ReadFromDatasheet.scenario_IdDS = scenario_IdDS;
	}

	public static String getScenario_DescriptionDS() {
		return scenario_DescriptionDS;
	}

	public static void setScenario_DescriptionDS(String scenario_DescriptionDS) {
		ReadFromDatasheet.scenario_DescriptionDS = scenario_DescriptionDS;
	}

	public static String getTestCase_IdDS() {
		return testCase_IdDS;
	}

	public static void setTestCase_IdDS(String testCase_IdDS) {
		ReadFromDatasheet.testCase_IdDS = testCase_IdDS;
	}

	public static String getTestCase_DescriptionDS() {
		return testCase_DescriptionDS;
	}

	public static void setTestCase_DescriptionDS(String testCase_DescriptionDS) {
		ReadFromDatasheet.testCase_DescriptionDS = testCase_DescriptionDS;
	}

	public static String getTestCase_typeDS() {
		return testCase_typeDS;
	}

	public static void setTestCase_typeDS(String testCase_typeDS) {
		ReadFromDatasheet.testCase_typeDS = testCase_typeDS;
	}

	public static String getRunStatusDS() {
		return runStatusDS;
	}

	public static void setRunStatusDS(String runStatusDS) {
		ReadFromDatasheet.runStatusDS = runStatusDS;
	}

	public static String getControlDS() {
		return controlDS;
	}

	public static void setControlDS(String controlDS) {
		ReadFromDatasheet.controlDS = controlDS;
	}

	public static String getObjectTypeDS() {
		return objectTypeDS;
	}

	public static void setObjectTypeDS(String objectTypeDS) {
		ReadFromDatasheet.objectTypeDS = objectTypeDS;
	}

	public static String getObjectDS() {
		return objectDS;
	}

	public static void setObjectDS(String objectDS) {
		ReadFromDatasheet.objectDS = objectDS;
	}

	public static String getPerformDS() {
		return performDS;
	}

	public static void setPerformDS(String performDS) {
		ReadFromDatasheet.performDS = performDS;
	}

	public static String getValuesDS() {
		return valuesDS;
	}

	public static void setValuesDS(String valuesDS) {
		ReadFromDatasheet.valuesDS = valuesDS;
	}

	public static ArrayList<String> getDatasheetListValue() {
		return datasheetListValue;
	}

	public static void setDatasheetListValue(ArrayList<String> datasheetListValue) {
		ReadFromDatasheet.datasheetListValue = datasheetListValue;
	}

}
