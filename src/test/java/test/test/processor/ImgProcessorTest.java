package test.test.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.Block;
import software.amazon.awssdk.services.textract.model.BlockType;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextResponse;
import test.test.model.FileType;
import test.test.model.ListTasks;
import test.test.model.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ImgProcessorTest {

    private TextractClient textractClient;
    private ImgProcessor imgProcessor;

    @BeforeEach
    void setUp() {
        textractClient = Mockito.mock(TextractClient.class);
        imgProcessor = new ImgProcessor(textractClient);
    }

    @Test
    @DisplayName("ImgProcessor: support IMG type")
    void isSupported_IMGType_ReturnsTrue() {
        assertTrue(imgProcessor.isSupported(FileType.IMG));
    }

    @Test
    @DisplayName("ImgProcessor: unsupported type")
    void isSupported_OtherType_ReturnsFalse() {
        assertFalse(imgProcessor.isSupported(FileType.CSV));
        assertFalse(imgProcessor.isSupported(FileType.TXT));
    }

    @Test
    @DisplayName("Extract file name from URL")
    void getNameFromUrl_ReturnsFileName() {
        String url = "http://example.com/path/file.png";
        assertEquals("file.png", imgProcessor.getNameFromUrl(url));
    }

    @Test
    @DisplayName("Process image successfully with text")
    void processFile_ValidImage_ReturnsTasks() {
        byte[] fakeImage = new byte[]{0x1, 0x2};

        // Spy to override downloadImage
        ImgProcessor processorSpy = Mockito.spy(imgProcessor);
        Mockito.doReturn(fakeImage).when(processorSpy).downloadImage(Mockito.anyString());

        // Mock TextractClient response
        Block block = Block.builder()
                .blockType(BlockType.LINE)
                .text("1 Task one\n2 Task two")
                .build();
        DetectDocumentTextResponse response = DetectDocumentTextResponse.builder()
                .blocks(block)
                .build();

        Mockito.when(textractClient.detectDocumentText(Mockito.any(software.amazon.awssdk.services.textract.model.DetectDocumentTextRequest.class)))
                .thenReturn(response);

        ListTasks result = processorSpy.processFile("http://example.com/test.png");
        List<Task> tasks = result.getTasksList();

        assertEquals(2, tasks.size());
        assertEquals(1, tasks.get(0).getNumber());
        assertEquals("Task one", tasks.get(0).getDescription());
        assertEquals(2, tasks.get(1).getNumber());
        assertEquals("Task two", tasks.get(1).getDescription());
        assertEquals("test.png", result.getFileName());
    }

    @Test
    @DisplayName("Process image with empty response returns empty list")
    void processFile_EmptyResponse_ReturnsEmptyTasks() {
        byte[] fakeImage = new byte[]{0x1, 0x2};

        ImgProcessor processorSpy = Mockito.spy(imgProcessor);
        Mockito.doReturn(fakeImage).when(processorSpy).downloadImage(Mockito.anyString());

        DetectDocumentTextResponse response = DetectDocumentTextResponse.builder()
                .blocks(List.of())
                .build();

        Mockito.when(textractClient.detectDocumentText(Mockito.any(software.amazon.awssdk.services.textract.model.DetectDocumentTextRequest.class)))
                .thenReturn(response);

        ListTasks result = processorSpy.processFile("http://example.com/test.png");
        assertTrue(result.getTasksList().isEmpty());
    }

    @Test
    @DisplayName("Process image throws exception")
    void processFile_TextractThrows_Exception() {
        byte[] fakeImage = new byte[]{0x1, 0x2};

        ImgProcessor processorSpy = Mockito.spy(imgProcessor);
        Mockito.doReturn(fakeImage).when(processorSpy).downloadImage(Mockito.anyString());

        Mockito.when(textractClient.detectDocumentText(Mockito.any(software.amazon.awssdk.services.textract.model.DetectDocumentTextRequest.class)))
                .thenThrow(new RuntimeException("AWS error"));

        assertThrows(RuntimeException.class, () -> processorSpy.processFile("http://example.com/test.png"));
    }

}
