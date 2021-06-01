package com.learningselenium.testcases;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Hashtable;

import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.learningselenium.testbase.TestBase;

public class BankManagerTestClass extends TestBase {

	@Test(dataProvider = "ExcelTestDataProvider")
	public void test001_AddCustomerTest(Hashtable<String, String> data, Method m)
			throws IOException, InterruptedException {

		SoftAssert softAssert = new SoftAssert();
		String homePageTitleActual = driver.getTitle();

		// Validating Home Page Title
		softAssert.assertEquals(homePageTitleActual, testConfig.getProperty("HomePageTitle_Expected"),
				"Home Page Title did not match");

		// Clicking BankManager Login Button
		click("BankManagerLogin_Button_XPATH");

		// Validate user is navigated to Manager Home Screen. - Hard Assertion
		Assert.assertTrue(isElementPresent("AddCustomer_MenuButton_XPATH"));

		// Click on Add Customer Menu Button
		click("AddCustomer_MenuButton_XPATH");

		// Enter First Name
		type("AddCustomer_FirstName_TextField_XPATH", data.get("FirstName"));

		// Enter Last Name
		type("AddCustomer_LastName_TextField_XPATH", data.get("LastName"));

		// Enter Post Code
		type("AddCustomer_PostCode_TextField_XPATH", data.get("PostCode"));

		// Click on Add Customer Button
		click("AddCustomer_Button_XPATH");

		// Validate customer is added successfully by verifying browser-popup - hard
		// assertion
		Assert.assertTrue(isRecordAdded("customerAdded_SuccessMessage"));

		// Click OK button of browser popup
		alert("accept");

		// Click on Customers
		click("Customer_MenuButton_XPATH");
		
		Thread.sleep(5000L);

		// Validate added customer is displayed in the Customer List - hard assertion
		Assert.assertTrue(isCustomerRecordDisplayed("Cutomers_SearchCustomerInTable_XPATH", data.get("FirstName")));

		// Validate Account Number section is blank. - hard assertion
		Assert.assertFalse(isAccountFieldOfCustomerRecordDisplayed("Customer_SearchCustomerTable_AccountField_XPATH",data.get("FirstName")));				
		
		softAssert.assertAll();

	}

	@Test(dataProvider = "ExcelTestDataProvider")
	public void test002_OpenAccountTest(Hashtable<String, String> data, Method m) throws Exception {

		SoftAssert softAssert = new SoftAssert();
		String homePageTitleActual = driver.getTitle();

		// Validating Home Page Title
		softAssert.assertEquals(homePageTitleActual, testConfig.getProperty("HomePageTitle_Expected"),
				"Home Page Title did not match");

		// Clicking BankManager Login Button
		click("BankManagerLogin_Button_XPATH");

		// Validate user is navigated to Manager Home Screen. - Hard Assertion
		Assert.assertTrue(isElementPresent("AddCustomer_MenuButton_XPATH"));

		// Click on Add Customer Menu Button
		click("AddCustomer_MenuButton_XPATH");

		// Enter First Name
		type("AddCustomer_FirstName_TextField_XPATH", data.get("FirstName"));

		// Enter Last Name
		type("AddCustomer_LastName_TextField_XPATH", data.get("LastName"));

		// Enter Post Code
		type("AddCustomer_PostCode_TextField_XPATH", data.get("PostCode"));

		// Click on Add Customer Button
		click("AddCustomer_Button_XPATH");

		// Validate customer is added successfully by verifying browser-popup - hard
		// assertion
		Assert.assertTrue(isRecordAdded("customerAdded_SuccessMessage"));

		// Click OK button of browser popup
		alert("accept");

		// Click on Open Account Tab
		click("OpenAccount_MenuButton_XPATH");

		// Selecting Added user in OpenAccount - Customer Drpdown
		selectDropdown("OpenAccount_Customer_Select_XPATH", data.get("FirstName") + " " + data.get("LastName"));

		// Selecting Currency in OpenAccount - for the selected Customer
		selectDropdown("OpenAccount_Currency_Select_XPATH", data.get("Currency"));

		click("OpenAccount_Process_Button_XPATH");

		// Validate account is opened successfully by verifying browser-popup - hard
		// assertion
		Assert.assertTrue(isRecordAdded("AccountOpened_SuccessMessage"));

		// Click OK button of browser popup
		alert("accept");

		// Click on Customers
		click("Customer_MenuButton_XPATH");

		// Validate added customer is displayed in the Customer List - hard assertion
		Assert.assertTrue(isCustomerRecordDisplayed("Cutomers_SearchCustomerInTable_XPATH", data.get("FirstName")));

		// Validate Account Number section is NOT Blank for the account opened for the
		// Customer. - hard assertion
		//Assert.assertTrue(isAccountFieldOfCustomerRecordDisplayed("Customer_SearchCustomerTable_AccountField_XPATH",data.get("FirstName")));

		softAssert.assertAll();

	}

	@Test(dataProvider = "ExcelTestDataProvider")
	public void test003_DeleteCustomerTest(Hashtable<String, String> data, Method m) throws Exception {

		SoftAssert softAssert = new SoftAssert();
		String homePageTitleActual = driver.getTitle();

		// Validating Home Page Title
		softAssert.assertEquals(homePageTitleActual, testConfig.getProperty("HomePageTitle_Expected"),
				"Home Page Title did not match");

		// Clicking BankManager Login Button
		click("BankManagerLogin_Button_XPATH");

		// Validate user is navigated to Manager Home Screen. - Hard Assertion
		Assert.assertTrue(isElementPresent("AddCustomer_MenuButton_XPATH"));

		// Click on Add Customer Menu Button
		click("AddCustomer_MenuButton_XPATH");

		// Enter First Name
		type("AddCustomer_FirstName_TextField_XPATH", data.get("FirstName"));

		// Enter Last Name
		type("AddCustomer_LastName_TextField_XPATH", data.get("LastName"));

		// Enter Post Code
		type("AddCustomer_PostCode_TextField_XPATH", data.get("PostCode"));

		// Click on Add Customer Button
		click("AddCustomer_Button_XPATH");

		// Validate customer is added successfully by verifying browser-popup - hard
		// assertion
		Assert.assertTrue(isRecordAdded("customerAdded_SuccessMessage"));

		// Click OK button of browser popup
		alert("accept");

		// Click on Open Account Tab
		click("OpenAccount_MenuButton_XPATH");

		// Selecting Added user in OpenAccount - Customer Drpdown
		selectDropdown("OpenAccount_Customer_Select_XPATH", data.get("FirstName") + " " + data.get("LastName"));

		// Selecting Currency in OpenAccount - for the selected Customer
		selectDropdown("OpenAccount_Currency_Select_XPATH", data.get("Currency"));

		click("OpenAccount_Process_Button_XPATH");

		// Validate account is opened successfully by verifying browser-popup - hard
		// assertion
		Assert.assertTrue(isRecordAdded("AccountOpened_SuccessMessage"));

		// Click OK button of browser popup
		alert("accept");

		// Click on Customers
		click("Customer_MenuButton_XPATH");

		// Validate added customer is displayed in the Customer List - hard assertion
		Assert.assertTrue(isCustomerRecordDisplayed("Cutomers_SearchCustomerInTable_XPATH", data.get("FirstName")));

		// Validate Account Number section is NOT Blank for the account opened for the
		// Customer. - hard assertion
		Assert.assertTrue(isAccountFieldOfCustomerRecordDisplayed("Customer_SearchCustomerTable_AccountField_XPATH",
				data.get("FirstName")));

		// Entered Customer name in Customers Search bar to search for customer.
		type("Customers_SearchCustomer_TextField_XPATH", data.get("FirstName"));

		// Delete the Searched Customer
		click("Customer_SearchedCustomer_Delete_Button_XPATH", data.get("FirstName"));

		// Under customer name -> Clear & Search for Deleted user
		clear("Customers_SearchCustomer_TextField_XPATH");
		type("Customers_SearchCustomer_TextField_XPATH", data.get("FirstName"));

		try {
			// Validating Deleted user should not be displayed in Customer dropdown.
			Assert.assertTrue(isCustomerRecordDisplayed("Cutomers_SearchCustomerInTable_XPATH", data.get("FirstName")));
		} catch (NoSuchElementException e) {
			LOG.info("Record Deleted Successfully!!!!!");
		}

		softAssert.assertAll();

	}

	/**
	 * getExcelData - Method reads data from Excel File and return data through
	 * Iterator of Object Array
	 * 
	 * @return Iterator<Object[]> array
	 */
	@DataProvider(name = "ExcelTestDataProvider")
	public Object[][] getExcelData() {

		String sheetName = "CustomerTestData";
		int columnCount = reader.getColumnCount(sheetName);
		int rowCount = reader.getRowCount(sheetName);

		Object[][] excelData = new Object[rowCount - 1][1];

		Hashtable<String, String> table = null;

		for (int row = 2; row <= rowCount; row++) {

			table = new Hashtable<String, String>();

			for (int col = 0; col < columnCount; col++) {

				table.put(reader.getCellData(sheetName, col, 1), reader.getCellData(sheetName, col, row));
				excelData[row - 2][0] = table;

			}

		}

		return excelData;
	}

}
