package br.com.ada.reactivejavasw.service;

import br.com.ada.reactivejavasw.converter.ClientConverter;
import br.com.ada.reactivejavasw.dto.ClientDTO;
import br.com.ada.reactivejavasw.dto.ResponseDTO;
import br.com.ada.reactivejavasw.model.Client;
import br.com.ada.reactivejavasw.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class ClientService {

    @Autowired
    private ClientConverter clientConverter;

    @Autowired
    private ClientRepository clientRepository;


    public Mono<ResponseDTO> create(ClientDTO clientDTO) {

        Client client = this.clientConverter.toClient(clientDTO);
        Mono<Client> productMono = this.clientRepository.save(client);
        return productMono
                .map((clientDocument) -> new ResponseDTO("Cliente cadastrado com sucesso!",
                        this.clientConverter.toClientDTO(clientDocument),
                        LocalDateTime.now()))
                .onErrorReturn(new ResponseDTO("Erro ao cadastrar Cliente",
                        new ClientDTO(),
                        LocalDateTime.now()));


    }

    public Flux<ResponseDTO<ClientDTO>> getAll() {
        Flux<Client> productFlux = this.clientRepository.findAll();
        return productFlux
                .map(client -> new ResponseDTO("Listagem de Clientes retornada com sucesso!",
                                              this.clientConverter.toClientDTO(client),
                                              LocalDateTime.now()
        ));
    }

    public Mono<ResponseDTO<ClientDTO>> findById(String id) {
        Mono<Client> productMono = this.clientRepository.findById(id);
        return productMono
                .map(client -> new ResponseDTO("Busca por id retornada com sucesso!",
                                               this.clientConverter.toClientDTO(client),
                                               LocalDateTime.now()
                        ));

    }

    public Mono<ResponseDTO> update(ClientDTO clientDTO) {

        Mono<Client> productMono = this.clientRepository.findById(clientDTO.getId());

        return productMono.flatMap((existingClient) -> {
            existingClient.setId(clientDTO.getId());
            existingClient.setName(clientDTO.getName());
            existingClient.setAge(clientDTO.getAge());
            existingClient.setEmail(clientDTO.getEmail());
            return this.clientRepository.save(existingClient);
        }).map(client -> new ResponseDTO<>("Cliente alterado com sucesso!",
                this.clientConverter.toClientDTO(client),
                LocalDateTime.now()));
    }

    public Mono<ResponseDTO> delete(String id) {
        return this.clientRepository
                        .deleteById(id).map((product) -> new ResponseDTO<>("Cliente removido com sucesso!",
                                                                    null,
                                                                         LocalDateTime.now()));
    }

}
