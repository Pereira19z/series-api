package br.com.fiap.series_api.controller;


import br.com.fiap.series_api.dto.responses.ErrorMessageDTO;
import br.com.fiap.series_api.dto.responses.MessageDTO;
import br.com.fiap.series_api.dto.responses.ValidationsErrorsDTO;
import br.com.fiap.series_api.dto.series.DadosSerieDTO;
import br.com.fiap.series_api.model.Serie;
import br.com.fiap.series_api.repository.SerieRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Optional;

@Tag(name = "Séries", description = "API para realizar CRUD das séries no banco de dados")
@Controller
@RequestMapping("/series")
public class SeriesController {

    @Autowired
    private SerieRepository serieRepository;


    @Operation(summary = "Lista todas as séries do sistema", responses = {
            @ApiResponse(responseCode = "200", description = "Sucesso",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Serie.class))))})
    @GetMapping
    public ResponseEntity listarSeries(){
        try {
            return ResponseEntity.status(200).body(serieRepository.findAll());
        } catch (Exception error){
            return ResponseEntity.status(400).body(new ErrorMessageDTO("Erro ao listar todas as séries"));
        }
    }

    @Operation(summary = "Busca uma série pelo código", responses = {
            @ApiResponse(responseCode = "200", description = "Sucesso",
                    content = @Content(schema = @Schema(implementation = Serie.class))),
            @ApiResponse(responseCode = "404", description = "Série não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorMessageDTO.class)))})
    @GetMapping("/{serieId}")
    public ResponseEntity buscarSeriePorId(@PathVariable Long serieId){
        try {
            Optional<Serie> serie = serieRepository.findById(serieId);

            if(!serie.isPresent()){
                return ResponseEntity.status(404).body(new ErrorMessageDTO("A série informada não existe."));
            }

            return ResponseEntity.status(200).body(serie.get());
        } catch (Exception error){
            return ResponseEntity.status(500).body(new ErrorMessageDTO("Erro ao buscar série por ID"));
        }
    }


    @Operation(summary = "Cadastra uma série no sistema", responses = {
            @ApiResponse(responseCode = "201", description = "Sucesso",
                    content = @Content(schema = @Schema(implementation = Serie.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao cadastrar série",
                    content = @Content(schema = @Schema(implementation = ErrorMessageDTO.class)))})
    @PostMapping
    @Transactional
    public ResponseEntity cadastrarSerie(@RequestBody @Valid DadosSerieDTO dadosSerieDTO, BindingResult bindingResult){
        try {
            if (bindingResult.hasErrors()) {
                var mensagens = bindingResult.getFieldErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage);

                return ResponseEntity.status(400).body(new ValidationsErrorsDTO(mensagens));
            }

            Serie serie = new Serie(dadosSerieDTO);
            Serie saveSerie = serieRepository.save(serie);
            return ResponseEntity.status(201).body(saveSerie);
        } catch (Exception error){
            return ResponseEntity.status(500).body(new ErrorMessageDTO("Erro ao cadastrar série"));
        }
    }

    @Operation(summary = "Altera uma série na base de dados", responses = {
            @ApiResponse(responseCode = "200", description = "Sucesso",
                    content = @Content(schema = @Schema(implementation = Serie.class))),
            @ApiResponse(responseCode = "400", description = "Requisição incorreta",
                    content = @Content(schema = @Schema(implementation = ErrorMessageDTO.class))),
            @ApiResponse(responseCode = "404", description = "Série não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorMessageDTO.class)))})
    @PutMapping("/{serieId}")
    @Transactional
    public ResponseEntity alterarSerie(@PathVariable Long serieId, @RequestBody @Valid DadosSerieDTO dadosSerieDTO, BindingResult bindingResult){
        try {
            Optional<Serie> serie = serieRepository.findById(serieId);

            if(!serie.isPresent()){
                return ResponseEntity.status(404).body(new ErrorMessageDTO("A série informada não existe."));
            }

            if (bindingResult.hasErrors()) {
                var mensagens = bindingResult.getFieldErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage);

                return ResponseEntity.status(400).body(new ValidationsErrorsDTO(mensagens));
            }

            serie.get().alterar(dadosSerieDTO);

            Serie serieAlterada = serieRepository.save(serie.get());

            return ResponseEntity.status(200).body(serieAlterada);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
        }
    }

    @Operation(summary = "Deleta uma série da base de dados", responses = {
            @ApiResponse(responseCode = "204", description = "Sucesso"),
            @ApiResponse(responseCode = "404", description = "Série não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorMessageDTO.class)))})
    @DeleteMapping("/{serieId}")
    @Transactional
    public ResponseEntity deletarSerie(@PathVariable Long serieId){
        try {
            Optional<Serie> serie = serieRepository.findById(serieId);

            if(!serie.isPresent()){
                return ResponseEntity.status(404).body(new ErrorMessageDTO("A série informada não existe."));
            }

            serieRepository.delete(serie.get());

            return ResponseEntity.status(204).body(new MessageDTO("Sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
        }
    }

}
