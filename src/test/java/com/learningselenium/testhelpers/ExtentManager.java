package com.learningselenium.testhelpers;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;


public class ExtentManager {

	private static ExtentReports extent;	

	    public static ExtentReports createInstance(String fileName) {
	        ExtentSparkReporter htmlReporter = new ExtentSparkReporter(fileName);       
	        
	        htmlReporter.config().setTheme(Theme.STANDARD);
	        htmlReporter.config().setDocumentTitle("Automation Test Result Report");
	        htmlReporter.config().setEncoding("utf-8");
	        htmlReporter.config().setReportName("Automation Test Result Extent Report ");
	        
	        extent = new ExtentReports();
	        extent.attachReporter(htmlReporter);
	        extent.setSystemInfo("Automation Tester", "Vinod Mallah");
	        extent.setSystemInfo("Organization", "Learning Selenium");
	        extent.setSystemInfo("Build no", "0.1");
	        
	        
	        return extent;
	    }   	
	    
	

	}
