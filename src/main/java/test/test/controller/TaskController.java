package test.test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import test.test.dto.FileRequestDto;
import test.test.dto.ListTaskDto;
import test.test.service.TaskService;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    final TaskService taskService;

    @PostMapping("/transform")
    public ListTaskDto transformFile(@RequestBody FileRequestDto fileRequestDto) {
        return taskService.transformFile(fileRequestDto);
    }

    @GetMapping("/{fileName}")
    public ListTaskDto findTaskList(@PathVariable String fileName) {
        return taskService.findTaskList(fileName);
    }

}
