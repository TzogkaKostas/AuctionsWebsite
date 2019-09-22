package com.auction_app.Model;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.sql.Timestamp;
import java.util.*;

@Entity
@Table(name = "item")
@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.NONE)
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@XmlAttribute
	private Integer ItemID;
	@XmlElement
	private String Name;
	@XmlElement
	private Double Currently;
	@XmlElement
	private String Last_Bidder;
	@XmlElement
	private Double Buy_Price;
	@XmlElement
	private Double First_Bid;
	@XmlElement
	private Integer Number_of_Bids;
    @Basic
	@XmlElement
	private java.sql.Timestamp Started;
    @Basic
	@XmlElement
	private java.sql.Timestamp Ends;
	@XmlElement
	private String Location;
	@XmlElement
	private String Country;
	@XmlElement
	private Double Latitude;
	@XmlElement
	private Double Longitude;
	@XmlElement
	private String Description;

	@JoinColumn(name = "User_id")
    @OneToOne
	@XmlElement
	private Seller seller;

	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(
		name = "item_has_bid",
        joinColumns = {
			@JoinColumn(name = "ItemID") },
        inverseJoinColumns = {
			@JoinColumn(name = "User_id", referencedColumnName="User_id", nullable = false),
			@JoinColumn(name = "Time", referencedColumnName="Time", nullable = false)
		}
	)
	@XmlElementWrapper(name = "bids")
	@XmlElement(name="bid")
	Set<Bid> bid = new HashSet<>();

	@ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "item_has_category",
        joinColumns = { @JoinColumn(name = "ItemID") },
        inverseJoinColumns = { @JoinColumn(name = "Category_id") }
    )
	@XmlElement
	Set <Category> category = new HashSet<>();

	@XmlTransient
	@ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "Ratings",
        joinColumns = {
        	@JoinColumn(name = "ItemID")
        },
		inverseJoinColumns = {
 	        @JoinColumn(name = "User_id", referencedColumnName="User_id", nullable = false),
		}
    )
    Set <User> user = new HashSet<>();

	@XmlTransient
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
    private Set <Image> image = new HashSet<Image>(0);

	public Item(String name, Double currently, Double buy_Price, Double first_Bid,
	            Integer number_of_Bids, Timestamp started, Timestamp ends, String description,
	            Seller seller, Set<Bid> bid, Set<Category> category) {
		Name = name;
		Currently = currently;
		Buy_Price = buy_Price;
		First_Bid = first_Bid;
		Number_of_Bids = number_of_Bids;
		Started = started;
		Ends = ends;
		Description = description;
		this.seller = seller;
		this.bid = bid;
		this.category = category;
	}

	public Item(Integer id, String name, Double currently, String last_Bidder, Double buy_Price, Double first_Bid, Integer number_of_Bids, Timestamp started, Timestamp ends, String location, String country, Double latitude, Double longitude, String description, Seller seller) {
		this.ItemID = id;
		Name = name;
		Currently = currently;
		Last_Bidder = last_Bidder;
		Buy_Price = buy_Price;
		First_Bid = first_Bid;
		Number_of_Bids = number_of_Bids;
		Started = started;
		Ends = ends;
		Location = location;
		Country = country;
		Latitude = latitude;
		Longitude = longitude;
		Description = description;
		this.seller = seller;
	}

	public Item() {
	}


	public Integer getItemID() {
		return ItemID;
	}

	public void setItemID(Integer itemID) {
		ItemID = itemID;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Double getCurrently() {
		return Currently;
	}

	public void setCurrently(Double currently) {
		Currently = currently;
	}

	public String getLast_Bidder() {
		return Last_Bidder;
	}

	public void setLast_Bidder(String last_Bidder) {
		Last_Bidder = last_Bidder;
	}

	public Double getBuy_Price() {
		return Buy_Price;
	}

	public void setBuy_Price(Double buy_Price) {
		Buy_Price = buy_Price;
	}

	public Double getFirst_Bid() {
		return First_Bid;
	}

	public void setFirst_Bid(Double first_Bid) {
		First_Bid = first_Bid;
	}

	public Integer getNumber_of_Bids() {
		return Number_of_Bids;
	}

	public void setNumber_of_Bids(Integer number_of_Bids) {
		Number_of_Bids = number_of_Bids;
	}

	public Timestamp getStarted() {
		return Started;
	}

	public void setStarted(Timestamp started) {
		Started = started;
	}

	public Timestamp getEnds() {
		return Ends;
	}

	public void setEnds(Timestamp ends) {
		Ends = ends;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
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

	public Double getLatitude() {
		return Latitude;
	}

	public void setLatitude(Double latitude) {
		Latitude = latitude;
	}

	public Double getLongitude() {
		return Longitude;
	}

	public void setLongitude(Double longitude) {
		Longitude = longitude;
	}

	public Set<Bid> getBid() {
		return bid;
	}

	public void setBid(Set<Bid> bid) {
		this.bid = bid;
	}

	public Set<Category> getCategory() {
		return category;
	}

	public void setCategory(Set<Category> category) {
		this.category = category;
	}

	public List<Integer> getImage() {
		List id_list = new ArrayList();
		for(Image img: this.image) {
			id_list.add(img.getImage_id());
		}
		return id_list;
	}

	public void setImage(List<Integer> image) {
		for (Integer image_id: image) {
			this.image.add( new Image(image_id));
		}
	}

	public Seller getSeller() {
		return seller;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	@Override
	public String toString() {
		return "Item{" +
				"ItemID=" + ItemID +
				", Name='" + Name + '\'' +
				", Currently=" + Currently +
				", Buy_Price=" + Buy_Price +
				", First_Bid=" + First_Bid +
				", Number_of_Bids=" + Number_of_Bids +
				", Started=" + Started +
				", Ends=" + Ends +
				", Location='" + Location + '\'' +
				", Country='" + Country + '\'' +
				", Latitude='" + Latitude + '\'' +
				", Longitude='" + Longitude + '\'' +
				", Description='" + Description + '\'' +
				", seller=" + seller +
				", bid=" + bid +
				", category=" + category +
				", image=" + image +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Item)) return false;
		Item item = (Item) o;
		return Objects.equals(getItemID(), item.getItemID());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getItemID(), getName(), getCurrently(), getLast_Bidder(), getBuy_Price(), getFirst_Bid(), getNumber_of_Bids(), getStarted(), getEnds(), getLocation(), getCountry(), getLatitude(), getLongitude(), getDescription(), getSeller(), getBid(), getCategory(), user, getImage());
	}
}