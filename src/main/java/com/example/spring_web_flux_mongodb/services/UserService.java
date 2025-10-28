package com.example.spring_web_flux_mongodb.services;

import com.example.spring_web_flux_mongodb.models.dto.UserDTO;
import com.example.spring_web_flux_mongodb.models.entities.User;
import com.example.spring_web_flux_mongodb.repositories.UserRepository;
import com.example.spring_web_flux_mongodb.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Flux<UserDTO> findAll() {
        return userRepository.findAll().map(user -> new UserDTO(user));
    }

    public Mono<UserDTO> findById(String id) {
        return userRepository.findById(id).map(existingUser -> new UserDTO(existingUser)).switchIfEmpty(Mono.error(new ObjectNotFoundException("Objeto não encontrado")));
    }

    public Mono<UserDTO> insert(UserDTO dto) {
        User entity = new User();
        copyDtoToEntity(dto, entity);
        Mono<UserDTO> result = userRepository.save(entity).map(user -> new UserDTO(user));
        return result;
    }

    public Mono<UserDTO> update(String id, UserDTO dto) {
        return userRepository.findById(id).flatMap(existingUser -> {
            existingUser.setName(dto.getName());
            existingUser.setEmail(dto.getEmail());
            return userRepository.save(existingUser);
        }).map(user -> new UserDTO(user)).switchIfEmpty(Mono.error(new ObjectNotFoundException("Objeto não encontrado")));
    }

    public Mono<Void> delete(String id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new ObjectNotFoundException("Objeto não encontrado")))
                        .flatMap(existingUser -> userRepository.delete(existingUser));
    }

    private void copyDtoToEntity(UserDTO dto, User entity) {
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
    }
}