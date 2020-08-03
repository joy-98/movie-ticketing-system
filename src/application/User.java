package application;

import java.io.IOException;
import java.io.Serializable;

public class User implements Serializable {
	//Declaring all the variable
	private static final long serialVersionUID = 8649739609810900325L;
	public int id;
	private String name;
	public String email;
	private String password;
	private String ic;
	private String contact;
	private String dob; //date of birth
	private String gender;
	private String Address;
	private String State;
	private String Country;
	private String Postcode;
	
	//Constructor
	User(){
		
	}
	
	User(int id, String name, String email, String password, String ic, String contact, String dob, String gender,
			String Address, String State, String Country, String Postcode) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.ic = ic;
		this.contact = contact;
		this.dob = dob;
		this.gender = gender;
		this.Address = Address;
		this.State = State;
		this.Country = Country;
		this.Postcode = Postcode;
	}
	
	// To check the password in the Login page
	boolean checkPassword(String password) {
		if (password.equals(this.password))
			return true;
		else
			return false;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		UserManager.usermanager.save();
	}

	public String getPassword() {
		
		return password;
		
	}

	public void setPassword(String password) {

		this.password = password;
		UserManager.usermanager.save();

	}

	public String getIc() {
		return ic;
	}

	public void setIc(String ic) {
		this.ic = ic;
		UserManager.usermanager.save();
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
		UserManager.usermanager.save();
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
		UserManager.usermanager.save();
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
		UserManager.usermanager.save();
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
		UserManager.usermanager.save();
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
		UserManager.usermanager.save();
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
		UserManager.usermanager.save();
	}

	public String getPostcode() {
		return Postcode;
	}

	public void setPostcode(String postcode) {
		Postcode = postcode;
		UserManager.usermanager.save();
	}

}
