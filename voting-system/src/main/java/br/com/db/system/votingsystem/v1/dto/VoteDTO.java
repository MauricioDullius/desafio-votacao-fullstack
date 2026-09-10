package br.com.db.system.votingsystem.v1.dto;

import br.com.db.system.votingsystem.v1.model.enums.VoteState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoteDTO {

    private Long id;

    @NotNull(message = "AgendaId must be provided")
    private Long agendaId;

    @NotBlank(message = "Member CPF must not be null or blank")
    private String memberCpf;

    @NotNull(message = "Vote must be provided")
    private VoteState vote;
}