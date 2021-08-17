package com.revature.project0.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.project0.exceptions.UserException;
import com.revature.project0.model.*;
import com.revature.project0.util.DBConnection;
public class EmployeeDAOImpl implements EmployeeDAO{

	Connection connection=DBConnection.getDBConnection();
	private final String ADD_EMPLOYEE_QUERY="insert into bank.employee values(?,?,?,?,?)";
	private final String VIEW_EMPLOYEE_BY_EMPLOYEEID="select * from bank.employee where employeeId=?";
	private final String VALIDATE_EMPLOYEE_QUERY="select * from bank.employee where employeeId=? and employeepassword=?";
	private final String UPDATE_EMPLOYEE_QUERY="update bank.employee set employeename=?,employeepassword=?,phoneno=?,address=? where employeeId=?";
	private final String DELETE_EMPLOYEE_QUERY="delete from bank.employee where employeeId=?";
	private final String VIEW_ALL_CUSTOMERS="select * from bank.customer";
	
	Employee employee=null;
	//TO CHECK THE EXISTENCE OF ACCOUNT
	public boolean isEmployeeExists(int employeeId) {
		
		boolean result=false;
		try {
			PreparedStatement statement=connection.prepareStatement(VIEW_EMPLOYEE_BY_EMPLOYEEID);
			statement.setInt(1, employeeId);
			ResultSet resultSet=statement.executeQuery();
			if(resultSet.next())
				result=true;
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
		
	}

	public boolean createAccount(Employee employee) {
		int res = 0;
		try {
		PreparedStatement statement=connection.prepareStatement(ADD_EMPLOYEE_QUERY);
		statement.setInt(1,employee.getemployeeId());
		statement.setString(2,employee.getemployeeName());
		statement.setString(3,employee.getemployeePassword());
		statement.setString(4,employee.getPhoneNo());
		statement.setString(5,employee.getAddress());
		res=statement.executeUpdate();
		}
		 catch (SQLException e) {
			
		}
		if(res==0)
			return false;
		else
			return true;
		
	}
	//validating employee account againt id and pass
	public Employee validateAccount(int employeeId, String employeePassword) throws UserException {
		
		
		ResultSet resultSet=null;
		try {
			PreparedStatement statement=connection.prepareStatement(VALIDATE_EMPLOYEE_QUERY);
			statement.setInt(1,employeeId );
			statement.setString(2,employeePassword);
			resultSet=statement.executeQuery();
			
			while(resultSet.next())
			{
				employee=new Employee();
				employee.setemployeeId(resultSet.getInt(1));
				employee.setemployeeName(resultSet.getString(2));
				employee.setemployeePassword(resultSet.getString(3));
				employee.setPhoneNo(resultSet.getString(4));
				employee.setAddress(resultSet.getString(5));			
			}
			
				
		}
		catch (SQLException e) {
			
		}
		return employee;
		
	}

	//to view the particular account by employeeid
	public List getAccountById(int employeeId) {
		
		List<Employee> employees=new ArrayList<Employee>();
		try{
			PreparedStatement statement=connection.prepareStatement(VIEW_EMPLOYEE_BY_EMPLOYEEID);
			statement.setInt(1,employeeId);
			ResultSet resultSet=statement.executeQuery();	
			while(resultSet.next())
			{
				Employee employee=new Employee();
				employee.setemployeeId(resultSet.getInt(1));
				employee.setemployeeName(resultSet.getString(2));
				employee.setemployeePassword(resultSet.getString(3));
				employee.setPhoneNo(resultSet.getString(4));
				employee.setAddress(resultSet.getString(5));	
				employees.add(employee);
				}	
		}
		 catch (SQLException e) {
				System.out.println("ERROR!!");
			}
		
		return employees;
		
	}


	public boolean updateAccount(Employee employee) {
		
		int res=0;
		try {
			
		PreparedStatement statement=connection.prepareStatement(UPDATE_EMPLOYEE_QUERY);
		statement.setString(1, employee.getemployeeName());
		statement.setString(2,employee.getemployeePassword());
		statement.setString(3, employee.getPhoneNo());
		statement.setString(4, employee.getAddress());
		statement.setInt(5, employee.getemployeeId());
		
		res=statement.executeUpdate();
		}
		catch (SQLException e) {
			
		}
		if(res==0)
			return false;
		else
			return true;
	}

	public boolean deleteAccount(int employeeId) {
		
		int res=0;
		try {
			PreparedStatement statement=connection.prepareStatement(DELETE_EMPLOYEE_QUERY);
			statement.setInt(1, employeeId);
			res = statement.executeUpdate();
			}
			 catch (SQLException e) {
				e.printStackTrace();
			}
		if(res==0)
			return false;
		else
			return true;
	}

	public List<Customer> viewAllCustomers() {
		
		List<Customer> customers=new ArrayList<Customer>();
		Customer customer=null;
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet res=statement.executeQuery(VIEW_ALL_CUSTOMERS);
			while(res.next())
			{
				customer=new Customer();
				customer.setAccountNo(res.getInt(1));
				customer.setUserName(res.getString(2));
				customer.setUserpassword(res.getString(3));
				customer.setPhoneNo(res.getString(4));
				customer.setAddress(res.getString(5));
				customer.setBalance(res.getInt(6));
				customers.add(customer);
			}
		}
		catch (SQLException e) {
			
			e.printStackTrace();
		}
			
			return customers;
		
	}

	
	


}
