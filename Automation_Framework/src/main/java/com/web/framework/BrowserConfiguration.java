package com.web.framework;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.framework.main.ReadDataFromConfigFile;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BrowserConfiguration extends Run_Automation {

	private static ChromeOptions options;
	private static boolean browserConfig;

	public static void main(String[] args) {

		configureBrowser();

	}

	public static ChromeOptions getOptions() {
		return options;
	}

	public static void setOptions(ChromeOptions options) {
		BrowserConfiguration.options = options;
	}

	public static void configureBrowser() {
		if (Run_Automation.browser.equalsIgnoreCase("Chrome")) {
			options = new ChromeOptions();
			options.addArguments("--disable-notifications");
			options.addArguments("test-type");
			if (ReadDataFromConfigFile.getHeadless().equalsIgnoreCase("true")) {
				options.addArguments("--headless");
			}
			options.addArguments("window-size=1920,1080");
			options.addArguments("allow-running-insecure-content");
			options.addArguments("--disable-extentions");
			options.addArguments("disable-infobars");
			options.addArguments("disable-captcha");
			options.addArguments("--remote-allow-origins=*");
			Map<String, Object> prefs = new HashMap<>();
			prefs.put("profile.default_content_setting_values.media_stream_mic", 1);
			prefs.put("profile.default_content_setting_values.media_stream_camera", 1);
			prefs.put("profile.default_content_setting_values.geolocation", 1);
			prefs.put("profile.default_content_setting_values.notifications", 1);
			prefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);
			options.setExperimentalOption("prefs", prefs);
			WebDriverManager.chromedriver().setup();
			browserConfig = true;

		} else if (Run_Automation.browser.equalsIgnoreCase("FireFox")) {
			DesiredCapabilities cap = new DesiredCapabilities();
			cap.setCapability("-marionette", true);
			cap.setCapability("marionettePort", "2828");
			cap.acceptInsecureCerts();
			WebDriverManager.firefoxdriver().setup();
			browserConfig = true;
		} else if (Run_Automation.browser.equalsIgnoreCase("Edge")) {

		} else {
			System.out.println("No Browser");
		}
	}

	public static boolean isBrowserConfig() {
		return browserConfig;
	}

	public static void setBrowserConfig(boolean browserConfig) {
		BrowserConfiguration.browserConfig = browserConfig;
	}

}
