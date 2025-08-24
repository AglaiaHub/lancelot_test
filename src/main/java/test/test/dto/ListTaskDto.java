package test.test.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListTaskDto {
    String fileName;
    List<TaskDto> tasksList;
}
