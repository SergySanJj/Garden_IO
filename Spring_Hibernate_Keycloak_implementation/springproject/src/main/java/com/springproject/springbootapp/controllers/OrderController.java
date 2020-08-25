package com.springproject.springbootapp.controllers;


import com.springproject.springbootapp.model.Order;
import com.springproject.springbootapp.OrderStatus;
import com.springproject.springbootapp.model.User;
import com.springproject.springbootapp.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderRepository orderRepository;

    private static EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("dev");

    @GetMapping("/all")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }


    @PutMapping("add/{ownerLogin}/{info}/{quantity}")
    public void addOrder(@PathVariable String ownerLogin, @PathVariable String info, @PathVariable int quantity) {
        Order order = new Order();
        order.setInfo(info);
        order.setQuantity(quantity);
        order.setStatus(OrderStatus.initial);
        User user = new UserController().findUser(ownerLogin);
        order.setUser(user);
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.persist(order);
        em.getTransaction().commit();
        em.close();
    }

    @DeleteMapping("delete/finished/{login}")
    public void deleteFinished(@PathVariable String login) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        User user = new UserController().findUser(login);
        Query query = em.createQuery("delete from Order where status = 'finished' and user = :userParam");
        query.setParameter("userParam", user);
        query.executeUpdate();

        em.getTransaction().commit();
        em.close();
    }

    @PostMapping("update/state/{orderId}/{state}")
    public void updateOrderState(@PathVariable Long orderId, @PathVariable String state) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery("update Order set status = :statusParam where id = :orderIdParam");
        query.setParameter("statusParam", state);
        query.setParameter("orderIdParam", orderId);
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

    @GetMapping("/owner/{login}")
    public List<Order> getOwnerOrders(@PathVariable String login) {
        EntityManager em = entityManagerFactory.createEntityManager();
        User user = new UserController().findUser(login);
        Query query = em.createQuery(
                "SELECT a FROM  Order a WHERE a.user = :userParam"
        );
        query.setParameter("userParam", user);
        List<Order> orders = query.getResultList();
        em.close();

        return orders;
    }

    @GetMapping("/gardener/available")
    public List<Order> getGardenerOrders() {
        EntityManager em = entityManagerFactory.createEntityManager();
        Query query = em.createQuery(
                "SELECT a FROM  Order a WHERE a.status = 'initial'"
        );
        List<Order> orders = query.getResultList();
        em.close();

        return orders;
    }


}
