package com.api.framework;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.web.framework.Run_Automation;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class RunApiAutomation {
	private static boolean saveResult;

	public static void getDataFromApiDatasheet() {
		Run_Automation.setStartTimeForLogs(new SimpleDateFormat("dd-MMM-yyyy HH mm ss").format(new Date()));
		ApiSaveResult.generateLogFolderAndFile();
		Fillo fillo = new Fillo();
		try {
			Connection con = fillo.getConnection(System.getProperty("user.dir") + "/Resources/DataSheet/API_DATA.xlsx");
			Recordset rs = con.executeQuery("Select * from Sheet1");
			while (rs.next()) {
				String srNo = rs.getField("SrNo");
				String url = rs.getField("URL");
				String body = rs.getField("BODY");
				String headerKeys = rs.getField("HeaderKeys");
				String headerValues = rs.getField("HeaderValues");
				String requestType = rs.getField("RequestType");
				String apiName = rs.getField("ApiName");
				String apiDescription = rs.getField("ApiDescription");
				HttpResponse<String> response = null;
				if (requestType.equalsIgnoreCase("GET")) {
					try {
						response = getRequest(url);
					} catch (Exception e) {
						System.out.println("Error while getting response using GET request");
						e.printStackTrace();
					}
				}
				if (requestType.equalsIgnoreCase("POST")) {
					try {
						response = postRequest(url, body, headerKeys, headerValues);
					} catch (Exception e) {
						System.out.println("Error while getting response using POST request");
						e.printStackTrace();
						
					}
				}
				if (requestType.equalsIgnoreCase("PUT")) {
					try {
						response = putRequest(url, body);
					} catch (Exception e) {
						System.out.println("Error while getting response using PUT request");
						e.printStackTrace();
					}
				}
				if (requestType.equalsIgnoreCase("DEL")) {
					try {
						response = delRequest(url);
					} catch (Exception e) {
						System.out.println("Error while getting response using DEL request");
						e.printStackTrace();
					}
				}
				if (requestType.equalsIgnoreCase("HEAD")) {
					try {
						response = headRequest(url);
					} catch (Exception e) {
						System.out.println("Error while getting response using HEAD request");
						e.printStackTrace();
					}
				}
				try {
					if (response != null) {
						System.out.println("Status : " + response.getStatus());
						System.out.println("Status Message : " + response.getStatusText());
						System.out.println("Response Body\n" + response.getBody());
						// ===========================================================
						ApiSaveResult.createJsonResponseFile(response.getBody(), apiName);
						ApiSaveResult.ApiReports(srNo, apiName, apiDescription);
					}else {
						System.out.println("Response is null, Kindly check if there is any wrong data for input");
					}
				} catch (Exception e) {
					e.printStackTrace();

				}
			}
			// save result
			try {
				ApiSaveResult.getWorkbook().write(ApiSaveResult.getFos());
				saveResult = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FilloException e) {
			e.printStackTrace();
		}

	}

	public static boolean isSaveResult() {
		return saveResult;
	}

	public static void setSaveResult(boolean saveResult) {
		RunApiAutomation.saveResult = saveResult;
	}

	private static HttpResponse<String> headRequest(String url) {
		HttpResponse<String> response = Unirest.head(url).asString();
		return response;
	}

	private static HttpResponse<String> delRequest(String url) {
		HttpResponse<String> response = Unirest.delete(url).asString();
		return response;
	}

	private static HttpResponse<String> putRequest(String url, String body) {
		HttpResponse<String> response = Unirest.put(url).body(body).asString();
		return response;
	}

	private static HttpResponse<String> postRequest(String url, String body, String headerKeys, String headerValues) {
		HttpResponse<String> response = Unirest.post(url).header("Content-Type", "application/json")
				.header(headerKeys, headerValues).body(body).asString();

		return response;
	}

	private static HttpResponse<String> getRequest(String url) {
		HttpResponse<String> response = Unirest.get(url).asString();
		return response;
	}

}
