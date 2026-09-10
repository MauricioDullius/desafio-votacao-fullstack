package br.com.db.system.votingsystem.v1.mapper;

import br.com.db.system.votingsystem.v1.dto.AgendaRequestDTO;
import br.com.db.system.votingsystem.v1.dto.AgendaResponseDTO;
import br.com.db.system.votingsystem.v1.model.entity.Agenda;
import br.com.db.system.votingsystem.v1.model.entity.Assembly;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AgendaMapperTest {

    @Autowired
    private AgendaMapper mapper;

    @Test
    void shouldMapToEntity() {
        AgendaRequestDTO dto = new AgendaRequestDTO();
        dto.setId(1L);
        dto.setDescription("Test agenda");
        dto.setStart(LocalDateTime.now());
        dto.setEnd(LocalDateTime.now().plusHours(1));
        dto.setAssemblyId(5L);

        Agenda entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getDescription(), entity.getDescription());
        assertEquals(dto.getStart(), entity.getStart());
        assertEquals(dto.getEnd(), entity.getEnd());
        // Assembly set separately, mapper does not set Assembly entity from id
    }

    @Test
    void shouldMapToDTO() {
        Agenda agenda = new Agenda();
        agenda.setId(2L);
        agenda.setDescription("Another agenda");
        agenda.setStart(LocalDateTime.now());
        agenda.setEnd(LocalDateTime.now().plusMinutes(30));

        Assembly assembly = new Assembly();
        assembly.setId(10L);
        agenda.setAssembly(assembly);

        AgendaResponseDTO dto = mapper.toDTO(agenda);

        assertNotNull(dto);
        assertEquals(agenda.getId(), dto.getId());
        assertEquals(agenda.getDescription(), dto.getDescription());
        assertEquals(agenda.getStart(), dto.getStart());
        assertEquals(agenda.getEnd(), dto.getEnd());
        assertEquals(assembly.getId(), dto.getAssemblyId());
    }

    @Test
    void shouldMapToDTOList() {
        Agenda a1 = new Agenda();
        a1.setId(1L);
        a1.setDescription("A1");
        a1.setStart(LocalDateTime.now());
        a1.setEnd(LocalDateTime.now().plusMinutes(10));

        Agenda a2 = new Agenda();
        a2.setId(2L);
        a2.setDescription("A2");
        a2.setStart(LocalDateTime.now());
        a2.setEnd(LocalDateTime.now().plusMinutes(20));

        List<AgendaResponseDTO> dtos = mapper.toDTOList(Arrays.asList(a1, a2));

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("A1", dtos.get(0).getDescription());
        assertEquals("A2", dtos.get(1).getDescription());
    }
}
