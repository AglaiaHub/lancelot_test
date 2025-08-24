package test.test.processor;

import test.test.dto.FileRequestDto;
import test.test.dto.ListTaskDto;
import test.test.dto.TaskDto;
import test.test.model.FileType;

import java.util.List;

public interface FileProcessor {
    ListTaskDto processFile(String fileUrl);

    boolean isSupported(FileType type);

    default String getNameFromUrl(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        int queryIndex = fileName.indexOf("?");
        if (queryIndex != -1) {
            fileName = fileName.substring(0, queryIndex);
        }
        return fileName;
    }
}
