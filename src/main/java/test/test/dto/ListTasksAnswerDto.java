package test.test.dto;

import lombok.*;
import test.test.model.Task;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListTasksAnswerDto {
    String fileName;
    List<TaskDto> tasksList;
}
