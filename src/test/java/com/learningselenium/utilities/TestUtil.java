package com.learningselenium.utilities;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.learningselenium.testbase.TestBase;

public class TestUtil extends TestBase{
	
	/**
	 * takeScreenshot method takes screenshot when invoked and save it in 
	 * src/test/resources TestScreenshots folder with current timestamp.
	 * @throws IOException
	 */
	public static void takeScreenshot(String path,String fileName) throws IOException {

		String filePath = path;

		File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(srcFile, new File(filePath));
		FileUtils.copyFile(srcFile, new File(System.getProperty("user.dir") + "\\target\\surefire-reports\\html\\" + fileName));
	}

}
