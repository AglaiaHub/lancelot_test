package test.test.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.test.dto.FileRequestDto;
import test.test.dto.ListTasksAnswerDto;
import test.test.model.ListTasks;
import test.test.service.TaskService;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Log4j2
public class TaskController {
    final TaskService taskService;

    @PostMapping("/transform")
    public ResponseEntity<Void> transformFile(@RequestBody FileRequestDto fileRequestDto) {
        log.info("Transforming file: type: " + fileRequestDto.getType() + ", uri: " + fileRequestDto.getFile());
        return taskService.transformFile(fileRequestDto);
    }

    @GetMapping("/{fileName}")
    public ListTasksAnswerDto findTasksList(@PathVariable String fileName) {
        return taskService.findTaskList(fileName);
    }

}
