package com.example.spring_web_flux_mongodb.services;

import com.example.spring_web_flux_mongodb.models.dto.PostDTO;
import com.example.spring_web_flux_mongodb.models.dto.UserDTO;
import com.example.spring_web_flux_mongodb.models.entities.User;
import com.example.spring_web_flux_mongodb.repositories.UserRepository;
import com.example.spring_web_flux_mongodb.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        List<User> list = userRepository.findAll();
        return list.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDTO findById(String id) {
        User entity = getEntityById(id);
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO insert(UserDTO dto) {
        User entity = new User();
        copyDtoToEntity(dto, entity);
        entity = userRepository.insert(entity);
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO update(String id, UserDTO dto) {
        User entity = getEntityById(id); // Pegar o objeto do banco de dados
        copyDtoToEntity(dto, entity); // Copiar os dados do objeto para o DTO
        entity = userRepository.save(entity);
        return new UserDTO(entity);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(String id) {
        getEntityById(id);
        userRepository.deleteById(id);
    }

    public List<PostDTO> getUserPosts(String id) {
        User user = getEntityById(id);
        return user.getPosts().stream().map(x -> new PostDTO(x)).collect(Collectors.toList()); // Acesso à lista de publicações
    }

    private void copyDtoToEntity(UserDTO dto, User entity) {
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
    }

    // Método auxiliar para buscar no banco de dados o id
    private User getEntityById(String id) {
        Optional<User> obj = userRepository.findById(id);
        User entity = obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado!"));
        return entity;
    }
}