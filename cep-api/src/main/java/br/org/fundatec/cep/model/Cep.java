package br.org.fundatec.cep.model;

import br.org.fundatec.cep.annotation.CepValidation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;


@Entity
@Table(name = "cep")
public class Cep {


    @Id
    @CepValidation(message = "Cep nulo ou em faixa invalida")
    private Integer cep;


    @Column
    @NotBlank(message = "Cidade nao pode ser nulo")
    private String cidade;


    @Column
    @NotBlank(message = "UF nao pode ser nulo")
    @Size(min = 2, max = 2, message = "UF nao pode ter mais de dois caracteres e menos de dois")
    private String uf;

    @Column
    private String logradouro;

    public Cep() {
    }

    public Cep(Integer cep, String cidade, String uf, String logradouro) {
        this.cep = cep;
        this.cidade = cidade;
        this.uf = uf;
        this.logradouro = logradouro;
    }


    public Integer getCep() {
        return cep;
    }

    public void setCep(Integer cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cep cep1 = (Cep) o;
        return Objects.equals(cep, cep1.cep);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cep);
    }
}
