package com.auction_app.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "bid", indexes = { @Index(name = "PRIMARY", columnList = "User_id, Time") })
@IdClass(BidId.class)
@XmlRootElement(name = "bid")
@XmlAccessorType(XmlAccessType.NONE)
public class Bid {
	@Id
	@XmlTransient
	private String User_id;
	@Id
	@XmlElement
	private String Time;
	@XmlElement
	private Double Amount;


	@JsonIgnore
	@XmlTransient
	@ManyToMany(mappedBy = "bid")
    private Set<Item> item = new HashSet<>();

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
    @MapsId
	@JoinColumn(name = "User_id", foreignKey=@ForeignKey(name = "fk_bid_bidder"))
	@XmlElement
	private Bidder bidder;


	public Bid() {

	}

	public Bid(String user_id, Double amount) {
		User_id = user_id;
		Amount = amount;
	}

	public Bid(String user_id, String time, Double amount, Set<Item> item, Bidder bidder) {
		User_id = user_id;
		Time = time;
		Amount = amount;
		this.item = item;
		this.bidder = bidder;
	}

	public String getUser_id() {
		return User_id;
	}

	public void setUser_id(String user_id) {
		User_id = user_id;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

	public Double getAmount() {
		return Amount;
	}

	public void setAmount(Double amount) {
		Amount = amount;
	}

	public Set<Item> getItem() {
		return item;
	}

	public void setItem(Set<Item> item) {
		this.item = item;
	}

	public Bidder getBidder() {
		return bidder;
	}

	public void setBidder(Bidder bidder) {
		this.bidder = bidder;
	}

	@Override
	public String toString() {
		return "Bid{" +
				"Time='" + Time + '\'' +
				", Amount=" + Amount +
				", bidder=" + bidder +
				'}';
	}
}
