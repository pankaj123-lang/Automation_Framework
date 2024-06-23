package com.framework.main;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.web.framework.Run_Automation;

public class DatabaseOperations {
	private static ResultSet rs;
	private static int run_Id = 1;
	private static Connection Localconnection;
	private static Connection connection;
	private static String startTime;
	private static String screenshotFileLocation_ID;

	public static String getScreenshotFileLocation_ID() {
		return screenshotFileLocation_ID;
	}

	public static void setScreenshotFileLocation_ID(String screenshotFileLocation_ID) {
		DatabaseOperations.screenshotFileLocation_ID = screenshotFileLocation_ID;
	}

	public static void RunDuration(String runStatus) {
		String status = "";
		String currentStatus = "";
		String flag = "N";
		int count = 0;
		if (Run_Automation.getdB_Connection().equalsIgnoreCase("Y")) {
			if (runStatus.equalsIgnoreCase("Started")) {
				String query = "select * FROM Run_history where RUN_ID=(SELECT max(RUN_ID) FROM Run_history where Application_Name='"
						+ Run_Automation.getApplication_Name() + "' and Status='RUNNING');";
				ResultSet RS = DatabaseOperations.getConnec_local_DB(query, "execute");
				if (RS != null)
					try {
						if (RS.next()) {
							flag = "Y";
							run_Id = RS.getInt("RUN_ID");
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				System.out.println("Current Status is " + currentStatus);
				if (flag.equals("N")) {
					try {
						connection = Localconnection_propoties();
						String pquery = "insert into Run_history (Application_Name) values (?)";
						System.out.println("connnection before call :" + connection);
						try (PreparedStatement pstmt = connection.prepareStatement(pquery);) {
							pstmt.setString(1, Run_Automation.getApplication_Name());
							pstmt.execute();
							System.out.println("Sucessfully inserted");
						}

						String pquery1 = "SELECT max(RUN_ID) FROM Run_history where Application_Name= ?";
						try (PreparedStatement pstmt = connection.prepareStatement(pquery1);) {
							pstmt.setString(1, Run_Automation.getApplication_Name());
							ResultSet RS1 = pstmt.executeQuery();

							while (RS1.next()) {
								DatabaseOperations.run_Id = RS1.getInt("max(RUN_ID)");
								System.out.println(DatabaseOperations.run_Id + " Not start through Tool    "
										+ Run_Automation.getApplication_Name());
							}
							RS1.close();
						}
					} catch (Exception e) {
						e.printStackTrace();

					} finally {
						try {
							connection.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}

				}
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				Date formatter1 = new Date();
				startTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
				String stmt = "update Run_history set Start_Time='" + formatter.format(formatter1)
						+ "',Status='RUNNING',PID=" + PID() + " where RUN_ID=" + DatabaseOperations.run_Id
						+ " and Application_Name='" + Run_Automation.getApplication_Name() + "' ";
				System.out.println(stmt);
				DatabaseOperations.getConnec_local_DB(stmt, "update");

				System.out.println("========RUN-ID=========>" + DatabaseOperations.run_Id);
				// RS.close();
			}

		}
		if (runStatus.equalsIgnoreCase("completed")) {

			String query = "SELECT count(*) FROM result_logs where RUN_ID='" + DatabaseOperations.run_Id
					+ "' and Application_Name='" + Run_Automation.getApplication_Name() + "'";
			ResultSet RS = DatabaseOperations.getConnec_local_DB(query, "execute");
			if (RS != null) {
				try {
					while (RS.next()) {
						count = RS.getInt("count(*)");
						System.out.println(count);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (count == 0) {
				status = "Fail";
			} else {
				Integer countStatusFail = 0;
//				String queryStatusFail = query + "and Status='FAIL'";
//				ResultSet rStatusFail = DatabaseOperations.getConnec_local_DB(queryStatusFail, "execute");
				try {
					while (RS.next()) {
						countStatusFail = RS.getInt("count(*)");
						System.out.println(count);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if (countStatusFail == 0) {
					status = "Success";
				} else {
					status = "Fail";
				}

			}
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			Date formatter1 = new Date();
			String stmt = "update Run_history set End_Time='" + formatter.format(formatter1) + "',Status='" + status
					+ "' where RUN_ID=" + DatabaseOperations.run_Id + " and Application_Name='"
					+ Run_Automation.getApplication_Name() + "' ";
			System.out.println(stmt);
			DatabaseOperations.getConnec_local_DB(stmt, "update");
		}
		if (runStatus.equalsIgnoreCase("Total")) {
			String Query = "SELECT * FROM Run_history where RUN_ID=" + DatabaseOperations.run_Id + "";
			ResultSet rs = DatabaseOperations.getConnec_local_DB(Query, "execute");
			if (rs != null) {
				try {
					while (rs.next()) {
						String START_TIME = rs.getString("Start_Time");
						String END_TIME = rs.getString("End_Time");

						String time1 = START_TIME;
						String time2 = END_TIME;

						SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
						Date date1 = format.parse(time1);
						Date date2 = format.parse(time2);
						long difference = date2.getTime() - date1.getTime();
						long secondsInMilli = 1000;
						long minutesInMilli = secondsInMilli * 60;
						long hoursInMilli = minutesInMilli * 60;

						long elapsedHours = difference / hoursInMilli;
						difference = difference % hoursInMilli;

						long elapsedMinutes = difference / minutesInMilli;
						difference = difference % minutesInMilli;

						long elapsedSeconds = difference / secondsInMilli;
						String Total_Time = elapsedHours + "hr:" + elapsedMinutes + "min:" + elapsedSeconds + "Sec";
						String datequery = "update Run_history set Execution_Time=" + "'" + Total_Time + "' where RUN_ID="
								+ DatabaseOperations.run_Id + " and Application_Name='"
								+ Run_Automation.getApplication_Name() + "' ";
						DatabaseOperations.getConnec_local_DB(datequery, "update");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

		}

	}

	public static int getRun_Id() {
		return run_Id;
	}

	public static void setRun_Id(int run_Id) {
		DatabaseOperations.run_Id = run_Id;
	}

	public static String getStartTime() {
		return startTime;
	}

	public static void setStartTime(String startTime) {
		DatabaseOperations.startTime = startTime;
	}

	private static Long PID() {
		Long PID = (long) 0;

		RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
		String jvmName = runtimeBean.getName();
		PID = Long.valueOf(jvmName.split("@")[0]);

		return PID;
	}

	private static ResultSet getConnec_local_DB(String query, String type) {
		try {
			Class.forName(Run_Automation.getDbDriver());
			Connection connection = DriverManager.getConnection(Run_Automation.getDbUrl(),
					Run_Automation.getdBUserName(), Run_Automation.getdBPassword());

			Statement stmt = connection.createStatement();
			if (type.equalsIgnoreCase("execute")) {
				rs = stmt.executeQuery(query);

			} else if (type.equalsIgnoreCase("update")) {
				int r = stmt.executeUpdate(query);
				System.out.println(r);
				rs = null;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public static Connection Localconnection_propoties() throws Exception {
		try {

			Class.forName(Run_Automation.getDbDriver());
		} catch (Exception e) {
			System.out.println("class loading issue " + e.getMessage());
			e.printStackTrace();
		}
		try {
			Localconnection = DriverManager.getConnection(Run_Automation.getDbUrl(), Run_Automation.getdBUserName(),
					Run_Automation.getdBPassword());
		} catch (Exception e) {
			System.out.println("connection obj:" + Localconnection);
			e.printStackTrace();
		}
		return Localconnection;
	}

}
