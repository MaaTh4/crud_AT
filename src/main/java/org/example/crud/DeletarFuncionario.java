package org.example.crud;

import java.util.List;

public class DeletarFuncionario {

    public void executar(List<Funcionario> banco, int id) {
        boolean removido = banco.removeIf(f -> f.getId() == id);

        if (!removido) {
            throw new IllegalArgumentException("Funcionário não encontrado para remoção.");
        }
    }
}
