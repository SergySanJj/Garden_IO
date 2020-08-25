package com.springproject.springbootapp.repository;

import com.springproject.springbootapp.model.Order;
import com.springproject.springbootapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByUser(User user);

    Optional<Order> findById(Long id);
}
