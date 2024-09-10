import com.api.dev.*;
import com.api.dev.dto.ClienteDTO;
import com.api.dev.entity.Cliente;
import com.api.dev.entity.Endereco;
import com.api.dev.repository.ClienteRepository;
import com.api.dev.service.ViaCepService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ClienteEnderecoApplication.class)
@AutoConfigureMockMvc
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ViaCepService viaCepService;

    @MockBean
    private ClienteRepository clienteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void criarClienteComCepValidoTest() throws Exception {
        ClienteDTO clienteDTO = new ClienteDTO("João", "joao@example.com", "12345000");

        Endereco enderecoMock = new Endereco();
        enderecoMock.setCep("12345000");
        enderecoMock.setLogradouro("Rua Exemplo");
        enderecoMock.setBairro("Bairro Exemplo");
        enderecoMock.setLocalidade("Cidade Exemplo");
        enderecoMock.setUf("SP");

        when(viaCepService.consultarCep("12345000")).thenReturn(enderecoMock);

        when(clienteRepository.findByEmail("joao@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João"))
                .andExpect(jsonPath("$.email").value("joao@example.com"));
    }

    @Test
    void criarClienteComEmailExistenteTest() throws Exception {
        ClienteDTO clienteDTO = new ClienteDTO("Carlos", "carlos@example.com", "12345678");

        Cliente clienteExistente = new Cliente();
        clienteExistente.setNome("Carlos");
        clienteExistente.setEmail("carlos@example.com");

        when(clienteRepository.findByEmail(clienteDTO.getEmail())).thenReturn(Optional.of(clienteExistente));

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Cliente já existe com o email fornecido"));
    }

    @Test
    void atualizarClienteComCepInvalidoTest() throws Exception {
        Long id = 1L;
        ClienteDTO clienteDTO = new ClienteDTO("Maria", "maria@example.com", "99999999");

        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(id);
        clienteExistente.setNome("João");
        clienteExistente.setEmail("joao@example.com");

        when(clienteRepository.findById(id)).thenReturn(Optional.of(clienteExistente));

        when(viaCepService.consultarCep(clienteDTO.getCep())).thenThrow(new RuntimeException("CEP inválido"));

        mockMvc.perform(put("/clientes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("CEP inválido ou não encontrado"));
    }

    @Test
    void deletarClienteExistenteTest() throws Exception {
        Long id = 1L;

        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(id);
        clienteExistente.setNome("Carlos");
        clienteExistente.setEmail("carlos@example.com");

        when(clienteRepository.findById(id)).thenReturn(Optional.of(clienteExistente));

        mockMvc.perform(delete("/clientes/{id}", id))
                .andExpect(status().isNoContent());

        verify(clienteRepository, times(1)).deleteById(id);
    }
    @Test
    void deletarClienteNaoExistenteTest() throws Exception {
        Long id = 999L;

        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/clientes/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cliente não encontrado"));

        verify(clienteRepository, never()).deleteById(id);
    }

    @Test
    void buscarClientePorIdExistenteTest() throws Exception {
        Long id = 1L;

        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome("João");
        cliente.setEmail("joao@example.com");
        cliente.setEndereco(new Endereco());

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/clientes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João"))
                .andExpect(jsonPath("$.email").value("joao@example.com"));
    }
    @Test
    void buscarClientePorIdNaoExistenteTest() throws Exception {
        Long id = 999L;

        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/clientes/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cliente não encontrado"));
    }
    @Test
    void atualizarClienteComSucessoTest() throws Exception {
        Long id = 1L;
        ClienteDTO clienteDTO = new ClienteDTO("Maria", "maria@example.com", "12305021");

        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(id);
        clienteExistente.setNome("João");
        clienteExistente.setEmail("joao@example.com");

        when(clienteRepository.findById(id)).thenReturn(Optional.of(clienteExistente));

        Endereco enderecoMock = new Endereco();
        enderecoMock.setCep("12305021");
        enderecoMock.setLogradouro("Rua Exemplo");
        enderecoMock.setBairro("Bairro Exemplo");
        enderecoMock.setLocalidade("Cidade Exemplo");
        enderecoMock.setUf("SP");

        when(viaCepService.consultarCep(clienteDTO.getCep())).thenReturn(enderecoMock);

        mockMvc.perform(put("/clientes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria"))
                .andExpect(jsonPath("$.email").value("maria@example.com"))
                .andExpect(jsonPath("$.endereco.cep").value("12305021"));
    }


}

