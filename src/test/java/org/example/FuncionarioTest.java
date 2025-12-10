package org.example;

import net.jqwik.api.*;
import org.example.domain.model.Funcionario;
import org.junit.jupiter.api.Assertions;

public class FuncionarioTest {

    @Property
    void deveAceitarNomesValidos(@ForAll("nomesValidos") String nome) {
        Funcionario f = new Funcionario(1, nome, "330.225.147-12", 2000.0);
        Assertions.assertNotNull(f);
    }

    @Provide
    Arbitrary<String> nomesValidos() {
        return Arbitraries.strings()
                .withCharRange('a', 'z')
                .ofMinLength(3)
                .ofMaxLength(30);
    }

    @Property
    void salarioDeveSerSempreAcimaDoMinimo(@ForAll("salariosValidos") double salario) {
        Funcionario f = new Funcionario(7, "Teste", "330.225.147-12", salario);
        Assertions.assertTrue(f.getSalario() >= 1300.0);
    }

    @Provide
    Arbitrary<Double> salariosValidos() {
        return Arbitraries.doubles()
                .greaterOrEqual(1300.0)
                .lessOrEqual(20000.0);
    }

    @Property
    void deveFalharComSalarioInvalido(@ForAll double salario) {
        Assume.that(salario < 1300.0);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Funcionario(8, "Erro", "330.225.147-12", salario));
    }

    @Property
    void deveFalharComIdadeInvalida(@ForAll int idade) {
        Assume.that(idade < 16 || idade > 100);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Funcionario(9, "Erro", "111.111.111-11", idade, 2000.0));
    }
}
