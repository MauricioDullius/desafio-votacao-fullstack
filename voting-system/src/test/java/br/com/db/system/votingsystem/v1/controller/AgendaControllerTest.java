package br.com.db.system.votingsystem.v1.controller;

import br.com.db.system.votingsystem.v1.dto.AgendaRequestDTO;
import br.com.db.system.votingsystem.v1.dto.AgendaResponseDTO;
import br.com.db.system.votingsystem.v1.model.enums.AgendaState;
import br.com.db.system.votingsystem.v1.service.AgendaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AgendaController.class)
class AgendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgendaService agendaService;

    private ObjectMapper objectMapper;
    private AgendaRequestDTO agendaRequestDTO;
    private AgendaResponseDTO agendaResponseDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        agendaRequestDTO = new AgendaRequestDTO();
        agendaRequestDTO.setId(1L);
        agendaRequestDTO.setDescription("Test Agenda");
        agendaRequestDTO.setAssemblyId(1L);
        agendaRequestDTO.setStart(LocalDateTime.now().plusMinutes(5));
        agendaRequestDTO.setEnd(LocalDateTime.now().plusMinutes(10));

        agendaResponseDTO = new AgendaResponseDTO();
        agendaResponseDTO.setId(1L);
        agendaResponseDTO.setDescription("Test Agenda");
        agendaResponseDTO.setAssemblyId(1L);
        agendaResponseDTO.setStart(agendaRequestDTO.getStart());
        agendaResponseDTO.setEnd(agendaRequestDTO.getEnd());
        agendaResponseDTO.setState(AgendaState.IN_VOTING);
    }

    @Test
    void shouldFindAllAgendas() throws Exception {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("id"));
        Page<AgendaResponseDTO> page = new PageImpl<>(Collections.singletonList(agendaResponseDTO));
        Mockito.when(agendaService.findAll(eq(pageable))).thenReturn(page);

        mockMvc.perform(get("/api/agenda/v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(agendaResponseDTO.getId()));
    }

    @Test
    void shouldFindAgendaById() throws Exception {
        Mockito.when(agendaService.findById(1L)).thenReturn(agendaResponseDTO);

        mockMvc.perform(get("/api/agenda/v1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(agendaResponseDTO.getId()));
    }

    @Test
    void shouldCreateAgenda() throws Exception {
        Mockito.when(agendaService.create(any(AgendaRequestDTO.class))).thenReturn(agendaResponseDTO);

        mockMvc.perform(post("/api/agenda/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendaRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(agendaResponseDTO.getId()));
    }

    @Test
    void shouldUpdateAgenda() throws Exception {
        Mockito.when(agendaService.update(any(AgendaRequestDTO.class))).thenReturn(agendaResponseDTO);

        mockMvc.perform(put("/api/agenda/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendaRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(agendaResponseDTO.getId()));
    }

    @Test
    void shouldDeleteAgenda() throws Exception {
        Mockito.doNothing().when(agendaService).deleteById(1L);

        mockMvc.perform(delete("/api/agenda/v1/1"))
                .andExpect(status().isNoContent());
    }
}
