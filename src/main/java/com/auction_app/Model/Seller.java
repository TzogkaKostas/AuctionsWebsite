package com.auction_app.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

@Entity
@Table(name = "seller")
@XmlRootElement(name = "seller")
@XmlAccessorType(XmlAccessType.NONE)
public class Seller {
	@Id
	@XmlAttribute
	private String User_id;
	@XmlAttribute
	private Integer Rating;

	@JsonIgnore
	@XmlTransient
	@OneToOne(fetch = FetchType.LAZY)
    @MapsId
	@JoinColumn(name = "User_id")
    private User user;

	public Seller(String user_id, Integer rating) {
		User_id = user_id;
		Rating = rating;
	}

	public Seller(String user_id) {
		User_id = user_id;
	}

	public Seller() {
	}

	public Seller(String user_id, Integer rating, User user) {
		User_id = user_id;
		Rating = rating;
		this.user = user;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Seller{" +
				"User_id='" + User_id + '\'' +
				", Rating=" + Rating +
				'}';
	}
}
