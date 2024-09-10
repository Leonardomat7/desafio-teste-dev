package com.api.dev.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteDTO {

    private String nome;
    private String email;
    private String cep;

    public ClienteDTO() {
    }

    public ClienteDTO(String nome, String email, String cep) {
        this.nome = nome;
        this.email = email;
        this.cep = cep;
    }
}
