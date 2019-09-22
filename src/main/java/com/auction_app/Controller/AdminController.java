package com.auction_app.Controller;


import com.auction_app.Repository.UserRepository;
import com.auction_app.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class AdminController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping(value = "/get_users")
    public Iterable<User> handle_get_users() {
	    return userRepository.findAll();
    }

    @GetMapping(value = "/get_user")
    public Optional<User> handle_get_user(@RequestParam(value = "id") String id) {
		return userRepository.findById(id);
    }

    @GetMapping(value = "/get_appending_users")
    public List <User> handle_get_appending_users() {
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "jcg-JPA" );
		EntityManager entitymanager = emfactory.createEntityManager();

		Query query = entitymanager.createQuery("Select u from User u where u.approved_flag = 0");
		return query.getResultList();
    }

    @PutMapping(value = "/confirm_user")
    public void handle_post_appending_user(@RequestParam(value = "id") String id) {
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "jcg-JPA" );
		EntityManager entitymanager = emfactory.createEntityManager();

		entitymanager.getTransaction().begin();
		Query query = entitymanager.createQuery("update User set approved_flag = 1 where User_id = " + "'"+id+"'");
		query.executeUpdate();
		entitymanager.getTransaction().commit();
    }

    @DeleteMapping(value = "/delete_user")
	public void handle_delete_user(@RequestParam(value = "id") String id) {
		userRepository.deleteById(id);
    }

	/*
	@Autowired
	private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/example")
    @ResponseBody
    public Integer handle_example() {

	    EntityManagerFactory emf = Persistence.createEntityManagerFactory("jcg-JPA");
	    EntityManager em = emf.createEntityManager();

	    em.getTransaction().begin();

	    Employee employee = new Employee("kostas22", "1234");

	    System.out.println("COMIITING");
	    //em.persist(employee);

	    em.getTransaction().commit();

    	System.out.println(12);
        return 1;
    }

    @GetMapping(value = "/login")
    public String handle_exampe_post() {
    	System.out.println(6666);
    	return "login";
    }


    @RequestMapping(value = "/home")
    public String handle_home() {
    	System.out.println(44);
    	return "home";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new Employee());
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") Employee userForm, BindingResult bindingResult) {
        //userValidator.validate(userForm, bindingResult);

	    userForm.setPassword(passwordEncoder.encode(userForm.getPassword()));
	    System.out.println("encode password:" + passwordEncoder.encode(userForm.getPassword()));
	    userForm.setRole("ROLE_USER");
        employeeRepository.save(userForm);

        //securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());
        return "hello";
    }

    @RequestMapping(value = "/hello")
    public String handle_page() {
    	System.out.println(66);
        return "hello";
    }

    @RequestMapping(value = "/test_users")
    @ResponseBody
    public List<Employee> handle_test_users() {
    	System.out.println(77);
    	Employee  emp_1 = new Employee("123", "george");
	    emp_1.setId(1);
	    emp_1.setRole("user");
    	Employee  emp_2 = new Employee("456", "micheal");
    	emp_2.setId(2);
    	emp_2.setRole("user");
    	Employee  emp_3 = new Employee("789", "john");
    	emp_3.setId(3);
    	emp_3.setRole("user");


    	List<Employee> list = new ArrayList<Employee>();
    	list.add(emp_1);
    	list.add(emp_2);
    	list.add(emp_3);
		return list;
    }
    */
}