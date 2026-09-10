package br.com.db.system.votingsystem.v1.controller;

import br.com.db.system.votingsystem.v1.controller.doc.MemberControllerDoc;
import br.com.db.system.votingsystem.v1.dto.MemberDTO;
import br.com.db.system.votingsystem.v1.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/member/v1")
public class MemberController implements MemberControllerDoc {

    @Autowired
    private MemberService service;

    @Autowired
    private Validator validator;

    @Override
    @GetMapping
    public ResponseEntity<Page<MemberDTO>> findAll(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "20") int size) {
        int maxSize = 30;
        if (size > maxSize) {
            size = maxSize;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<MemberDTO> resultPage = service.findAll(pageable);
        return ResponseEntity.ok(resultPage);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<MemberDTO> create(@RequestBody @Valid MemberDTO memberDTO) throws Exception {
        MemberDTO created = service.create(memberDTO);
        return ResponseEntity.status(201).body(created);
    }

    @Override
    @PutMapping
    public ResponseEntity<MemberDTO> update(@RequestBody @Valid MemberDTO memberDTO) {
        return ResponseEntity.ok(service.update(memberDTO));
    }

    @Override
    @GetMapping("/findMemberByCpf/{cpf}")
    public ResponseEntity<MemberDTO> findByCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(service.findByCpf(cpf));
    }

    @Override
    @DeleteMapping
    public ResponseEntity<Void> deleteByCpf(@RequestParam String cpf) {
        service.deleteByCpf(cpf);
        return ResponseEntity.noContent().build();
    }

}