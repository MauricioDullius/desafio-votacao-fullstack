package br.com.db.system.votingsystem.v1.model.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class AssemblyTest {

    @Test
    void testGettersAndSetters() {
        Assembly assembly = new Assembly();

        assembly.setId(1L);
        assembly.setName("Assembly");

        LocalDateTime start = LocalDateTime.of(2025, 7, 2, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 7, 2, 12, 0);

        assembly.setStart(start);
        assembly.setEnd(end);

        List<Agenda> agendas = new ArrayList<>();
        Agenda agenda = new Agenda();
        agenda.setId(10L);
        agendas.add(agenda);

        assembly.setAgendas(agendas);

        assertEquals(1L, assembly.getId());
        assertEquals("Assembly", assembly.getName());
        assertEquals(start, assembly.getStart());
        assertEquals(end, assembly.getEnd());
        assertEquals(1, assembly.getAgendas().size());
        assertEquals(10L, assembly.getAgendas().get(0).getId());
    }

    @Test
    void testEqualsAndHashCode() {
        Assembly a1 = new Assembly();
        a1.setId(1L);

        Assembly a2 = new Assembly();
        a2.setId(1L);

        Assembly a3 = new Assembly();
        a3.setId(2L);

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());

        assertNotEquals(a1, a3);
        assertNotEquals(a1.hashCode(), a3.hashCode());
    }
}