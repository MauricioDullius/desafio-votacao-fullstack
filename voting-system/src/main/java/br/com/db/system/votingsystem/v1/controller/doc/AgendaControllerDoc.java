package br.com.db.system.votingsystem.v1.controller.doc;

import br.com.db.system.votingsystem.v1.dto.AgendaRequestDTO;
import br.com.db.system.votingsystem.v1.dto.AgendaResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Agenda")
public interface AgendaControllerDoc {

    @Operation(summary = "List all agendas paginated",
            description = "Returns a page of Agenda, sorted by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful retrieval"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content)
    })
    ResponseEntity<Page<AgendaResponseDTO>> findAll(
            @Parameter(description = "Page number, starting at 0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size, max 30") @RequestParam(defaultValue = "20") int size);

    @Operation(summary = "Find agenda by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agenda found"),
            @ApiResponse(responseCode = "404", description = "Agenda not found", content = @Content)
    })
    ResponseEntity<AgendaResponseDTO> findById(@Parameter(description = "ID of the agenda") @PathVariable Long id);

    @Operation(summary = "Create a new agenda")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Agenda created"),
            @ApiResponse(responseCode = "400", description = "Invalid agenda data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Related Assembly not found", content = @Content),
            @ApiResponse(responseCode = "422", description = "Business rule violation (e.g., invalid dates)", content = @Content)
    })
    ResponseEntity<AgendaResponseDTO> create(@RequestBody AgendaRequestDTO agendaDTO);

    @Operation(summary = "Update an existing agenda")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agenda updated"),
            @ApiResponse(responseCode = "400", description = "Invalid agenda data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Agenda or Assembly not found", content = @Content),
            @ApiResponse(responseCode = "422", description = "Business rule violation (e.g., invalid dates)", content = @Content)
    })
    ResponseEntity<AgendaResponseDTO> update(@RequestBody AgendaRequestDTO agendaDTO);

    @Operation(summary = "Delete agenda by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Agenda deleted"),
            @ApiResponse(responseCode = "404", description = "Agenda not found", content = @Content)
    })
    ResponseEntity<Void> delete(@Parameter(description = "ID of the agenda") @PathVariable Long id);
}