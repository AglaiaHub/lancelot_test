package test.test.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import test.test.model.FileType;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class FileProcessorFactory {
    private final Map<String, FileProcessor> processors;

    public FileProcessor getFileProcessor(FileType type) {

        for (var entry : processors.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue().isSupported(type) + " " + type);
        }

        return processors.get(type.name().toLowerCase() + "Processor");

    }


}
