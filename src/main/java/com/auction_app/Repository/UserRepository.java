package com.auction_app.Repository;

import com.auction_app.Model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, String> {
	User save(User user);
	void deleteById(String user_id);
	List<User> findAll();

	@Query(value="select * from user where User_id =?1", nativeQuery = true)
	User get_by_user_id(String user_id);

	@Query(value="select User_id from user where admin_flag=0 order by User_id", nativeQuery = true)
    List<String> get_all_user_ids();

    @Query(value="select i.ItemID from item i join item_has_bid ihb on i.ItemID = ihb.itemID" +
		    " where ihb.User_id = ?1 and i.Started is not null and i.Ends is null", nativeQuery = true)
	List<Integer> get_user_active_bids(String User_id);

    @Query(value="select Rating from ratings where User_id = ?1", nativeQuery = true)
	double[] get_user_ratings(String User_id);

    @Query(value="select Rating from ratings order by User_id, ItemID", nativeQuery = true)
	Double[] get_all_ratings();

    @Query(value="select count(*) from user where admin_flag=0", nativeQuery = true)
	Integer get_users_count();
}