package com.pratice.demo.repository;

import com.pratice.demo.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Content, Long> {

    int countByUserId(String userId);

}
