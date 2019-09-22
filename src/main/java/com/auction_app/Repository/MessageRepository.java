package com.auction_app.Repository;

import com.auction_app.Model.Message;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Integer> {

	@Transactional
	@Modifying
    @Query(value = "insert into	message(Message,Created_at,Read_flag,Sender, Receiver, ItemID)" +
		    " values (?1, ?2, ?3, ?4, ?5, ?6)",
		    nativeQuery = true)
	void save_message(String message, Timestamp ts, Boolean read_flag, String sender,
	                  String receiver, Integer ItemID);

	@Transactional
	@Modifying
	@Query(value = "delete from message where message_id = ?1", nativeQuery = true)
	void delete_message(Integer message_id);

	@Transactional
	@Modifying
    @Query(value = "UPDATE Message SET Read_flag = 1 WHERE Message_id in " +
		    " (select Message_id from Message where ItemID = ?1 and Read_flag = false " +
		    " order by Created_at desc limit ?2)",
		    nativeQuery = true)
	void set_messages_as_read(Integer item_id, Integer amount);

	@Query(value="select * from Message where Sender = ?1", nativeQuery = true)
	List <Message> get_sent_messages(String user_id);

	@Query(value="select * from Message where Receiver = ?1", nativeQuery = true)
	List <Message> get_received_messages(String user_id);

	@Query(value="select * from Message where ItemID = ?1 order by Created_at desc limit ?2", nativeQuery = true)
	List<Message> get_item_messages(Integer itemID, Integer amount);

	@Query(value="select * from Message where ItemID = ?1 and Read_flag = false order by Created_at desc limit ?2", nativeQuery = true)
	List<Message> get_item_unread_messages(Integer itemID, Integer amount);

	@Query(value="select LAST_INSERT_ID()", nativeQuery = true)
	Integer get_last_insert_id();
}
