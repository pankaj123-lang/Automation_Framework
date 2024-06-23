package com.framework.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;

import com.mobile.framework.RunMobAutomation;
import com.web.framework.Run_Automation;

import ru.yandex.qatools.ashot.AShot;

public class Screenshot {

	private static String screenshotFileLocation;
	private static String screenshotFileLocation1;

	public static String getScreenshotFileLocation() {
		return screenshotFileLocation;
	}

	public static void setScreenshotFileLocation(String screenshotFileLocation) {
		Screenshot.screenshotFileLocation = screenshotFileLocation;
	}

	public static String getScreenshotFileLocation1() {
		return screenshotFileLocation1;
	}

	public static void setScreenshotFileLocation1(String screenshotFileLocation1) {
		Screenshot.screenshotFileLocation1 = screenshotFileLocation1;
	}

	public static void main(String[] args) {

	}

	public static void TakeScrennshot() {
		try {
			BufferedImage screenShot;
			// BufferedImage screenShot = new
			// AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver).getImage();
			if (ReadDataFromConfigFile.getAutomationType().equalsIgnoreCase("Web")) {
				screenShot = new AShot().takeScreenshot(Run_Automation.getDriver()).getImage();
			} else {
				screenShot = new AShot().takeScreenshot(RunMobAutomation.getDriver()).getImage();

			}
			Date now = new Date();
			int year = Calendar.getInstance().get(Calendar.YEAR);
			String monthName = new SimpleDateFormat("MMM").format(now);
			int monthday = Calendar.getInstance().get(Calendar.DATE);

			String file1;
			if (ReadDataFromConfigFile.getAutomationType().equalsIgnoreCase("Web")) {
				if (RunFramework.getAutomationType().equalsIgnoreCase("Monitoring")) {
					file1 = Run_Automation.getHomedir() + "/Reports/Web/ExcelReport_" + monthday + " " + monthName + " "
							+ year + "/" + Run_Automation.getApplication_Name() + "/Screenshots/";
				} else {
					file1 = Run_Automation.getHomedir() + "/Reports/Web/HtmlReport_" + monthday + " " + monthName + " "
							+ year + "/" + Run_Automation.getApplication_Name() + "/Screenshots/";
				}
			} else {
				if (RunFramework.getAutomationType().equalsIgnoreCase("Monitoring")) {
					file1 = Run_Automation.getHomedir() + "/Reports/Mobile/ExcelReport_" + monthday + " " + monthName
							+ " " + year + "/" + RunMobAutomation.getApplication_Name() + "/Screenshots/";
				} else {
					file1 = Run_Automation.getHomedir() + "/Reports/Mobile/HtmlReport_" + monthday + " " + monthName
							+ " " + year + "/" + RunMobAutomation.getApplication_Name() + "/Screenshots/";
				}

			}
			String file = "./Screenshots/";

			Date nowScreen = new Date();
			@SuppressWarnings("deprecation")
			String ScreenShotTime = ReadFromDatasheet.getPageNameDS() + "_" + nowScreen.getHours() + "_"
					+ nowScreen.getMinutes() + "_" + nowScreen.getSeconds();
			screenshotFileLocation = file + ScreenShotTime + ".png";
			screenshotFileLocation1 = file1 + ScreenShotTime + ".png";
			ImageIO.write(screenShot, "PNG", new File(screenshotFileLocation1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
