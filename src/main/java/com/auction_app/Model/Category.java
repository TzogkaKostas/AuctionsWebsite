package com.auction_app.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category")
@XmlRootElement(name = "category")
@XmlAccessorType(XmlAccessType.NONE)
public class Category {

	@Id
	@XmlTransient
	private Integer Category_id;
	@XmlElement
	private String Name;

	@JsonIgnore
	@XmlTransient
	@ManyToMany(mappedBy = "category")
    private Set<Item> item = new HashSet<>();

	public Category(Integer category_id, String name, Set<Item> item) {
		Category_id = category_id;
		Name = name;
		this.item = item;
	}

	public Category() {
	}

	public Category(Integer category_id) {
		Category_id = category_id;
	}

	public Integer getCategory_id() {
		return Category_id;
	}

	public void setCategory_id(Integer category_id) {
		Category_id = category_id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}


	@Override
	public String toString() {
		return "Category{" +
				"Category_id=" + Category_id +
				", Name='" + Name + '\'' +
				'}';
	}
}