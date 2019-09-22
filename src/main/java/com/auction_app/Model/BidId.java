package com.auction_app.Model;

import java.io.Serializable;
import java.util.Objects;

public class BidId implements Serializable {
	private String User_id;
    private String Time;

    public BidId() {
	}

	public BidId(String user_id, String time) {
		User_id = user_id;
		Time = time;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BidId bidId = (BidId) o;
		return Objects.equals(User_id, bidId.User_id) &&
				Objects.equals(Time, bidId.Time);
	}

	@Override
	public int hashCode() {
		return Objects.hash(User_id, Time);
	}
}
