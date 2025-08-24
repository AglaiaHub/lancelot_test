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
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service("csvProcessor")
@RequiredArgsConstructor
public class CsvProcessor implements FileProcessor {

    @Override
    public ListTaskDto processFile(String fileUrl) {
        log.info("Processing csv file");

        return new ListTaskDto().builder()
                .fileName(getNameFromUrl(fileUrl))
                .tasksList(getTaskDtos(getStrings(fileUrl)))
                .build();
    }

    @Override
    public boolean isSupported(FileType type) {
        return type == FileType.CSV;
    }

    private static List<TaskDto> getTaskDtos(String csvContent) {
        CsvToBean<TaskDto> csvToBean = new CsvToBeanBuilder<TaskDto>(new StringReader(csvContent))
                .withType(TaskDto.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();

        return csvToBean.parse();
    }

}
