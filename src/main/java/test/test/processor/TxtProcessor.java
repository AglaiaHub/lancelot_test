package test.test.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import test.test.dto.FileRequestDto;
import test.test.dto.TaskDto;
import test.test.model.FileType;

import java.util.List;

@Log4j2
@Service("txtProcessor")
public class TxtProcessor implements FileProcessor {
    @Override
    public List<TaskDto> processFile(String fileUrl) {
        log.info("Processing txt file");
        return List.of();
    }

    @Override
    public boolean isSupported(FileType type) {
        return type == FileType.TXT;
    }
}
