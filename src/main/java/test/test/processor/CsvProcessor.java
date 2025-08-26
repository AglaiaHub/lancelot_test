package test.test.processor;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import test.test.model.FileType;
import test.test.model.ListTasks;
import test.test.model.Task;

import java.io.StringReader;
import java.util.List;

import static test.test.processor.FileProcessor.CSV;

@Log4j2
@Service(CSV)
@RequiredArgsConstructor
public class CsvProcessor implements FileProcessor {

    @Override
    public ListTasks processFile(String fileUrl) {
        log.info("Processing csv file");

        return new ListTasks()
                .builder()
                .fileName(getNameFromUrl(fileUrl))
                .tasksList(getTaskDtos(getStrings(fileUrl)))
                .build();
    }

    @Override
    public boolean isSupported(FileType type) {
        return type == FileType.CSV;
    }

    public List<Task> getTaskDtos(String csvContent) {
        CsvToBean<Task> csvToBean = new CsvToBeanBuilder<Task>(new StringReader(csvContent))
                .withType(Task.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();

        return csvToBean.parse();
    }

}
