package test.test.processor;

import org.springframework.web.client.RestTemplate;
import test.test.model.FileType;
import test.test.model.ListTasks;
import test.test.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface FileProcessor {

    String IMG = "img";
    String CSV = "csv";
    String TXT = "txt";

    ListTasks processFile(String fileUrl);

    boolean isSupported(FileType type);

    default String getNameFromUrl(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        int queryIndex = fileName.indexOf("?");
        if (queryIndex != -1) {
            fileName = fileName.substring(0, queryIndex);
        }
        return fileName;
    }

    default String getStrings(String fileUrl) {

        RestTemplate restTemplate = new RestTemplate();

        try {
            return restTemplate.getForObject(fileUrl, String.class);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to download file: " + fileUrl, e);
        }
    }

    default List<Task> getTaskDtosFromString(String txtContent) {
        List<Task> tasks = new ArrayList<>();
        String[] lines = txtContent.split("\\r?\\n");


        for (String line : lines) {
            String[] parts = line.split(" ", 2); // делим на два: номер и описание
            if (parts.length == 2) {
                Task task = new Task();
                String numberPart = parts[0].replaceAll("\\.$", "");
                task.setNumber(Integer.parseInt(numberPart));
                task.setDescription(parts[1].trim());
                tasks.add(task);
            }
        }
        return tasks;
    }

}
