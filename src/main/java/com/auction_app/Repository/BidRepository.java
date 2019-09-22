package com.auction_app.Repository;

import com.auction_app.Model.Bid;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

public interface BidRepository extends CrudRepository<Bid, String> {
	Bid save(Bid bid);
    List<Bid> findAll();
    void deleteById (String Bidder_user_id);

    @Transactional
	@Modifying
    @Query(value = "insert into bid values(?1,?2,?3)", nativeQuery = true)
	void save_bid(String user_id, Timestamp ts, Double amount);

    @Transactional
	@Modifying
    @Query(value = "insert into item_has_bid values(?1,?2,?3)", nativeQuery = true)
	void save_item_has_bid(Integer item_id, String user_id, Timestamp ts);

}