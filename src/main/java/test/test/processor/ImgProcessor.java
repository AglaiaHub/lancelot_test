package test.test.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import test.test.dto.FileRequestDto;
import test.test.dto.ListTaskDto;
import test.test.dto.TaskDto;
import test.test.model.FileType;

import java.util.List;

@Log4j2
@Service("imgProcessor")
public class ImgProcessor implements FileProcessor {
    @Override
    public ListTaskDto processFile(String fileUrl) {
        log.info("Processing img file");

        RestTemplate restTemplate = new RestTemplate();
        try {
            // Получаем файл как строку
            String csvContent = restTemplate.getForObject(fileUrl, String.class);
            System.out.println(csvContent);


        } catch (Exception e) {
            System.err.println("Ошибка при скачивании CSV: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean isSupported(FileType type) {
        return type == FileType.IMG;
    }
}
