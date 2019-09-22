package com.auction_app.Model;

import javax.persistence.*;

@Entity
@Table(name = "image")
public class Image {

	@Id
	private Integer Image_id;
	private String Image_path;

	@ManyToOne
	@JoinColumn(name = "ItemID")
    private Item item;


	public Image(Integer Image_id) {
		this.Image_id = Image_id;
	}

	public Image() {
	}

	public String getImage_path() {
		return Image_path;
	}

	public void setImage_path(String image_path) {
		Image_path = image_path;
	}

	public Integer getImage_id() {
		return Image_id;
	}

	public void setImage_id(Integer image_id) {
		Image_id = image_id;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "Image{" +
				"Image_id=" + Image_id +
				", Image_path='" + Image_path + '\'' +
				'}';
	}
}
