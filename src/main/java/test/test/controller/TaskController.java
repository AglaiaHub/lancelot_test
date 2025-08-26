package test.test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.test.dto.FileRequestDto;
import test.test.dto.ListTasksAnswerDto;
import test.test.service.TaskService;



@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Log4j2
public class TaskController {
    final TaskService taskService;

    @Operation(
            summary = "Transform file",
            description = "Transforms the given file according to the specified type and stores the result.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "File transformation request",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = FileRequestDto.class),
                            mediaType = "application/json"
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "File transformed successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("/transform")
    public ResponseEntity<Void> transformFile(@org.springframework.web.bind.annotation.RequestBody FileRequestDto fileRequestDto) {
        log.info("Transforming file: type: " + fileRequestDto.getType() + ", uri: " + fileRequestDto.getFile());
        return taskService.transformFile(fileRequestDto);
    }

    @Operation(
            summary = "Get tasks list by file name",
            description = "Returns the list of tasks extracted from the specified CSV file.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tasks list found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ListTasksAnswerDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "File not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("/{fileName}")
    public ListTasksAnswerDto findTasksList(@PathVariable String fileName) {
        return taskService.findTaskList(fileName);
    }

}
