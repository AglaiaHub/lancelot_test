package test.test.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import test.test.dto.FileRequestDto;
import test.test.dto.ListTaskDto;
import test.test.dto.TaskDto;
import test.test.model.FileType;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service("txtProcessor")
public class TxtProcessor implements FileProcessor {
    @Override
    public ListTaskDto processFile(String fileUrl) {
        log.info("Processing txt file");

        return new ListTaskDto().builder()
                .fileName(getNameFromUrl(fileUrl))
                .tasksList(getTaskDtos(getStrings(fileUrl)))
                .build();
    }

    @Override
    public boolean isSupported(FileType type) {
        return type == FileType.TXT;
    }

    private static List<TaskDto> getTaskDtos(String txtContent) {
        List<TaskDto> tasks = new ArrayList<>();
        String[] lines = txtContent.split("\\r?\\n");


        for (String line : lines) {
            String[] parts = line.split(" ", 2); // делим на два: номер и описание
            if (parts.length == 2) {
                TaskDto task = new TaskDto();
                task.setNumber(Integer.parseInt(parts[0]));
                task.setDescription(parts[1]);
                tasks.add(task);
            }
        }
        return tasks;
    }



}
