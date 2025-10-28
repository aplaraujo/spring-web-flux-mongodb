package com.example.spring_web_flux_mongodb.repositories;

import com.example.spring_web_flux_mongodb.models.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

    @Query("{ 'email': { $regex: ?0, $options: 'i' } }")
    Mono<User> searchEmail(String email);
}
