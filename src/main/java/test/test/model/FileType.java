package test.test.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum FileType {
    IMG, TXT, CSV;

    @JsonCreator
    public static FileType fromString(String value) {
        return FileType.valueOf(value.toUpperCase());
    }
}
