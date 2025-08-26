package test.test.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import test.test.dao.TaskRepository;
import test.test.dto.FileRequestDto;
import test.test.dto.ListTasksAnswerDto;
import test.test.model.ListTasks;
import test.test.processor.FileProcessor;

import java.util.Map;


@Log4j2
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{
    private final Map<String, FileProcessor> processors;
    final TaskRepository taskRepository;
    final ModelMapper modelMapper;


    @Override
    public ResponseEntity<Void> transformFile(FileRequestDto fileRequestDto) {
        log.info("Transforming file");
        FileProcessor fileProcessor = processors.get(fileRequestDto.getType().name());
        ListTasks listTasks = fileProcessor.processFile(fileRequestDto.getFile());

        log.info(listTasks.toString());

        if(taskRepository.existsByFileName(listTasks.getFileName())){
            log.info("File already exists");
            return ResponseEntity.badRequest().build();
        }

        taskRepository.save(listTasks);
        log.info("File saved" + listTasks.getFileName() + " " + listTasks.getId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ListTasksAnswerDto findTaskList(String fileName) {
        log.info("Finding tasks list");
        ListTasks listTasks = taskRepository
                .findByFileName(fileName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404)));
        return modelMapper.map(listTasks, ListTasksAnswerDto.class);
    }
}
