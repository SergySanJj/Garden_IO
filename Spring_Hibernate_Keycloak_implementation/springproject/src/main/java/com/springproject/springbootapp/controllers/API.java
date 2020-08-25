package com.springproject.springbootapp.controllers;


import com.springproject.springbootapp.OrderStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class API {
    @RequestMapping(value = "owner/get-orders/{username}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void getOrders(@PathVariable String username, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/order/owner/" + username);
    }

    @RequestMapping(value = "gardener/get-tasks/{username}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void getTasks(@PathVariable String username, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/order/gardener/available");
    }

    @PostMapping(value = "gardener/done-task/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public void markTasksDone(@PathVariable Long taskId) {
        new OrderController().updateOrderState(taskId, OrderStatus.waiting_for_approval);
    }


    @PostMapping(value = "owner/done-order/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public void markOrderFinished(@PathVariable Long orderId) {
        new OrderController().updateOrderState(orderId, OrderStatus.finished);
    }

    @PostMapping(value = "owner/add-order/{username}/{quantity}/{info}")
    @ResponseStatus(HttpStatus.CREATED)
    public String addOrder(@PathVariable String username, @PathVariable int quantity, @PathVariable String info) {
        new OrderController().addOrder(username, info, quantity);
        return username;
    }

    @PostMapping(value = "owner/delete/finished/{login}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void clearFinished(@PathVariable String login) {
        new OrderController().deleteFinished(login);
    }
}
