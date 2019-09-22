package com.auction_app.Repository;

import com.auction_app.Model.Seller;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SellerRepository extends CrudRepository<Seller, String> {
	Seller save(Seller user);
	List<Seller> findAll();

	@Transactional
	@Modifying
	@Query(value = "insert into Seller(User_id) values(?1)", nativeQuery = true)
	void save_seller(String user_id);

}