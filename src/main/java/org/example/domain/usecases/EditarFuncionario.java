package org.example.domain.usecases;

import org.example.domain.model.Funcionario;

import java.util.List;
import java.util.Optional;

public class EditarFuncionario {

    public Funcionario executar(List<Funcionario> banco, int id, Funcionario dadosAtualizados) {

        Optional<Funcionario> existenteOpt = banco.stream()
                .filter(f -> f.getId() == id)
                .findFirst();

        if (existenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Funcionário não encontrado.");
        }

        Funcionario existente = existenteOpt.get();

        boolean cpfJaExiste = banco.stream()
                .anyMatch(f -> f.getId() != id && f.getCpf().equals(dadosAtualizados.getCpf()));

        if (cpfJaExiste) {
            throw new IllegalArgumentException("CPF já cadastrado para outro funcionário.");
        }

        existente.setNome(dadosAtualizados.getNome());
        existente.setCpf(dadosAtualizados.getCpf());
        existente.setIdade(dadosAtualizados.getIdade());
        existente.setSalario(dadosAtualizados.getSalario());

        return existente;
    }
}
