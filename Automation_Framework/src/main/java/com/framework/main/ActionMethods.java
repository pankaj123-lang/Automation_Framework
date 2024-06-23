package com.framework.main;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;

import com.codoid.products.exception.FilloException;
import com.mobile.framework.RunMobAutomation;
import com.web.framework.BrowserConfiguration;
import com.web.framework.Run_Automation;

import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.PressesKey;
import io.github.bonigarcia.wdm.WebDriverManager;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class ActionMethods extends ActionClass {
	private static String parenwindow;

	private static char manualOtp1;
	private static char manualOtp2;
	private static char manualOtp3;
	private static char manualOtp4;
	private static char manualOtp5;
	private static char manualOtp6;

	public static void main(String[] args) {

	}

	protected static <F> void fluentWait() {
		String condition = ReadFromDatasheet.newHashMap.get("valuesDS");
		long waitval = 0;
		if (!condition.equalsIgnoreCase("")) {
			String value = condition.split("\\(")[1].split("\\)")[0];
			waitval = Long.valueOf(value);
		} else {
			waitval = 60;
		}
		if (ReadDataFromConfigFile.getAutomationType().equalsIgnoreCase("web")) {
			Wait<WebDriver> wait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(waitval))
					.pollingEvery(Duration.ofMillis(5)).ignoring(Exception.class);
			wait.until(ExpectedConditions.visibilityOf(webElement));
		} else {
			Wait<WebDriver> wait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(waitval))
					.pollingEvery(Duration.ofMillis(5)).ignoring(Exception.class);
			wait.until(ExpectedConditions.visibilityOf(webElement));
		}
	}

	protected static void explicitWait() {
		String condition = ReadFromDatasheet.newHashMap.get("valuesDS");
		long waitval = 0;
		if (!condition.equalsIgnoreCase("")) {
//			String value = condition.split("\\(")[1].split("\\)")[0];
			waitval = Long.valueOf(condition);
		} else {
			waitval = 60;
		}

		WebDriverWait wait;
		if (ReadDataFromConfigFile.getAutomationType().equalsIgnoreCase("web")) {
			wait = new WebDriverWait(driver, Duration.ofSeconds(waitval));
		} else {
			wait = new WebDriverWait(RunMobAutomation.getDriver(), Duration.ofSeconds(waitval));
		}
		wait.until(ExpectedConditions.visibilityOf(webElement));
	}

	protected static void waitUntilCondition(String condition, String webElement) {
		try {

			String value = condition.split("\\(")[1].split("\\)")[0];
			long waitval = Long.valueOf(value);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitval));
			By loc = null;
			String locatorType = ReadFromDatasheet.newHashMap.get("objectTypeDS").toUpperCase().trim();
			switch (locatorType) {
			case "ID":
				loc = By.id(webElement);
				break;
			case "NAME":
				loc = By.name(webElement);
				break;
			case "XPATH":
				loc = By.xpath(webElement);
				break;
			case "LINKTEXT":
				loc = By.linkText(webElement);
				break;
			case "TAGNAME":
				loc = By.tagName(webElement);
				break;
			case "CLASSNAME":
				loc = By.className(webElement);
				break;
			default:
				loc = null;
			}
			System.out.println("Locator is -----> " + loc + "  :" + webElement + ":");
			if (loc != null) {
				if (condition.contains("invisibilityOfElement")) {
					System.out.println("waiting for invisibilityOfElement ------->" + (new Date()).toString());
					wait.until(ExpectedConditions.invisibilityOfElementLocated(loc));
					System.out.println("waiting for invisibilityOfElement ------->" + (new Date()).toString());
				} else if (condition.contains("visibilityOfElement")) {
					System.out.println("waiting for visibilityOfElement ------->" + (new Date()).toString());
					wait.until(ExpectedConditions.visibilityOfElementLocated(loc));
					System.out.println("waiting for visibilityOfElement ------->" + (new Date()).toString());
				} else if (condition.contains("presenceOfElement")) {
					System.out.println("waiting for Presence ------->" + (new Date()).toString());
					wait.until(ExpectedConditions.presenceOfElementLocated(loc));
					System.out.println("waiting End for Presence ------->" + (new Date()).toString());
				} else if (condition.contains("elementToBeClickable")) {
					System.out.println("waiting for elementToBeClickable ------->" + (new Date()).toString());
					wait.until(ExpectedConditions.elementToBeClickable(loc));
					System.out.println("waiting for elementToBeClickable ------->" + (new Date()).toString());
				}

			} else if (condition.contains("alertIsPresent")) {
				wait.until(ExpectedConditions.alertIsPresent());
			}

		} catch (Exception e) {
			System.out.println("In catch block of wait until condition");
		}
	}

	public static void startBrowser() {
		if (browser.equalsIgnoreCase("chrome")) {
			if (BrowserConfiguration.isBrowserConfig() == false) {
				BrowserConfiguration.configureBrowser();
			}
			try {
				driver = new ChromeDriver(BrowserConfiguration.getOptions());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			driver.manage().window().maximize();
		} else if (browser.equalsIgnoreCase("firefox")) {
			if (BrowserConfiguration.isBrowserConfig() == false) {
				BrowserConfiguration.configureBrowser();
			}
			driver = new FirefoxDriver();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			driver.manage().window().maximize();
		} else if (browser.equalsIgnoreCase("edge")) {
			if (BrowserConfiguration.isBrowserConfig() == false) {
				BrowserConfiguration.configureBrowser();
			}
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			driver.manage().window().maximize();
		} else {
			System.out.println("Browser not found");
		}
	}

	protected static void sleep() {
		try {
			System.out.println("Waiting ...........................");
			String value = ReadFromDatasheet.getPerformDS().split("\\(")[1].split("\\)")[0];
			long waitval = Long.valueOf(value);
			Thread.sleep(waitval);
		} catch (Exception e) {
			Thread.currentThread().interrupt();

		}

	}

	protected static void browseUrl() {
		if (ReadFromDatasheet.newHashMap.get("valuesDS").equalsIgnoreCase("T")) {
			Run_Automation.startTime = Validator.getCurrentTime();
		}
		Run_Automation.driver.get(ReadFromDatasheet.newHashMap.get("valuesDS"));

	}

	protected static void datePick() {

	}

	protected static void acceptAlert() {
		try {
			String msg = "";
			Thread.sleep(2000);
			Alert alert = Run_Automation.driver.switchTo().alert();
			msg = alert.getText();
			System.out.println("alert is" + msg);
			if (!msg.equals("")) {
				alert.accept();
			} else {
				System.out.println("alert is not present");
			}
		} catch (Exception e) {
			Thread.currentThread().interrupt();

		}
	}

	protected static void dismissAlert() {
		Alert alert = Run_Automation.driver.switchTo().alert();
		alert.dismiss();
	}

	protected static void selectByIndex() {
		Select select = new Select(webElement);
		select.selectByIndex(Integer.parseInt(ReadFromDatasheet.newHashMap.get("valuesDS")));
	}

	protected static void selectByValue() {
		Select select = new Select(webElement);
		select.selectByValue(ReadFromDatasheet.newHashMap.get("valuesDS"));
	}

	protected static void selectByVisibleText() {
		Select select = new Select(webElement);
		select.selectByVisibleText(ReadFromDatasheet.newHashMap.get("valuesDS"));

	}

	protected static void checkInvisible() {
		String actualResult = Validator.checkElementVisible(webElement);
		System.out.println("Element invisible = " + actualResult);
		Screenshot.TakeScrennshot();
		String expected = ReadFromDatasheet.newHashMap.get("valuesDS");
		if (ReadFromDatasheet.getControlDS().equalsIgnoreCase("I")) {

			SaveResult.Reports(actualResult, expected);
		}
	}

	protected static void checkVisible() {
		String webElementVisible = Validator.checkElementVisible(webElement);
		System.out.println("Element visible = " + webElementVisible);
		if (ReadFromDatasheet.newHashMap.get("controlDS").equalsIgnoreCase("V")) {
			Screenshot.TakeScrennshot();
		}
		String expected = ReadFromDatasheet.newHashMap.get("valuesDS");
		if (ReadFromDatasheet.newHashMap.get("controlDS").equalsIgnoreCase("V")) {
			SaveResult.Reports(webElementVisible, expected);
		}
	}

	protected static void checkDisable() {
		String webElementDisable = Validator.checkElementDisable(webElement);
		System.out.println("Element Disable = " + webElementDisable);
		Screenshot.TakeScrennshot();
		String expected = ReadFromDatasheet.newHashMap.get("valuesDS");
		if (ReadFromDatasheet.newHashMap.get("controlDS").equalsIgnoreCase("V")) {
			SaveResult.Reports(webElementDisable, expected);
		}
	}

	protected static void checkEnable() {
		String webElementDisable = Validator.checkElementDisable(webElement);
		System.out.println("Element Disable = " + webElementDisable);
		Screenshot.TakeScrennshot();
		String expected = ReadFromDatasheet.newHashMap.get("valuesDS");
		if (ReadFromDatasheet.newHashMap.get("controlDS").equalsIgnoreCase("V")) {
			SaveResult.Reports(webElementDisable, expected);
		}
	}

	protected static void getTextValue() {
		String actualValue = webElement.getText();
		if (actualValue.equalsIgnoreCase("")) {
			actualValue = webElement.getAttribute("value");
		}
		System.out.println("Value is = " + actualValue);
		String expected = ReadFromDatasheet.newHashMap.get("valuesDS");
		Screenshot.TakeScrennshot();
		if (ReadFromDatasheet.newHashMap.get("controlDS").equalsIgnoreCase("V")) {
			System.out.println("Inside save result");
			SaveResult.Reports(actualValue, expected);
		}
	}

	protected static void clearUsingAction() {
		Actions action;
		if (ReadDataFromConfigFile.getAutomationType().equalsIgnoreCase("web")) {
			action = new Actions(Run_Automation.driver);
		} else {
			action = new Actions(RunMobAutomation.getDriver());
		}
		action.moveToElement(webElement).keyDown(Keys.CONTROL).sendKeys("a", Keys.BACK_SPACE).keyUp(Keys.CONTROL)
				.build().perform();
	}

	protected static void mouseHover() {
		Actions action;
		if (ReadDataFromConfigFile.getAutomationType().equalsIgnoreCase("web")) {
			action = new Actions(Run_Automation.driver);
		} else {
			action = new Actions(RunMobAutomation.getDriver());
		}
		action.moveToElement(webElement).build().perform();
	}

	protected static void clearEditBox() {
		webElement.clear();
	}

	protected static void rightClick() {
		Actions action;
		if (ReadDataFromConfigFile.getAutomationType().equalsIgnoreCase("web")) {
			action = new Actions(Run_Automation.driver);
		} else {
			action = new Actions(RunMobAutomation.getDriver());
		}
		action.contextClick(webElement).build().perform();
	}

	protected static void doubleClickElement() {
		Actions action;
		if (ReadDataFromConfigFile.getAutomationType().equalsIgnoreCase("web")) {
			action = new Actions(Run_Automation.driver);
		} else {
			action = new Actions(RunMobAutomation.getDriver());
		}
		action.doubleClick(webElement).build().perform();
	}

	protected static void clickElement() {
		webElement.click();

	}

	protected static void sendKeys() {
		webElement.clear();
		webElement.sendKeys(newHashMap.get("valuesDS").trim());

	}

	protected static void launchApp() {
		if (ReadFromDatasheet.getControlDS().equalsIgnoreCase("T")) {
			Run_Automation.startTime = Validator.getCurrentTime();
		}
	}

	protected static void checkFileDownloaded(String option) {

		try {
			String expectedResult = "", actualResult = "";

			File[] listFiles = new File(ReadFromDatasheet.getValuesDS()).listFiles();

			for (int i = 0; i < listFiles.length; i++) {

				if (listFiles[i].isFile()) {
					String fileName = listFiles[i].getName();
					if (option.equalsIgnoreCase("pdf")) {
						if (fileName.startsWith("") && fileName.endsWith(".pdf")) {
							System.out.println("found file" + " " + fileName);
							actualResult = "File Found";
							expectedResult = "File Found";
							boolean success = listFiles[i].delete();
							if (success) {
								System.out.println("File Deleted Successfully");
							}
							break;
						}
					} else if (option.equalsIgnoreCase("csv") || option.equalsIgnoreCase("xlsx")
							|| option.equalsIgnoreCase("docs")) {
						if (fileName.startsWith("") && (fileName.endsWith(".csv") || fileName.endsWith(".xlsx")
								|| fileName.endsWith(".docs"))) {
							System.out.println("found file" + " " + fileName);
							actualResult = "File Found";
							expectedResult = "File Found";
							boolean success = listFiles[i].delete();
							if (success) {
								System.out.println("File Deleted Successfully");
							}
							break;
						}
					} else if (option.equalsIgnoreCase("image")) {
						if (fileName.startsWith("") && (fileName.endsWith(".png") || fileName.endsWith(".jpeg")
								|| fileName.endsWith(".jpg"))) {
							System.out.println("found file" + " " + fileName);
							actualResult = "File Found";
							expectedResult = "File Found";
							boolean success = listFiles[i].delete();
							if (success) {
								System.out.println("File Deleted Successfully");
							}
							break;
						}
					} else {
						System.out.println("not found");
						actualResult = "File Found";
						expectedResult = "File Not Found";
					}

				}
			}
			SaveResult.Reports(actualResult, expectedResult);
		} catch (Exception e) {
			SaveResult.Reports("Error in finding downloaded file", "File Found");
		}
	}

	protected static void robotClick() {
		try {
			Robot rb = new Robot();
			int x = Integer.parseInt(newHashMap.get("objectDS").split(" ")[0]);
			int y = Integer.parseInt(newHashMap.get("objectDS").split(" ")[1]);
			rb.mouseMove(x, y);
			rb.keyPress(InputEvent.BUTTON1_DOWN_MASK);
			rb.keyRelease(InputEvent.BUTTON1_DOWN_MASK);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	protected static void actionClick() {
		Actions act = new Actions(driver);
		act.click(webElement).build().perform();
	}

	protected static void sikuliClick() {
		try {
			Screen sc = new Screen();
			sc.wait(newHashMap.get("objectDS"), 30.0);
			sc.exists(newHashMap.get("objectDS")).click();
		} catch (FindFailed e) {
			e.printStackTrace();
		}
	}

	protected static void sikuliSendkeys() {
		try {
			Screen sc = new Screen();
			sc.wait(newHashMap.get("objectDS"), 60.0);
			sc.exists(newHashMap.get("objectDS")).click();
			sc.type(newHashMap.get("valuesDS").trim());
		} catch (FindFailed e) {
			e.printStackTrace();
		}
	}

	protected static void sikuliScrollDown() {
		Screen sc = new Screen();
		sc.wheel(1, 10);
	}

	protected static void sikuliScrollRight() {
		try {
			int val = Integer.parseInt(newHashMap.get("Options"));
			Screen sc = new Screen();
			Thread.sleep(1000L);
			Robot r = new Robot();
			r.keyPress(KeyEvent.VK_SHIFT);
			sc.wheel(1, val);
			r.keyRelease(KeyEvent.VK_SHIFT);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	protected static void sikuliCheckVisibility() {

	}

	protected static void javascriptClick() {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", webElement);
	}

	protected static void scrollToElement() {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].scrollIntoView();", webElement);
	}

	protected static void moveToElement() {
		Actions act = new Actions(driver);
		act.moveToElement(webElement).build().perform();

	}

	protected static void tabButton() {
		try {
			Robot rb = new Robot();
			String val = newHashMap.get("optionsDS");
			if (!val.equalsIgnoreCase("")) {
				int count = Integer.parseInt(val);
				while (count != 0) {
					rb.keyPress(KeyEvent.VK_TAB);
					rb.keyRelease(KeyEvent.VK_TAB);
					Thread.sleep(500);
					count--;
				}
			} else {
				rb.keyPress(KeyEvent.VK_TAB);
				rb.keyRelease(KeyEvent.VK_TAB);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (AWTException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected static void enterButton() {
		try {
			Robot rb = new Robot();
			String val = newHashMap.get("optionsDS");
			System.out.println(val);
			if (!val.equalsIgnoreCase("")) {
				int count = Integer.parseInt(val);
				while (count != 0) {
					rb.keyPress(KeyEvent.VK_ENTER);
					rb.keyRelease(KeyEvent.VK_ENTER);
					Thread.sleep(500);
					count--;
				}
			} else {
				rb.keyPress(KeyEvent.VK_ENTER);
				rb.keyRelease(KeyEvent.VK_ENTER);
			}
		} catch (AWTException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	protected static void backButton() {

		try {
			Robot rb = new Robot();
			String val = newHashMap.get("Options");
			if (!val.equalsIgnoreCase("")) {
				int count = Integer.parseInt(val);
				while (count != 0) {
					rb.keyPress(KeyEvent.VK_BACK_SPACE);
					rb.keyRelease(KeyEvent.VK_BACK_SPACE);
					Thread.sleep(500);
					count--;
				}
			} else {
				rb.keyPress(KeyEvent.VK_BACK_SPACE);
				rb.keyRelease(KeyEvent.VK_BACK_SPACE);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (AWTException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected static void escButton() {
		try {
			Robot rb = new Robot();
			rb.keyPress(KeyEvent.VK_ESCAPE);
			rb.keyRelease(KeyEvent.VK_ESCAPE);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	protected static void pagedownButton() {
		try {
			Robot rb = new Robot();
			String val = newHashMap.get("Options");
			if (!val.equalsIgnoreCase("")) {
				int count = Integer.parseInt(val);
				while (count != 0) {
					rb.keyPress(KeyEvent.VK_PAGE_DOWN);
					rb.keyRelease(KeyEvent.VK_PAGE_DOWN);
					Thread.sleep(500);
					count--;
				}
			} else {
				rb.keyPress(KeyEvent.VK_PAGE_DOWN);
				rb.keyRelease(KeyEvent.VK_PAGE_DOWN);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (AWTException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	protected static void pageupButton() {
		try {
			Robot rb = new Robot();
			String val = newHashMap.get("Options");
			if (!val.equalsIgnoreCase("")) {
				int count = Integer.parseInt(val);
				while (count != 0) {
					rb.keyPress(KeyEvent.VK_PAGE_UP);
					rb.keyRelease(KeyEvent.VK_PAGE_UP);
					Thread.sleep(500);
					count--;
				}
			} else {
				rb.keyPress(KeyEvent.VK_PAGE_UP);
				rb.keyRelease(KeyEvent.VK_PAGE_UP);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (AWTException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected static void zoomOut() {
		try {
			Robot rb1 = new Robot();
			rb1.keyPress(KeyEvent.VK_CONTROL);
			rb1.keyPress(KeyEvent.VK_MINUS);
			rb1.keyRelease(KeyEvent.VK_CONTROL);
			rb1.keyRelease(KeyEvent.VK_MINUS);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	protected static void zoomIn() {
		try {
			Robot rb1 = new Robot();
			rb1.keyPress(KeyEvent.VK_CONTROL);
			rb1.keyPress(KeyEvent.VK_PLUS);
			rb1.keyRelease(KeyEvent.VK_CONTROL);
			rb1.keyRelease(KeyEvent.VK_PLUS);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	protected static void refresh() {
		try {
			driver.navigate().refresh();
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected static void closeWindow() {
		driver.close();
	}

	protected static void switchWindow() {
		try {
			String urlwin = ActionMethods.parenwindow;
			Set<String> chilwin = driver.getWindowHandles();
			System.out.println("Total Window " + chilwin.size());
			System.out.println("Parent window is " + urlwin);
			for (String currentwin : chilwin) {
				if (!currentwin.equalsIgnoreCase(urlwin)) {
					System.out.println(urlwin + "\n" + currentwin);
					driver.switchTo().window(currentwin);
					Thread.sleep(3000);
					System.out.println("Current window Title is ---->  " + driver.getTitle());
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected static void setParentWindow() {
		ActionMethods.parenwindow = driver.getWindowHandle();
	}

	protected static void switchParentWindow() {
		driver.switchTo().window(ActionMethods.parenwindow);
		ActionMethods.parenwindow = driver.getWindowHandle();
	}

	protected static void moveToStep() {

		try {
			System.out.println(".....................Move to step ...........................");
			String value = ReadFromDatasheet.getPerformDS().split("\\(")[1].split("\\)")[0];

			if (ReadFromDatasheet.getOptionsDS().equalsIgnoreCase("stepno")) {
				ReadFromDatasheet.setSrNO_DS(Run_Automation.rsDatasheet.getField("SrNo"));
				try {
					WebDriverWait wait = new WebDriverWait(Run_Automation.driver, Duration.ofSeconds(10));
					WebElement xpath = (WebElement) wait.until(
							ExpectedConditions.visibilityOfElementLocated(By.xpath(ReadFromDatasheet.getObjectDS())));
					if (xpath.isDisplayed()) {

					} else {
						while (ReadFromDatasheet.getSrNO_DS().equals(value)) {
							Run_Automation.rsDatasheet.moveNext();
							ReadFromDatasheet.setSrNO_DS(Run_Automation.rsDatasheet.getField("SrNo"));
						}
					}
				} catch (Exception e) {
					while (ReadFromDatasheet.getSrNO_DS().equals(value)) {
						Run_Automation.rsDatasheet.moveNext();
						ReadFromDatasheet.setSrNO_DS(Run_Automation.rsDatasheet.getField("SrNo"));
					}
					Run_Automation.rsDatasheet.movePrevious();
				}
			} else {
				try {
					ReadFromDatasheet.setTestCase_IdDS(Run_Automation.rsDatasheet.getField("TestCase_Id"));
					WebDriverWait wait = new WebDriverWait(Run_Automation.driver, Duration.ofSeconds(10));
					WebElement xpath = (WebElement) wait.until(
							ExpectedConditions.visibilityOfElementLocated(By.xpath(ReadFromDatasheet.getObjectDS())));
					if (xpath.isDisplayed()) {

					} else {
						while (ReadFromDatasheet.getTestCase_IdDS().equals(value)) {
							Run_Automation.rsDatasheet.moveNext();
							ReadFromDatasheet.setTestCase_IdDS(Run_Automation.rsDatasheet.getField("TestCase_Id"));
						}
					}
				} catch (Exception e) {
					while (ReadFromDatasheet.getSrNO_DS().equals(value)) {
						Run_Automation.rsDatasheet.moveNext();
						ReadFromDatasheet.setTestCase_IdDS(Run_Automation.rsDatasheet.getField("TestCase_Id"));
					}
					Run_Automation.rsDatasheet.movePrevious();
				}
			}
		} catch (FilloException e) {
			e.printStackTrace();
		}

	}

	protected static void openInputField() {

		System.out.println("-------Input field---------");
		Frame frame = new Frame();
		frame.setVisible(true);
		frame.toFront();
		String data = JOptionPane.showInputDialog(frame, "Enter details here");
		webElement.sendKeys(new CharSequence[] { data });
		frame.setVisible(false);

	}

	protected static void singleManualOtpField() {
		Frame frame = new Frame();
		frame.setVisible(true);
		frame.toFront();
		String data = JOptionPane.showInputDialog(frame, "Enter details here");
		frame.setVisible(false);
		if (data.length() == 6) {
			manualOtp1 = data.charAt(0);
			manualOtp2 = data.charAt(1);
			manualOtp3 = data.charAt(2);
			manualOtp4 = data.charAt(3);
			manualOtp5 = data.charAt(4);
			manualOtp6 = data.charAt(5);
		} else if (data.length() == 4) {
			manualOtp1 = data.charAt(0);
			manualOtp2 = data.charAt(1);
			manualOtp3 = data.charAt(2);
			manualOtp4 = data.charAt(3);
		} else {
			System.out.println("Details not entered into input field");
		}

	}

	protected static void ManualOtp1() {
		String otp1 = String.valueOf(manualOtp1);
		if (otp1 != null) {
			webElement.sendKeys(otp1);

		} else {
			java.awt.Frame frame = new java.awt.Frame();
			frame.setVisible(true);
			frame.toFront();
			otp1 = JOptionPane.showInputDialog(frame, "Enter 1st OTP");
			webElement.sendKeys(otp1);
			frame.setVisible(false);
		}
	}

	protected static void ManualOtp2() {
		String otp2 = String.valueOf(manualOtp2);
		if (otp2 != null) {
			webElement.sendKeys(otp2);

		} else {
			java.awt.Frame frame = new java.awt.Frame();
			frame.setVisible(true);
			frame.toFront();
			otp2 = JOptionPane.showInputDialog(frame, "Enter 2nd OTP");
			webElement.sendKeys(otp2);
			frame.setVisible(false);
		}

	}

	protected static void ManualOtp3() {
		String otp3 = String.valueOf(manualOtp3);
		if (otp3 != null) {
			webElement.sendKeys(otp3);

		} else {
			java.awt.Frame frame = new java.awt.Frame();
			frame.setVisible(true);
			frame.toFront();
			otp3 = JOptionPane.showInputDialog(frame, "Enter 3rd OTP");
			webElement.sendKeys(otp3);
			frame.setVisible(false);
		}

	}

	protected static void ManualOtp4() {
		String otp4 = String.valueOf(manualOtp4);
		if (otp4 != null) {
			webElement.sendKeys(otp4);

		} else {
			java.awt.Frame frame = new java.awt.Frame();
			frame.setVisible(true);
			frame.toFront();
			otp4 = JOptionPane.showInputDialog(frame, "Enter 4th OTP");
			webElement.sendKeys(otp4);
			frame.setVisible(false);
		}

	}

	protected static void ManualOtp5() {
		String otp5 = String.valueOf(manualOtp5);
		if (otp5 != null) {
			webElement.sendKeys(otp5);

		} else {
			java.awt.Frame frame = new java.awt.Frame();
			frame.setVisible(true);
			frame.toFront();
			otp5 = JOptionPane.showInputDialog(frame, "Enter 5th OTP");
			webElement.sendKeys(otp5);
			frame.setVisible(false);
		}

	}

	protected static void ManualOtp6() {
		String otp6 = String.valueOf(manualOtp6);
		if (otp6 != null) {
			webElement.sendKeys(otp6);

		} else {
			java.awt.Frame frame = new java.awt.Frame();
			frame.setVisible(true);
			frame.toFront();
			otp6 = JOptionPane.showInputDialog(frame, "Enter 6th OTP");
			webElement.sendKeys(otp6);
			frame.setVisible(false);
		}

	}

	protected static void ReadCaptcha() {

		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		int elementX = webElement.getLocation().getX();
		int elementY = webElement.getLocation().getY();
		int elementWidth = webElement.getSize().getWidth();
		int elementHeight = webElement.getSize().getHeight();
		System.out.println(elementX + "\n" + elementY + "\n" + elementWidth + "\n" + elementHeight);

		// Crop the screenshot to capture only the element
		try {
			BufferedImage fullImage = ImageIO.read(screenshot);
			BufferedImage elementScreenshot = fullImage.getSubimage(elementX, elementY, elementWidth, elementHeight);
			ImageIO.write(elementScreenshot, "png", screenshot);

			// Save the screenshot as an image file
			File destination = new File("canvas_screenshot.png");
			FileUtils.copyFile(screenshot, destination);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Perform OCR on the screenshot
		Tesseract tesseract = new Tesseract();
		String text = null;
		try {
			text = tesseract.doOCR(new File("canvas_screenshot.png"));
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		System.out.println("Extracted Text: \n" + text);
	}

	protected static void hideKeyboard() {
//		RunMobAutomation.getDriver().getKeyboard();
		((PressesKey) RunMobAutomation.getDriver())
				.pressKey(new io.appium.java_client.android.nativekey.KeyEvent(AndroidKey.BACK));
	}

	protected static void robotSendkeys() {
		Robot rb = null;
		try {
			rb = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		System.out.println(newHashMap.get("valuesDS").trim());
		char[] ch = new char[newHashMap.get("valuesDS").trim().length()];
		for (int i = 0; i < newHashMap.get("valuesDS").trim().length(); ++i) {
			ch[i] = newHashMap.get("valuesDS").trim().charAt(i);
		}
		for (int j = 0; j < ch.length; ++j) {
			if (ch[j] == '.') {
				System.out.println(ch[j]);
				rb.keyPress(46);
				rb.keyRelease(46);
			} else if (ch[j] == '@') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(50);
				rb.keyRelease(16);
				rb.keyRelease(50);
			} else if (ch[j] == 'A') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(65);
				rb.keyRelease(16);
				rb.keyRelease(65);
			} else if (ch[j] == 'B') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(66);
				rb.keyRelease(16);
				rb.keyRelease(66);
			} else if (ch[j] == 'C') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(67);
				rb.keyRelease(16);
				rb.keyRelease(67);
			} else if (ch[j] == 'D') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(68);
				rb.keyRelease(16);
				rb.keyRelease(68);
			} else if (ch[j] == 'E') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(69);
				rb.keyRelease(16);
				rb.keyRelease(69);
			} else if (ch[j] == 'F') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(70);
				rb.keyRelease(16);
				rb.keyRelease(70);
			} else if (ch[j] == 'G') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(71);
				rb.keyRelease(16);
				rb.keyRelease(71);
			} else if (ch[j] == 'H') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(72);
				rb.keyRelease(16);
				rb.keyRelease(72);
			} else if (ch[j] == 'I') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(73);
				rb.keyRelease(16);
				rb.keyRelease(73);
			} else if (ch[j] == 'J') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(74);
				rb.keyRelease(16);
				rb.keyRelease(74);
			} else if (ch[j] == 'K') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(75);
				rb.keyRelease(16);
				rb.keyRelease(75);
			} else if (ch[j] == 'L') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(76);
				rb.keyRelease(16);
				rb.keyRelease(76);
			} else if (ch[j] == 'M') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(77);
				rb.keyRelease(16);
				rb.keyRelease(77);
			} else if (ch[j] == 'N') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(78);
				rb.keyRelease(16);
				rb.keyRelease(78);
			} else if (ch[j] == 'O') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(79);
				rb.keyRelease(16);
				rb.keyRelease(79);
			} else if (ch[j] == 'P') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(80);
				rb.keyRelease(16);
				rb.keyRelease(80);
			} else if (ch[j] == 'Q') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(81);
				rb.keyRelease(16);
				rb.keyRelease(81);
			} else if (ch[j] == 'R') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(82);
				rb.keyRelease(16);
				rb.keyRelease(82);
			} else if (ch[j] == 'S') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(83);
				rb.keyRelease(16);
				rb.keyRelease(83);
			} else if (ch[j] == 'T') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(84);
				rb.keyRelease(16);
				rb.keyRelease(84);
			} else if (ch[j] == 'U') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(85);
				rb.keyRelease(16);
				rb.keyRelease(85);
			} else if (ch[j] == 'V') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(86);
				rb.keyRelease(16);
				rb.keyRelease(86);
			} else if (ch[j] == 'W') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(87);
				rb.keyRelease(16);
				rb.keyRelease(87);
			} else if (ch[j] == 'X') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(88);
				rb.keyRelease(16);
				rb.keyRelease(88);
			} else if (ch[j] == 'Y') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(89);
				rb.keyRelease(16);
				rb.keyRelease(89);
			} else if (ch[j] == 'Z') {
				System.out.println(ch[j]);
				rb.keyPress(16);
				rb.keyPress(90);
				rb.keyRelease(16);
				rb.keyRelease(90);
			} else if (ch[j] == 'a') {
				System.out.println(ch[j]);
				rb.keyPress(65);
				rb.keyRelease(65);
			} else if (ch[j] == 'b') {
				System.out.println(ch[j]);
				rb.keyPress(66);
				rb.keyRelease(66);
			} else if (ch[j] == 'c') {
				System.out.println(ch[j]);
				rb.keyPress(67);
				rb.keyRelease(67);
			} else if (ch[j] == 'd') {
				System.out.println(ch[j]);
				rb.keyPress(68);
				rb.keyRelease(68);
			} else if (ch[j] == 'e') {
				System.out.println(ch[j]);
				rb.keyPress(69);
				rb.keyRelease(69);
			} else if (ch[j] == 'f') {
				System.out.println(ch[j]);
				rb.keyPress(70);
				rb.keyRelease(70);
			} else if (ch[j] == 'g') {
				System.out.println(ch[j]);
				rb.keyPress(71);
				rb.keyRelease(71);
			} else if (ch[j] == 'h') {
				System.out.println(ch[j]);
				rb.keyPress(72);
				rb.keyRelease(72);
			} else if (ch[j] == 'i') {
				System.out.println(ch[j]);
				rb.keyPress(73);
				rb.keyRelease(73);
			} else if (ch[j] == 'j') {
				System.out.println(ch[j]);
				rb.keyPress(74);
				rb.keyRelease(74);
			} else if (ch[j] == 'k') {
				System.out.println(ch[j]);
				rb.keyPress(75);
				rb.keyRelease(75);
			} else if (ch[j] == 'l') {
				System.out.println(ch[j]);
				rb.keyPress(76);
				rb.keyRelease(76);
			} else if (ch[j] == 'm') {
				System.out.println(ch[j]);
				rb.keyPress(77);
				rb.keyRelease(77);
			} else if (ch[j] == 'n') {
				System.out.println(ch[j]);
				rb.keyPress(78);
				rb.keyRelease(78);
			} else if (ch[j] == 'o') {
				System.out.println(ch[j]);
				rb.keyPress(79);
				rb.keyRelease(79);
			} else if (ch[j] == 'p') {
				System.out.println(ch[j]);
				rb.keyPress(80);
				rb.keyRelease(80);
			} else if (ch[j] == 'q') {
				System.out.println(ch[j]);
				rb.keyPress(81);
				rb.keyRelease(81);
			} else if (ch[j] == 'r') {
				System.out.println(ch[j]);
				rb.keyPress(82);
				rb.keyRelease(82);
			} else if (ch[j] == 's') {
				System.out.println(ch[j]);
				rb.keyPress(83);
				rb.keyRelease(83);
			} else if (ch[j] == 't') {
				System.out.println(ch[j]);
				rb.keyPress(84);
				rb.keyRelease(84);
			} else if (ch[j] == 'u') {
				System.out.println(ch[j]);
				rb.keyPress(85);
				rb.keyRelease(85);
			} else if (ch[j] == 'v') {
				System.out.println(ch[j]);
				rb.keyPress(86);
				rb.keyRelease(86);
			} else if (ch[j] == 'w') {
				System.out.println(ch[j]);
				rb.keyPress(87);
				rb.keyRelease(87);
			} else if (ch[j] == 'x') {
				System.out.println(ch[j]);
				rb.keyPress(88);
				rb.keyRelease(88);
			} else if (ch[j] == 'y') {
				System.out.println(ch[j]);
				rb.keyPress(89);
				rb.keyRelease(89);
			} else if (ch[j] == 'z') {
				System.out.println(ch[j]);
				rb.keyPress(90);
				rb.keyRelease(90);
			} else if (ch[j] == '1') {

				System.out.println(ch[j]);
				rb.keyPress(49);
				rb.keyRelease(49);
			} else if (ch[j] == '2') {
				System.out.println(ch[j]);
				rb.keyPress(50);
				rb.keyRelease(50);
			} else if (ch[j] == '3') {
				System.out.println(ch[j]);
				rb.keyPress(51);
				rb.keyRelease(51);
			} else if (ch[j] == '4') {
				System.out.println(ch[j]);
				rb.keyPress(52);
				rb.keyRelease(52);
			} else if (ch[j] == '5') {
				System.out.println(ch[j]);
				rb.keyPress(53);
				rb.keyRelease(53);
			} else if (ch[j] == '6') {
				System.out.println(ch[j]);
				rb.keyPress(54);
				rb.keyRelease(54);
			} else if (ch[j] == '7') {
				System.out.println(ch[j]);
				rb.keyPress(55);
				rb.keyRelease(55);
			} else if (ch[j] == '8') {
				System.out.println(ch[j]);
				rb.keyPress(56);
				rb.keyRelease(56);
			} else if (ch[j] == '9') {
				System.out.println(ch[j]);
				rb.keyPress(57);
				rb.keyRelease(57);
			} else if (ch[j] == '0') {
				System.out.println(ch[j]);
				rb.keyPress(48);
				rb.keyRelease(48);
			}
		}

	}

	protected static void pressKeycode() {
//		webElement.click();
		char[] ch = new char[newHashMap.get("valuesDS").trim().length()];
		for (int i = 0; i < newHashMap.get("valuesDS").trim().length(); ++i) {
			ch[i] = newHashMap.get("valuesDS").trim().charAt(i);
		}
		for (int j = 0; j < ch.length; ++j) {
			if (ch[j] == '1') {
				System.out.println(ch[j]);
				((PressesKey) RunMobAutomation.getDriver())
						.pressKey(new io.appium.java_client.android.nativekey.KeyEvent(AndroidKey.DIGIT_1));
			} else if (ch[j] == '2') {
				System.out.println(ch[j]);
				((PressesKey) RunMobAutomation.getDriver())
						.pressKey(new io.appium.java_client.android.nativekey.KeyEvent(AndroidKey.DIGIT_2));
			} else if (ch[j] == '3') {
				System.out.println(ch[j]);
				((PressesKey) RunMobAutomation.getDriver())
						.pressKey(new io.appium.java_client.android.nativekey.KeyEvent(AndroidKey.DIGIT_3));
			} else if (ch[j] == '4') {
				System.out.println(ch[j]);
				((PressesKey) RunMobAutomation.getDriver())
						.pressKey(new io.appium.java_client.android.nativekey.KeyEvent(AndroidKey.DIGIT_4));
			} else if (ch[j] == '5') {
				System.out.println(ch[j]);
				((PressesKey) RunMobAutomation.getDriver())
						.pressKey(new io.appium.java_client.android.nativekey.KeyEvent(AndroidKey.DIGIT_5));
			} else if (ch[j] == '6') {
				System.out.println(ch[j]);
				((PressesKey) RunMobAutomation.getDriver())
						.pressKey(new io.appium.java_client.android.nativekey.KeyEvent(AndroidKey.DIGIT_6));
			} else if (ch[j] == '7') {
				System.out.println(ch[j]);
				((PressesKey) RunMobAutomation.getDriver())
						.pressKey(new io.appium.java_client.android.nativekey.KeyEvent(AndroidKey.DIGIT_7));
			} else if (ch[j] == '8') {
				System.out.println(ch[j]);
				((PressesKey) RunMobAutomation.getDriver())
						.pressKey(new io.appium.java_client.android.nativekey.KeyEvent(AndroidKey.DIGIT_8));
			} else if (ch[j] == '9') {
				System.out.println(ch[j]);
				((PressesKey) RunMobAutomation.getDriver())
						.pressKey(new io.appium.java_client.android.nativekey.KeyEvent(AndroidKey.DIGIT_9));
			} else if (ch[j] == '0') {
				System.out.println(ch[j]);
				((PressesKey) RunMobAutomation.getDriver())
						.pressKey(new io.appium.java_client.android.nativekey.KeyEvent(AndroidKey.DIGIT_0));
			}
		}

	}

	protected static void coordinateSwipe() {
		try {
			String[] value = ReadFromDatasheet.getObjectDS().split(" ");
			int startx = Integer.valueOf(value[0]);
			int starty = Integer.valueOf(value[1]);
			int endx = Integer.valueOf(value[2]);
			int endy = Integer.valueOf(value[3]);
			
			System.out.println("startx = " + startx + "starty = " + starty + " ,endx = " + endx + " , endy = " + endy);
			PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
			Sequence scroll = new Sequence(finger, 0);

			scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startx, starty));
			scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
			scroll.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), startx, endy));
			scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
			RunMobAutomation.getDriver().perform(List.of(scroll));
		} catch (Exception e) {
		}

	}

	public static void swipeToElementMobile() {
		Dimension size = driver.manage().window().getSize();
		int starty = (int) (size.height * 0.70);
		int endy = (int) (size.height * 0.30);
		int startx = size.width / 2;
		int endx = size.width / 2;
		
		int i = 0;
		while(i<5) {
			try {
				RunMobAutomation.getDriver().findElement(By.xpath(ReadFromDatasheet.getObjectDS()));
				break;
			}catch (Exception e) {
				System.out.println("startx = " + startx + "starty = " + starty + " ,endx = " + endx + " , endy = " + endy);
				
				PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
				Sequence scroll = new Sequence(finger, 0);
				scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startx, starty));
				scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
				scroll.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), startx, endy));
				scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
				RunMobAutomation.getDriver().perform(List.of(scroll));
			}
			i++;
		}
	}

}
