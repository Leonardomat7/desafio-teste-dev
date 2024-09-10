import com.api.dev.entity.Cliente;
import com.api.dev.entity.Endereco;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void criarClienteTest() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João");
        cliente.setEmail("joao@example.com");

        assertNotNull(cliente);
        assertEquals(1L, cliente.getId());
        assertEquals("João", cliente.getNome());
        assertEquals("joao@example.com", cliente.getEmail());
    }

    @Test
    void associarEnderecoAoClienteTest() {
        Cliente cliente = new Cliente();
        cliente.setNome("Maria");
        cliente.setEmail("maria@example.com");

        Endereco endereco = new Endereco();
        endereco.setCep("12345-678");
        endereco.setLogradouro("Rua Teste");
        endereco.setBairro("Bairro Teste");
        endereco.setLocalidade("Cidade Teste");
        endereco.setUf("SP");

        cliente.setEndereco(endereco);

        assertNotNull(cliente.getEndereco());
        assertEquals("12345-678", cliente.getEndereco().getCep());
        assertEquals("Rua Teste", cliente.getEndereco().getLogradouro());
    }

    @Test
    void alterarClienteTest() {
        Cliente cliente = new Cliente();
        cliente.setNome("Ana");
        cliente.setEmail("ana@example.com");

        cliente.setNome("Ana Atualizada");
        cliente.setEmail("ana.atualizada@example.com");

        assertEquals("Ana Atualizada", cliente.getNome());
        assertEquals("ana.atualizada@example.com", cliente.getEmail());
    }

}
