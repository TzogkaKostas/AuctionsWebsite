package com.auction_app.Controller;

import com.auction_app.Model.Item;
import com.auction_app.Repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin
public class SearchController {

	@Autowired
	private ItemRepository itemRepository;

	@GetMapping(value = "/get_items_id_by_category")
	public List<Integer> handle_get_items_id_by_category(@RequestParam(value = "category") String category) {
		return itemRepository.get_items_id_by_category(category);
    }

    @GetMapping(value = "/get_items_by_location")
	public List<Item> handle_get_items_by_location(@RequestParam(value = "location") String location) {
		return itemRepository.get_items_by_location(location);
    }

    @GetMapping(value = "/get_items_by_description")
	public List<Item> handle_get_items_by_description(@RequestParam(value = "description") String text) {
		EntityManagerFactory em_factory = Persistence.createEntityManagerFactory( "jcg-JPA" );
		EntityManager entitymanager = em_factory.createEntityManager();
		String q = "SELECT i FROM Item i WHERE i.Description LIKE "+ "'%"+text+"%'";
		Query query = entitymanager.createQuery(q);
		return (List<Item>) (query.getResultList());
    }

    @GetMapping(value = "/get_items_with_price_more_or_equal_to")
	public List<Item> handle_get_items_with_price_more_or_equal_to(@RequestParam(value = "price") Double Price) {
		return itemRepository.get_items_with_price_more_or_equal_to(Price);
    }

    @GetMapping(value = "/get_items_with_price_more_than")
	public List<Item> handle_get_items_with_price_more_than(@RequestParam(value = "price") Double Price) {
		return itemRepository.get_items_with_price_more_than(Price);
    }

    @GetMapping(value = "/get_items_with_price_less_or_equal_to")
	public List<Item> handle_get_items_with_price_less_or_equal_to(@RequestParam(value = "price") Double Price) {
		return itemRepository.get_items_with_price_less_or_equal_to(Price);
    }

    @GetMapping(value = "/get_items_with_price_less_than")
	public List<Item> handle_get_items_with_price_less_than(@RequestParam(value = "price") Double Price) {
		return itemRepository.get_items_with_price_less_than(Price);
    }

    @GetMapping(value = "/get_items_with_price_between")
	public List<Item> handle_get_items_with_price_between(@RequestParam(value = "price1") Double Price1,
            @RequestParam(value = "price2") Double Price2) {
		return itemRepository.get_items_with_price_between(Price1, Price2);
    }

    @GetMapping(value = "/get_items_between")
	public List<Item> handle_get_items_between(@RequestParam(value = "start") Integer start,
            @RequestParam(value = "end") Integer end) {
		return itemRepository.get_items_between(start - 1, end - start + 1);
    }

    @GetMapping(value = "/search_items")
	public List<Item> handle_search_items(@RequestParam Double min, @RequestParam Double max,
                                                   @RequestParam String location, @RequestParam String description,
                                                   @RequestParam List<Integer> categories) {

		List<Item> all_items = itemRepository.findAll();
		List<Item> category_items = all_items;
		List<Item> location_items = all_items;
		List<Item> description_items = all_items;
		List<Item> start_items = all_items;
		List<Item> end_items = all_items;

		if (!categories.isEmpty()) {
			category_items = find_items_by_categories(categories);
		}
		if (!location.equals("")) {
 			location_items = itemRepository.get_items_by_location(location);
		}
		if (!description.equals("")) {
			description_items = handle_get_items_by_description(description);
		}
		if (min != -1) {
			start_items = itemRepository.get_items_with_price_more_or_equal_to(min);
		}
		if (max != -1) {
			end_items = itemRepository.get_items_with_price_less_or_equal_to(max);
		}

		List<Item> filtered_items = new ArrayList<>();
		for (Item item: all_items) {
			if (description_items.contains(item) && location_items.contains(item) &&
				category_items.contains(item) && start_items.contains(item) &&
				end_items.contains(item) && item.getEnds() == null &&
				item.getStarted() != null) {
				filtered_items.add(item);
			}
		}
		return filtered_items;
    }

    @GetMapping(value = "/search_items_between")
	public List<Item> handle_search_items_between(@RequestParam Double min, @RequestParam Double max,
                                                   @RequestParam String location, @RequestParam String description,
                                                   @RequestParam List<Integer> categories,
            @RequestParam(value = "start") Integer start, @RequestParam(value = "end") Integer end) {

		List<Item> items = handle_search_items(min, max, location, description, categories);
		return items.subList(start - 1, end);
    }

    @GetMapping(value = "/get_search_items_count")
	public Integer handle_get_searched_items_count(@RequestParam Double min, @RequestParam Double max,
                                                   @RequestParam String location, @RequestParam String description,
                                                   @RequestParam List<Integer> categories) {
		List<Item> items = handle_search_items(min, max, location, description, categories);
		return items.size();
    }

    private List<Item> find_items_by_categories(List<Integer> categories_id) {
		if (categories_id.isEmpty()) {
			return Collections.emptyList();
		}

		List<Integer> item_cat_ids;
		List<Item> filtered_items = new ArrayList();;
		List<Item> all_items = itemRepository.findAll();

		for (Item item : all_items) {
			item_cat_ids = itemRepository.get_item_categories_id(item.getItemID());

			if (item_cat_ids.containsAll(categories_id)) {
				filtered_items.add(item);
			}

		}
		return filtered_items;
    }
}
