package br.com.db.system.votingsystem.v1.repository;

import br.com.db.system.votingsystem.v1.model.enums.VoteState;

public interface VoteCountProjection {

    Long getAgendaId();

    VoteState getVote();

    Long getTotal();
}
