package com.example.spring_web_flux_mongodb.services;

import com.example.spring_web_flux_mongodb.models.dto.PostDTO;
import com.example.spring_web_flux_mongodb.repositories.PostRepository;
import com.example.spring_web_flux_mongodb.services.exceptions.ObjectNotFoundException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;


@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Mono<PostDTO> findById(String id) {
        return postRepository.findById(id).map(existingPost -> new PostDTO(existingPost)).switchIfEmpty(Mono.error(new ObjectNotFoundException("Objeto não encontrado")));
    }

    public Flux<PostDTO> findByTitle(String text) {
        return postRepository.searchTitle(text).map(found -> new PostDTO(found));
    }

    public Flux<PostDTO> fullSearch(String text, Instant minDate, Instant maxDate) {
        maxDate = maxDate.plusSeconds(86400); // 24 * 60 * 60
        return postRepository.fullSearch(text, minDate, maxDate).map(x -> new PostDTO(x));
    }

    public Flux<PostDTO> findByUser(String id) {
        return postRepository.findByUser(new ObjectId(id)).map(post -> new PostDTO(post));
    }
}
