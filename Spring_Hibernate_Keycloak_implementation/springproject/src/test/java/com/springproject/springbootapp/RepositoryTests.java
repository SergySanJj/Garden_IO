package com.springproject.springbootapp;

import com.springproject.springbootapp.model.Order;
import com.springproject.springbootapp.model.User;
import com.springproject.springbootapp.repository.OrderRepository;
import com.springproject.springbootapp.repository.UserRepository;
import com.springproject.springbootapp.service.data.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Test(expected = Exception.class)
    public void emptyOrderStateUpdateTest() throws Exception {
        Long id = 123L;
        OrderService orderService = new OrderService(userRepository, orderRepository);
        when(orderRepository.findById(id)).thenReturn(Optional.empty());
        orderService.updateOrderState(id, "new state");
    }

    @Test
    public void uprateOrderStateTest() throws Exception {
        Long id = 123L;
        OrderService orderService = new OrderService(userRepository, orderRepository);
        Order order = new Order();
        order.setId(id);
        order.setStatus(OrderStatus.initial);
        when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        assertEquals(OrderStatus.initial, order.getStatus());
        assertEquals(order, orderService.updateOrderState(id, OrderStatus.finished));


    }

    @Test
    public void userRepositorySaveTest() throws Exception {
        User user = new User();
        user.setLogin("t1");
        userRepository.save(user);
        when(userRepository.findByLogin("t1")).thenReturn(Optional.of(user));
        Optional<User> ou = userRepository.findByLogin("t1");
        if (ou.isPresent()) {
            user = ou.get();
            assertEquals("t1", user.getLogin());
        } else {
            throw new Exception("user repo is not saving users");
        }
    }
}