import com.api.dev.exception.CepInvalidoException;
import com.api.dev.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    private MethodArgumentNotValidException methodArgumentNotValidException;
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bindingResult = mock(BindingResult.class);
        methodArgumentNotValidException = new MethodArgumentNotValidException(null, bindingResult);
    }

    @Test
    void handleValidationExceptionsTest() {
        // Simular um erro de campo
        FieldError fieldError = new FieldError("clienteDTO", "nome", "O nome é obrigatório");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        // Executar o método de exceção
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(methodArgumentNotValidException);

        // Verificar o status da resposta
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verificar o corpo da resposta
        Map<String, String> errors = response.getBody();
        assertEquals(1, errors.size());
        assertEquals("O nome é obrigatório", errors.get("nome"));
    }

    @Test
    void handleCepInvalidoExceptionTest() {
        String mensagemErro = "CEP inválido";
        CepInvalidoException cepInvalidoException = new CepInvalidoException(mensagemErro);

        // Executar o método de exceção
        ResponseEntity<String> response = globalExceptionHandler.handleCepInvalidoException(cepInvalidoException);

        // Verificar o status da resposta
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verificar o corpo da resposta
        assertEquals(mensagemErro, response.getBody());
    }

    @Test
    void handleRuntimeExceptionTest() {
        String mensagemErro = "Erro de execução";
        RuntimeException runtimeException = new RuntimeException(mensagemErro);

        // Executar o método de exceção
        ResponseEntity<String> response = globalExceptionHandler.handleRuntimeException(runtimeException);

        // Verificar o status da resposta
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verificar o corpo da resposta
        assertEquals(mensagemErro, response.getBody());
    }
}
