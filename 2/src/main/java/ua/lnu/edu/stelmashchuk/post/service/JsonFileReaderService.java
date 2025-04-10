package ua.lnu.edu.stelmashchuk.post.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonFileReaderService {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> List<T> readListFromJsonFile(String filePath, TypeReference<List<T>> typeReference) {
        try {
            return objectMapper.readValue(new File(filePath), typeReference);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to read JSON: " + filePath, ex);
        }
    }
}
