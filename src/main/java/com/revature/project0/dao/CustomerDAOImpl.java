package com.revature.project0.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.revature.project0.exceptions.InSufficientBalance;
import com.revature.project0.exceptions.MinAmountException;
import com.revature.project0.exceptions.UserException;
import com.revature.project0.model.Customer;
import com.revature.project0.model.Employee;
import com.revature.project0.util.DBConnection;

public class CustomerDAOImpl implements CustomerDAO{
	
	Connection connection=DBConnection.getDBConnection();
	
	Scanner sc=new Scanner(System.in);
	private final String ADD_CUSTOMER_QUERY="insert into bank.customer values(?,?,?,?,?,?)";
	private final String VALIDATE_CUSTOMER_QUERY="select * from bank.Customer where accountno=? and userPassword=?";
	private final String VIEW_CUSTOMER_BALANCE_QUERY="select * from bank.Customer where accountno=?";
	private final String UPDATE_CUSTOMER_QUERY="update bank.Customer set username=?,userpassword=?,phoneno=?,address=?,balance=? where accountno=?";
	private final String DELETE_CUSTOMER_QUERY="delete from bank.Customer where accountno=?";
	private final String VIEW_ACCOUNT_BY_ACCOUNTNO="select * from bank.Customer where accountno=?";
	private final String WITHDRAW_FROM_ACCOUNT="update bank.customer set balance=balance-? where accountno=?";
	private final String DEPOSIT_TO_ACCOUNT="update bank.customer set balance=balance+? where accountno=?";
	//to create a account 
	public boolean createAccount(Customer customer) {
		
		
		int res = 0;
		try {
		PreparedStatement statement=connection.prepareStatement(ADD_CUSTOMER_QUERY);
		statement.setInt(1,customer.getAccountNo());
		statement.setString(2,customer.getUserName());
		statement.setString(3,customer.getUserpassword());
		statement.setString(4,customer.getPhoneNo());
		statement.setString(5,customer.getAddress());
		statement.setInt(6,customer.getBalance());
		res=statement.executeUpdate();
		}
		 catch (SQLException e) {
			
		}
	
		if(res==0)
			return false;
		else
			return true;
	}
	
	public Customer validateAccount(int accountNo,String userPassword) throws UserException {
		
		Customer customer=null;
		ResultSet resultSet=null;
		try {
			PreparedStatement statement=connection.prepareStatement(VALIDATE_CUSTOMER_QUERY);
			statement.setInt(1, accountNo);
			statement.setString(2,userPassword);
			resultSet=statement.executeQuery();
			
			while(resultSet.next())
			{
				customer=new Customer();
				customer.setAccountNo(resultSet.getInt(1));
				customer.setUserName(resultSet.getString(2));
				customer.setUserpassword(resultSet.getString(3));
				customer.setPhoneNo(resultSet.getString(4));
				customer.setAddress(resultSet.getString(5));
				customer.setBalance(resultSet.getInt(6));				
			}
			
				
		}
		catch (SQLException e) {
			
		}
		return customer;
		
	}

	public int viewBalance(int accountNo) {
		int balance=0;
		ResultSet resultSet=null;
		try {
			PreparedStatement statement=connection.prepareStatement(VIEW_CUSTOMER_BALANCE_QUERY);
			statement.setInt(1, accountNo);
			resultSet=statement.executeQuery();
			
			while(resultSet.next())
			{
				balance=resultSet.getInt(6);
			}
		} 
		
		catch (SQLException e) {
			System.out.println("ERROR!!");
		}
		return balance;
	}
	
	public boolean updateAccount(Customer customer) {
		int res=0;
		try {
			
		PreparedStatement statement=connection.prepareStatement(UPDATE_CUSTOMER_QUERY);
		statement.setString(1, customer.getUserName());
		statement.setString(2,customer.getUserpassword());
		statement.setString(3, customer.getPhoneNo());
		statement.setString(4, customer.getAddress());
		statement.setInt(5, customer.getBalance());
		statement.setInt(6, customer.getAccountNo());
		
		res=statement.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println("error");
			
		}
		if(res==0)
			return false;
		else
			return true;
	}

	

	public boolean deleteAccount(int AccountNo) {
		
		
		int res=0;
		try {
			PreparedStatement statement=connection.prepareStatement(DELETE_CUSTOMER_QUERY);
			statement.setInt(1, AccountNo);
			res = statement.executeUpdate();
			}
			 catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(res==0)
			return false;
		else
			return true;
		
	}
	
	
	public List getAccountById(int AccoutNo) {
		
		List<Customer> customers=new ArrayList<Customer>();
		try{
			PreparedStatement statement=connection.prepareStatement(VIEW_ACCOUNT_BY_ACCOUNTNO);
			statement.setInt(1,AccoutNo);
			ResultSet resultSet=statement.executeQuery();	
			while(resultSet.next())
			{
				Customer customer=new Customer();
				customer.setAccountNo(resultSet.getInt(1));
				customer.setUserName(resultSet.getString(2));
				customer.setUserpassword(resultSet.getString(3));
				customer.setPhoneNo(resultSet.getString(4));
				customer.setAddress(resultSet.getString(5));
				customer.setBalance(resultSet.getInt(6));	
				customers.add(customer);
				}	
		}
		 catch (SQLException e) {
				System.out.println("ERROR!!");
			}
		
		return customers;
	}

	public void transferAmount(int senderAccountNo, int recieverAccountNo, int amount) throws InSufficientBalance{
		
		int debitorBalance=0,creditorBalance=0;
		
		int balance=viewBalance(senderAccountNo);
			try {
				if(balance>amount){
				PreparedStatement statement1=connection.prepareStatement("select balance from bank.Customer where accountno=?");
				statement1.setInt(1, senderAccountNo);
				ResultSet set=statement1.executeQuery();
				CallableStatement statement=connection.prepareCall("call bank.transferAmoundAndReturenBalance(?,?,?,?,?)");
				statement.setInt(1,senderAccountNo);
				statement.setInt(2,recieverAccountNo);
				statement.setInt(3,amount);
				statement.registerOutParameter(4, Types.INTEGER);
				statement.setInt(4, debitorBalance);
				statement.registerOutParameter(5, Types.INTEGER);
				statement.setInt(5, creditorBalance);
				statement.execute();
				debitorBalance=statement.getInt(4);
				creditorBalance=statement.getInt(5);
				System.out.println("Debitor BALANCE : " +debitorBalance);
				System.out.println("Creditor BALANCE : " +creditorBalance);
				}
				else
					throw new InSufficientBalance("InSufficient Balance!! to send the amount , your balance "+balance);
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
			
		} 
		
	public void withdraw(int accountNo, int amount) throws InSufficientBalance, MinAmountException  {
		
		int balance=viewBalance(accountNo);
		if(balance>=amount)
		{
			if(balance<=500)
			{
				throw new MinAmountException("Withdrawn Denied!! Minimum Amount in your account should be 500 and Your balance is:"+balance);
			}
			
			try {
				PreparedStatement statement=connection.prepareStatement(WITHDRAW_FROM_ACCOUNT);
				statement.setInt(1, amount);
				statement.setInt(2, accountNo);	
				statement.executeUpdate();
				balance=viewBalance(accountNo);
				System.out.println("Withdrawan successfully!!, Avaliable balance :"+balance);
				} 
			catch (SQLException e) {
				e.printStackTrace();
			}
			
			
		}
		else
			throw new InSufficientBalance("InSufficient Balance!! to withdraw, your balalnce is:"+balance);
		
	}
	
	
	public void deposit(int accountNo, int amount) {
		
		try {
			PreparedStatement statement=connection.prepareStatement(DEPOSIT_TO_ACCOUNT);
			statement.setInt(1, amount);
			statement.setInt(2, accountNo);	
			statement.executeUpdate();
			int balance=viewBalance(accountNo);
			System.out.println("Deposited successfully!!, Avaliable balance :"+balance);
			} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public  boolean isAccountExists(int accountNo)
	{
		boolean result=false;
		try {
			PreparedStatement statement=connection.prepareStatement(VIEW_ACCOUNT_BY_ACCOUNTNO);
			statement.setInt(1,accountNo);
			ResultSet resultSet=statement.executeQuery();
			if(resultSet.next())
				result=true;
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
		
	}

	
	

	


	

	
}
