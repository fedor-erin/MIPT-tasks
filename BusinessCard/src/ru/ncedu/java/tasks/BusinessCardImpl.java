package ru.ncedu.java.tasks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BusinessCardImpl implements BusinessCard {

	private String name = "";
	private String lastName = "";
	private String department = "";
	private String dateOfBirth = "";
	private char gender = 'm';
	private int salary = 0;
	private String phoneNumber = "";
	
	@Override
	public BusinessCard getBusinessCard(Scanner scanner) {
		String buffer = scanner.nextLine();
		String[] parts = buffer.split(";");
		if(parts.length != 7) {
			throw new NoSuchElementException();
		}
		name = parts[0];
		lastName = parts[1];
		department = parts[2];
		dateOfBirth = parts[3];
		try {
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			Date first = new Date();
			first = format.parse(dateOfBirth);
		} catch(ParseException e) {
			throw new InputMismatchException();
		}
		gender = parts[4].charAt(0);
		if(gender != 'M' && gender != 'F') {
			throw new InputMismatchException();
		}
		try {
			salary = Integer.parseInt(parts[5]);
		} catch(NumberFormatException e) {
			throw new InputMismatchException();
		}
		phoneNumber = parts[6];
		try {
			Double.parseDouble(phoneNumber);
		} catch(NumberFormatException e) {
			throw new InputMismatchException();
		}
		return this;
	}

	@Override
	public String getEmployee() {
		return name + " " + lastName;
	}

	@Override
	public String getDepartment() {
		return department;
	}

	@Override
	public int getSalary() {
		return salary;
	}

	@Override
	public int getAge() {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date first = new Date();
		Date last = new Date();
		try {
			first = format.parse(dateOfBirth);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    Calendar a = Calendar.getInstance();
	    Calendar b = Calendar.getInstance();
		a.setTime(first);
	    b.setTime(last);
	    int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
	    if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) || 
	        (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
	        diff--;
	    }
		return diff;
	}

	@Override
	public String getGender() {
		if(gender == 'M') {
			return "Male";
		}
		if(gender == 'F') {
			return "Female";
		}
		return "";
	}

	@Override
	public String getPhoneNumber() {
		return "+7 "+phoneNumber.substring(0, 3)+"-"+phoneNumber.substring(3, 6)+
				"-"+phoneNumber.substring(6, 8)+"-"+phoneNumber.substring(8, 10);
	}
}