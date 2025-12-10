package tests;

import org.example.controller.ControllerFuncionario;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeout;

public class ResilienceTimeoutTest {

    @Test
    void listarFuncionariosNaoDeveUltrapassar5Segundos() {
        ControllerFuncionario controller = new ControllerFuncionario();

        assertTimeout(Duration.ofSeconds(5), () -> {
            controller.listar();
        });
    }
}