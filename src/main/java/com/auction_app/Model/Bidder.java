package com.auction_app.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

@Entity
@Table(name = "bidder")
@XmlRootElement(name = "bidder")
@XmlAccessorType(XmlAccessType.NONE)
public class Bidder {
	@Id
	@XmlAttribute
	private String User_id;
	@XmlAttribute
	private Integer Rating;
	@XmlElement
	private String Location;
	@XmlElement
	private String Country;


	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
    @MapsId
	@JoinColumn(name = "User_id")
	private User user;

	public Bidder() {
	}

	public Bidder(String user_id) {
		this.User_id = user_id;
	}

	public String getUser_id() {
		return User_id;
	}

	public void setUser_id(String user_id) {
		User_id = user_id;
	}

	public Integer getRating() {
		return Rating;
	}

	public void setRating(Integer rating) {
		Rating = rating;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Bidder{" +
				"User_id='" + User_id + '\'' +
				", Rating=" + Rating +
				", Location='" + Location + '\'' +
				", Country='" + Country + '\'' +
				'}';
	}
}

