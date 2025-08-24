package test.test.service;

import test.test.dto.FileRequestDto;
import test.test.dto.ListTaskDto;

public interface TaskService {
    ListTaskDto transformFile(FileRequestDto fileRequestDto);

    ListTaskDto findTaskList(String fileName);
}
