package com.auction_app.Controller;


import com.auction_app.Model.Item;
import com.auction_app.Repository.ItemRepository;
import com.auction_app.Model.Message;
import com.auction_app.Repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.StringReader;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class MessageController {

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private ItemRepository itemRepository;

	@GetMapping(value = "/get_sent_messages")
    public List<Message> handle_get_sent_messages(@RequestParam(value = "id") String user_id) {
		return messageRepository.get_sent_messages(user_id);
    }

	@GetMapping(value = "/get_receiver_messages")
    public List<Message> handle_get_receiver_messages(@RequestParam(value = "id") String user_id) {
		return messageRepository.get_received_messages(user_id);
    }

    //get the
    @GetMapping(value = "/get_item_messages")
    public List<Message> handle_get_item_messages(@RequestParam(value = "itemID") Integer itemID,
                                                  @RequestParam(value = "amount") Integer amount) {
		List<Message> messages = messageRepository.get_item_messages(itemID, amount);
		for (Message msg : messages) {
			msg.setRead_flag(true);
		}
		messageRepository.saveAll(messages);

		return messages;
    }

    @PostMapping(value = "/save_message")
	public Integer handle_save_message(@RequestBody String json_string) {
		JsonObject json = Json.createReader(new StringReader(json_string)).readObject();

		String message_string = json.getString("message");
		Integer itemID = json.getJsonNumber("itemID").intValue();
		String sender = json.getString("sender");
		String receiver;

		Date date= new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);

		System.out.println(json_string);

		Optional<Item> item = itemRepository.findById(itemID);

		System.out.println("item seller: " + item.get().getSeller().getUser_id());
		System.out.println("item bidder: " + item.get().getLast_Bidder());

		if (item.get().getSeller().getUser_id().equals(sender)) {
			receiver = item.get().getLast_Bidder();
		}
		else {
			receiver = item.get().getSeller().getUser_id();
		}

		System.out.println("sender: " + sender);
		System.out.println("receiver: " + receiver);

		messageRepository.save_message(message_string, ts, false, sender, receiver, itemID);

		Integer message_id = messageRepository.get_last_insert_id();
		return message_id;
	}

	@DeleteMapping(value = "/delete_message")
    public void delete_message(@RequestParam(value = "id") Integer message_id) {
		messageRepository.delete_message(message_id);
    }
}
