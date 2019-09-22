package com.auction_app.Model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "message")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer Message_id;
	private String Message;
	@Basic
    private java.sql.Timestamp Created_at;
	private boolean Read_flag;

	public Message(String message, Timestamp created_at, boolean read_flag, String sender_id, String receiver_id, Integer itemID) {
		Message = message;
		Created_at = created_at;
		Read_flag = read_flag;
		this.Sender = new User();
		this.Receiver = new User();

		this.item.setItemID(itemID);
	}

	@JoinColumn(name = "Sender")
    @OneToOne
    private User Sender;

	@JoinColumn(name = "Receiver")
    @OneToOne
    private User Receiver;

	@JoinColumn(name = "ItemID")
    @OneToOne
    private Item item;

	public Message() {
	}

	public Integer getMessage_id() {
		return Message_id;
	}

	public void setMessage_id(Integer message_id) {
		Message_id = message_id;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public Timestamp getCreated_at() {
		return Created_at;
	}

	public void setCreated_at(Timestamp created_at) {
		Created_at = created_at;
	}

	public String getSender() {
		return Sender.getUser_id();
	}

	public void setSender(String sender_id) {
		Sender.setUser_id(sender_id);
	}

	public String getReceiver() {
		return Receiver.getUser_id();
	}

	public void setReceiver(String receiver_id) {
		Receiver.setUser_id(receiver_id);
	}

	public Integer getItem() {
		return item.getItemID();
	}

	public void setItem(Integer itemID) {
		item.setItemID(itemID);
	}

	@Override
	public String toString() {
		return "Message{" +
				"Message_id=" + Message_id +
				", Message='" + Message + '\'' +
				", Created_at=" + Created_at +
				", Read_flag=" + Read_flag +
				", Sender=" + Sender +
				", Receiver=" + Receiver +
				", item=" + item +
				'}';
	}

	public boolean isRead_flag() {
		return Read_flag;
	}

	public void setRead_flag(boolean read_flag) {
		Read_flag = read_flag;
	}
}
