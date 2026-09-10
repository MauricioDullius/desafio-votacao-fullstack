package br.com.db.system.votingsystem.v1.controller;

import br.com.db.system.votingsystem.v1.dto.VoteDTO;
import br.com.db.system.votingsystem.v1.model.enums.VoteState;
import br.com.db.system.votingsystem.v1.service.VoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VoteController.class)
class VoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VoteService voteService;

    private ObjectMapper objectMapper;
    private VoteDTO voteDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        voteDTO = new VoteDTO();
        voteDTO.setId(1L);
        voteDTO.setAgendaId(1L);
        voteDTO.setMemberCpf("12345678901");
        voteDTO.setVote(VoteState.YES);
    }

    @Test
    void shouldFindAllVotes() throws Exception {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("id"));
        Page<VoteDTO> page = new PageImpl<>(Collections.singletonList(voteDTO));
        Mockito.when(voteService.findAll(eq(pageable))).thenReturn(page);

        mockMvc.perform(get("/api/vote/v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(voteDTO.getId()));
    }

    @Test
    void shouldFindVoteById() throws Exception {
        Mockito.when(voteService.findById(1L)).thenReturn(voteDTO);

        mockMvc.perform(get("/api/vote/v1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(voteDTO.getId()));
    }

    @Test
    void shouldCreateVote() throws Exception {
        Mockito.when(voteService.create(any(VoteDTO.class))).thenReturn(voteDTO);

        mockMvc.perform(post("/api/vote/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voteDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(voteDTO.getId()));
    }

    @Test
    void shouldUpdateVote() throws Exception {
        Mockito.when(voteService.update(any(VoteDTO.class))).thenReturn(voteDTO);

        mockMvc.perform(put("/api/vote/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voteDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(voteDTO.getId()));
    }

    @Test
    void shouldDeleteVoteById() throws Exception {
        Mockito.doNothing().when(voteService).deleteById(1L);

        mockMvc.perform(delete("/api/vote/v1/1"))
                .andExpect(status().isNoContent());
    }
}