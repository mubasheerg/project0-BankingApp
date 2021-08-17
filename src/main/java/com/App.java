package com;

import java.util.InputMismatchException;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.project0.model.Employee;

public class App {

	static String name;

	public static void main(String[] args) {
		Logger logger = Logger.getLogger("App");
		logger.info("\nBanking Mangement");
		Scanner sc = new Scanner(System.in);
		System.out.println("Please Enter Your Name:");
		name = sc.nextLine();
		startApp();
	}

	public static void startApp() throws InputMismatchException {
		Logger logger = Logger.getLogger("App");
		Scanner sc = new Scanner(System.in);
		logger.info("\nWelcome," + name);
		try {
			while (true) {
				int choice = 0;
				System.out.println();
				System.out.println("1.Create Account");
				System.out.println("2.Login");
				System.out.println("3.About us");
				System.out.println("4.About Developer");
				System.out.println("5.Exit ");
				System.out.println("Enter Your Choice");
				choice = sc.nextInt();
				switch (choice) {
				case 1:
					// to create account of any type(E/C)
					logger.info("Enter your Account type to create");
					logger.info("\n(E for Employee/C for Customer)");
					String type = sc.next();
					if (type.equals("C") || type.equals("c")) {
						CustomerApp customer = new CustomerApp();
						customer.startCustomer();
					} else if (type.equals("E") || type.equals("e")) {
						EmployeeApp employee = new EmployeeApp();
						employee.startEmployee();
					} else {
						logger.error("Invalid Type : " + type + "Enter valid type");
					}
					break;

				case 2:// to Login account of any type(E/C)
					logger.info("Enter your Account type to create");
					logger.info("\n(E for Employee/C for Customer)");
					type = sc.next();
					if (type.equals("C") || type.equals("c")) {
						CustomerApp customer = new CustomerApp();
						customer.startCustomerLogin();
					} else if (type.equals("E") || type.equals("e")) {
						EmployeeApp employee = new EmployeeApp();
						employee.startEmlpoyeeLogin();
					} else {
						logger.error("Invalid Type : " + type);
					}
					break;

				case 3:// to know more about bank
					System.out.println(
							"A bank is a financial institution licensed to receive deposits and make loans. Banks may also provide financial services such as wealth management, currency exchange, and safe deposit boxes. There are several different kinds of banks including retail banks, commercial or corporate banks, and investment banks.");
					break;
				case 4:// to Know more about developers
					System.out.println("Mubasheer G,a trainee who practices to become a Java full stack developer");
					break;
				case 5:
					logger.info("Thanks for using this APP :)");
					System.exit(0);

				default:
					logger.error("Invalid Option : " + choice);
					break;
				}
			}
		} catch (InputMismatchException e1) {
			System.out.println("Input Mismatch!!- Enter Correct Input format");
			startApp();
		}
	}
}
