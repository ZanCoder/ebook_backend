package com.ebook.repository;

import com.ebook.entity.ImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path = "image-product")
public interface ImageProductRepository extends JpaRepository<ImageProduct, Integer> {
}
