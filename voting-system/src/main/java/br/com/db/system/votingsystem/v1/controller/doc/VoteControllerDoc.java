package br.com.db.system.votingsystem.v1.controller.doc;

import br.com.db.system.votingsystem.v1.dto.VoteDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Vote")
public interface VoteControllerDoc {

    @Operation(summary = "List all votes",
            description = "Returns a page of Vote, sorted by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful retrieval"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content)
    })
    ResponseEntity<Page<VoteDTO>> findAll(
            @Parameter(description = "Page number, starting at 0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size, max 30") @RequestParam(defaultValue = "20") int size);

    @Operation(summary = "Find vote by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vote found"),
            @ApiResponse(responseCode = "404", description = "Vote not found", content = @Content)
    })
    ResponseEntity<VoteDTO> findById(@Parameter(description = "ID of the vote") @PathVariable Long id);

    @Operation(summary = "Create a new vote")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vote created"),
            @ApiResponse(responseCode = "400", description = "Invalid vote data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Member or Agenda not found", content = @Content),
            @ApiResponse(responseCode = "422", description = "Business rule violation (e.g., duplicate vote)", content = @Content)
    })
    ResponseEntity<VoteDTO> create(@RequestBody VoteDTO voteDTO) throws Exception;

    @Operation(summary = "Update an existing vote")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vote updated"),
            @ApiResponse(responseCode = "400", description = "Invalid vote data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Vote or Member not found", content = @Content)
    })
    ResponseEntity<VoteDTO> update(@RequestBody VoteDTO voteDTO) throws Exception;

    @Operation(summary = "Delete vote by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Vote deleted"),
            @ApiResponse(responseCode = "404", description = "Vote not found", content = @Content)
    })
    ResponseEntity<Void> delete(@Parameter(description = "ID of the vote") @PathVariable Long id);
}