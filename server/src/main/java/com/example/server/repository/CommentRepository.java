package com.example.server.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.server.model.Comment;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findTop10ByCharacterIdOrderByTimestampDesc(int characterId);
}

