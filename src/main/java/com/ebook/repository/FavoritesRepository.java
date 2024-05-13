package com.ebook.repository;

import com.ebook.entity.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path = "favorites")
public interface FavoritesRepository extends JpaRepository<Favorites, Integer> {
}
