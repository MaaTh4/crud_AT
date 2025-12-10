package tests;

import org.example.controller.ControllerFuncionario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ControllerFailGracefullyTest {

    private final MockMvc mockMvc =
            MockMvcBuilders.standaloneSetup(new ControllerFuncionario()).build();

    @Test
    @DisplayName("Fail gracefully: deve retornar 400 quando salário é inválido")
    void deveRetornar400QuandoSalarioInvalido() throws Exception {

        String jsonFuncionario = """
        {
          "id": 1,
          "nome": "Teste",
          "cpf": "123.456.789-10",
          "idade": 25,
          "salario": 500
        }
        """;

        mockMvc.perform(
                        post("/funcionarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonFuncionario)
                )
                .andExpect(status().isBadRequest());
    }
}