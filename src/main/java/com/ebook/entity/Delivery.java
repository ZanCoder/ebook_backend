package com.ebook.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "deliverys")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name_delivery")
    private String nameDelivery;

    @Column(name = "description")
    private String description;

    @Column(name = "shipping_cost")
    private double shipping_cost;

    @OneToMany(mappedBy = "delivery", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order> orderList;
}
