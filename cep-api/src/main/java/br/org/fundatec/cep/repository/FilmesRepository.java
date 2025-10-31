package br.org.fundatec.cep.repository;

import br.org.fundatec.cep.model.Filmes;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FilmesRepository extends CrudRepository<Filmes, Integer> {
    List<Filmes> findFilmesByNomeContains(String nome);

}
