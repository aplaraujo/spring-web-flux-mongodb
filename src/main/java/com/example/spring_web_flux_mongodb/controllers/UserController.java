package com.example.spring_web_flux_mongodb.controllers;

import com.example.spring_web_flux_mongodb.models.dto.UserDTO;
import com.example.spring_web_flux_mongodb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public Flux<UserDTO> findAll() {
        return userService.findAll();
    }

    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity<UserDTO>> findById(@PathVariable String id) {
        return userService.findById(id).map(dto -> ResponseEntity.ok().body(dto));
    }

    @PostMapping
    public Mono<ResponseEntity<UserDTO>> insert(@RequestBody UserDTO dto, UriComponentsBuilder uriComponentsBuilder) {
        return userService.insert(dto).map(newUser -> ResponseEntity.created(uriComponentsBuilder.path("/users/${id}").buildAndExpand(newUser.getId()).toUri()).body(newUser));
    }

    @PutMapping(value = "/{id}")
    public Mono<ResponseEntity<UserDTO>> update(@PathVariable String id, @RequestBody UserDTO dto) {
        return userService.update(id, dto).map(up -> ResponseEntity.ok().body(up));
    }

    @DeleteMapping(value = "/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return userService.delete(id).then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }

}