package test.test.service;

import org.springframework.http.ResponseEntity;
import test.test.dto.FileRequestDto;
import test.test.dto.ListTasksAnswerDto;

public interface TaskService {
    ResponseEntity<Void> transformFile(FileRequestDto fileRequestDto);

    ListTasksAnswerDto findTaskList(String fileName);
}
