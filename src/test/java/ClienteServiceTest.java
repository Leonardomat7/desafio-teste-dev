
import com.api.dev.dto.ClienteDTO;
import com.api.dev.entity.Cliente;
import com.api.dev.entity.Endereco;
import com.api.dev.repository.ClienteRepository;
import com.api.dev.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ViaCepService viaCepService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void criarClienteTest() {
        ClienteDTO clienteDTO = new ClienteDTO("João", "joao@example.com", "12345-678");

        Endereco enderecoMock = new Endereco();
        enderecoMock.setCep("12345-678");
        enderecoMock.setLogradouro("Rua Exemplo");
        when(viaCepService.consultarCep(clienteDTO.getCep())).thenReturn(enderecoMock);

        Cliente clienteSalvo = new Cliente();
        clienteSalvo.setNome("João");
        clienteSalvo.setEmail("joao@example.com");
        clienteSalvo.setEndereco(enderecoMock);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteSalvo);

        Cliente novoCliente = clienteService.criarCliente(clienteDTO);

        assertNotNull(novoCliente);
        assertEquals("João", novoCliente.getNome());
        assertEquals("joao@example.com", novoCliente.getEmail());
        assertEquals("12345-678", novoCliente.getEndereco().getCep());

        verify(viaCepService, times(1)).consultarCep(clienteDTO.getCep());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void deletarClienteTest() {
        Long id = 1L;

        when(clienteRepository.existsById(id)).thenReturn(true);
        clienteService.deletarCliente(id);
        verify(clienteRepository, times(1)).deleteById(id);
    }

    @Test
    void atualizarClienteNaoEncontradoTest() {
        Long id = 1L;
        ClienteDTO clienteDTO = new ClienteDTO("Maria", "maria@example.com", "87654-321");

        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            clienteService.atualizarCliente(id, clienteDTO);
        }, "Cliente não encontrado");

        verify(clienteRepository, times(1)).findById(id);
        verifyNoMoreInteractions(clienteRepository);
    }

    @Test
    void atualizarClienteComCepInvalidoTest() {
        Long id = 1L;
        ClienteDTO clienteDTO = new ClienteDTO("Maria", "maria@example.com", "99999-999");

        Cliente clienteExistente = new Cliente();
        clienteExistente.setNome("João");
        clienteExistente.setEmail("joao@example.com");
        clienteExistente.setEndereco(new Endereco());

        when(clienteRepository.findById(id)).thenReturn(Optional.of(clienteExistente));

        when(viaCepService.consultarCep(clienteDTO.getCep()))
                .thenThrow(new RuntimeException("CEP inválido"));

        assertThrows(RuntimeException.class, () -> {
            clienteService.atualizarCliente(id, clienteDTO);
        });

        verify(viaCepService, times(1)).consultarCep(clienteDTO.getCep());
        verifyNoMoreInteractions(viaCepService);
    }



}
