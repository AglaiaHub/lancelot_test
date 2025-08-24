package test.test.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import test.test.dto.FileRequestDto;
import test.test.dto.TaskDto;
import test.test.model.FileType;

import java.util.List;

@Log4j2
@Service("csvProcessor")
public class CsvProcessor implements FileProcessor {
    @Override
    public List<TaskDto> processFile(String fileUrl) {
        log.info("Processing csv file");
        return List.of();
    }

    @Override
    public boolean isSupported(FileType type) {
        return type == FileType.CSV;
    }
}
