package org.example.crud;

import java.util.List;

public class CriarFuncionario {

    public Funcionario executar(List<Funcionario> banco, Funcionario novo) {
        boolean cpfJaExiste = banco.stream()
                .anyMatch(f -> f.getCpf().equals(novo.getCpf()));

        if (cpfJaExiste) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }

        banco.add(novo);
        return novo;
    }
}
