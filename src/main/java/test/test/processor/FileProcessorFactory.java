package test.test.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import test.test.model.FileType;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FileProcessorFactory {
    private final List<FileProcessor> processors;

    public FileProcessor getFileProcessor(FileType type) {
        return processors.stream()
                .filter(p -> p.isSupported(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported file type: " + type));
    }
}
