package test.test.processor;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import test.test.dto.ListTaskDto;
import test.test.dto.TaskDto;
import test.test.model.FileType;

import java.io.StringReader;
import java.util.List;

@Log4j2
@Service("csvProcessor")
@RequiredArgsConstructor
public class CsvProcessor implements FileProcessor {

    @Override
    public ListTaskDto processFile(String fileUrl) {
        log.info("Processing csv file");

        RestTemplate restTemplate = new RestTemplate();
        List<TaskDto> tasks = List.of();
        String fileName = getNameFromUrl(fileUrl);
        try {
            String csvContent = restTemplate.getForObject(fileUrl, String.class);
            System.out.println(csvContent);


            CsvToBean<TaskDto> csvToBean = new CsvToBeanBuilder<TaskDto>(new StringReader(csvContent))
                    .withType(TaskDto.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            tasks = csvToBean.parse();

        } catch (Exception e) {
            System.err.println("Ошибка при скачивании CSV: " + e.getMessage());
            e.printStackTrace();
        }


        return new ListTaskDto().builder()
                .fileName(fileName)
                .tasksList(tasks)
                .build();
    }



    @Override
    public boolean isSupported(FileType type) {
        return type == FileType.CSV;
    }
}
