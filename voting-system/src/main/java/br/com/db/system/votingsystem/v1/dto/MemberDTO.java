package br.com.db.system.votingsystem.v1.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberDTO {

    private Long id;

    @NotBlank(message = "Name must not be null or blank")
    private String name;

    @NotBlank(message = "CPF must not be null or blank")
    private String cpf;

    private boolean active;
}
