package br.org.fundatec.cep.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;
@Entity
@Table(name = "filmes")
public class Filmes {

    @Id
    private Integer id;

    @Column
    private String nome;

    @Column
    private String genero;

    @Column
    private Integer ano;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Filmes filmes = (Filmes) o;
        return Objects.equals(id, filmes.id) && Objects.equals(nome, filmes.nome) && Objects.equals(genero, filmes.genero) && Objects.equals(ano, filmes.ano);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, genero, ano);
    }

    @Override
    public String toString() {
        return "Filmes" +
                "id = " + id +
                "Nome = " + nome +
                "Genero = " + genero +
                "Ano =" + ano;
    }
}
