package test.test.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import test.test.model.FileType;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FileProcessorFactoryTest {

    private CsvProcessor csvProcessor;
    private TxtProcessor txtProcessor;
    private ImgProcessor imgProcessor;
    private FileProcessorFactory factory;

    @BeforeEach
    void setUp() {
        csvProcessor = Mockito.mock(CsvProcessor.class);
        txtProcessor = Mockito.mock(TxtProcessor.class);
        imgProcessor = Mockito.mock(ImgProcessor.class);

        Map<String, FileProcessor> processors = Map.of(
                "csvProcessor", csvProcessor,
                "txtProcessor", txtProcessor,
                "imgProcessor", imgProcessor
        );

        factory = new FileProcessorFactory(processors);
    }

    @Test
    @DisplayName("Factory returns CSV processor for CSV type")
    void getFileProcessor_CSV_ReturnsCsvProcessor() {
        FileProcessor processor = factory.getFileProcessor(FileType.CSV);
        assertSame(csvProcessor, processor);
    }

    @Test
    @DisplayName("Factory returns TXT processor for TXT type")
    void getFileProcessor_TXT_ReturnsTxtProcessor() {
        FileProcessor processor = factory.getFileProcessor(FileType.TXT);
        assertSame(txtProcessor, processor);
    }

    @Test
    @DisplayName("Factory returns IMG processor for IMG type")
    void getFileProcessor_IMG_ReturnsImgProcessor() {
        FileProcessor processor = factory.getFileProcessor(FileType.IMG);
        assertSame(imgProcessor, processor);
    }

    @Test
    @DisplayName("Factory throws exception for unsupported type")
    void getFileProcessor_Unsupported_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> factory.getFileProcessor(FileType.valueOf("PDF")));
    }

    @Test
    @DisplayName("Factory throws exception for null type")
    void getFileProcessor_Null_ThrowsException() {
        assertThrows(NullPointerException.class, () -> factory.getFileProcessor(null));
    }
}
