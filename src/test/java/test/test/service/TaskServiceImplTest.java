package test.test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import test.test.dao.TaskRepository;
import test.test.dto.FileRequestDto;
import test.test.dto.ListTasksAnswerDto;
import test.test.model.FileType;
import test.test.model.ListTasks;
import test.test.model.Task;
import test.test.processor.FileProcessor;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("TaskServiceImpl Unit Tests")
class TaskServiceImplTest {

    @Mock
    private FileProcessorFactory fileProcessorFactory;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private FileProcessor fileProcessor;

    private FileRequestDto fileRequestDto;
    private ListTasks listTasks;
    private ListTasksAnswerDto listTasksAnswerDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        fileRequestDto = FileRequestDto.builder()
                .file("path/to/file.csv")
                .type(FileType.CSV)
                .build();

        Task task1 = new Task();
        task1.setNumber(1);
        task1.setDescription("Task 1");

        Task task2 = new Task();
        task2.setNumber(2);
        task2.setDescription("Task 2");

        listTasks = ListTasks.builder()
                .fileName("file.csv")
                .tasksList(Arrays.asList(task1, task2))
                .build();

        listTasksAnswerDto = ListTasksAnswerDto.builder()
                .fileName("file.csv")
                .tasksList(Arrays.asList(
                        new test.test.dto.TaskDto(1, "Task 1"),
                        new test.test.dto.TaskDto(2, "Task 2")
                ))
                .build();
    }

    @Test
    @DisplayName("transformFile - success")
    void transformFile_Success() {
        when(fileProcessorFactory.getFileProcessor(FileType.CSV)).thenReturn(fileProcessor);
        when(fileProcessor.processFile(fileRequestDto.getFile())).thenReturn(listTasks);
        when(taskRepository.existsByFileName(listTasks.getFileName())).thenReturn(false);

        ResponseEntity<Void> response = taskService.transformFile(fileRequestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(taskRepository).save(listTasks);
    }

    @Test
    @DisplayName("transformFile - file already exists")
    void transformFile_FileAlreadyExists() {
        when(fileProcessorFactory.getFileProcessor(FileType.CSV)).thenReturn(fileProcessor);
        when(fileProcessor.processFile(fileRequestDto.getFile())).thenReturn(listTasks);
        when(taskRepository.existsByFileName(listTasks.getFileName())).thenReturn(true);

        ResponseEntity<Void> response = taskService.transformFile(fileRequestDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("transformFile - processor throws exception")
    void transformFile_ProcessorThrows() {
        when(fileProcessorFactory.getFileProcessor(FileType.CSV)).thenReturn(fileProcessor);
        when(fileProcessor.processFile(fileRequestDto.getFile())).thenThrow(new RuntimeException("Processor error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> taskService.transformFile(fileRequestDto));
        assertEquals("Processor error", ex.getMessage());
    }

    @Test
    @DisplayName("findTaskList - success")
    void findTaskList_Success() {
        when(taskRepository.findByFileName("file.csv")).thenReturn(Optional.of(listTasks));
        when(modelMapper.map(listTasks, ListTasksAnswerDto.class)).thenReturn(listTasksAnswerDto);

        ListTasksAnswerDto result = taskService.findTaskList("file.csv");

        assertNotNull(result);
        assertEquals("file.csv", result.getFileName());
        assertEquals(2, result.getTasksList().size());
    }

    @Test
    @DisplayName("findTaskList - file not found")
    void findTaskList_FileNotFound() {
        when(taskRepository.findByFileName("file.csv")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.findTaskList("file.csv"));
    }

    @Test
    @DisplayName("findTaskList - mapper throws exception")
    void findTaskList_MapperThrows() {
        when(taskRepository.findByFileName("file.csv")).thenReturn(Optional.of(listTasks));
        when(modelMapper.map(listTasks, ListTasksAnswerDto.class)).thenThrow(new RuntimeException("Mapping error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> taskService.findTaskList("file.csv"));
        assertEquals("Mapping error", ex.getMessage());
    }

    @Test
    @DisplayName("transformFile - different file types")
    void transformFile_DifferentFileTypes() {
        for (FileType type : FileType.values()) {
            FileRequestDto dto = FileRequestDto.builder()
                    .file("path/to/file." + type.name().toLowerCase())
                    .type(type)
                    .build();

            when(fileProcessorFactory.getFileProcessor(type)).thenReturn(fileProcessor);
            when(fileProcessor.processFile(dto.getFile())).thenReturn(listTasks);
            when(taskRepository.existsByFileName(listTasks.getFileName())).thenReturn(false);

            ResponseEntity<Void> response = taskService.transformFile(dto);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }
    }
}
