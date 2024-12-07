package org.example.service2.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.example.service2.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.example.service2.dto.ClientDto;
import org.example.service2.model.Client;
import org.example.service2.util.ClientMapper;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class LegacyClientService {
    private final ClientRepository repository;
    private final Map<Long, Client> cache;

    public LegacyClientService(ClientRepository repository) {
        this.repository = repository;
        this.cache = new HashMap<>();
    }

    @PostConstruct
    void init() {
        getClient(1L);
    }

    public ClientDto getClient(Long id) {
        log.debug("Call method getClient with id {}", id);
        ClientDto clientDto = null;

        if (cache.containsKey(id)) {
            return ClientMapper.toDto(cache.get(id));
        }

        try {
            Client entity = repository.findById(id).get();
            clientDto = ClientMapper.toDto(entity);
            cache.put(id, entity);
        } catch (Exception e) {
            log.error("Error: ", e);
//            throw new ClientException();
        }

//        log.debug("Client info: {}", clientDto.toString());
        return clientDto;
    }

}
