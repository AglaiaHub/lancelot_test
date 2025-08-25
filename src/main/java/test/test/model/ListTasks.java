package test.test.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "tasks")
public class ListTasks {
    @Id
    String id;

    @Indexed(unique = true)
    String fileName;
    List<Task> tasksList;
}
