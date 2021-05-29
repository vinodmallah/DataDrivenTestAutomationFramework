package com.learningselenium.testbase;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.learningselenium.utilities.DatabaseManager;
import com.learningselenium.utilities.ExcelReader;
import com.learningselenium.utilities.MonitoringMail;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestBase {

	public static WebDriver driver;
	public static Properties testConfig = new Properties();
	public static Properties objectRepository = new Properties();
	public static Logger LOG = LogManager.getLogger(TestBase.class);
	public static ExcelReader reader = new ExcelReader("src\\test\\resources\\ExcelFiles\\TestData.xlsx");
	public static FileInputStream fis;
	public static MonitoringMail mail;
	public static WebDriverWait wait;

	@BeforeSuite(alwaysRun = true)
	public void setupDatabaseConnection() throws IOException {
		DatabaseManager.connectMySqlDatabase();
		LOG.info("Connected to MySQL Database");
	}

	@BeforeTest(alwaysRun = true)
	public void performPreTestActivity() throws IOException {
		loadProperties();
		LOG.info("Loaded TestConfig & Object Repository Properties File.");

	}

	@BeforeMethod(alwaysRun = true)
	public void launchBrowser() throws IOException {
		webDriverSetup();
		LOG.info("WebDriver setup successfully completed.");
		openBrowser();
	}

	@AfterMethod(alwaysRun = true)
	public void closeBrowser() throws IOException {
		closeAllBrowserInstances();
		LOG.info("Closed all driver browser instances.");
	}

	@AfterTest(alwaysRun = true)
	public void performPostTestActivity() throws IOException {
		LOG.info("Performing Post Test Activity");
	}

	@AfterSuite(alwaysRun = true)
	public void tearDown() throws IOException {
		DatabaseManager.close();
		LOG.info("Successfully closed MySQL Database Connection");
	}

	/**
	 * loadProperties method loads TestConfig & Object repository Properties File
	 * 
	 * @throws IOException
	 */
	private static void loadProperties() throws IOException {

		FileInputStream fisTestConfig = new FileInputStream("src\\test\\resources\\Properties\\TestConfig.properties");
		testConfig.load(fisTestConfig);

		FileInputStream fisOR = new FileInputStream("src\\test\\resources\\Properties\\ObjectRepository.properties");
		objectRepository.load(fisOR);

	}

	/**
	 * webDriverSetup method setup's driver instance based in browser property in
	 * TestConfig properties file
	 * 
	 */
	private static void webDriverSetup() {

		String browser = (testConfig.getProperty("browser")).toLowerCase();

		if (browser.equals("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		} else if (browser.equals("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		} else {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		}

	}

	/**
	 * openBrowser assigns implicit wait for the driver instance, maximize window &
	 * navigates to Test URL specified in TestConfig file.
	 * 
	 * @throws IOException
	 */
	private static void openBrowser() throws IOException {

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Integer.parseInt(testConfig.getProperty("implicit.wait")),
				TimeUnit.SECONDS);
		driver.get(testConfig.getProperty("TestUrl"));
		LOG.info("Navigated to " + testConfig.getProperty("TestUrl") + " url.");

	}

	/**
	 * closeAllBrowserInstances calls quit method on driver instances which closes
	 * all browser instances opened by driver
	 * 
	 * @throws IOException
	 */
	private static void closeAllBrowserInstances() throws IOException {
		driver.quit();
	}

	/************************************
	 * Action Methods
	 ********************************************/

	/**
	 * click methods is used to click on any Clickable field
	 * 
	 * @param key
	 */
	public static void click(String key) {

		if (key.endsWith("_XPATH")) {
			driver.findElement(By.xpath(objectRepository.getProperty(key))).click();
		} else if (key.endsWith("_CSS")) {
			driver.findElement(By.cssSelector(objectRepository.getProperty(key))).click();
		} else if (key.endsWith("_ID")) {
			driver.findElement(By.id(objectRepository.getProperty(key))).click();
		} else if (key.endsWith("_NAME")) {
			driver.findElement(By.name(objectRepository.getProperty(key))).click();
		} else if (key.endsWith("_LINKTEXT")) {
			driver.findElement(By.linkText(objectRepository.getProperty(key))).click();
		}

		LOG.info("Clicked " + key + " field");
	}

	/**
	 * Overloaded click method to click on an element based on value provided
	 * 
	 * @param key
	 * @param value
	 */
	public static void click(String key, String value) {

		String dynamicLocator = objectRepository.getProperty(key);

		if (dynamicLocator.contains("SearchString")) {
			dynamicLocator = dynamicLocator.replace("SearchString", value);
		}

		if (key.endsWith("_XPATH")) {
			driver.findElement(By.xpath(dynamicLocator)).click();
		} else if (key.endsWith("_CSS")) {
			driver.findElement(By.cssSelector(dynamicLocator)).click();
		} else if (key.endsWith("_ID")) {
			driver.findElement(By.id(dynamicLocator)).click();
		} else if (key.endsWith("_NAME")) {
			driver.findElement(By.name(dynamicLocator)).click();
		} else if (key.endsWith("_LINKTEXT")) {
			driver.findElement(By.linkText(dynamicLocator)).click();
		}

		LOG.info("Clicked " + key + " field");
	}

	/**
	 * type method is used Type values in Text Field
	 * 
	 * @param key
	 * @param value
	 */
	public static void type(String key, String value) {

		if (key.endsWith("_XPATH")) {
			driver.findElement(By.xpath(objectRepository.getProperty(key))).sendKeys(value);
		} else if (key.endsWith("_CSS")) {
			driver.findElement(By.cssSelector(objectRepository.getProperty(key))).sendKeys(value);
		} else if (key.endsWith("_ID")) {
			driver.findElement(By.id(objectRepository.getProperty(key))).sendKeys(value);
		} else if (key.endsWith("_NAME")) {
			driver.findElement(By.name(objectRepository.getProperty(key))).sendKeys(value);
		} else if (key.endsWith("_LINKTEXT")) {
			driver.findElement(By.linkText(objectRepository.getProperty(key))).sendKeys(value);
		}

		LOG.info("Entered " + value + " in " + key + " field");
	}

	/**
	 * clear method is used clear values in Text Field
	 * 
	 * @param key
	 * @param value
	 */
	public static void clear(String key) {

		if (key.endsWith("_XPATH")) {
			driver.findElement(By.xpath(objectRepository.getProperty(key))).clear();
		} else if (key.endsWith("_CSS")) {
			driver.findElement(By.cssSelector(objectRepository.getProperty(key))).clear();
		} else if (key.endsWith("_ID")) {
			driver.findElement(By.id(objectRepository.getProperty(key))).clear();
		} else if (key.endsWith("_NAME")) {
			driver.findElement(By.name(objectRepository.getProperty(key))).clear();
		} else if (key.endsWith("_LINKTEXT")) {
			driver.findElement(By.linkText(objectRepository.getProperty(key))).clear();
		}

	}

	/**
	 * getText method is used to return field values
	 * 
	 * @param key
	 * @return String value of the located field
	 */
	@SuppressWarnings("unused")
	private static String getText(String key) {

		String returnValue = null;

		if (key.endsWith("_XPATH")) {
			returnValue = driver.findElement(By.xpath(objectRepository.getProperty(key))).getText();
		} else if (key.endsWith("_CSS")) {
			returnValue = driver.findElement(By.cssSelector(objectRepository.getProperty(key))).getText();
		} else if (key.endsWith("_ID")) {
			returnValue = driver.findElement(By.id(objectRepository.getProperty(key))).getText();
		} else if (key.endsWith("_NAME")) {
			returnValue = driver.findElement(By.name(objectRepository.getProperty(key))).getText();
		} else if (key.endsWith("_LINKTEXT")) {
			returnValue = driver.findElement(By.linkText(objectRepository.getProperty(key))).getText();
		}

		return returnValue;
	}

	/**
	 * selectDropdown method selects any value of dropdown by visible text
	 * 
	 * @param key
	 * @param value
	 */
	public static void selectDropdown(String key, String value) {

		WebElement element = null;

		if (key.endsWith("_XPATH")) {
			element = driver.findElement(By.xpath(objectRepository.getProperty(key)));
		} else if (key.endsWith("_CSS")) {
			element = driver.findElement(By.cssSelector(objectRepository.getProperty(key)));
		} else if (key.endsWith("_ID")) {
			element = driver.findElement(By.id(objectRepository.getProperty(key)));
		} else if (key.endsWith("_NAME")) {
			element = driver.findElement(By.name(objectRepository.getProperty(key)));
		} else if (key.endsWith("_LINKTEXT")) {
			element = driver.findElement(By.linkText(objectRepository.getProperty(key)));
		}

		Select selectElement = new Select(element);
		selectElement.selectByVisibleText(value);

		LOG.info("Selected " + value + " in " + key + " Select field");

	}

	/**
	 * alert method is used to either accept or dismiss alert box
	 * 
	 * @param action
	 */
	public void alert(String action) {
		Alert alert = driver.switchTo().alert();

		if (action.equals("accept")) {
			alert.accept();
		} else if (action.equals("dismiss")) {
			alert.dismiss();
		}
	}

	/**
	 * isRecordAdded validate if the record is added by validting alert displayed
	 * post add customer button is clicked
	 * 
	 * @param expectedMessage
	 * @return
	 */
	public boolean isRecordAdded(String key) {

		Alert alert = driver.switchTo().alert();
		String alertMessage = alert.getText();
		String expectedMessage = testConfig.getProperty(key);

		if (alertMessage.contains(expectedMessage))
			return true;
		else
			return false;

	}

	/**
	 * isCustomerRecordDisplayed method validates whether record of customer is
	 * displayed in Customer tab after adding customer
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean isCustomerRecordDisplayed(String key, String value) {

		boolean result = false;

		String dynamicXpath = objectRepository.getProperty(key);
		dynamicXpath = dynamicXpath.replace("SearchString", value);

		System.out.println(dynamicXpath);

		result = driver.findElement(By.xpath(dynamicXpath)).isDisplayed();

		if (result)
			LOG.info("Added record : " + value + " is present in Customers Table");
		else
			LOG.info("Added record : " + value + " is NOT present in Customers Table");

		return result;

	}

	/**
	 * isAccountFieldOfCustomerRecordDisplayed method validates whether account
	 * field of added customer customer is displayed in Customer tab.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean isAccountFieldOfCustomerRecordDisplayed(String key, String value) {

		String accounttext = null;

		String dynamicXpath = objectRepository.getProperty(key);
		dynamicXpath = dynamicXpath.replace("SearchString", value);

		accounttext = driver.findElement(By.xpath(dynamicXpath)).getText();

		System.out.println("Account Text : " + accounttext);

		if (accounttext == "" || accounttext == null || accounttext == " ") {
			LOG.info("Account text for the record : " + value + " is NOT present in Customers Table");
			return false;
		} else {
			LOG.info("Account text for the record : " + value + " is present in Customers Table");
			return true;
		}

	}

	/**
	 * isElementPresent method validate if an element is present & displayed on the
	 * screen and return boolean value true/false
	 * 
	 * @param key
	 * @return
	 */
	public static boolean isElementPresent(String key) {
		boolean result = false;

		if (key.endsWith("_XPATH")) {
			result = driver.findElement(By.xpath(objectRepository.getProperty(key))).isDisplayed();
		} else if (key.endsWith("_CSS")) {
			result = driver.findElement(By.cssSelector(objectRepository.getProperty(key))).isDisplayed();
		} else if (key.endsWith("_ID")) {
			result = driver.findElement(By.id(objectRepository.getProperty(key))).isDisplayed();
		} else if (key.endsWith("_NAME")) {
			result = driver.findElement(By.name(objectRepository.getProperty(key))).isDisplayed();
		} else if (key.endsWith("_LINKTEXT")) {
			result = driver.findElement(By.linkText(objectRepository.getProperty(key))).isDisplayed();
		}

		return result;
	}

	/**
	 * switchTo method is used to switch between New Tabs & Parent Window based
	 * on @param value.
	 * 
	 * @param window
	 * @param parentWindowHandle
	 */
	public static void switchTo(String window, String parentWindowHandle) {

		if (window.equals("NewTab")) {

			Set<String> windowHandles = driver.getWindowHandles();

			for (String handle : windowHandles) {

				if (!handle.equals(parentWindowHandle)) {

					driver.switchTo().window(handle);
				}

			} // end of NewTab for each loop

		} // end of NewTab If

		else if (window.equals("ParentWindow")) {

			driver.switchTo().window(parentWindowHandle);
		}

	}

}
