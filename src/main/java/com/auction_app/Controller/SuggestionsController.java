package com.auction_app.Controller;

import com.auction_app.Model.Item;
import com.auction_app.Repository.ItemRepository;
import com.auction_app.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Stream;
import smile.neighbor.LSH;
import smile.neighbor.Neighbor;


@RestController
@CrossOrigin
public class SuggestionsController {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private UserRepository userRepository;

    @GetMapping(value = "/get_suggestions")
    public List<Item> handle_get_suggestions(@RequestParam(value = "id") String user_id) {
		List<String> all_users = userRepository.get_all_user_ids();
		Integer num_of_users = userRepository.get_users_count();
		Integer num_of_items = (int)(itemRepository.count());

		if (num_of_items == 0 || num_of_users == 0) {
			return Collections.emptyList();
		}

		Integer k  = Math.min(20, num_of_users);
		Integer num_of_suggested_items = Math.min(5, num_of_items);

		System.out.println("num of users: " + num_of_users);
		System.out.println("num of items: " + num_of_items);

		Integer user_index = all_users.indexOf(user_id);

		Double[] temp_ratings_1d = userRepository.get_all_ratings();
		if (temp_ratings_1d.length == 0) {
			return Collections.emptyList();
		}

		//fill the non rated items with 0.0
	    for (int i = 0; i < temp_ratings_1d.length; i++) {
		    if (temp_ratings_1d[i] == null) {
		    	temp_ratings_1d[i] = 0.0;
		    }
	    }

	    //copy the 1d array of ratings to a 2d array(one row per user, one column per item)
	    double[] ratings_1d = Stream.of(temp_ratings_1d).mapToDouble(Double::doubleValue).toArray();
		double[][] ratings = new double[num_of_users][num_of_items];
	    for (int i = 0; i < num_of_users; i++) {
			ratings[i] = Arrays.copyOfRange(ratings_1d, i*num_of_items, (i+1)*(num_of_items));
	    }

	    //find the k nearest neighbors
		LSH<double[]> lsh = new LSH<>(ratings, ratings);
		Neighbor[] neighbors = lsh.knn(ratings[user_index], k);

		Double z = 0.0;
		for (Neighbor n : neighbors) {
				z += n.distance;
		}

		//Derive ratings from similar users(k nearest neighbors)
		Double rating;
	    for (int i = 0; i < num_of_items; i++) {
			if (ratings[user_index][i] == 0.0) {
				rating = 0.0;
				for (Neighbor n : neighbors) {
					double[] values = (double[]) n.key;
					rating += n.distance*values[i];
				}
				ratings[user_index][i] = z*rating;
			}
			else if (ratings[user_index][i] == 1.0) { //dont recommend items that are already bought
				ratings[user_index][i] = -1.0;
			}
	    }

	    //get the 5 top-rated items
	    HashMap<Integer, Double> unSortedMap = new HashMap<>();
	    for (int i = 0; i < num_of_items ; i++) {
	    	unSortedMap.put(i, ratings[user_index][i]);
	    }
		LinkedHashMap<Integer, Double> sortedMap = new LinkedHashMap<>();
		unSortedMap.entrySet()
		    .stream()
		    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
		    .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
	    List <Item> all_items = itemRepository.findAll();
	    List<Item> suggested_items = new ArrayList<>();
		int i = 0;
		for (Map.Entry<Integer, Double> entry : sortedMap.entrySet()) {
			if (i == num_of_suggested_items) {
				break;
			}
			Item item = all_items.get(entry.getKey());
			if (item.getStarted() != null && item.getEnds() == null) {
				suggested_items.add(item);
				i++;
			}
		}

		System.out.println(suggested_items);
	    return suggested_items;
    }

}
