package test.test.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import test.test.model.FileType;
import test.test.model.ListTasks;
import test.test.model.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvProcessorTest {

    private CsvProcessor csvProcessor;

    @BeforeEach
    void setUp() {
        csvProcessor = new CsvProcessor();
    }

    @Test
    @DisplayName("CSV Processor: support CSV type")
    void isSupported_CSVType_ReturnsTrue() {
        assertTrue(csvProcessor.isSupported(FileType.CSV));
    }

    @Test
    @DisplayName("CSV Processor: unsupported type")
    void isSupported_OtherType_ReturnsFalse() {
        assertFalse(csvProcessor.isSupported(FileType.TXT));
        assertFalse(csvProcessor.isSupported(FileType.IMG));
    }

    @Test
    @DisplayName("Extract file name from URL without query")
    void getNameFromUrl_NoQuery_ReturnsFileName() {
        String url = "http://example.com/path/file.csv";
        String fileName = csvProcessor.getNameFromUrl(url);
        assertEquals("file.csv", fileName);
    }

    @Test
    @DisplayName("Extract file name from URL with query")
    void getNameFromUrl_WithQuery_ReturnsFileName() {
        String url = "http://example.com/path/file.csv?version=1";
        String fileName = csvProcessor.getNameFromUrl(url);
        assertEquals("file.csv", fileName);
    }

    @Test
    @DisplayName("Parse valid CSV with mocked getStrings")
    void processFile_ValidCSV_ReturnsTasks() {
        // Mock CsvProcessor to override getStrings
        CsvProcessor processor = new CsvProcessor() {
            @Override
            public String getStrings(String fileUrl) {
                // Return CSV content that matches Task class annotations
                return "number,description\n1,Task one\n2,Task two";
            }
        };

        ListTasks result = processor.processFile("http://example.com/test.csv");

        assertEquals("test.csv", result.getFileName());
        List<Task> tasks = result.getTasksList();
        assertEquals(2, tasks.size());

        assertEquals(1, tasks.get(0).getNumber());
        assertEquals("Task one", tasks.get(0).getDescription());

        assertEquals(2, tasks.get(1).getNumber());
        assertEquals("Task two", tasks.get(1).getDescription());
    }

    @Test
    @DisplayName("Parse empty CSV")
    void processFile_EmptyCSV_ReturnsEmptyTasks() {
        CsvProcessor processor = new CsvProcessor() {
            @Override
            public String getStrings(String fileUrl) {
                return "";
            }
        };

        ListTasks result = processor.processFile("http://example.com/test.csv");
        assertEquals("test.csv", result.getFileName());
        assertTrue(result.getTasksList().isEmpty());
    }

    @Test
    @DisplayName("Valid CSV line is parsed correctly")
    void processFile_ValidLine_ReturnsTask() {
        // Spy to mock getTaskDtos and getStrings
        CsvProcessor processor = Mockito.spy(new CsvProcessor());

        Mockito.doReturn("ignored").when(processor).getStrings(Mockito.anyString());
        Mockito.doReturn(List.of(new Task(1, "Task one"))).when(processor).getTaskDtos(Mockito.anyString());

        ListTasks result = processor.processFile("http://example.com/test.csv");
        List<Task> tasks = result.getTasksList();

        assertEquals(1, tasks.size());
        assertEquals(1, tasks.get(0).getNumber());
        assertEquals("Task one", tasks.get(0).getDescription());
        assertEquals("test.csv", result.getFileName());
    }

    @Test
    @DisplayName("CSV with invalid line ignores it")
    void processFile_InvalidLine_IgnoresInvalid() {
        CsvProcessor processor = Mockito.spy(new CsvProcessor());

        Mockito.doReturn("ignored").when(processor).getStrings(Mockito.anyString());
        // Only valid task returned, invalid line ignored
        Mockito.doReturn(List.of(new Task(1, "Task one"))).when(processor).getTaskDtos(Mockito.anyString());

        ListTasks result = processor.processFile("http://example.com/test.csv");
        List<Task> tasks = result.getTasksList();

        assertEquals(1, tasks.size());
        assertEquals(1, tasks.get(0).getNumber());
        assertEquals("Task one", tasks.get(0).getDescription());
    }

    @Test
    @DisplayName("CSV with spaces and empty descriptions")
    void processFile_TrimAndEmptyDescription() {
        CsvProcessor processor = Mockito.spy(new CsvProcessor());

        Mockito.doReturn("ignored").when(processor).getStrings(Mockito.anyString());
        Mockito.doReturn(List.of(
                new Task(1, "Task one"),
                new Task(2, "")
        )).when(processor).getTaskDtos(Mockito.anyString());

        ListTasks result = processor.processFile("http://example.com/test.csv");
        List<Task> tasks = result.getTasksList();

        assertEquals(2, tasks.size());
        assertEquals("Task one", tasks.get(0).getDescription());
        assertEquals("", tasks.get(1).getDescription());
    }

}
