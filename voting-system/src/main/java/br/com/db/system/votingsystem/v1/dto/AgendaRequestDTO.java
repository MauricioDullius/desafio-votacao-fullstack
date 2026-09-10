package br.com.db.system.votingsystem.v1.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AgendaRequestDTO {

    private Long id;

    @NotBlank(message = "Description must not be null or blank")
    private String description;

    private LocalDateTime start;

    private LocalDateTime end;

    private Long assemblyId;
}
