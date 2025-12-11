package br.com.fiap.series_api.model;


import br.com.fiap.series_api.dto.series.DadosSerieDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serieId;
    private String nome;
    private String genero;
    private Integer anoLancamento;
    private Integer quantidadeTemporadas;
    private String classificacaoEnum;

    public Serie() {
    }

    public Serie(Long serieId, String nome, String genero, Integer anoLancamento, Integer quantidadeTemporadas, String classificacaoEnum) {
        this.serieId = serieId;
        this.nome = nome;
        this.genero = genero;
        this.anoLancamento = anoLancamento;
        this.quantidadeTemporadas = quantidadeTemporadas;
        this.classificacaoEnum = classificacaoEnum;
    }

    public Serie(@Valid DadosSerieDTO dadosSerieDTO) {
        this.nome = dadosSerieDTO.nome();
        this.genero = dadosSerieDTO.genero();
        this.anoLancamento = dadosSerieDTO.anoLancamento();
        this.quantidadeTemporadas = dadosSerieDTO.quantidadeTemporadas();
        this.classificacaoEnum = dadosSerieDTO.classificacaoEnum();
    }


    public void alterar(DadosSerieDTO dadosSerieDTO){
        if(dadosSerieDTO.nome() != null) this.nome = dadosSerieDTO.nome();
        if(dadosSerieDTO.genero() != null) this.genero = dadosSerieDTO.genero();
        if(dadosSerieDTO.anoLancamento() != null) this.anoLancamento = dadosSerieDTO.anoLancamento();
        if(dadosSerieDTO.quantidadeTemporadas() != null) this.quantidadeTemporadas = dadosSerieDTO.quantidadeTemporadas();
        if(dadosSerieDTO.classificacaoEnum() != null) this.classificacaoEnum = dadosSerieDTO.classificacaoEnum();
    }


    public Long getSerieId() {
        return serieId;
    }

    public void setSerieId(Long serieId) {
        this.serieId = serieId;
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

    public Integer getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(Integer anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public Integer getQuantidadeTemporadas() {
        return quantidadeTemporadas;
    }

    public void setQuantidadeTemporadas(Integer quantidadeTemporadas) {
        this.quantidadeTemporadas = quantidadeTemporadas;
    }

    public String getClassificacaoEnum() {
        return classificacaoEnum;
    }

    public void setClassificacaoEnum(String classificacaoEnum) {
        this.classificacaoEnum = classificacaoEnum;
    }
}
