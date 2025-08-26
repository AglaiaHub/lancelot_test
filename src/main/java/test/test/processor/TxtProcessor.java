package test.test.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import test.test.model.FileType;
import test.test.model.ListTasks;

@Log4j2
@Service("TXT")
public class TxtProcessor implements FileProcessor {
    @Override
    public ListTasks processFile(String fileUrl) {
        log.info("Processing txt file");

        return new ListTasks().builder()
                .fileName(getNameFromUrl(fileUrl))
                .tasksList(getTaskDtosFromString(getStrings(fileUrl)))
                .build();
    }

    @Override
    public boolean isSupported(FileType type) {
        return type == FileType.TXT;
    }


}
