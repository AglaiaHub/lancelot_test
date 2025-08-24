package test.test.processor;

import test.test.dto.FileRequestDto;
import test.test.dto.ListTaskDto;
import test.test.dto.TaskDto;
import test.test.model.FileType;

import java.util.List;

public interface FileProcessor {
    ListTaskDto processFile(String fileUrl);

    boolean isSupported(FileType type);
}
