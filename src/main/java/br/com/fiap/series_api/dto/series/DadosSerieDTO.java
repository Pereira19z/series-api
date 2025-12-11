package br.com.fiap.series_api.dto.series;

import jakarta.validation.constraints.*;

public record DadosSerieDTO(
        @NotBlank(message = "O nome da série não pode ser vazio")
        String nome,

        @NotBlank(message = "O gênero não pode ser vazio")
        String genero,

        @NotNull(message = "O ano de lançamento é obrigatório")
        @Min(value = 1900, message = "O ano de lançamento deve ser maior ou igual a 1900")
        Integer anoLancamento,

        @NotNull(message = "A quantidade de temporadas é obrigatória")
        @Positive(message = "A quantidade de temporadas deve ser um número positivo")
        Integer quantidadeTemporadas,

        @NotBlank(message = "A classificação indicativa é obrigatória")
        @Pattern(regexp = "Livre|12\\+|16\\+|18\\+",
                message = "A classificação deve ser uma das seguintes: Livre, 12+, 16+, 18+")
        String classificacaoEnum
) {
}

