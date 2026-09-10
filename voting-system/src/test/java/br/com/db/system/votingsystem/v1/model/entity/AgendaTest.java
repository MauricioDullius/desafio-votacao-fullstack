package br.com.db.system.votingsystem.v1.model.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class AgendaTest {

    // Note: the AgendaState resolution logic (IN_VOTING / APPROVED / REJECTED)
    // used to live here as Agenda.getState(), but was moved to
    // AgendaService#resolveState so it can use COUNT queries instead of
    // loading the full votes collection into memory. See AgendaServiceTest
    // for the state-resolution test cases.

    @Test
    public void testGettersAndSetters() {
        Agenda agenda = new Agenda();
        agenda.setId(123L);
        agenda.setDescription("description");
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        agenda.setStart(start);
        agenda.setEnd(end);

        Assembly assembly = new Assembly();
        agenda.setAssembly(assembly);

        assertThat(agenda.getId()).isEqualTo(123L);
        assertThat(agenda.getDescription()).isEqualTo("description");
        assertThat(agenda.getStart()).isEqualTo(start);
        assertThat(agenda.getEnd()).isEqualTo(end);
        assertThat(agenda.getAssembly()).isEqualTo(assembly);
    }
}
