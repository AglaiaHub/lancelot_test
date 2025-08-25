package test.test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.test.dto.FileRequestDto;
import test.test.dto.ListTasksAnswerDto;
import test.test.model.ListTasks;
import test.test.service.TaskService;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    final TaskService taskService;

    @PostMapping("/transform")
    public ResponseEntity<Void> transformFile(@RequestBody FileRequestDto fileRequestDto) {
        return taskService.transformFile(fileRequestDto);
    }

    @GetMapping("/{fileName}")
    public ListTasksAnswerDto findTasksList(@PathVariable String fileName) {
        return taskService.findTaskList(fileName);
    }

}
