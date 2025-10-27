package com.example.spring_web_flux_mongodb.services;

import com.example.spring_web_flux_mongodb.models.dto.PostDTO;
import com.example.spring_web_flux_mongodb.models.entities.Post;
import com.example.spring_web_flux_mongodb.repositories.PostRepository;
import com.example.spring_web_flux_mongodb.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Transactional(readOnly = true)
    public PostDTO findById(String id) {
        Post entity = getEntityById(id);
        return new PostDTO(entity);
    }

    public List<PostDTO> findByTitle(String text) {
        List<Post> list = postRepository.searchTitle(text);
        return list.stream().map(x -> new PostDTO(x)).collect(Collectors.toList());
    }

    public List<PostDTO> fullSearch(String text, String start, String end) {
        Instant startMoment = convertMoment(start, Instant.ofEpochMilli(0L));
        Instant endMoment = convertMoment(end, Instant.now());
        List<Post> list = postRepository.fullSearch(text, startMoment, endMoment);
        return list.stream().map(x -> new PostDTO(x)).collect(Collectors.toList());
    }

    private Instant convertMoment(String originalString, Instant alternative) {
        try {
            return Instant.parse(originalString);
        } catch (DateTimeParseException e) {
            return alternative;
        }
    }

    // Método auxiliar para buscar no banco de dados o id
    @Transactional(readOnly = true)
    private Post getEntityById(String id) {
        Optional<Post> obj = postRepository.findById(id);
        Post entity = obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado!"));
        return entity;
    }
}
