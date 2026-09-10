package br.com.db.system.votingsystem.v1.controller.doc;

import br.com.db.system.votingsystem.v1.dto.MemberDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member")
public interface MemberControllerDoc {

    @Operation(summary = "List all members",
            description = "Returns a page of Member, sorted by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful retrieval"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content)
    })
    ResponseEntity<Page<MemberDTO>> findAll(
            @Parameter(description = "Page number, starting at 0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size, max 30") @RequestParam(defaultValue = "20") int size);

    @Operation(summary = "Find member by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Member found"),
            @ApiResponse(responseCode = "404", description = "Member not found", content = @Content)
    })
    ResponseEntity<MemberDTO> findById(@Parameter(description = "ID of the member") @PathVariable Long id);

    @Operation(summary = "Find member by CPF")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Member found"),
            @ApiResponse(responseCode = "404", description = "Member not found", content = @Content)
    })
    ResponseEntity<MemberDTO> findByCpf(@Parameter(description = "CPF of the member") @PathVariable String cpf);

    @Operation(summary = "Create a new member")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Member created"),
            @ApiResponse(responseCode = "400", description = "Invalid member data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Invalid CPF or related resource not found", content = @Content),
            @ApiResponse(responseCode = "422", description = "Business rule violation (e.g., CPF already exists)", content = @Content)
    })
    ResponseEntity<MemberDTO> create(@RequestBody MemberDTO memberDTO) throws Exception;

    @Operation(summary = "Update an existing member")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Member updated"),
            @ApiResponse(responseCode = "400", description = "Invalid member data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Member not found or invalid CPF", content = @Content)
    })
    ResponseEntity<MemberDTO> update(@RequestBody MemberDTO memberDTO);

    @Operation(summary = "Delete member by CPF")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Member deleted"),
            @ApiResponse(responseCode = "404", description = "Member not found", content = @Content)
    })
    ResponseEntity<Void> deleteByCpf(@Parameter(description = "CPF of the member") @RequestParam String cpf);
}