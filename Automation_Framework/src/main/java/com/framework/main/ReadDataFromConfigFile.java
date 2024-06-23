package com.framework.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadDataFromConfigFile {
	private static String automationType;
	private static String headless;

	static void readFromConfigFile() throws IOException {
		Properties properties = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/Resources/config.properties");
		properties.load(fis);
		automationType = properties.getProperty("automationType");
		headless = properties.getProperty("headless");
	}

	public static String getHeadless() {
		return headless;
	}

	public static void setHeadless(String headless) {
		ReadDataFromConfigFile.headless = headless;
	}

	public static String getAutomationType() {
		return automationType;
	}

	public static void setAutomationType(String automationType) {
		ReadDataFromConfigFile.automationType = automationType;
	}
}
