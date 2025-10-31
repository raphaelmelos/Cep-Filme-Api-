package br.org.fundatec.cep.service;

import br.org.fundatec.cep.model.Cep;
import br.org.fundatec.cep.model.Filmes;
import br.org.fundatec.cep.repository.FilmesRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class FilmesService {

    @Autowired
    private FilmesRepository filmesRepository;

    public Filmes salvar(@Valid Filmes nome) {
        filmesRepository.save(nome);
        return nome;
    }

    public List<Filmes> buscaTodos() {
        return StreamSupport.stream(filmesRepository.findAll().spliterator(), false).toList();
    }

    public List<Filmes> busca(String nome) {
        return filmesRepository.findFilmesByNomeContains(nome);
    }
}
