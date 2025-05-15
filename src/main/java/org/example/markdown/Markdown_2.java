package org.example.markdown;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Markdown_2 { // 提取所有图片至图床，并重命名

    private static final String IMAGE_REGEX = "!\\[.*?]\\((.*?)\\)";
    private static final Pattern IMAGE_PATTERN = Pattern.compile(IMAGE_REGEX);
    private static final String URL_PREFIX = "http://8.138.214.176:5173/项目图床/";
    private static final String FILE_PREFIX = "声骸评分系统 项目文档";
    private static final String TYPE = ".png";

    public static void main(String[] args) {
        try {
            processMarkdown(Paths.get("src\\main\\resources\\笔记"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void processMarkdown(Path notesDir) throws Exception {
        List<Path> mdFiles = findMarkdownFiles(notesDir);
        if (mdFiles.size() != 1) {
            throw new RuntimeException("目录中必须且只能包含一个MD文件");
        }
        Path mdFile = mdFiles.getFirst();
        String content = Files.readString(mdFile, StandardCharsets.UTF_8);
        Path imageDir = notesDir.resolveSibling("笔记图片");
        Files.createDirectories(imageDir);
        List<String> newFileNames = processImages(content, mdFile, imageDir);
        String updatedContent = updateMarkdownContent(content, newFileNames);
        Files.writeString(mdFile, updatedContent);
    }

    private static List<Path> findMarkdownFiles(Path dir) throws IOException {
        try (Stream<Path> stream = Files.list(dir)) {
            return stream.filter(p -> p.toString().endsWith(".md"))
                    .collect(Collectors.toList());
        }
    }

    private static List<String> processImages(String content, Path mdFile, Path outputDir) {
        List<String> imageUrls = extractImageUrls(content);
        List<String> newFileNames = new ArrayList<>();
        int counter = 1;
        for (String url : imageUrls) {
            try {
                String extension = getFileExtension(url);
                String newFileName = FILE_PREFIX + "-" + counter++ + extension;
                Path outputPath = outputDir.resolve(newFileName);
                if (isRemoteUrl(url)) {
                    downloadImage(url, outputPath);
                } else {
                    copyLocalFile(resolveLocalPath(url, mdFile), outputPath);
                }
                newFileNames.add(newFileName);
            } catch (Exception e) {
                e.printStackTrace();
                newFileNames.add("");
            }
        }
        return newFileNames;
    }

    private static List<String> extractImageUrls(String content) {
        List<String> urls = new ArrayList<>();
        Matcher matcher = IMAGE_PATTERN.matcher(content);
        while (matcher.find()) {
            urls.add(matcher.group(1));
        }
        return urls;
    }

    private static boolean isRemoteUrl(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

    private static String getFileExtension(String url) {
        try {
            if (isRemoteUrl(url)) {
                return extractRemoteExtension(url);
            } else {
                return extractLocalExtension(url);
            }
        } catch (Exception e) {
            return TYPE;
        }
    }

    private static String extractRemoteExtension(String url) throws Exception {
        Connection.Response response = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36")
                .ignoreContentType(true)
                .execute();
        String contentType = response.contentType();
        String extension = null;
        if (contentType != null) {
            extension = getExtensionFromMimeType(contentType);
        }
        if (extension != null && extension.isEmpty()) {
            extension = getExtensionFromPath(url);
        }
        return extension.isEmpty() ? TYPE : extension;
    }

    private static String extractLocalExtension(String url) {
        String fileName = new File(url).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex != -1) ? fileName.substring(dotIndex) : TYPE;
    }

    private static String getExtensionFromMimeType(String mimeType) {
        String[] parts = mimeType.split(";");
        return switch (parts[0]) {
            case "image/png" -> ".png";
            case "image/jpeg" -> ".jpg";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";
            default -> "";
        };
    }

    private static String getExtensionFromPath(String url) throws URISyntaxException {
        String path = new URI(url).getPath();
        int dotIndex = path.lastIndexOf('.');
        return (dotIndex != -1) ? path.substring(dotIndex) : "";
    }

    private static Path resolveLocalPath(String url, Path mdFile) {
        Path baseDir = mdFile.getParent();
        return Paths.get(url).isAbsolute() ? Paths.get(url) : baseDir.resolve(url);
    }

    private static void downloadImage(String url, Path output) throws IOException {
        Connection.Response response = Jsoup.connect(url)
                .ignoreContentType(true)
                .execute();
        Files.write(output, response.bodyAsBytes());
    }

    private static void copyLocalFile(Path source, Path target) throws IOException {
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
    }

    private static String updateMarkdownContent(String content, List<String> newFileNames) {
        AtomicInteger index = new AtomicInteger(0);
        return IMAGE_PATTERN.matcher(content).replaceAll(mr -> {
            int i = index.getAndIncrement();
            if (i >= newFileNames.size() || newFileNames.get(i).isEmpty()) {
                return mr.group();
            }
            String name = newFileNames.get(i).replaceAll(" ", "%20") + "?v=1";
            return "![" + newFileNames.get(i) + "](" + URL_PREFIX + name + ")";
        });
    }
}