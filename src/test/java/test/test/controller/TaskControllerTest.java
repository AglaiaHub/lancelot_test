package test.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;
import test.test.dto.FileRequestDto;
import test.test.dto.ListTasksAnswerDto;
import test.test.dto.TaskDto;
import test.test.model.FileType;
import test.test.controller.TaskController;
import test.test.service.TaskService;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Task Controller Tests")
class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private ObjectMapper objectMapper;
    private FileRequestDto fileRequestDto;
    private ListTasksAnswerDto listTasksAnswerDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
        objectMapper = new ObjectMapper();

        fileRequestDto = FileRequestDto.builder()
                .type(FileType.CSV)
                .file("path/to/test-file.csv")
                .build();

        TaskDto taskDto1 = TaskDto.builder()
                .number(1)
                .description("Description 1")
                .build();

        TaskDto taskDto2 = TaskDto.builder()
                .number(2)
                .description("Description 2")
                .build();

        listTasksAnswerDto = ListTasksAnswerDto.builder()
                .fileName("test-file.csv")
                .tasksList(Arrays.asList(taskDto1, taskDto2))
                .build();
    }

    @Test
    @DisplayName("POST /tasks/transform - успешная трансформация файла")
    void transformFile_Success() throws Exception {
        when(taskService.transformFile(any(FileRequestDto.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

        mockMvc.perform(post("/tasks/transform")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fileRequestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /tasks/transform - файл уже существует")
    void transformFile_FileAlreadyExists() throws Exception {
        when(taskService.transformFile(any(FileRequestDto.class)))
                .thenReturn(ResponseEntity.badRequest().build());

        mockMvc.perform(post("/tasks/transform")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fileRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /tasks/transform - тест с разными типами файлов")
    void transformFile_DifferentFileTypes() throws Exception {
        FileRequestDto imgRequest = FileRequestDto.builder()
                .type(FileType.IMG)
                .file("path/to/image.jpg")
                .build();

        FileRequestDto txtRequest = FileRequestDto.builder()
                .type(FileType.TXT)
                .file("path/to/text.txt")
                .build();

        FileRequestDto csvRequest = FileRequestDto.builder()
                .type(FileType.CSV)
                .file("path/to/data.csv")
                .build();

        when(taskService.transformFile(any(FileRequestDto.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

        mockMvc.perform(post("/tasks/transform")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(imgRequest)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/tasks/transform")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(txtRequest)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/tasks/transform")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(csvRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /tasks/transform - невалидный тип файла в JSON")
    void transformFile_InvalidFileType() throws Exception {
        String invalidJson = """
                {
                    "file": "path/to/file.pdf",
                    "type": "PDF"
                }
                """;

        mockMvc.perform(post("/tasks/transform")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /tasks/transform - тип файла в нижнем регистре")
    void transformFile_LowerCaseFileType() throws Exception {
        when(taskService.transformFile(any(FileRequestDto.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

        String jsonWithLowerCase = """
                {
                    "file": "path/to/file.csv",
                    "type": "csv"
                }
                """;

        mockMvc.perform(post("/tasks/transform")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithLowerCase))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /tasks/transform - невалидный JSON")
    void transformFile_InvalidJson() throws Exception {
        mockMvc.perform(post("/tasks/transform")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /tasks/transform - пустое тело запроса")
    void transformFile_EmptyBody() throws Exception {
        when(taskService.transformFile(any(FileRequestDto.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

        mockMvc.perform(post("/tasks/transform")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("GET /tasks/{fileName} - успешное получение списка задач")
    void findTasksList_Success() throws Exception {
        String fileName = "test-file.csv";
        when(taskService.findTaskList(eq(fileName)))
                .thenReturn(listTasksAnswerDto);

        mockMvc.perform(get("/tasks/{fileName}", fileName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fileName").value("test-file.csv"))
                .andExpect(jsonPath("$.tasksList").isArray())
                .andExpect(jsonPath("$.tasksList.length()").value(2))
                .andExpect(jsonPath("$.tasksList[0].number").value(1))
                .andExpect(jsonPath("$.tasksList[0].description").value("Description 1"))
                .andExpect(jsonPath("$.tasksList[1].number").value(2))
                .andExpect(jsonPath("$.tasksList[1].description").value("Description 2"));
    }

    @Test
    @DisplayName("GET /tasks/{fileName} - файл не найден")
    void findTasksList_FileNotFound() throws Exception {
        String fileName = "non-existing-file.csv";

        when(taskService.findTaskList(eq(fileName)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));

        mockMvc.perform(get("/tasks/{fileName}", fileName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // ✅ вместо isInternalServerError()
    }



    @Test
    @DisplayName("GET /tasks/{fileName} - пустой список задач")
    void findTasksList_EmptyTasks() throws Exception {
        String fileName = "empty-file.csv";
        ListTasksAnswerDto emptyResponse = ListTasksAnswerDto.builder()
                .fileName(fileName)
                .tasksList(Collections.emptyList())
                .build();

        when(taskService.findTaskList(eq(fileName)))
                .thenReturn(emptyResponse);

        mockMvc.perform(get("/tasks/{fileName}", fileName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fileName").value(fileName))
                .andExpect(jsonPath("$.tasksList").isArray())
                .andExpect(jsonPath("$.tasksList.length()").value(0));
    }
}
