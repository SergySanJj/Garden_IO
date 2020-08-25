package com.springproject.springbootapp.service;

import com.springproject.springbootapp.Application;
import com.springproject.springbootapp.Roles;
import com.springproject.springbootapp.model.User;
import com.springproject.springbootapp.model.dto.OrderDTO;
import com.springproject.springbootapp.repository.OrderRepository;
import com.springproject.springbootapp.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.properties"})
@DirtiesContext
public class OrderControllerServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserControllerService userControllerService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderControllerService orderControllerService;

    @Before
    @Transactional
    public void createDatabase() {
        orderRepository.deleteAll();
        userRepository.deleteAll();

        User user1 = new User();
        user1.setLogin("testUser1");
        user1.setUser_role(Roles.gardener);
        userRepository.save(user1);

        User user2 = new User();
        user2.setLogin("testUser2");
        user2.setUser_role(Roles.owner);
        userRepository.save(user2);

        orderControllerService.addOrder("testUser2", "o1", 123);
        orderControllerService.addOrder("testUser2", "o2", 1234);
    }

    @Test
    public void testInitialOrders() {
        List<OrderDTO> testUserOrders = orderControllerService.findAllOrders("testUser2");
        assertEquals(2, testUserOrders.size());
    }

    @Test
    public void testAddOrder() {
        List<OrderDTO> testUserOrders = orderControllerService.findAllOrders("testUser2");
        int initial = testUserOrders.size();
        orderControllerService.addOrder("testUser2", "o3", 12345);
        List<OrderDTO> testUserOrdersAfter = orderControllerService.findAllOrders("testUser2");
        int after = testUserOrdersAfter.size();
        assertEquals(initial + 1, after);
    }

    @After
    @Transactional
    public void clear() {
        orderRepository.deleteAll();
        userRepository.deleteAll();
    }
}