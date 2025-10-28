package com.example.spring_web_flux_mongodb.config;

import com.example.spring_web_flux_mongodb.models.entities.Post;
import com.example.spring_web_flux_mongodb.models.entities.User;
import com.example.spring_web_flux_mongodb.repositories.PostRepository;
import com.example.spring_web_flux_mongodb.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Arrays;

@Configuration
@Profile("test") // Configuração aplicada ao perfil de testes
public class TestConfig implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    public void run(String... args) throws Exception {
        Mono<Void> deleteUsers = userRepository.deleteAll();
        deleteUsers.subscribe();

        Mono<Void> deletePosts = postRepository.deleteAll();
        deletePosts.subscribe();

        User maria = new User(null, "Maria Brown", "maria@gmail.com");
        User alex = new User(null, "Alex Green", "alex@gmail.com");
        User bob = new User(null, "Bob Grey", "bob@gmail.com");

        Flux<User> insertUsers = userRepository.saveAll(Arrays.asList(maria, alex, bob));
        insertUsers.subscribe();

        Post post1 = new Post(null, Instant.parse("2022-11-21T18:35:24.00Z"), "Partiu viagem", "Vou viajar para São Paulo. Abraços!", maria.getId(), maria.getName());
        Post post2 = new Post(null, Instant.parse("2022-11-23T17:30:24.00Z"), "Bom dia", "Acordei feliz hoje!", maria.getId(), maria.getName());
        post1.addComment("Boa viagem mano!", Instant.parse("2022-11-21T18:52:24.00Z"), alex.getId(), alex.getName());
        post1.addComment("Aproveite!", Instant.parse("2022-11-22T11:35:24.00Z"), bob.getId(), bob.getName());

        post2.addComment("Tenha um ótimo dia!", Instant.parse("2022-11-23T18:35:24.00Z"), alex.getId(), alex.getName());

        Flux<Post> insertPosts = postRepository.saveAll(Arrays.asList(post1, post2));
        insertPosts.subscribe();

//        @Autowired
//        private UserRepository userRepository;
//
//        @Autowired
//        private PostRepository postRepository;
//
//        @Override
//        public void run(String... args) throws Exception {
//
//            userRepository.deleteAll();
//            postRepository.deleteAll();
//
//            User maria = new User(null, "Maria Brown", "maria@gmail.com");
//            User alex = new User(null, "Alex Green", "alex@gmail.com");
//            User bob = new User(null, "Bob Grey", "bob@gmail.com");
//
//            userRepository.saveAll(Arrays.asList(maria, alex, bob));
//
//            Post post1 = new Post(null, Instant.parse("2022-11-21T18:35:24.00Z"), "Partiu viagem",
//                    "Vou viajar para São Paulo. Abraços!", maria.getId(), maria.getName());
//            Post post2 = new Post(null, Instant.parse("2022-11-23T17:30:24.00Z"), "Bom dia", "Acordei feliz hoje!",
//                    maria.getId(), maria.getName());
//
//            post1.addComment("Boa viagem mano!", Instant.parse("2022-11-21T18:52:24.00Z"), alex.getId(), alex.getName());
//            post1.addComment("Aproveite!", Instant.parse("2022-11-22T11:35:24.00Z"), bob.getId(), bob.getName());
//
//            post2.addComment("Tenha um ótimo dia!", Instant.parse("2022-11-23T18:35:24.00Z"), alex.getId(), alex.getName());
//
//            postRepository.saveAll(Arrays.asList(post1, post2));
//
//            maria.getPosts().addAll(Arrays.asList(post1, post2));
//            userRepository.save(maria);
//        }
    }
}
