package com.learningselenium.testhelpers;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import javax.mail.MessagingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.ITestAnnotation;
import org.testng.internal.annotations.IAnnotationTransformer;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.learningselenium.utilities.MonitoringMail;
import com.learningselenium.utilities.TestUtil;

public class CustomListeners implements ITestListener, ISuiteListener, IRetryAnalyzer, IAnnotationTransformer {

	private static final Logger LOG = LogManager.getLogger();

	static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss");
	static LocalDateTime now = LocalDateTime.now();
	static String timeStamp = (dtf.format(now));

	static String extentReportfileName = "TestExecutionReport" + timeStamp + ".html";
	static String extentReportFilePath = System.getProperty("user.dir") + "\\Reports\\" + extentReportfileName;

	private static ExtentReports extent = ExtentManager.createInstance(extentReportFilePath);
	public static ThreadLocal<ExtentTest> testReport = new ThreadLocal<ExtentTest>();

	/**
	 * onTestStart method initializes Extent Test on Start-Up
	 */
	public void onTestStart(ITestResult result) {
		ExtentTest test = extent
				.createTest(result.getTestClass().getName() + "     @TestCase : " + result.getMethod().getMethodName());
		testReport.set(test);
	}

	/**
	 * Invoked each time a test fails.
	 *
	 * @param result <code>ITestResult</code> containing information about the run
	 *               test
	 * @see ITestResult#FAILURE
	 */
	public void onTestFailure(ITestResult result) {

		String excepionMessage = Arrays.toString(result.getThrowable().getStackTrace());
		testReport.get()
				.fail("<details>" + "<summary>" + "<b>" + "<font color=" + "red>" + "Exception Occured:Click to see"
						+ "</font>" + "</b >" + "</summary>" + excepionMessage.replaceAll(",", "<br>") + "</details>"
						+ " \n");

		LOG.info("TestCase : " + result.getName() + " failed.");
		String failedMethodName = result.getMethod().getMethodName();

		String fileName = "FailureScreenshot_" + failedMethodName + "_" + timeStamp + ".jpg";
		String filePath = System.getProperty("user.dir") + "src\\test\\resources\\TestScreenshots\\" + fileName;

		try {

			TestUtil.takeScreenshot(filePath, fileName);
			testReport.get().fail("<b>" + "<font color=" + "red>" + "Screenshot of failure" + "</font>" + "</b>",
					MediaEntityBuilder.createScreenCaptureFromPath(filePath).build());
		} catch (IOException e) {

		}

		System.setProperty("org.uncommons.reportng.escape-output", "false");
		Reporter.log("<a target=\"_blank\" href=" + fileName + ">Screenshot link</a>");
		Reporter.log("<br>");
		Reporter.log("<a target=\"_blank\" href=" + fileName + "><img src=" + fileName + " height=200 width=200></a>");

		String failureLogg = "TEST CASE FAILED";
		Markup m = MarkupHelper.createLabel(failureLogg, ExtentColor.RED);
		testReport.get().log(Status.FAIL, m);

		result.getThrowable().printStackTrace();

	}

	/**
	 * Invoked each time a test is skipped.
	 *
	 * @param result <code>ITestResult</code> containing information about the run
	 *               test
	 * @see ITestResult#SKIP
	 */
	public void onTestSkipped(ITestResult result) {

		// add Retry Logic Here

		String methodName = result.getMethod().getMethodName();
		String logText = "<b>" + "Test Case:- " + methodName + " Skipped" + "</b>";
		Markup m = MarkupHelper.createLabel(logText, ExtentColor.YELLOW);
		testReport.get().skip(m);

		LOG.info("TestCase : " + result.getName() + " skipped.");
	}

	/**
	 * Invoked each time a test succeeds.
	 *
	 * @param result <code>ITestResult</code> containing information about the run
	 *               test
	 * @see ITestResult#SUCCESS
	 */
	public void onTestSuccess(ITestResult result) {

		LOG.info("TestCase : " + result.getName() + " Passed.");
		String methodName = result.getMethod().getMethodName();
		String logText = "<b>" + "TEST CASE:- " + methodName.toUpperCase() + " PASSED" + "</b>";
		Markup m = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
		testReport.get().pass(m);

	}

	/**
	 * This method is invoked after the SuiteRunner has run all the tests in the
	 * suite.
	 *
	 * @param suite The suite
	 */
	public void onFinish(ISuite suite) {
		try {
			MonitoringMail.sendMail(extentReportFilePath);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		if (extent != null) {
			extent.flush();
		}

	}

	
	int counter = 0;
	int retryLimit = 3;

	/**
	 * retry methods sets the counter on the number of times a Test will run after
	 * failure.
	 */
	@Override
	public boolean retry(ITestResult result) {

		while (counter < retryLimit) {
			counter++;
			return true;
		}

		return false;
	}
	
	/**
	 * Listener to call retry method on Test Failure
	 */
	@SuppressWarnings("rawtypes")
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod,
			Class<?> occurringClazz) {

		annotation.setRetryAnalyzer(CustomListeners.class);
	}

}
