package br.com.ada.reactivejavasw.repository;

import br.com.ada.reactivejavasw.model.Client;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ClientRepository extends ReactiveMongoRepository<Client, String> {

    Mono<Client> findById(String id);

    Mono<Void> deleteById(String id);
}
