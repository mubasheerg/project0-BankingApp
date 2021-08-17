package com.revature.pms.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.project0.dao.CustomerDAOImpl;
import com.revature.project0.dao.EmployeeDAOImpl;
import com.revature.project0.model.Customer;
import com.revature.project0.model.Employee;

public class EmployeeDAOImplTest {

	EmployeeDAOImpl employeeDAOImpl;
	CustomerDAOImpl customerDAOImpl;

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
		employeeDAOImpl = null;
		customerDAOImpl = null;
	}

	@Test
	public void testCreateAccount() {

		boolean res;
		int employeeId = -1;
		Employee employee = new Employee("demoEmployee", "demoEmp@123", "demoAdd", "9791843028", employeeId);
		res = employeeDAOImpl.createAccount(employee);
		List<Employee> employees = employeeDAOImpl.getAccountById(employeeId);
		employee = employees.get(0);
		assertEquals(employeeId, employee.getemployeeId());
		assertEquals("demoemp", employee.getemployeeName());
		employeeDAOImpl.deleteAccount(employeeId);
	}

	@Test
	public void testGetAccountById() {
		boolean res;
		int employeeId = -1;
		Employee employee = new Employee("demoEmployee", "demoEmp@123", "demoAdd", "9791843028", employeeId);
		res = employeeDAOImpl.createAccount(employee);
		List<Employee> employees = employeeDAOImpl.getAccountById(employeeId);
		employee = employees.get(0);
		assertEquals(employeeId, employee.getemployeeId());
		assertEquals("demoemp", employee.getemployeeName());
		employeeDAOImpl.deleteAccount(employeeId);
	}

	@Test
	public void testUpdateAccount() {

		boolean res;
		int employeeId = -1;
		String phoneno1 = "9488026154";
		Employee employee = new Employee("demoEmployee", "demoEmp@123", "demoAdd", "9791843028", employeeId);
		res = employeeDAOImpl.createAccount(employee);

		employee = new Employee("demoEmployee", "demoEmp@123", "demoAdd", phoneno1, employeeId);
		res = employeeDAOImpl.updateAccount(employee);
		List<Employee> employees = employeeDAOImpl.getAccountById(employeeId);
		employee = employees.get(0);
		String phoneno2 = employee.getPhoneNo();
		assertEquals(phoneno1, phoneno2);
		employeeDAOImpl.deleteAccount(employeeId);
	}

	@Test
	public void testDeleteAccount() {

		boolean resut1 = false, result2;
		int employeeId = -1;
		Employee employee = new Employee("demoEmployee", "demoEmp@123", "demoAdd", "9791843028", employeeId);
		result2 = employeeDAOImpl.createAccount(employee);

		employeeDAOImpl.deleteAccount(employeeId);
		result2 = employeeDAOImpl.isEmployeeExists(employeeId);
		assertEquals(resut1, result2);
	}

	@Test
	public void testViewAllCustomers() {

		boolean res;
		int accountNo = -1;
		List<Customer> original = employeeDAOImpl.viewAllCustomers();
		Customer customer = new Customer(accountNo, "demoEmployee", "demoEmp@123", "demoAdd", "9791843028", 100);
		res = customerDAOImpl.createAccount(customer);
		List<Customer> afteradded = employeeDAOImpl.viewAllCustomers();
		assertEquals((original.size() + 1), afteradded.size());
		res = customerDAOImpl.deleteAccount(accountNo);
	}
}
