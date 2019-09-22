package com.auction_app.Controller;

import com.auction_app.Model.JwtResponse;
import com.auction_app.Model.User;
import com.auction_app.Repository.ItemRepository;
import com.auction_app.Repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.*;

@RestController
@CrossOrigin
public class SignUpController {

	public long JWT_TOKEN_VALIDITY = 5*60*60;

	@Value("${jwt.secret}")
	private String secret;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ItemRepository itemRepository;

	private Double initial_value = 0.0;

    @PostMapping(value = "get_jwt")
    public ResponseEntity handle_get_jwt(@RequestBody Map<String, Object> json) {

		String user_id = (String) json.get("username");
		String password = (String) json.get("password");

		User user = userRepository.get_by_user_id(user_id);

		if (user == null ) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		if (!user.getPassword().equals(password)) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}

		String token = create_user_token(user);

		return ResponseEntity.ok(new JwtResponse(token));
    }

    public String create_user_token(User user) {
		Map<String, Object> claims = new HashMap<>();

		claims.put("username", user.getUser_id());
		claims.put("name", user.getName());
		claims.put("surname", user.getSurname());
		claims.put("email", user.getEmail());
		claims.put("telephone", user.getTelephone());
		claims.put("adress", user.getAddress());
		claims.put("latitude", user.getLatitude());
		claims.put("lognitude", user.getLongitude());
		claims.put("AFM", user.getAFM());
		claims.put("admin_flag", user.getAdmin_flag());
		claims.put("approved_flag", user.getApproved_flag());

		return generateToken(new org.springframework.security.core.userdetails.
						User(user.getUser_id(), user.getPassword(), new ArrayList<>()), claims);
    }

	private String generateToken(UserDetails userDetails, Map<String, Object> claims) {
		return doGenerateToken(claims, userDetails.getUsername());
	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
			.setHeaderParam("typ", "JWT")
			.signWith(SignatureAlgorithm.HS256, secret).compact();
	}

	@PostMapping(value = "sign_up")
    public ResponseEntity handle_get_jwt(@RequestBody User user) {
    	Optional<User> found_user =  userRepository.findById(user.getUser_id());
    	if (found_user.isPresent()) {
    		return new ResponseEntity(HttpStatus.UNAUTHORIZED);
	    }
    	userRepository.save(user);
    	initialize_user_ratings(user.getUser_id());
    	String token = create_user_token(user);
    	return ResponseEntity.ok(new JwtResponse(token));
    }

    private void initialize_user_ratings(String user_id) {
    	List<Integer> item_ids = itemRepository.get_all_item_ids();

    	if (item_ids.isEmpty()) {
    		return;
	    }
    	String str = "";
        for (Integer item_id : item_ids) {
            str = str + "(" + item_id + ",'"+ user_id + "'),";
        }
        str = str.substring(0,str.length() - 1);

        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "jcg-JPA" );
		EntityManager entitymanager = emfactory.createEntityManager();

		entitymanager.getTransaction().begin();
		String q = "insert into ratings(ItemID, User_id) values " + str;
		Query query = entitymanager.createNativeQuery(q);
		query.executeUpdate();
		entitymanager.getTransaction().commit();
    }
}
