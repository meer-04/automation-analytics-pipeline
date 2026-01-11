package com.fw.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public class CSVUtil {

    private CSVUtil() {
    }

    public static void writeCsv(Path filePath, List<String> headers, List<List<String>> rows) {
        try {
            // Ensure parent directories exist
            Files.createDirectories(filePath.getParent());

            // UTF-8 to avoid OS specific issues, overwrite existing file
            try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                // Header
                writer.write(csvLine(headers));
                writer.newLine();

                // Data
                for (List<String> row : rows) {
                    writer.write(csvLine(row));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new FrameworkException("CSV write failed: " + filePath, e);
        }
    }

    private static String csvLine(List<String> fields) {
        return fields.stream().map(CSVUtil::escape).collect(Collectors.joining(","));
    }

    /**
     * null → empty cell
     * comma / quote / newline → quoted
     */
    private static String escape(String value) {
        if (value == null) return "";
        boolean quote = value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r");
        String escaped = value.replace("\"", "\"\"");
        return quote ? "\"" + escaped + "\"" : escaped;
    }

}
