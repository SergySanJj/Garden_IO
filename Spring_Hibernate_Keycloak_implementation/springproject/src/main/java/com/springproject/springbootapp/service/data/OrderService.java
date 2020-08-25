package com.springproject.springbootapp.service.data;

import com.springproject.springbootapp.OrderStatus;
import com.springproject.springbootapp.model.Order;
import com.springproject.springbootapp.model.User;
import com.springproject.springbootapp.repository.OrderRepository;
import com.springproject.springbootapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;

    private final OrderRepository orderRepository;

    public List<Order> findAllOrders(User user) {
        return orderRepository.findAllByUser(user);
    }

    @Transactional
    public Order updateOrderState(Long orderId, String state) throws Exception {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (!optionalOrder.isPresent()) {
            throw new Exception("Bill with id " + orderId + " not found");
        }
        Order order = optionalOrder.get();
        order.setStatus(state);
        return order;
    }

    @Transactional
    public Order addOrder(String login, String info, int quantity) {
        Optional<User> user = userRepository.findByLogin(login);
        if (user.isPresent()) {
            Order order = new Order();
            order.setStatus(OrderStatus.initial);
            order.setQuantity(quantity);
            order.setInfo(info);
            order.setUser(user.get());

            return orderRepository.save(order);
        } else {
            return null;
        }
    }
}
