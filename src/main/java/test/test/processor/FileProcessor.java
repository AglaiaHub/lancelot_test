package test.test.processor;

import org.springframework.web.client.RestTemplate;
import test.test.model.ListTasks;
import test.test.model.Task;
import test.test.model.FileType;

import java.util.ArrayList;
import java.util.List;

public interface FileProcessor {
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
            System.err.println("Ошибка при скачивании TXT: " + e.getMessage());
            e.printStackTrace();
        }

        //todo more save
        return "";
    }

    default List<Task> getTaskDtosFromString(String txtContent) {
        List<Task> tasks = new ArrayList<>();
        String[] lines = txtContent.split("\\r?\\n");


        for (String line : lines) {
            String[] parts = line.split(" ", 2); // делим на два: номер и описание
            if (parts.length == 2) {
                Task task = new Task();
                task.setNumber(Integer.parseInt(parts[0]));
                task.setDescription(parts[1]);
                tasks.add(task);
            }
        }
        return tasks;
    }

}
