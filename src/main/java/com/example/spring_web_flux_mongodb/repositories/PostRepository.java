package com.example.spring_web_flux_mongodb.repositories;

import com.example.spring_web_flux_mongodb.models.entities.Post;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.List;

public interface PostRepository extends ReactiveMongoRepository<Post, String> {
    @Query("{ 'user' : ?0 }")
    Flux<Post> findByUser(ObjectId id);

    @Query("{ 'title': { $regex: ?0, $options: 'i' } }")
    Flux<Post> searchTitle(String text);

    Flux<Post> findByTitleContainingIgnoreCase(String text);

    @Query("{ $and: [ { moment: { $gte: ?1 } }, { moment: { $lte: ?2 } }, { $or: [ { 'title': { $regex: ?0, $options: 'i' } }, { 'body': { $regex: ?0, $options: 'i' } }, { 'comments.text': { $regex: ?0, $options: 'i' } } ] } ] }")
    Flux<Post> fullSearch(String text, Instant startMoment, Instant endMoment);
}