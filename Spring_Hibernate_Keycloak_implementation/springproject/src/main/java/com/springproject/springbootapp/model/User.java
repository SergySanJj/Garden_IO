package com.springproject.springbootapp.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PACKAGE)
@Table(name = "users")
public class User {
    @Id
    @Getter
    @Setter
    @GeneratedValue(generator = "users_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Getter
    @Setter
    @Column(name = "login")
    private String login;


    @Getter
    @Setter
    @Column(name = "user_role")
    private String user_role;

    public  void addOrder(Order order){
        this.orders.add(order);
    }

    @Getter(value = AccessLevel.PACKAGE)
    @Setter
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private Set<Order> orders;

    @Override
    public String toString() {
        return "[" + this.id + " : " + this.login + " " + this.user_role + "]";
    }
}
