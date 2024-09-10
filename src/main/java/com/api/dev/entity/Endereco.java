package com.api.dev.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cep;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;
}
