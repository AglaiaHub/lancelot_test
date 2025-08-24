package test.test.dto;

import lombok.*;
import test.test.model.FileType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileRequestDto {

    private String file;
    private FileType type;
}
