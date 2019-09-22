package com.auction_app.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @Id
    private String User_id;
    private String Password;
    private String Name;
    private String Surname;
    private String Email;
    private String Telephone;
    private String Address;
    private Double Latitude;
    private Double Longitude;
    private String AFM;

    @JsonIgnore
	@XmlTransient
	@ManyToMany(mappedBy = "user")
    private Set<Item> item = new HashSet<>();

    private boolean admin_flag = false;
    private boolean approved_flag = false;

	public User(String user_id, String password, String name, String surname,
	            String email, String telephone, String address, Double latitude,
	            Double longitude, String AFM) {
		User_id = user_id;
		Password = password;
		Name = name;
		Surname = surname;
		Email = email;
		Telephone = telephone;
		Address = address;
		Latitude = latitude;
		Longitude = longitude;
		this.AFM = AFM;
	}

	public User() {
	}

	public String getUser_id() {
		return User_id;
	}

	public void setUser_id(String user_id) {
		User_id = user_id;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getSurname() {
		return Surname;
	}

	public void setSurname(String surname) {
		Surname = surname;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getTelephone() {
		return Telephone;
	}

	public void setTelephone(String telephone) {
		Telephone = telephone;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public Double getLatitude() {
		return Latitude;
	}

	public void setLatitude(Double latitude) {
		Latitude = latitude;
	}

	public String getAFM() {
		return AFM;
	}

	public void setAFM(String AFM) {
		this.AFM = AFM;
	}

	public Double getLongitude() {
		return Longitude;
	}

	public void setLongitude(Double longitude) {
		Longitude = longitude;
	}

	public boolean isAdmin_flag() {
		return admin_flag;
	}

	public boolean getAdmin_flag() {return admin_flag;}

	public void setAdmin_flag(boolean admin_flag) {
		this.admin_flag = admin_flag;
	}

	public boolean isApproved_flag() {
		return approved_flag;
	}

	public boolean getApproved_flag() {return approved_flag;}

	public void setApproved_flag(boolean approved_flag) {
		this.approved_flag = approved_flag;
	}
}