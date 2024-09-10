package com.api.dev.service;

import com.api.dev.dto.ClienteDTO;
import com.api.dev.entity.Cliente;
import com.api.dev.entity.Endereco;
import com.api.dev.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ViaCepService viaCepService;

    @Transactional
    public Cliente criarCliente(ClienteDTO clienteDTO) {
        Endereco endereco = viaCepService.consultarCep(clienteDTO.getCep());

        Cliente cliente = new Cliente();
        cliente.setNome(clienteDTO.getNome());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setEndereco(endereco);

        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente atualizarCliente(Long id, ClienteDTO clienteDTO) {
        return clienteRepository.findById(id)
                .map(clienteExistente -> {
                    clienteExistente.setNome(clienteDTO.getNome());
                    clienteExistente.setEmail(clienteDTO.getEmail());
                    clienteExistente.setEndereco(viaCepService.consultarCep(clienteDTO.getCep()));

                    return clienteRepository.save(clienteExistente);
                })
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    @Transactional
    public void deletarCliente(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
        } else {
            throw new RuntimeException("Cliente não encontrado");
        }
    }

    public Cliente buscarClientePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }
}
