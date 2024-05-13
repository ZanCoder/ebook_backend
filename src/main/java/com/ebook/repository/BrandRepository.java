package com.ebook.repository;

import com.ebook.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path = "brands")
public interface BrandRepository extends JpaRepository<Brand, Integer> {
}
