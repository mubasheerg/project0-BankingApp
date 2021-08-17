package com;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.revature.project0.dao.CustomerDAO;
import com.revature.project0.dao.CustomerDAOImpl;
import com.revature.project0.exceptions.InSufficientBalance;
import com.revature.project0.exceptions.MinAmountException;
import com.revature.project0.exceptions.NegAmountException;
import com.revature.project0.exceptions.UserException;
import com.revature.project0.model.Customer;

import org.apache.log4j.Logger;

public class CustomerApp {

	Logger logger = Logger.getLogger("CustomerApp");
	Customer customer = null;
	CustomerDAO customerDAO = new CustomerDAOImpl();
	CustomerDAOImpl customerDAOImpl = new CustomerDAOImpl();
	Scanner sc = new Scanner(System.in);

	// for creating an account
	public void startCustomer() {
		boolean result = false;
		try {
			customer = acceptCustomerDetails();
			// to check account Existence
			if (customerDAOImpl.isAccountExists(customer.getAccountNo())) {
				throw new UserException(
						"\nSorry " + customer.getUserName() + " your Account is NOT CREATED successfully!!");
			}
			// if account doesn't exists - creating new account
			else {
				result = customerDAO.createAccount(customer);
				logger.info("\n" + customer.getUserName() + ", your bank account opened successfully on " + new Date());
			}
		} catch (MinAmountException e) {
			logger.error("Denied!" + e.getMessage());
		} catch (InputMismatchException e1) {
			logger.error("Input Mismatch!");
		} catch (UserException e) {
			logger.error("Accout Already Exists!!\n" + e.getMessage());
		}
	}

	// to login the account
	public void startCustomerLogin() {
		boolean result = false;
		// login needs account no and password..
		System.out.println("Enter account number:");
		int accountNo = sc.nextInt();
		System.out.println("Enter password:");
		String userpassword = sc.next();
		Customer customer = new Customer();
		try {
			customer = customerDAO.validateAccount(accountNo, userpassword);
			// if there is no customer found
			if (customer == null) {
				throw new UserException("Invalid Name/Password!Come again with correct details");
			}
			logger.info("########## Login Screen ##########");

			// if customer found- performing customer operations(update,delete...)
			customerOperations(customer);
		} catch (UserException e) {
			logger.error(e.getMessage());
		}

	}

	private void customerOperations(Customer customer) {

		// a welcome page for customer- can perform operation as per their requirements
		try {
			logger.info("\nWelcome , " + customer.getUserName());
			logger.info("Personal page for " + customer.getUserName() + "");
			int choice = 0;
			boolean result = false;
			while (true) {

				System.out.println("\n1. View Balance");
				System.out.println("2. Update account");
				System.out.println("3. Transfer amount");
				System.out.println("4. Withdraw");
				System.out.println("5. Deposit");
				System.out.println("6. View Account");
				System.out.println("7. Logout");
				System.out.println("8. Exit");
				choice = sc.nextInt();
				switch (choice) {
				case 1:
					logger.info("\n# Welcome to View Balance");
					int balance = 0;
					// view balance with account no
					balance = customerDAOImpl.viewBalance(customer.getAccountNo());
					logger.info("\nACCOUNT NO:" + customer.getAccountNo());
					logger.info("TOTAL BALANCE:" + balance);

					break;
				case 2:
					logger.info("\n# Welcome to Update Section");
					// updating the account
					customer = getUpdate(customer);

					try {
						if (customerDAOImpl.isAccountExists(customer.getAccountNo())) {
							// update account(as per requirements like name,pass,address..)
							result = customerDAO.updateAccount(customer);
							if (result == true)
								logger.info(customer.getUserName() + " ,Your account Updated Successfully!! on "
										+ new Date());
						} else
							logger.error(customer.getUserName() + " Your account Not Updated");
					} catch (InputMismatchException e1) {
						System.out.println("Input Mismatch!Enter properly");
					}
					break;
				case 3:
					logger.info("\nWelcome to Transfer Amount");
					int senderAccountNo = customer.getAccountNo();
					logger.info("Enter Reciever Account no");
					int recieverAccountNo = sc.nextInt();
					logger.info("Enter Amount to send");
					int amount = sc.nextInt();
					if (customerDAOImpl.isAccountExists(recieverAccountNo)) {
						try {
							if (amount > 0) {
								customerDAOImpl.transferAmount(senderAccountNo, recieverAccountNo, amount);
							} else {
								logger.error("Negavite amount can't be transferd");
							}
						} catch (InSufficientBalance e) {
							logger.error("FUND NOT TRANSFERED ," + e.getMessage());
						}

					} else {
						logger.error("Recevier account with account no: " + recieverAccountNo + ", doesn't exists ");
					}
					break;
				case 4:
					logger.info("\n# Welcome to Withdraw Section");
					logger.info("Enter amount to withdraw");
					amount = sc.nextInt();

					try {
						if (amount > 0) {
							customerDAOImpl.withdraw(customer.getAccountNo(), amount);
						} else {
							throw new NegAmountException("Negative amount cannot be withdrawn!");
						}
					} catch (InSufficientBalance e) {
						logger.error(e.getMessage());
					} catch (MinAmountException e) {
						logger.error(e.getMessage());
					} catch (NegAmountException e) {
						logger.error(e.getMessage());
					}
					break;
				case 5:
					logger.info("\n# Welcome to Deposit Section");
					logger.info("Enter amount to Deposit");
					amount = sc.nextInt();

					if (amount > 0) {
						customerDAOImpl.deposit(customer.getAccountNo(), amount);
					} else {
						try {
							throw new NegAmountException("Negative amount cannot be withdrawn!");
						} catch (NegAmountException e) {
							logger.error(e.getMessage());
						}
					}
					break;
				case 6:
					logger.info("\nWelcome to View Account Section");
					List<Customer> customers = customerDAO.getAccountById(customer.getAccountNo());
					if (customers.isEmpty() == true)
						logger.error("There is No account with " + customer.getAccountNo());
					else
						System.out.println(customers);
					break;

				case 7:
					logger.info("Logged out Successfully!!");
					App app = new App();
					app.startApp();
					break;
				case 8:
					logger.info("\nThank You for using our application!Come back again");
					System.exit(0);
					break;
				default:
					System.out.println("Invalid Option!");
					break;
				}

			}
		} catch (InputMismatchException e) {
			System.out.println("Input Mismatch!Enter Properly");
			customerOperations(customer);
		}

	}

	private Customer getUpdate(Customer customer) throws InputMismatchException {
		int choice = 0;
		Customer customer2 = null;

		while (true) {
			try {
				logger.info("Choose to Update");
				System.out.println("1. User Name");
				System.out.println("2. User Password");
				System.out.println("3. Phone Number");
				System.out.println("4. Address");
				choice = sc.nextInt();
				switch (choice) {
				case 1:
					logger.info("Enter USER NAME to update");
					String name = sc.next();
					name += sc.nextLine();
					customer.setUserName(name);
					return customer;

				case 2:
					logger.info("Enter PASSWORD to update");
					String password = sc.next();
					customer.setUserpassword(password);
					return customer;

				case 3:
					logger.info("Enter PHONE NUMBER to update");
					String phoneno = sc.next();
					customer.setPhoneNo(phoneno);
					return customer;

				case 4:
					logger.info("Enter ADDRESS to update");
					String address = sc.next();
					customer.setAddress(address);
					return customer;
				}
			} catch (InputMismatchException e) {
				System.out.println("Input Mismatch!Enter Correct Input format");
			}
		}
	}

	private Customer acceptCustomerDetails() throws MinAmountException, InputMismatchException {

		System.out.println("Please Enter ACCOUNT NO:");
		int accountNo = sc.nextInt();
		System.out.println("Please Enter Your NAME:");
		String userName = sc.next();
		userName += sc.nextLine();
		System.out.println("Set a password:");
		String userPassword = sc.next();
		while (!userPassword.matches((("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*_]).{6,}")))) {
			System.out.println("Invalid password condition. Set again");
			userPassword = sc.next();
		}
		System.out.println("Confirm Your password by Reentering it");
		String ConfirmPassword = sc.next();
		while (!userPassword.equals(ConfirmPassword)) {
			System.out.println("Password doesn't match");
			ConfirmPassword = sc.next();
		}
		System.out.println("Please Enter your PHONE NUMBER");
		String phoneNo = sc.next();
		System.out.println("Please Enter your ADDRESS");
		String address = sc.next();
		System.out.println("Please Enter Your BALANCE");
		int balance = sc.nextInt();
		while (balance < 500) {
			logger.info("Initial balance should be >500,deposit again");
			balance = sc.nextInt();

			if (balance < 500)
				throw new MinAmountException("Minimum balance to create Your account is 500");
		}
		customer = new Customer(accountNo, userName, userPassword, address, phoneNo, balance);
		return customer;
	}

}
