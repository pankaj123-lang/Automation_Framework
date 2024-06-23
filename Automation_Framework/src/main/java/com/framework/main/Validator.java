package com.framework.main;

import java.time.Duration;
import java.util.Calendar;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mobile.framework.RunMobAutomation;
import com.web.framework.Run_Automation;

public class Validator extends ReadFromDatasheet {

	private static WebElement webElement;
	private static int extentStatus;

	public static void validateObjects(HashMap<String, String> newHashMap) {
//		System.out.println("Serial no			-------->" + newHashMap.get("srNO_DS"));
//		System.out.println("Application Name		-------->" + newHashMap.get("application_Name_DS"));
//		System.out.println("Page Name			-------->" + newHashMap.get("pageNameDS"));
//		System.out.println("Scenario Id			-------->" + newHashMap.get("scenario_IdDS"));
//		System.out.println("Scenario Description		-------->" + newHashMap.get("scenario_DescriptionDS"));
//		System.out.println("Testcase Id			-------->" + newHashMap.get("testCase_IdDS"));
//		System.out.println("Testcase Description		-------->" + newHashMap.get("testCase_DescriptionDS"));
//		System.out.println("Testcase Type			-------->" + newHashMap.get("testCase_typeDS"));
//		System.out.println("Run Status			-------->" + newHashMap.get("runStatusDS"));
//		System.out.println("Control				-------->" + newHashMap.get("controlDS"));
//		System.out.println("Object Type			-------->" + newHashMap.get("objectTypeDS"));
//		System.out.println("Object				-------->" + newHashMap.get("objectDS"));
//		System.out.println("Performs			-------->" + newHashMap.get("performDS"));
//		System.out.println("Value				-------->" + newHashMap.get("valuesDS"));
//		System.out.println("Options				-------->" + newHashMap.get("optionsDS"));

//		System.out.println("Step no:- " + newHashMap.get("srNO_DS") + " Application Name is:- "
//				+ newHashMap.get("application_Name_DS") + " Page Name:- " + newHashMap.get("pageNameDS"));
		extentStatus = 1;
		if (newHashMap.get("performDS").equalsIgnoreCase("startbrowser")) {
			ActionMethods.startBrowser();
		} else if (newHashMap.get("performDS").equalsIgnoreCase("LaunchApp")) {
			ActionMethods.launchApp();
		} else {
			webElement = checkElementPresent(newHashMap);
		}

		ActionClass.takeAction(newHashMap, webElement);
	}

	public static String checkElementVisible(WebElement webElement2) {
		String webElementValue = null;
		try {
			if (webElement2 != null) {
				boolean visible = webElement2.isDisplayed();
				if(!visible) {
					if(newHashMap.get("optionsDS").equalsIgnoreCase("waituntilvisible")) {
						ActionMethods.waitUntilCondition("visibilityOfElement(30)",newHashMap.get("objectDS"));
						}
				}
				if (webElement2.isDisplayed()) {
					webElementValue = "VISIBLE";
					System.out.println("Element visible " + webElement2.isDisplayed());
				} else {
					webElementValue = "NOT VISIBLE";
					System.out.println("Element Visible " + webElement2.isDisplayed());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return webElementValue;
	}
	public static String checkElementDisable(WebElement webElement2) {
		String webElementValue = null;
		try {
			if (webElement2 != null) {
				boolean enable = webElement2.isEnabled();
				if(!enable) {
					if(newHashMap.get("optionsDS").equalsIgnoreCase("waituntilvisible")) {
						ActionMethods.waitUntilCondition("visibilityOfElement(30)",newHashMap.get("objectDS"));
						}
				}
				if (webElement2.isEnabled()) {
					webElementValue = "Enabled";
					System.out.println("Element enabled " + webElement2.isEnabled());
				} else {
					webElementValue = "Not Enable";
					System.out.println("Element enabled: " + webElement2.isEnabled());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return webElementValue;
	}
	private static WebElement checkElementPresent(HashMap<String, String> newHashMap) {
		WebElement webElementValue = null;
		WebDriverWait wait;
		if (ReadDataFromConfigFile.getAutomationType().equalsIgnoreCase("web")) {
			System.out.println("Maximum Waiting time is = " + Run_Automation.maxExplicitWait_Time);
			wait = new WebDriverWait(Run_Automation.driver, Duration.ofSeconds(Run_Automation.maxExplicitWait_Time));
		} else {
			System.out.println("Maximum Waiting time is = " + RunMobAutomation.getMaxExplicitWait_Time());
			wait = new WebDriverWait(RunMobAutomation.getDriver(),
					Duration.ofSeconds(RunMobAutomation.getMaxExplicitWait_Time()));
		}
		String objectType = newHashMap.get("objectTypeDS");
		String objectValue = newHashMap.get("objectDS");
		if (ReadDataFromConfigFile.getAutomationType().equalsIgnoreCase("web")) {
			try {
				objectType = objectType.toUpperCase().trim();
				if (objectType.equalsIgnoreCase(null) && objectType.equalsIgnoreCase("")) {
					System.out.println("Object Type field is Blank");
				} else {
					switch (objectType) {
					case "ID":
						System.out.println(objectValue);
						wait.until(ExpectedConditions.presenceOfElementLocated(By.id(objectValue)));
						webElementValue = Run_Automation.driver.findElement(By.id(objectValue));
						break;
					case "XPATH":
						System.out.println(objectValue);
						wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objectValue)));
						webElementValue = Run_Automation.driver.findElement(By.xpath(objectValue));
						break;
					case "CLASSNAME":
						System.out.println(objectValue);
						wait.until(ExpectedConditions.presenceOfElementLocated(By.className(objectValue)));
						webElementValue = Run_Automation.driver.findElement(By.className(objectValue));
						break;
					case "NAME":
						System.out.println(objectValue);
						wait.until(ExpectedConditions.presenceOfElementLocated(By.name(objectValue)));
						webElementValue = Run_Automation.driver.findElement(By.name(objectValue));
						break;
					case "CSSSELECTOR":
						System.out.println(objectValue);
						wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(objectValue)));
						webElementValue = Run_Automation.driver.findElement(By.cssSelector(objectValue));
						break;
					case "LINKTEXT":
						System.out.println(objectValue);
						wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(objectValue)));
						webElementValue = Run_Automation.driver.findElement(By.linkText(objectValue));
						break;
					case "PARTIALLINKTEXT":
						System.out.println(objectValue);
						wait.until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(objectValue)));
						webElementValue = Run_Automation.driver.findElement(By.partialLinkText(objectValue));
						break;
					case "TAGNAME":
						System.out.println(objectValue);
						wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName(objectValue)));
						webElementValue = Run_Automation.driver.findElement(By.tagName(objectValue));
						break;
					case "FRAMEINDEX":
						int index = Integer.parseInt(objectValue);
						Run_Automation.driver.switchTo().frame(index);
						break;
					case "FRAMENAME":
						Run_Automation.driver.switchTo()
								.frame((Run_Automation.driver.findElement(By.name(objectValue))));
						System.out.println("switch to..............................." + objectValue);
						break;
					case "FRAMEID":
						Run_Automation.driver.switchTo().frame((Run_Automation.driver.findElement(By.id(objectValue))));
						System.out.println("switch to..............................." + objectValue);
						break;
					case "FRAMEXPATH":
						Run_Automation.driver.switchTo()
								.frame((Run_Automation.driver.findElement(By.xpath(objectValue))));
						System.out.println("switch to..............................." + objectValue);
						break;
					default:
						webElementValue = null;
						break;

					}
				}

			} catch (Exception e) {
				System.out.println("Element not present on the screen");
//				e.printStackTrace();
				Screenshot.TakeScrennshot();
				SaveResult.Reports("Element not present on the screen/Locator not found", newHashMap.get("valuesDS"));
			}
		} else {

			try {
				objectType = objectType.toUpperCase().trim();
				if (objectType.equalsIgnoreCase(null) && objectType.equalsIgnoreCase("")) {
					System.out.println("Object Type field is Blank");
				} else {
					switch (objectType) {
					case "ID":
						System.out.println(objectValue);
						wait.until(ExpectedConditions.presenceOfElementLocated(By.id(objectValue)));
						webElementValue = RunMobAutomation.getDriver().findElement(By.id(objectValue));
						break;
					case "XPATH":
						System.out.println(objectValue);
						wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objectValue)));
						webElementValue = RunMobAutomation.getDriver().findElement(By.xpath(objectValue));
						break;
					case "CLASSNAME":
						System.out.println(objectValue);
						wait.until(ExpectedConditions.presenceOfElementLocated(By.className(objectValue)));
						webElementValue = RunMobAutomation.getDriver().findElement(By.className(objectValue));
						break;
					case "NAME":
						System.out.println(objectValue);
						wait.until(ExpectedConditions.presenceOfElementLocated(By.name(objectValue)));
						webElementValue = RunMobAutomation.getDriver().findElement(By.name(objectValue));
						break;
					case "CSSSELECTOR":
						System.out.println(objectValue);
						wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(objectValue)));
						webElementValue = RunMobAutomation.getDriver().findElement(By.cssSelector(objectValue));
						break;
					case "LINKTEXT":
						System.out.println(objectValue);
						wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(objectValue)));
						webElementValue = RunMobAutomation.getDriver().findElement(By.linkText(objectValue));
						break;
					case "PARTIALLINKTEXT":
						System.out.println(objectValue);
						wait.until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(objectValue)));
						webElementValue = RunMobAutomation.getDriver().findElement(By.partialLinkText(objectValue));
						break;
					case "TAGNAME":
						System.out.println(objectValue);
						wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName(objectValue)));
						webElementValue = RunMobAutomation.getDriver().findElement(By.tagName(objectValue));
						break;

					default:
						webElementValue = null;
						break;

					}
				}

			} catch (Exception e) {
				System.out.println("Element not present on the screen");
//				e.printStackTrace();
				Screenshot.TakeScrennshot();
				SaveResult.Reports("Element not present on the screen/Locator not found", newHashMap.get("valuesDS"));
			}

		}
		return webElementValue;

	}

	public static double getCurrentTime() {
		Calendar lCDateTime = Calendar.getInstance();
		double startTime = lCDateTime.getTimeInMillis();
		return startTime;
	}

	public static WebElement getWebElement() {
		return webElement;
	}

	public static void setWebElement(WebElement webElement) {
		Validator.webElement = webElement;
	}

	public static int getExtentStatus() {
		return extentStatus;
	}

	public static void setExtentStatus(int extentStatus) {
		Validator.extentStatus = extentStatus;
	}

	public static void main(String[] args) {

	}

	
}
