package com.api.dev.service;

import com.api.dev.entity.Endereco;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ViaCepService {

    public Endereco consultarCep(String cep) {
        String url = String.format("https://viacep.com.br/ws/%s/json/", cep);

        RestTemplate restTemplate = new RestTemplate();
        Endereco endereco = restTemplate.getForObject(url, Endereco.class);

        if (endereco == null || endereco.getCep() == null) {
            throw new RuntimeException("CEP inválido ou não encontrado.");
        }

        return endereco;
    }
}
