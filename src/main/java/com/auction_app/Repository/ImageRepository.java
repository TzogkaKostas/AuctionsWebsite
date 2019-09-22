package com.auction_app.Repository;

import com.auction_app.Model.Image;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ImageRepository extends CrudRepository<Image, Integer> {

	List<Image> findAll();

	@Query(value = "select * from image", nativeQuery = true)
	List<Image> get_images_objects();

	@Query(value = "select * from image where ItemID = ?1", nativeQuery = true)
	List<Image> get_item_images_objects(Integer item_id);

	@Query(value = "select Image_path from image where ItemID = ?1", nativeQuery = true)
	List<String> get_item_images_paths(Integer item_id);

	@Transactional
	@Modifying
	@Query(value = "insert into image values()", nativeQuery = true)
	void save_image();

	@Query(value = "select LAST_INSERT_ID()", nativeQuery = true)
	Integer get_last_insert_id();

	@Transactional
	@Modifying
	@Query(value = "update image set Image_path = ?2 where Image_id = ?1", nativeQuery = true)
	void set_image_path(Integer image_id, String image_path);

	@Transactional
	@Modifying
	@Query(value = "update image set ItemID = ?2 where Image_id = ?1", nativeQuery = true)
	void set_image_item_id(Integer image_id, Integer item_id);

	@Query(value = "select Image_path from image where Image_id= ?1", nativeQuery = true)
	String get_image_path(Integer image_id);

	@Transactional
	@Modifying
	@Query(value = "delete from image where ItemID = ?1", nativeQuery = true)
	void delete_item_images(Integer item_id);
}