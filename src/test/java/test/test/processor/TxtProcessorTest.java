package test.test.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.test.model.FileType;
import test.test.model.ListTasks;
import test.test.model.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TxtProcessorTest {

    private TxtProcessor txtProcessor;

    @BeforeEach
    void setUp() {
        txtProcessor = new TxtProcessor();
    }

    @Test
    @DisplayName("TXT Processor: support TXT type")
    void isSupported_TXTType_ReturnsTrue() {
        assertTrue(txtProcessor.isSupported(FileType.TXT));
    }

    @Test
    @DisplayName("TXT Processor: unsupported type")
    void isSupported_OtherType_ReturnsFalse() {
        assertFalse(txtProcessor.isSupported(FileType.CSV));
        assertFalse(txtProcessor.isSupported(FileType.IMG));
    }

    @Test
    @DisplayName("Extract file name from URL without query")
    void getNameFromUrl_NoQuery_ReturnsFileName() {
        String url = "http://example.com/path/file.txt";
        String fileName = txtProcessor.getNameFromUrl(url);
        assertEquals("file.txt", fileName);
    }

    @Test
    @DisplayName("Extract file name from URL with query")
    void getNameFromUrl_WithQuery_ReturnsFileName() {
        String url = "http://example.com/path/file.txt?version=1";
        String fileName = txtProcessor.getNameFromUrl(url);
        assertEquals("file.txt", fileName);
    }

    @Test
    @DisplayName("Parse valid TXT content")
    void processFile_ValidTXT_ReturnsTasks() {
        // Override getStrings to return TXT content
        TxtProcessor processor = new TxtProcessor() {
            @Override
            public String getStrings(String fileUrl) {
                return "1 Task one\n2 Task two";
            }
        };

        ListTasks result = processor.processFile("http://example.com/test.txt");

        assertEquals("test.txt", result.getFileName());
        List<Task> tasks = result.getTasksList();
        assertEquals(2, tasks.size());
        assertEquals(1, tasks.get(0).getNumber());
        assertEquals("Task one", tasks.get(0).getDescription());
        assertEquals(2, tasks.get(1).getNumber());
        assertEquals("Task two", tasks.get(1).getDescription());
    }

    @Test
    @DisplayName("Parse empty TXT content")
    void processFile_EmptyTXT_ReturnsEmptyTasks() {
        TxtProcessor processor = new TxtProcessor() {
            @Override
            public String getStrings(String fileUrl) {
                return "";
            }
        };

        ListTasks result = processor.processFile("http://example.com/test.txt");
        assertEquals("test.txt", result.getFileName());
        assertTrue(result.getTasksList().isEmpty());
    }

    @Test
    @DisplayName("Parse TXT with invalid line")
    void processFile_InvalidLine_IgnoresInvalid() {
        TxtProcessor processor = new TxtProcessor() {
            @Override
            public String getStrings(String fileUrl) {
                return "1 Task one\ninvalid_line";
            }
        };

        ListTasks result = processor.processFile("http://example.com/test.txt");
        List<Task> tasks = result.getTasksList();

        assertEquals(1, tasks.size());
        assertEquals(1, tasks.get(0).getNumber());
        assertEquals("Task one", tasks.get(0).getDescription());
    }

    @Test
    @DisplayName("Parse TXT with extra spaces")
    void processFile_TrimSpaces() {
        TxtProcessor processor = new TxtProcessor() {
            @Override
            public String getStrings(String fileUrl) {
                return "1  Task one  \n2  Task two  ";
            }
        };

        ListTasks result = processor.processFile("http://example.com/test.txt");
        List<Task> tasks = result.getTasksList();

        assertEquals(2, tasks.size());
        assertEquals("Task one", tasks.get(0).getDescription());
        assertEquals("Task two", tasks.get(1).getDescription());
    }
}
