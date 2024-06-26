package com.ebook.repository;

import com.ebook.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path = "feedbacks")
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
}
