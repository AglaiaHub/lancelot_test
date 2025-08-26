package test.test.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum FileType {

    IMG("img"),
    TXT("txt"),
    CSV("csv");

    private final String type;

    @JsonCreator
    public FileType fromString(String type) {
        return Arrays.stream(FileType.values())
                .filter(value -> value.type.equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid file type: " + type));
    }
}
