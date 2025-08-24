package test.test.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import test.test.model.FileType;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FileProcessorFactory {
    private final Map<String, FileProcessor> processors;
//TODO - change to type, but no Autowired

    public FileProcessor getFileProcessor(FileType type) {

        return switch (type) {
            case CSV -> processors.get("csvProcessor");
            case TXT -> processors.get("txtProcessor");
            case IMG -> processors.get("imgProcessor");
            default -> throw new IllegalArgumentException("Unsupported type: " + type);
        };
    }


//    private final List<FileProcessor> processors;
//
//    public FileProcessor getFileProcessor(FileType type) {
//        return processors.stream()
//                .filter(p -> p.isSupported(type))
//                .findFirst()
//                .orElseThrow(() -> new IllegalArgumentException("Unsupported file type: " + type));
//    }
}
