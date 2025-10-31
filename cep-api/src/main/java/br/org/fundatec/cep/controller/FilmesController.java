package br.org.fundatec.cep.controller;

import br.org.fundatec.cep.model.Cep;
import br.org.fundatec.cep.model.Filmes;
import br.org.fundatec.cep.service.FilmesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "filmes")
public class FilmesController {

    @Autowired
    private FilmesService filmesService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Filmes>> get() {
        return ResponseEntity.status(HttpStatus.OK).body(filmesService.buscaTodos());
    }

    @PostMapping( consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Filmes> adicionar(@RequestBody @Valid Filmes filme) {
        return ResponseEntity.status(HttpStatus.CREATED).body(filmesService.salvar(filme));
    }



}
