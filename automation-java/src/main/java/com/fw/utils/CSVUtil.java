package com.fw.utils;

import io.qameta.allure.Step;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public class CSVUtil {

    private CSVUtil() {
    }

    @Step("Write data to CSV file at {filePath}")
    public static void writeCsv(Path filePath, List<String> headers, List<List<String>> rows) {
        try {
            // Ensure parent directories exist
            Files.createDirectories(filePath.getParent());

            if (headers == null || headers.isEmpty()) {
                throw new FrameworkException("CSV write failed: Headers cannot be null or empty.");
            }

            if (rows == null || rows.isEmpty()) {
                throw new FrameworkException("CSV write failed: Rows cannot be null.");
            }

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


    @Step("Validate CSV file at {csvPath}")
    public static void validateCsv(Path csvPath) {
        try {
            if (!Files.exists(csvPath)) {
                throw new RuntimeException("CSV file not generated: " + csvPath.toAbsolutePath());
            }

            if (Files.size(csvPath) == 0) {
                throw new RuntimeException("CSV file is empty: " + csvPath.toAbsolutePath());
            }

            List<String> lines = Files.readAllLines(csvPath);

            if (lines.size() < 2) {
                throw new RuntimeException(
                        "CSV has no data rows (only header or empty): " + csvPath.toAbsolutePath()
                );
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to validate CSV: " + csvPath.toAbsolutePath(), e);
        }
    }


}
