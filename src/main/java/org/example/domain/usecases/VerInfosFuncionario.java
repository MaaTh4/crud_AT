package org.example.domain.usecases;
import org.example.domain.model.Funcionario;

import java.util.List;
import java.util.Optional;

public class VerInfosFuncionario {

    public List<Funcionario> listarTodos(List<Funcionario> banco) {
        return banco;
    }

    public Optional<Funcionario> buscarPorId(List<Funcionario> banco, int id) {
        return banco.stream().filter(f -> f.getId() == id).findFirst();
    }
}
