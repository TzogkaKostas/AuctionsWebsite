package com.auction_app.Repository;

import com.auction_app.Model.Bidder;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface BidderRepository extends CrudRepository<Bidder, String> {
	Bidder save(Bidder bidder);
    List<Bidder> findAll();

    @Transactional
	@Modifying
    @Query(value = "insert into bidder(User_id) values(?1)", nativeQuery = true)
	void save_bidder(String user_id);
}