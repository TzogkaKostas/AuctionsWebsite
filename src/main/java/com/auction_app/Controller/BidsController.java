package com.auction_app.Controller;

import com.auction_app.Repository.BidRepository;
import com.auction_app.Repository.BidderRepository;
import com.auction_app.Repository.ItemRepository;
import com.auction_app.Model.Bid;
import com.auction_app.Model.Bidder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.StringReader;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.lang.Double.max;

@RestController
@CrossOrigin
public class BidsController {
	@Autowired
	private BidRepository bidRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private BidderRepository bidderRepository;

	private Double bid_value = 0.6;

	@GetMapping(value = "/get_bids")
	public List<Bid> handle_get_bids() {
		return bidRepository.findAll();
	}

	//Create a bid for a specific item
	@PostMapping(value = "/save_bid")
	public ResponseEntity handle_save_bid(@RequestBody String json_string) {
		JsonObject json = Json.createReader(new StringReader(json_string)).readObject();

		Double amount = json.getJsonNumber("amount").doubleValue();
		String user_id = json.getString("user_id");
		Integer item_id = json.getJsonNumber("itemID").intValue();

		Double currently = itemRepository.get_item_currently(json.getJsonNumber("itemID").intValue());

		if (currently != null) {
			if (amount <= currently) {
				return new ResponseEntity(HttpStatus.UNAUTHORIZED);
			}
		}

		Optional<Bidder> found_bidder = bidderRepository.findById(user_id);
		if( !found_bidder.isPresent() ) {
			bidderRepository.save_bidder(user_id);
		}

		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);

		System.out.println("user_id:" + user_id);
		System.out.println("ts:" + ts);
		System.out.println("amount:" + amount);

			bidRepository.save_bid(user_id, ts, amount);
		bidRepository.save_item_has_bid(item_id, user_id, ts);

		itemRepository.set_item_currently(item_id, amount);
		itemRepository.set_item_last_bidder(item_id, user_id);
		itemRepository.increase_item_num_of_bids(item_id);

		set_item_bade_by_user(item_id, user_id);

		return new ResponseEntity(HttpStatus.OK);
	}

	//save that a user made a bid for a item
	//this is needed for the recommendation system
	 public void set_item_bade_by_user(@RequestParam(value = "itemID") Integer itemID,
                                       @RequestParam(value = "user_id") String user_id) {
    	Integer count = itemRepository.get_item_ratings_by_user_count(itemID, user_id);
    	if (count == 0) {
    		itemRepository.save_item_rating_by_user(itemID, user_id, bid_value);
	    }
    	else {
    		Double rating = itemRepository.get_item_rating_by_user(itemID, user_id);
    		if (rating == null) rating = 0.0;
			itemRepository.set_item_rating_by_user(itemID, user_id, max(bid_value, rating));
	    }
    }

	@DeleteMapping(value = "/delete_bid")
	public void handle_delete_bid(@RequestParam(value = "id") String id) {
		bidRepository.deleteById(id);
	}

	@GetMapping(value = "/get_bidders")
	public List<Bidder> handle_get_bidders() {
		return bidderRepository.findAll();
	}

	@PostMapping(value = "save_bidder")
	public void handle_save_bidder(@RequestBody Bidder bidder) {
		bidderRepository.save(bidder);
	}

}