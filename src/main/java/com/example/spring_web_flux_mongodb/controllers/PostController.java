package com.example.spring_web_flux_mongodb.controllers;

import com.example.spring_web_flux_mongodb.controllers.utils.URL;
import com.example.spring_web_flux_mongodb.models.dto.PostDTO;
import com.example.spring_web_flux_mongodb.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping(value = "/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity<PostDTO>> findById(@PathVariable String id) {
        return postService.findById(id).map(dto -> ResponseEntity.ok().body(dto));
    }

    @GetMapping(value = "/titlesearch")
    public Flux<PostDTO> findByTitle(@RequestParam(value = "text", defaultValue = "") String text) throws UnsupportedEncodingException {
        text = URL.decodeParam(text);
        return postService.findByTitle(text);
    }

    @GetMapping(value = "/fullsearch")
    public Flux<PostDTO> fullSearch(
            @RequestParam(value = "text", defaultValue = "") String text,
            @RequestParam(value = "minDate", defaultValue = "") String minDate,
            @RequestParam(value = "maxDate", defaultValue = "") String maxDate) throws UnsupportedEncodingException, ParseException {

        text = URL.decodeParam(text);
        Instant min = URL.convertDate(minDate, Instant.EPOCH);
        Instant max = URL.convertDate(maxDate, Instant.now());

        return postService.fullSearch(text, min, max);

    }

    @GetMapping(value = "/user/{id}")
    public Flux<PostDTO> findByUser(@PathVariable String id) {
        return postService.findByUser(id);
    }
}