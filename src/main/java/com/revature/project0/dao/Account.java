package com.revature.project0.dao;

import java.util.List;

import com.revature.project0.exceptions.UserException;
import com.revature.project0.model.Customer;

public interface Account {

	public boolean createAccount(Customer customer);
	public boolean updateAccount(Customer customer);
	public boolean deleteAccount(int AccountNo);
	public List getAccountById(int AccoutNo);
	public Customer validateAccount(int accountNo,String userPassword) throws UserException;
}
