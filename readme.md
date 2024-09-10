# Rewriting the content due to code reset and saving the file

# Projeto: API de Cadastro de Clientes

## Descrição

Este projeto é uma API RESTful desenvolvida com **Spring Boot**, que oferece serviços de cadastro de clientes, incluindo a criação, atualização, consulta e exclusão de clientes. Também integra com a API **ViaCEP** para consulta de endereços a partir de um CEP.

## Funcionalidades

- **Cadastro de clientes**: Criação de clientes com nome, email e endereço via integração com a API ViaCEP.
- **Atualização de clientes**: Edição das informações de clientes existentes.
- **Consulta de clientes por ID**: Busca de clientes cadastrados através de seu ID.
- **Exclusão de clientes**: Remoção de clientes do sistema.
- **Consulta de endereço por CEP**: Integração com a API ViaCEP para obter o endereço completo a partir do CEP.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot** (Spring Data JPA, Spring MVC, RestTemplate)
- **H2 Database** (banco de dados em memória para testes)
- **JUnit 5** (para testes unitários)
- **Mockito** (para simulação de dependências em testes)

## Execução

### Pré-requisitos

- **Java 17** instalado
- **Maven** instalado

