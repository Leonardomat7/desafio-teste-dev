package com.api.dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClienteEnderecoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClienteEnderecoApplication.class, args);
        synchronized (ClienteEnderecoApplication.class) {
            try {
                ClienteEnderecoApplication.class.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
