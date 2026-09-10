package br.com.db.system.votingsystem.v1.client;

import br.com.db.system.votingsystem.v1.client.dto.ValidationResult;
import br.com.db.system.votingsystem.v1.model.enums.VotingStatus;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class FakeCpfValidatorClient {

    private final Random random = new Random();

    public ValidationResult validateCpf(String cpf) {
        ValidationResult result = new ValidationResult();

        boolean valid = random.nextBoolean();
        result.setValid(valid);

        if (!valid) {
            result.setMessage("Invalid CPF");
            result.setVotingStatus(null);
        } else {
            result.setMessage("Valid CPF");
            boolean canVote = random.nextBoolean();
            result.setVotingStatus(canVote ? VotingStatus.ABLE_TO_VOTE: VotingStatus.UNABLE_TO_VOTE);
        }

        return result;
    }
}