package br.com.db.system.votingsystem.v1.client.dto;

import br.com.db.system.votingsystem.v1.model.enums.VotingStatus;
import lombok.Data;

@Data
public class ValidationResult {
    private boolean valid;
    private String message;
    private VotingStatus votingStatus;

}