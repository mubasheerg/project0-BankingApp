package com.revature.pms.dao;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.project0.dao.CustomerDAOImpl;
import com.revature.project0.dao.EmployeeDAOImpl;
import com.revature.project0.exceptions.InSufficientBalance;
import com.revature.project0.exceptions.MinAmountException;
import com.revature.project0.model.Customer;
import com.revature.project0.util.DBConnection;

public class CustomerDAOImplTest {

	CustomerDAOImpl customerDAOImpl;
	EmployeeDAOImpl employeeDAOImpl;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

	}

	@Before
	public void setUp() throws Exception {
		customerDAOImpl = new CustomerDAOImpl();
		employeeDAOImpl = new EmployeeDAOImpl();

	}

	@After
	public void tearDown() throws Exception {
		customerDAOImpl = null;
		employeeDAOImpl = null;
	}

	@Test
	public void testCreateAccount() {
		boolean res;
		int accountNo = -1;
		List<Customer> original = employeeDAOImpl.viewAllCustomers();
		Customer customer = new Customer(accountNo, "demoEmployee", "demoEmp@123", "demoAdd", "9791843028", 2000);
		res = customerDAOImpl.createAccount(customer);
		List<Customer> afteradded = employeeDAOImpl.viewAllCustomers();
		assertEquals((original.size() + 1), afteradded.size());
		res = customerDAOImpl.deleteAccount(accountNo);
	}

	@Test
	public void testViewBalance() {

		boolean res;
		int expectedValue = 2000, balance, accountNo = -1;
		Customer customer = new Customer(accountNo, "demoEmployee", "demoEmp@123", "demoAdd", "9791843028", 2000);
		res = customerDAOImpl.createAccount(customer);

		balance = customerDAOImpl.viewBalance(accountNo);

		assertEquals(expectedValue, balance);
		res = customerDAOImpl.deleteAccount(accountNo);

	}

	@Test
	public void testUpdateAccount() {

		boolean res;
		int accountNo = -1;
		String phoneno1 = "9488026154";
		Customer customer = new Customer(accountNo, "demoEmployee", "demoEmp@123", "demoAdd", "9791843028", 2000);
		res = customerDAOImpl.createAccount(customer);
		customer = new Customer(accountNo, "Demo", "demo@123", "demo", phoneno1, 2000);
		res = customerDAOImpl.updateAccount(customer);
		List<Customer> customer1 = customerDAOImpl.getAccountById(accountNo);
		customer = customer1.get(0);
		String phoneno2 = customer.getPhoneNo();
		assertEquals(phoneno1, phoneno2);
		res = customerDAOImpl.deleteAccount(accountNo);
	}

	@Test
	public void testDeleteAccount() {

		boolean res;
		int accountNo = -1;
		List<Customer> afteradded = employeeDAOImpl.viewAllCustomers();
		Customer customer = new Customer(accountNo, "demoEmployee", "demoEmp@123", "demoAdd", "9791843028", 2000);
		res = customerDAOImpl.createAccount(customer);
		res = customerDAOImpl.deleteAccount(accountNo);
		List<Customer> afterdeleted = employeeDAOImpl.viewAllCustomers();
		assertEquals(afteradded.size(), afterdeleted.size());

	}

	@Test
	public void testGetAccountById() {

		boolean res;
		int accountNo = -1;
		Customer customer = new Customer(accountNo, "demoEmployee", "demoEmp@123", "demoAdd", "9791843028", 2000);
		res = customerDAOImpl.createAccount(customer);

		List<Customer> customer1 = customerDAOImpl.getAccountById(accountNo);
		Customer a = customer1.get(0);

		assertEquals(a.getAccountNo(), -1);
		assertEquals(a.getUserName(), "Demo");
		res = customerDAOImpl.deleteAccount(accountNo);
	}

	@Test
	public void testTransferAmount() {

		boolean res;
		int accountNo1 = -1, accountNo2 = -2, expectedvalue = 50 + 100;
		// debit account
		Customer customer = new Customer(accountNo1, "demoEmployee", "demoEmp@123", "demoAdd", "9791843028", 100);
		res = customerDAOImpl.createAccount(customer);
		// credit account
		customer = new Customer(accountNo2, "demoEmployee", "demoEmp@123", "demoAdd", "9791843028", 50);
		res = customerDAOImpl.createAccount(customer);

		// transfer fund
		try {
			customerDAOImpl.transferAmount(accountNo1, accountNo2, 2000);
		} catch (InSufficientBalance e) {
			e.printStackTrace();
		}
		int actualvalue = customerDAOImpl.viewBalance(accountNo1) + customerDAOImpl.viewBalance(accountNo2);
		assertEquals(expectedvalue, actualvalue);
		res = customerDAOImpl.deleteAccount(accountNo1);
		res = customerDAOImpl.deleteAccount(accountNo2);

	}

	@Test
	public void testWithdraw() throws InSufficientBalance, MinAmountException {

		boolean res;
		int actualValue = 1500, balance, accountNo = -1;
		Customer customer = new Customer(accountNo, "demoEmployee", "demoEmp@123", "demoAdd", "9791843028", 2000);
		res = customerDAOImpl.createAccount(customer);

		customerDAOImpl.withdraw(accountNo, 500);

		balance = customerDAOImpl.viewBalance(accountNo);

		assertEquals(balance, actualValue);
		res = customerDAOImpl.deleteAccount(accountNo);
	}

}