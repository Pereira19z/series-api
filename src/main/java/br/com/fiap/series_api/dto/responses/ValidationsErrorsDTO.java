package br.com.fiap.series_api.dto.responses;

import java.util.stream.Stream;

public record ValidationsErrorsDTO(Stream<String> validationErrors) {
}
