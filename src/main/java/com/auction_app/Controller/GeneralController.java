package com.auction_app.Controller;

import com.auction_app.Model.*;
import com.auction_app.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.List;
import javax.json.*;
import static java.lang.Double.max;


@RestController
@CrossOrigin
public class GeneralController {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private SellerRepository sellerRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private UserRepository userRepository;

	//values used for the recommendation system
	private Double visited_value = 0.3;
	private Double bought_value = 1.0;

	@GetMapping(value = "/get_items")
    public List<Item> handle_get_items() {
		return itemRepository.findAll();
    }

    @GetMapping(value = "/get_item")
    public Optional<Item> handle_get_item(@RequestParam(value = "itemID") Integer itemID,
		@RequestParam(value = "user_id") String user_id) {

		if (user_id != "") {
			handle_set_item_visited_by_user(itemID, user_id);
		}
		return itemRepository.findById(itemID);
    }

    //get the current bid's amount of an item
    @GetMapping(value = "/get_item_currently")
    public Double handle_get_item_currently(@RequestParam(value = "id") Integer item_id) {
		return itemRepository.get_item_currently(item_id);
    }

    //get the current bid's amount and bidder id of an item
    @GetMapping(value = "/get_item_current_bid")
    public String handle_get_item_current_bid(@RequestParam(value = "id") Integer item_id) {
		JsonObject json;

		Item item = itemRepository.findById(item_id).get();
		Double currently = item.getCurrently();
		String user_id = item.getLast_Bidder();
		Timestamp ends = item.getEnds();

		if (ends != null) {
			json = Json.createObjectBuilder()
					.add("Currently", -1)
            .build();
			return json.toString();
		}

		if (user_id != null) {
			json = Json.createObjectBuilder()
					.add("Currently", currently)
 		            .add("Last_Bidder", user_id)
            .build();
		}
		else {
			json = Json.createObjectBuilder()
				.add("Currently", currently)
 		        .add("Last_Bidder", "")
            .build();
		}

		return json.toString();
    }

    @GetMapping(value = "/get_items_count")
    public Long handle_get_items_count() {
		return itemRepository.count();
    }

    //get the number of active items
    @GetMapping(value = "/get_online_items_count")
    public Integer handle_get_online_items_count() {
		return itemRepository.get_online_items_count();
    }

    //get all items by a specific seller
    @GetMapping(value = "/get_user_items")
    public List<Item> handle_get_user_items(@RequestParam(value = "id") String user_id) {
		return itemRepository.get_items_by_user_id(user_id);
    }

    @GetMapping(value = "/get_sellers")
    public List<Seller> handle_get_sellers() {
		return sellerRepository.findAll();
    }

    @GetMapping(value = "/get_categories")
    public List<Category> handle_get_categories(){
		return categoryRepository.findAll();
    }

	@GetMapping(value = "get_images_objects")
	public List<Image> handle_get_images_objects() {
		return imageRepository.findAll();
	}

	@GetMapping(value = "/get_item_images_objects")
    public List<?> handle_get_item_images_objects(@RequestParam(value = "id") Integer id) {
		return imageRepository.get_item_images_objects(id);
    }

    //get the images' paths of a specific item
    @GetMapping(value = "/get_item_images_paths")
    public List<String> handle_get_item_images_paths(@RequestParam(value = "id") Integer id) {
		return imageRepository.get_item_images_paths(id);
    }

    //get the categories of a specific item
    @GetMapping(value = "/get_item_categories")
    public List<String> handle_get_item_categories(@RequestParam(value = "id") Integer id) {
		return itemRepository.get_item_categories(id);
    }

    //this is needed for the recommendation system
    private void initialize_user_ratings(Integer item_id) {
    	List<String> user_ids = userRepository.get_all_user_ids();

    	if (user_ids.isEmpty()) {
    		return;
	    }
		//create a string of all 'item_id - user_id' tuples. e.g. (1,"user1"), (1, "user2"), (1, "user3), ...
		//to avoid multiple inserts in the database
    	String str = "";
        for (String user_id : user_ids) {
            str = str + "(" + item_id + ",'"+ user_id + "'),";
        }
        str = remove_last_char(str);
		execute_sql("insert into ratings(ItemID, User_id) values " + str);
    }

    public void execute_sql(String q) {
		EntityManagerFactory em_factory = Persistence.createEntityManagerFactory( "jcg-JPA" );
		EntityManager entitymanager = em_factory.createEntityManager();

		entitymanager.getTransaction().begin();
		Query query = entitymanager.createNativeQuery(q);
		query.executeUpdate();
		entitymanager.getTransaction().commit();
    }

	@PostMapping(value = "/create_item")
	public Integer handle_create_item(@RequestBody String json_string) {
		JsonObject json = Json.createReader(new StringReader(json_string)).readObject();

		String seller_id = json.getString("user_id");
		Optional <Seller> found_seller = sellerRepository.findById(seller_id);
		if (!found_seller.isPresent()) {
			sellerRepository.save_seller(seller_id);
		}

		itemRepository.save_item(json.getString("name"),
				json.getJsonNumber("buy_Price").doubleValue(),
				json.getJsonNumber("first_Bid").doubleValue(),json.getString("country"),
				json.getString("description"), json.getString("user_id"),
				json.getJsonNumber("latitude").doubleValue(),
				json.getJsonNumber("longitude").doubleValue(), json.getString("location"),
				json.getJsonNumber("first_Bid").doubleValue()-0.01);

		Integer item_id = itemRepository.get_last_insert_id();

		JsonArray categories = json.getJsonArray("category");

		if (!categories.isEmpty()) {
			//create a string of all 'item_id - category_id' tuples. e.g. (1, 71), (1, 72), (1, 73), ...
			//to avoid multiple inserts in the database
			String str = "";
			for (int i=0; i < categories.size(); i++) {
                Integer cat_id = categories.getJsonNumber(i).intValue();
                str = str + "(" + item_id + ","+ cat_id + "),";
			}

			str = remove_last_char(str); //eg delete the last comma
			execute_sql("insert into item_has_category(ItemID, Category_id) values" + str);
		}

		//set to which item id does each image belong to
		JsonArray images = json.getJsonArray("image");
		if (!images.isEmpty()) {
			for (int i=0; i < images.size(); i++) {
                Integer image_id = images.getJsonNumber(i).intValue();
                imageRepository.set_image_item_id(image_id, item_id);
			}
		}

		//initialization is needed for the recommendation system
		initialize_user_ratings(item_id);
		return item_id;
	}

	String remove_last_char(String str) {
		return str.substring(0,str.length() - 1);

	}

	@PutMapping(value = "/update_item")
	public ResponseEntity handle_update_item(@RequestBody Item item) {

		if (itemRepository.get_item_num_of_bids(item.getItemID()) != 0) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}

		itemRepository.update_item(item.getItemID(), item.getName(), item.getBuy_Price(), item.getFirst_Bid(),
				item.getCountry(), item.getDescription(), item.getSeller().getUser_id(), item.getLatitude(),
				item.getLongitude(), item.getLocation(), item.getFirst_Bid()- 0.01);

		Set<Category> categories = item.getCategory();

		if (categories.isEmpty() == false) {
			//create a string of all 'item_id - category_id' tuples. e.g. (1, 71), (1, 72), (1, 73), ...
			//to avoid multiple inserts in the database
			String str = "";
	        for (Category cat : categories) {
	            str = str + "(" + item.getItemID() + ","+ cat.getCategory_id()+ "),";
	        }
	        str = remove_last_char(str);

	        execute_sql( "delete from item_has_category where ItemID="+item.getItemID());
			execute_sql("insert into item_has_category(ItemID, Category_id) values" + str);
		}

		List<Integer> images = item.getImage();

		if (!images.isEmpty()) {
			for(Integer image_id : images) {
				imageRepository.set_image_item_id(image_id, item.getItemID());
			}
		}
		return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/create_image")
	public Integer handle_create_image(@RequestBody byte[] data) {
		imageRepository.save_image();
		Integer image_id = imageRepository.get_last_insert_id();

		File file = new File("images/"+image_id + ".jpg");
		try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(data);
			imageRepository.set_image_path(image_id, file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image_id;
	}

	@GetMapping(value = "/get_image")
    public byte[] handle_get_image(@RequestParam(value = "id") Integer image_id) {
		byte[] array = null;

		String image_path = imageRepository.get_image_path(image_id);
		try {
			array = Files.readAllBytes(Paths.get(image_path));

		} catch (Exception e) {

		}
		return array;
	}

    @DeleteMapping(value = "/delete_image")
	public void handle_delete_image(@RequestParam(value = "id") Integer image_id) {
		String path = imageRepository.get_image_path(image_id);
		imageRepository.deleteById(image_id);
		delete_local_image(path);
    }

    private void delete_local_image(String path) {
		try
			{Files.deleteIfExists(Paths.get(path));
		}
        catch(NoSuchFileException e)
        {
            System.out.println("No such file/directory exists");
        }
        catch(DirectoryNotEmptyException e)
        {
            System.out.println("Directory is not empty.");
        }
        catch(IOException e)
        {
            System.out.println("Invalid permissions.");
        }
    }

    //make an item active(start the auction)
    @PutMapping(value = "/start_item")
    public void handle_start_item(@RequestParam(value = "id") Integer item_id) {
		Date date= new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);

		itemRepository.set_started(ts, item_id);
    }

    @PutMapping(value = "/end_item_custom")
    public String handle_end_item_custom(@RequestParam(value = "itemID") Integer item_id,
	                                 @RequestParam(value = "user_id") String user_id,
                                 @RequestParam(value = "amount") Double amount) {
		Date date= new Date();
		long time = date.getTime();

		Timestamp ts = new Timestamp(time);

		itemRepository.stop_item(item_id, user_id, amount, ts);
		set_item_bought_by_user(item_id, user_id);

		JsonObject json = Json.createObjectBuilder()
			.add("ends", ts.toString())
        .build();

		return json.toString();
    }

    //end an auction
    @PutMapping(value = "/end_item")
    public String handle_end_item(@RequestParam(value = "itemID") Integer item_id) {
		Date date= new Date();
		long time = date.getTime();

		Timestamp ts = new Timestamp(time);

		itemRepository.set_end(ts, item_id);
		Item item = itemRepository.findById(item_id).get();

		if (itemRepository.get_item_num_of_bids(item_id) == 0) {
			handle_delete_item(item_id);
		}

		String user_id = item.getLast_Bidder();
		set_item_bought_by_user(item_id, user_id);

		JsonObject json = Json.createObjectBuilder()
			.add("ends", ts.toString())
        .build();

		return json.toString();
    }

    //buy an item with the amount equal to the buy price
    @PutMapping(value = "/buy_full_price")
    public String handle_buy_full_price(@RequestParam(value = "itemID") Integer item_id,
                                        @RequestParam(value = "user_id") String user_id) {
		JsonObject json;

		Item item = itemRepository.findById(item_id).get();
		if (item.getEnds() != null) {
			json = Json.createObjectBuilder()
				.add("ends", "")
	        .build();
		}

		Date date= new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);

		itemRepository.buy_item(item_id, user_id, ts);
		set_item_bought_by_user(item_id, user_id);

		json = Json.createObjectBuilder()
			.add("ends", ts.toString())
        .build();

		return json.toString();
    }


    @DeleteMapping(value = "/delete_item")
	public ResponseEntity handle_delete_item(@RequestParam(value = "id") Integer item_id) {
		if (itemRepository.get_item_num_of_bids(item_id) != 0) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}

		List<String> image_paths = imageRepository.get_item_images_paths(item_id);

		for(String image_path : image_paths) {
			delete_local_image(image_path);
		}

		imageRepository.delete_item_images(item_id);
		itemRepository.delete_item_categories(item_id);
		itemRepository.delete_item_ratings(item_id);
		itemRepository.deleteById(item_id);
		return new ResponseEntity(HttpStatus.OK);
    }

    //get all active items
    @GetMapping(value = "/get_online_items")
    public List<Item> handle_get_online_items() {
		return itemRepository.get_online_items();
    }

    //get all items bought by a specific user
    @GetMapping(value = "/get_user_bought_items")
	public List<Item> handle_get_user_bought_items(@RequestParam(value = "id") String user_id) {
		return itemRepository.get_user_bought_items(user_id);
    }

    //get all items sold by a specific user
    @GetMapping(value = "/get_user_sold_items")
	public List<Item> handle_get_user_sold_items(@RequestParam(value = "id") String user_id) {
		return itemRepository.get_user_sold_items(user_id);
    }

     //get all items with a specific category
    @GetMapping(value = "/get_items_by_category")
	public List<Item> handle_get_items_by_category(@RequestParam(value = "category") String category) {
		return itemRepository.get_items_by_category(category);
    }

    //get all items that a specific user has made a bid for
    @GetMapping(value = "/get_user_active_bids")
	public Iterable<Item> handle_get_user_active_bids(@RequestParam(value = "id") String user_id) {
    	List<Integer> item_ids = userRepository.get_user_active_bids(user_id);
    	return itemRepository.findAllById(item_ids);
    }

    //get all items' id with new messages for a specific user
    @GetMapping(value = "/get_user_item_ids_with_new_messages")
	public List<Integer> handle_get_user_items_with_new_messages(@RequestParam(value = "id") String user_id) {
		return itemRepository.get_user_item_ids_with_new_messages(user_id);
	}

    @GetMapping(value = "/are_there_new_messages")
	public ResponseEntity handle_are_there_new_messages(@RequestParam(value = "id") String user_id) {
    	Integer count = itemRepository.get_user_new_messages_count(user_id);
    	if (count == 0) {
    		return new ResponseEntity(HttpStatus.UNAUTHORIZED);
	    }
    	else {
    		return new ResponseEntity(HttpStatus.OK);
	    }
    }

    @PostMapping(value = "/rate_bidder")
	public void handle_rate_bidder(@RequestParam(value = "id") String user_id,
                                   @RequestParam(value = "rating") Integer rating) {
		itemRepository.rate_bidder(user_id, rating);
	}

	@PostMapping(value = "/rate_seller")
	public void handle_rate_seller(@RequestParam(value = "id") String user_id,
                                   @RequestParam(value = "rating") Integer rating) {
		itemRepository.rate_seller(user_id, rating);
	}
	@GetMapping(value = "/get_seller_rating")
	public Double handle_get_seller_rating(@RequestParam(value = "id") String user_id) {
		return itemRepository.get_seller_rating(user_id);
	}

	@GetMapping(value = "/get_bidder_rating")
	public Double handle_get_bidder_rating(@RequestParam(value = "id") String user_id) {
		return itemRepository.get_bidder_rating(user_id);
	}

	//this is needed for the recommendation system
	@PostMapping(value = "/set_item_visited_by_user")
	public void handle_set_item_visited_by_user(@RequestParam(value = "itemID") Integer itemID,
	                                            @RequestParam(value = "user_id") String user_id) {
		Integer count = itemRepository.get_item_ratings_by_user_count(itemID, user_id);
		if (count == 0) {
			itemRepository.save_item_rating_by_user(itemID, user_id, visited_value);
		}
		else {
			Double rating = itemRepository.get_item_rating_by_user(itemID, user_id);
			if (rating == null) rating = 0.0;
			itemRepository.set_item_rating_by_user(itemID, user_id, max(visited_value, rating));
		}
	}

	//this is needed for the recommendation system
    public void set_item_bought_by_user(@RequestParam(value = "itemID") Integer itemID,
                                       @RequestParam(value = "user_id") String user_id) {
    	Integer count = itemRepository.get_item_ratings_by_user_count(itemID, user_id);
    	if (count == 0) {
    		itemRepository.save_item_rating_by_user(itemID, user_id, bought_value);
	    }
    	else {
    		Double rating = itemRepository.get_item_rating_by_user(itemID, user_id);
    		if (rating == null) rating = 0.0;
			itemRepository.set_item_rating_by_user(itemID, user_id, max(bought_value, rating));
	    }
    }

	@RequestMapping(value = "/get_xml", method = RequestMethod.GET, produces = {"application/xml"})
	public Item handle_get_xml() {
		Item item =  itemRepository.findById(105).get();
		System.out.println(item);
		return item;
	}
}
