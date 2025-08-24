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

        RestTemplate restTemplate = new RestTemplate();

        String fileName = getNameFromUrl(fileUrl);
        List<TaskDto> tasks = new ArrayList<>();

        try {
            // Получаем файл как строку
            String txtContent = restTemplate.getForObject(fileUrl, String.class);
            System.out.println(txtContent);

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

        } catch (Exception e) {
            System.err.println("Ошибка при скачивании TXT: " + e.getMessage());
            e.printStackTrace();
        }

        return new ListTaskDto().builder()
                .fileName(fileName)
                .tasksList(tasks)
                .build();
    }

    @Override
    public boolean isSupported(FileType type) {
        return type == FileType.TXT;
    }

    private String getNameFromUrl(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        int queryIndex = fileName.indexOf("?");
        if (queryIndex != -1) {
            fileName = fileName.substring(0, queryIndex);
        }
        return fileName;
    }
}
