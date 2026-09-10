package br.com.db.system.votingsystem.v1.mapper;

import br.com.db.system.votingsystem.v1.dto.AssemblyDTO;
import br.com.db.system.votingsystem.v1.model.entity.Assembly;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AssemblyMapperTest {

    @Autowired
    private AssemblyMapper mapper;

    @Test
    void shouldMapToEntity() {
        AssemblyDTO dto = new AssemblyDTO();
        dto.setId(1L);
        dto.setName("Assembly One");
        dto.setStart(LocalDateTime.now());
        dto.setEnd(LocalDateTime.now().plusHours(2));

        Assembly entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getStart(), entity.getStart());
        assertEquals(dto.getEnd(), entity.getEnd());
    }

    @Test
    void shouldMapToDTO() {
        Assembly assembly = new Assembly();
        assembly.setId(2L);
        assembly.setName("Assembly Two");
        assembly.setStart(LocalDateTime.now());
        assembly.setEnd(LocalDateTime.now().plusHours(3));

        AssemblyDTO dto = mapper.toDTO(assembly);

        assertNotNull(dto);
        assertEquals(assembly.getId(), dto.getId());
        assertEquals(assembly.getName(), dto.getName());
        assertEquals(assembly.getStart(), dto.getStart());
        assertEquals(assembly.getEnd(), dto.getEnd());
    }

    @Test
    void shouldMapToDTOList() {
        Assembly a1 = new Assembly();
        a1.setId(1L);
        a1.setName("A1");
        a1.setStart(LocalDateTime.now());
        a1.setEnd(LocalDateTime.now().plusMinutes(30));

        Assembly a2 = new Assembly();
        a2.setId(2L);
        a2.setName("A2");
        a2.setStart(LocalDateTime.now());
        a2.setEnd(LocalDateTime.now().plusMinutes(60));

        List<AssemblyDTO> dtos = mapper.toDTOList(Arrays.asList(a1, a2));

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("A1", dtos.get(0).getName());
        assertEquals("A2", dtos.get(1).getName());
    }
}