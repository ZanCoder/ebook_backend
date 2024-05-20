package com.ebook.repository;

import com.ebook.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@RepositoryRestResource(path = "products")
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page <Product> findByNameProductContaining(@RequestParam("nameProduct") String nameProduct, Pageable pageable);

    Page <Product> findByBrandList_id(@RequestParam("id") int id, Pageable pageable);

    Page <Product> findByNameProductContainingAndBrandList_id(@RequestParam("nameProduct") String nameProduct, @RequestParam("id") int id , Pageable pageable);
}
