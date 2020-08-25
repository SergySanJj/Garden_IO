package com.springproject.springbootapp.controllers;

import com.springproject.springbootapp.model.User;
import com.springproject.springbootapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    private static EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("dev");

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/add/{login}/{role}")
    public void addUser(@PathVariable String login, @PathVariable String role) {
        User user = new User();
        user.setLogin(login);
        user.setUser_role(role);
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        em.close();
    }

    @GetMapping("/find/{login}")
    public User findUser(@PathVariable String login) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Query query = em.createQuery(
                "SELECT a FROM  User a where a.login = :loginParam"
        );
        query.setParameter("loginParam", login);

        List<User> users = query.getResultList();
        em.close();
        try {
            return users.get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @GetMapping("exists/{login}")
    public Boolean userExists(@PathVariable String login) {
        return findUser(login) != null;
    }

}
