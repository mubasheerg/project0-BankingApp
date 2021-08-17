package com.revature.project0.dao;

import java.util.List;

import com.revature.project0.exceptions.UserException;
import com.revature.project0.model.Employee;

public interface EmployeeDAO {

		public boolean createAccount(Employee employee);
		public boolean updateAccount(Employee employee);
		public boolean deleteAccount(int AccountNo);
		public List getAccountById(int AccoutNo);
		public Employee validateAccount(int accountNo,String userPassword) throws UserException;
}
