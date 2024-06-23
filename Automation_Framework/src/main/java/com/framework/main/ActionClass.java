
package com.framework.main;

import java.util.HashMap;

import org.openqa.selenium.WebElement;

import com.web.framework.Run_Automation;

public class ActionClass extends ReadFromDatasheet {

	protected static WebElement webElement;

	public static void main(String[] args) {

	}

	public static void takeAction(HashMap<String, String> newHashMap, WebElement element) {

		System.out.println("Step no:- " + newHashMap.get("srNO_DS") + " Application Name is:- "
				+ newHashMap.get("application_Name_DS") + " Page Name:- " + newHashMap.get("pageNameDS"));
		System.out.println("Serial no			-------->" + newHashMap.get("srNO_DS"));
		System.out.println("Application Name		-------->" + newHashMap.get("application_Name_DS"));
		System.out.println("Page Name			-------->" + newHashMap.get("pageNameDS"));
		System.out.println("Scenario Id			-------->" + newHashMap.get("scenario_IdDS"));
		System.out.println("Scenario Description		-------->" + newHashMap.get("scenario_DescriptionDS"));
		System.out.println("Testcase Id			-------->" + newHashMap.get("testCase_IdDS"));
		System.out.println("Testcase Description		-------->" + newHashMap.get("testCase_DescriptionDS"));
		System.out.println("Testcase Type			-------->" + newHashMap.get("testCase_typeDS"));
		System.out.println("Run Status			-------->" + newHashMap.get("runStatusDS"));
		System.out.println("Control				-------->" + newHashMap.get("controlDS"));
		System.out.println("Object Type			-------->" + newHashMap.get("objectTypeDS"));
		System.out.println("Object				-------->" + newHashMap.get("objectDS"));
		System.out.println("Performs			-------->" + newHashMap.get("performDS"));
		System.out.println("Value				-------->" + newHashMap.get("valuesDS"));
		System.out.println("Options				-------->" + newHashMap.get("optionsDS"));
		String actionValue = newHashMap.get("performDS").toUpperCase();
		String controlValue = newHashMap.get("controlDS");
		webElement = element;

		System.out.println("Performs ===== " + actionValue);
		System.out.println("WebElement=== " + element);
		System.out.println("Values1===" + newHashMap.get("valuesDS"));
		if (webElement != null) {
			if (controlValue.equalsIgnoreCase("T")) {
				Run_Automation.startTime = Validator.getCurrentTime();
			}
			switch (actionValue) {
			case "EXPLICITWAIT":
				ActionMethods.explicitWait();
				break;
			case "FLUENTWAIT":
				ActionMethods.fluentWait();
				break;
			case "SENDKEYS":
				ActionMethods.sendKeys();
				break;
			case "CLICK":
				ActionMethods.clickElement();
				break;
			case "JAVASCRIPTCLICK":
				ActionMethods.javascriptClick();
				break;
			case "ACTIONCLICK":
				ActionMethods.actionClick();
				break;
			case "DOUBLECLICK":
				ActionMethods.doubleClickElement();
				break;
			case "RIGHTCLICK":
				ActionMethods.rightClick();
				break;
			case "CLEAR":
				ActionMethods.clearEditBox();
				break;
			case "CLEAR_USING_ACTION":
				ActionMethods.clearUsingAction();
				break;
			case "MOUSEHOVER":
				ActionMethods.mouseHover();
				break;
			case "GETTEXT":
				ActionMethods.getTextValue();
				break;
			case "CHECK_ENABILITY":
				ActionMethods.checkEnable();
				break;
			case "CHECK_DISABILITY":
				ActionMethods.checkDisable();
				break;
			case "CHECK_VISIBILITY":
				ActionMethods.checkVisible();
				break;
			case "CHECK_INVISIBILITY":
				ActionMethods.checkInvisible();
				break;
			case "SELECT_VISIBLE_TEXT":
				ActionMethods.selectByVisibleText();
				break;
			case "SELECTVALUE":
				ActionMethods.selectByValue();
				break;
			case "SELECTINEDX":
				ActionMethods.selectByIndex();
				break;
			case "DATEPICKER":
				ActionMethods.datePick();
				break;
			case "SCROLLTOELEMENT":
				ActionMethods.scrollToElement();
				break;
			case "MOVETOELEMENT":
				ActionMethods.moveToElement();
				break;
			case "INPUTFIELD":
				ActionMethods.openInputField();
				break;
			case "OTP1":
				ActionMethods.ManualOtp1();
				break;
			case "OTP2":
				ActionMethods.ManualOtp2();
				break;
			case "OTP3":
				ActionMethods.ManualOtp3();
				break;
			case "OTP4":
				ActionMethods.ManualOtp4();
				break;
			case "OTP5":
				ActionMethods.ManualOtp5();
				break;
			case "OTP6":
				ActionMethods.ManualOtp6();
				break;
			case "CAPTCHA":
				ActionMethods.ReadCaptcha();
				break;
			case "SWIPE_TO_ELEMENT_MOBILE":
				ActionMethods.swipeToElementMobile();
				break;

			}
			if (actionValue.contains("MOVETOSTEP(")) {
				ActionMethods.moveToStep();
			}

		}
		// For Webelemnt is null
		else {
			if (controlValue.equalsIgnoreCase("T")) {
				Run_Automation.startTime = Validator.getCurrentTime();
			}
			switch (actionValue) {
			case "BROWSEURL":
				ActionMethods.browseUrl();
				break;
			case "ACCEPTALERT":
				ActionMethods.acceptAlert();
				break;
			case "ROBOTCLICK":
				ActionMethods.robotClick();
				break;
			case "SIKULICLICK":
				ActionMethods.sikuliClick();
				break;
			case "SIKULISENDKEYS":
				ActionMethods.sikuliSendkeys();
				break;
			case "SIKULISCROLLDOWN":
				ActionMethods.sikuliScrollDown();
				break;
			case "SIKULISCROLLRIGHT":
				ActionMethods.sikuliScrollRight();
				break;
			case "SIKULICHECKVISIBILITY":
				ActionMethods.sikuliCheckVisibility();
				break;
			case "TAB":
				ActionMethods.tabButton();
				break;
			case "ENTER":
				ActionMethods.enterButton();
				break;
			case "BACKSPACE":
				ActionMethods.backButton();
				break;
			case "ESC":
				ActionMethods.escButton();
				break;
			case "PAGEDOWN":
				ActionMethods.pagedownButton();
				break;
			case "PADEUP":
				ActionMethods.pageupButton();
				break;
			case "ZOOMOUT":
				ActionMethods.zoomOut();
				break;
			case "ZOOMIN":
				ActionMethods.zoomIn();
				break;
			case "REFRESH":
				ActionMethods.refresh();
				break;
			case "CLOSEWINDOW":
				ActionMethods.closeWindow();
				break;
			case "SWITCH_WINDOW":
				ActionMethods.switchWindow();
				break;
			case "SET_PARENTWINDOW":
				ActionMethods.setParentWindow();
				break;
			case "SWITCH_PARENTWINDOW":
				ActionMethods.switchParentWindow();
				break;
			case "ALERTDISMISS":
				ActionMethods.dismissAlert();
				break;
			case "CHECKFILEDOWNLOADED":
				ActionMethods.checkFileDownloaded(newHashMap.get("Options"));
				break;
			case "SINGLE_MANUAL_OTP":
				ActionMethods.singleManualOtpField();
				break;
			case "HIDE_KEYBOARD":
				ActionMethods.hideKeyboard();
				break;
			case "ROBOT_SENDKEYS":
				ActionMethods.robotSendkeys();
				break;
			case "PRESS_KEYCODE":
				ActionMethods.pressKeycode();
				break;
			case "COORDINATE_SWIPE":
				ActionMethods.coordinateSwipe();
				break;
			
			}
			if (actionValue.contains("SLEEP(")) {
				ActionMethods.sleep();
			}

		}

	}

	public static WebElement getWebElement() {
		return webElement;
	}

	public static void setWebElement(WebElement webElement) {
		ActionClass.webElement = webElement;
	}

}
