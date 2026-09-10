package br.com.db.system.votingsystem.v1.dto;

import br.com.db.system.votingsystem.v1.model.enums.AgendaState;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AgendaResponseDTO {

    private Long id;

    private String description;

    private LocalDateTime start;

    private LocalDateTime end;

    private AgendaState state;

    private Long assemblyId;
}
