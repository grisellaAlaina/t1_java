package org.example.service2.service;

import org.example.service2.model.Client;

import java.io.IOException;
import java.util.List;

public interface ClientService {
    List<Client> parseJson() throws IOException;
}
