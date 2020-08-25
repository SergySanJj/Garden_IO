package com.springproject.springbootapp.service;


import com.springproject.springbootapp.model.Order;
import com.springproject.springbootapp.model.OrderConverter;
import com.springproject.springbootapp.model.User;
import com.springproject.springbootapp.model.dto.OrderDTO;
import com.springproject.springbootapp.service.data.OrderService;
import com.springproject.springbootapp.service.data.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderControllerService {
    private final OrderService orderService;
    private final UserService userService;

    private final OrderConverter orderConverter;

    public List<OrderDTO> findAllOrders(String login) {
        Optional<User> op = userService.findByLogin(login);
        if (op.isPresent()) {
            User user = op.get();
            return orderConverter.convertToListDTO(orderService.findAllOrders(user));
        } else {
            return new ArrayList<>();
        }
    }

    public Order addOrder(String login, String info, int quantity) {
        return orderService.addOrder(login, info, quantity);
    }

    public Order updateOrderStatus(Long orderId, String state) throws Exception {
        return orderService.updateOrderState(orderId, state);
    }
}
