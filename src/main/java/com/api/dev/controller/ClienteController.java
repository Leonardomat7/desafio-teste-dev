package com.api.dev.controller;

import com.api.dev.dto.ClienteDTO;
import com.api.dev.entity.Cliente;
import com.api.dev.entity.Endereco;
import com.api.dev.repository.ClienteRepository;
import com.api.dev.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ViaCepService viaCepService;

    @PostMapping
    public ResponseEntity<?> criarCliente(@RequestBody ClienteDTO clienteDTO) {
        try {
            Endereco endereco = viaCepService.consultarCep(clienteDTO.getCep());

            Optional<Cliente> clienteExistente = clienteRepository.findByEmail(clienteDTO.getEmail());
            if (clienteExistente.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Cliente já existe com o email fornecido");
            }

            Cliente novoCliente = new Cliente();
            novoCliente.setNome(clienteDTO.getNome());
            novoCliente.setEmail(clienteDTO.getEmail());
            novoCliente.setEndereco(endereco);

            clienteRepository.save(novoCliente);

            return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CEP inválido ou não encontrado");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCliente(@PathVariable Long id, @RequestBody ClienteDTO clienteDTO) {
        Optional<Cliente> clienteExistenteOpt = clienteRepository.findById(id);
        if (clienteExistenteOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
        }

        Cliente clienteExistente = clienteExistenteOpt.get();

        try {
            Endereco endereco = viaCepService.consultarCep(clienteDTO.getCep());

            clienteExistente.setNome(clienteDTO.getNome());
            clienteExistente.setEmail(clienteDTO.getEmail());
            clienteExistente.setEndereco(endereco);

            clienteRepository.save(clienteExistente);

            return ResponseEntity.ok(clienteExistente);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CEP inválido ou não encontrado");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarClientePorId(@PathVariable Long id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
        }
        return ResponseEntity.ok(clienteOpt.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarCliente(@PathVariable Long id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
        }

        clienteRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
