package test.test.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import test.test.dto.FileRequestDto;
import test.test.dto.ListTaskDto;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{
    @Override
    public ListTaskDto transformFile(FileRequestDto fileRequestDto) {
        return null;
    }

    @Override
    public ListTaskDto findTaskList(String fileName) {
        return null;
    }
}
