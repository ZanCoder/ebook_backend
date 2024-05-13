package com.ebook.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name_product")
    private String nameProduct;

    @Column(name = "description_product", columnDefinition = "text")
    private String descriptionProduct;

    @Column(name = "creator")
    private String creator;

    @Column(name = "isbn")
    private String ISBN;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price_product")
    private double priceProduct;

    @Column(name = "fixed_price")
    private double fixedPrice;

    @Column(name = "average_rating")
    private Double average_rating;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH
    })
    @JoinTable(name = "products_brands",
                joinColumns = @JoinColumn(name = "product_id"),
                inverseJoinColumns = @JoinColumn(name = "brand_id"))
    private List<Brand> brandList;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ImageProduct> imageProductList;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Feedback> feedbackList;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH
    })
    private List<OrderDetail> orderDetailList;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Favorites> favoritesList;
}
