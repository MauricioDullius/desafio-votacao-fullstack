package br.com.db.system.votingsystem.v1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AssemblyDTO {

    private Long id;

    @NotBlank(message = "Name must not be null or blank")
    private String name;

    @NotNull(message = "Start must not be null")
    private LocalDateTime start;

    @NotNull(message = "End must not be null")
    private LocalDateTime end;
}