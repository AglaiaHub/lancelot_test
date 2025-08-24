package test.test.dto;

import lombok.*;
import test.test.model.FileType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileRequestDto {
    /*
    The URL path to the file
     */
    private String file;
    private FileType type;
}
