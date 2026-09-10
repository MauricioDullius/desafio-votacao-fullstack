package br.com.db.system.votingsystem.v1.controller;

import br.com.db.system.votingsystem.v1.dto.MemberDTO;
import br.com.db.system.votingsystem.v1.service.MemberService;
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

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    private ObjectMapper objectMapper;
    private MemberDTO memberDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        memberDTO = new MemberDTO();
        memberDTO.setId(1L);
        memberDTO.setName("João");
        memberDTO.setCpf("12345678901");
        memberDTO.setActive(true);
    }

    @Test
    void shouldFindAllMembers() throws Exception {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("id"));
        Page<MemberDTO> page = new PageImpl<>(Collections.singletonList(memberDTO));
        Mockito.when(memberService.findAll(eq(pageable))).thenReturn(page);

        mockMvc.perform(get("/api/member/v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(memberDTO.getId()));
    }

    @Test
    void shouldFindMemberById() throws Exception {
        Mockito.when(memberService.findById(1L)).thenReturn(memberDTO);

        mockMvc.perform(get("/api/member/v1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(memberDTO.getId()));
    }

    @Test
    void shouldCreateMember() throws Exception {
        Mockito.when(memberService.create(any(MemberDTO.class))).thenReturn(memberDTO);

        mockMvc.perform(post("/api/member/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(memberDTO.getId()));
    }

    @Test
    void shouldUpdateMember() throws Exception {
        Mockito.when(memberService.update(any(MemberDTO.class))).thenReturn(memberDTO);

        mockMvc.perform(put("/api/member/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(memberDTO.getId()));
    }

    @Test
    void shouldFindMemberByCpf() throws Exception {
        Mockito.when(memberService.findByCpf("12345678901")).thenReturn(memberDTO);

        mockMvc.perform(get("/api/member/v1/findMemberByCpf/12345678901"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value("12345678901"));
    }

    @Test
    void shouldDeleteByCpf() throws Exception {
        Mockito.doNothing().when(memberService).deleteByCpf("12345678901");

        mockMvc.perform(delete("/api/member/v1")
                        .param("cpf", "12345678901"))
                .andExpect(status().isNoContent());
    }
}