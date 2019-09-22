package com.auction_app.Repository;

import com.auction_app.Model.Item;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Integer> {
	Item save(Item item);
    List<Item> findAll();

    @Query(value="select c.Name from item i " +
		    "join item_has_category ihc on i.ItemID= ihc.itemID join category c on "+
		    "c.Category_id = ihc.Category_id where i.ItemId = ?1", nativeQuery = true)
    List<String> get_item_categories(Integer id);

    @Query(value="select c.Category_id from item i " +
		    "join item_has_category ihc on i.ItemID= ihc.itemID join category c on "+
		    "c.Category_id = ihc.Category_id where i.ItemId = ?1", nativeQuery = true)
    List<Integer> get_item_categories_id(Integer id);

    @Query(value="select count(*) from item_has_bid where ItemID=?1", nativeQuery = true)
    Integer get_item_num_of_bids(Integer id);

    @Query(value="select ItemID from Item", nativeQuery = true)
    List<Integer> get_all_item_ids();

    @Query(value="select * from Item order by ItemID", nativeQuery = true)
    List<Item> get_all_items();

    @Transactional
	@Modifying
    @Query(value = "insert into	item(Name, Buy_Price, First_Bid, Country,"+
		    "Description, User_id, Latitude, Longitude, Location, Currently) " +
		    "values (?1,?2,?3,?4,?5,?6,?7,?8,?9, ?10) ",
		    nativeQuery = true)
	void save_item(String name, Double buy_Price, Double first_Bid,
				String country, String description, String user_id, Double latitude,
				   Double longitude, String location, Double currently);

    @Transactional
	@Modifying
    @Query(value = "update item set Name=?2,Buy_price=?3,First_Bid=?4,Country=?5,"+
		    "Description=?6, User_id=?7,Latitude=?8,Longitude=?9,Location=?10,Currently=?11 " +
		    " where ItemID = ?1  ",
		    nativeQuery = true)
	void update_item(Integer itemID, String name, Double buy_Price, Double first_Bid,
				String country, String description, String User_id, Double latitude,
				   Double longitude, String location, Double currently);

    @Transactional
	@Modifying
    @Query(value = "insert into item_has_category values(?1, ?2)", nativeQuery = true)
	void save_item_category(Integer itemID, Integer category_id);

    @Transactional
	@Modifying
    @Query(value = "update item set Started = ?1 where ItemID = ?2", nativeQuery = true)
	void set_started(Timestamp started, Integer item_id);

    @Transactional
	@Modifying
    @Query(value = "update item set Ends = ?1 where ItemID = ?2", nativeQuery = true)
	void set_end(Timestamp ends, Integer item_id);

    @Query(value="select * from item where User_id = ?1 and Ends is not null ", nativeQuery = true)
	List<Item> get_items_by_user_id(String User_id);

    @Query(value="select i.* from item i join item_has_category ihc on i.ItemID = ihc.itemID " +
			"JOIN category c on c.Category_id = ihc.Category_id where c.Name = ?1", nativeQuery = true)
	List<Item> get_items_by_category(String category);

    @Query(value="select * from item i where i.Location = ?1", nativeQuery = true)
	List<Item> get_items_by_location(String category);

    @Query(value="select count(*) from item i where i.Started IS NOT NULL and i.Ends IS NULL", nativeQuery = true)
	Integer get_online_items_count();

    @Query(value="select * from item i where i.Started IS NOT NULL and i.Ends IS NULL", nativeQuery = true)
	List<Item> get_online_items();

    @Query(value="select * from item i where Last_Bidder = ?1 and Ends IS NOT NULL", nativeQuery = true)
	List<Item> get_user_bought_items(String User_id);

    @Query(value="select * from item i where User_id = ?1 and Ends IS NOT NULL ", nativeQuery = true)
	List<Item> get_user_sold_items(String User_id);

    @Query(value="select i.itemID from item i join item_has_category ihc on i.ItemID = ihc.itemID " +
			"JOIN category c on c.Category_id = ihc.Category_id where c.Name = ?1", nativeQuery = true)
	List<Integer> get_items_id_by_category(String category);

    @Query(value="select * from item i where i.Buy_Price >= ?1", nativeQuery = true)
	List<Item> get_items_with_price_more_or_equal_to(Double Price);

      @Query(value="select * from item i where i.Buy_Price > ?1", nativeQuery = true)
	List<Item> get_items_with_price_more_than(Double Price);

    @Query(value="select * from item i where i.Buy_Price <= ?1", nativeQuery = true)
	List<Item> get_items_with_price_less_or_equal_to(Double Price);

    @Query(value="select * from item i where i.Buy_Price < ?1", nativeQuery = true)
	List<Item> get_items_with_price_less_than(Double Price);

    @Query(value="select * from item i where i.Buy_Price >= ?1 and i.Buy_Price <= ?2", nativeQuery = true)
	List<Item> get_items_with_price_between(Double Price, Double Price2);

    @Query(value="select LAST_INSERT_ID()", nativeQuery = true)
	Integer get_last_insert_id();

    @Query(value="select * from item where Started IS NOT NULL and Ends IS NULL" +
		    " order by Started desc limit ?2 offset ?1", nativeQuery = true)
	List<Item> get_items_between(Integer start, Integer count);

    @Transactional
	@Modifying
	@Query(value = "delete from item_has_category where ItemID = ?1", nativeQuery = true)
	void delete_item_categories(Integer item_id);

    @Transactional
	@Modifying
	@Query(value = "delete from ratings where ItemID = ?1", nativeQuery = true)
	void delete_item_ratings(Integer item_id);

    @Transactional
	@Modifying
	@Query(value = "delete ihb, b from item_has_bid ihb join bid b on b.User_id = ihb.User_id " +
			"and b.Time = ihb.Time where ihb.ItemID = ?1;", nativeQuery = true)
	void delete_item_bids(Integer item_id);

    @Query(value="select Currently from Item where ItemID = ?1", nativeQuery = true)
	Double get_item_currently(Integer item_id);

    @Query(value="select Last_Bidder from Item where ItemID = ?1", nativeQuery = true)
    String get_item_last_bidder(Integer item_id);

    @Query(value="select Currently, Last_Bidder from Item where ItemID = ?1", nativeQuery = true)
    List<?> get_item_current_bid(Integer item_id);

    @Transactional
	@Modifying
    @Query(value = "update item set Currently = ?2 where ItemID = ?1", nativeQuery = true)
	void set_item_currently(Integer item_id, Double amount);

    @Transactional
	@Modifying
    @Query(value = "update item set Number_of_Bids = Number_of_Bids + 1 where ItemID = ?1", nativeQuery = true)
	void increase_item_num_of_bids(Integer item_id);

    @Transactional
	@Modifying
    @Query(value = "update item set Last_Bidder = ?2 where ItemID = ?1", nativeQuery = true)
	void set_item_last_bidder(Integer item_id, String user_id);

    @Query(value="select Rating from Ratings where ItemID=?1 and User_id=?2", nativeQuery = true)
    Double get_item_rating_by_user(Integer item_id, String user_id);

    @Query(value="select count(*) from Ratings where ItemID=?1 and User_id=?2", nativeQuery = true)
    Integer get_item_ratings_by_user_count(Integer item_id, String user_id);

    @Transactional
	@Modifying
    @Query(value = "update Ratings set Rating = ?3 where ItemID = ?1 and User_id=?2", nativeQuery = true)
	void set_item_rating_by_user(Integer item_id, String user_id, Double rating);

    @Transactional
	@Modifying
    @Query(value = "insert into Ratings values (?1, ?2 , ?3)", nativeQuery = true)
	void save_item_rating_by_user(Integer item_id, String user_id, Double rating);

    @Transactional
	@Modifying
    @Query(value = "update Item set Currently = ?3, Last_Bidder = ?2, Ends=?4 where ItemID = ?1", nativeQuery = true)
	void stop_item(Integer item_id, String user_id, Double amount, Timestamp ts);

    @Transactional
	@Modifying
    @Query(value = "update Item set Last_Bidder = ?2, Ends=?3, Currently=Buy_Price where ItemID = ?1", nativeQuery = true)
	void buy_item(Integer item_id, String user_id, Timestamp ts);

    @Query(value="select distinct ItemID from Message where Receiver = ?1 and Read_flag = 0", nativeQuery = true)
	List<Integer> get_user_item_ids_with_new_messages(String User_id);

    @Query(value="select count(*) from message where Receiver = ?1 and Read_flag = 0", nativeQuery = true)
	Integer get_user_new_messages_count(String User_id);

    @Transactional
	@Modifying
	@Query(value = "update bidder set Rating = Rating + ?2 where User_id = ?1", nativeQuery = true)
	void rate_bidder(String user_id, Integer rating);

    @Transactional
	@Modifying
    @Query(value = "update seller set Rating = Rating + ?2 where User_id = ?1", nativeQuery = true)
	void rate_seller(String user_id, Integer rating);

    @Query(value="select rating from seller where User_id = ?1", nativeQuery = true)
	Double get_seller_rating(String User_id);

    @Query(value="select rating from bidder where User_id = ?1", nativeQuery = true)
	Double get_bidder_rating(String User_id);
}