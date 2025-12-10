package tests;

import org.example.domain.model.Funcionario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FuncionarioFailEarlyTest {

    @Test
    @DisplayName("Fail early: não deve permitir salário menor que o mínimo ao criar o funcionário")
    void naoDeveCriarFuncionarioComSalarioInvalido() {

        assertThrows(IllegalArgumentException.class, () -> {
            Funcionario f = new Funcionario();
            f.setId(1);
            f.setNome("Teste");
            f.setCpf("123.456.789-10");
            f.setSalario(500.0); // < 1300 → deve lançar exceção imediatamente
        });
    }

    @Test
    @DisplayName("Fail early: não deve aceitar CPF em formato inválido")
    void naoDeveAceitarCpfInvalido() {

        assertThrows(IllegalArgumentException.class, () -> {
            Funcionario f = new Funcionario();
            f.setId(1);
            f.setNome("Teste");
            f.setCpf("12345678900"); // sem máscara → deve falhar
            f.setSalario(2000.0);
        });
    }

    @Test
    @DisplayName("Fail early: não deve aceitar idade fora do intervalo permitido")
    void naoDeveAceitarIdadeForaDoLimite() {

        assertThrows(IllegalArgumentException.class, () -> {
            Funcionario f = new Funcionario();
            f.setId(1);
            f.setNome("Teste");
            f.setCpf("123.456.789-10");
            f.setIdade(10);       // < 16 → inválido
            f.setSalario(2000.0);
        });
    }
}
