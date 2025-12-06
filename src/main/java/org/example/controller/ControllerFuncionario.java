package org.example.controller;

import org.example.crud.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/funcionarios")
@CrossOrigin(origins = "*")
public class ControllerFuncionario {

    // "Banco de dados" em memória
    private static final List<Funcionario> funcionarios = new ArrayList<>();

    private final CriarFuncionario criar = new CriarFuncionario();
    private final EditarFuncionario modificar = new EditarFuncionario();
    private final DeletarFuncionario deletar = new DeletarFuncionario();
    private final VerInfosFuncionario visualizar = new VerInfosFuncionario();

    @GetMapping
    public List<Funcionario> listar() {
        System.out.println("🔵 Frontend pediu a lista. Enviando " + funcionarios.size() + " funcionários.");
        return visualizar.listarTodos(funcionarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable int id) {
        return visualizar.buscarPorId(funcionarios, id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Funcionario novoFuncionario) {
        try {
            criar.executar(funcionarios, novoFuncionario);
            return ResponseEntity.ok(novoFuncionario);
        } catch (IllegalArgumentException e) {
            e.printStackTrace(); // ajuda a depurar se algo der errado
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable int id,
                                       @RequestBody Funcionario dados) {
        try {
            Funcionario atualizado = modificar.executar(funcionarios, id, dados);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable int id) {
        try {
            deletar.executar(funcionarios, id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
