package com.springproject.springbootapp.model;

import com.springproject.springbootapp.model.dto.OrderDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderConverter {
    public OrderDTO convertToDto(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .info(order.getInfo())
                .quantity(order.getQuantity())
                .status(order.getStatus())
                .build();
    }

    public Order convertToEntity(OrderDTO orderDTO) {
        return Order.builder()
                .id(orderDTO.getId())
                .info(orderDTO.getInfo())
                .quantity(orderDTO.getQuantity())
                .status(orderDTO.getStatus())
                .build();
    }

    public Set<OrderDTO> convertToSetDTO(Set<Order> services) {
        return services.stream().map(this::convertToDto).collect(Collectors.toSet());
    }

    public Set<Order> convertToSetEntity(Set<OrderDTO> services) {
        return services.stream().map(this::convertToEntity).collect(Collectors.toSet());
    }

    public List<OrderDTO> convertToListDTO(List<Order> services) {
        return services.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
