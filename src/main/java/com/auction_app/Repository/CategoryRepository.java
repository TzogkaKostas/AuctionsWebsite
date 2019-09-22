package com.auction_app.Repository;

import com.auction_app.Model.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
	 List<Category> findAll();
}