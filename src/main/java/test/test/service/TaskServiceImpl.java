package test.test.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import test.test.dto.FileRequestDto;
import test.test.dto.ListTaskDto;
import test.test.dto.TaskDto;
import test.test.processor.FileProcessor;
import test.test.processor.FileProcessorFactory;

import java.awt.*;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{
    final FileProcessorFactory fileProcessorFactory;
    @Override
    public ListTaskDto transformFile(FileRequestDto fileRequestDto) {
        log.info("Transforming file");
        FileProcessor fileProcessor = fileProcessorFactory.getFileProcessor(fileRequestDto.getType());
        List<TaskDto> list = fileProcessor.processFile(fileRequestDto.getFile());

        return null;
    }

    @Override
    public ListTaskDto findTaskList(String fileName) {
        return null;
    }
}
