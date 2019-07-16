package com.generate.framework.util;

import java.io.File;
import java.io.IOException;

/**
 * @author SONGJIUHUA386
 * @since 2019/7/12
 */
public class GenerateDirectoryUtil {

    /**
     * 创建目录
     * @param basePath
     * @param tableName
     * @throws IOException
     */
    public static void createDirectory(String basePath, String tableName) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(basePath).append("\\").append(tableName.toLowerCase());
        File contentFile = new File(stringBuilder.toString());
        if (contentFile.exists()){
            return;
        }
        contentFile.mkdir();
    }

    /**
     * 创建文件即java类
     * @param basePath
     * @param tableName
     * @param fileName
     * @param fileType DAO/DTO 等
     * @return
     * @throws IOException
     */
    public static File createFile(String basePath, String tableName, String fileName, String fileType) throws IOException {
        StringBuilder directoryPath = new StringBuilder(basePath).append("\\").append(tableName.toLowerCase()).append("\\").append(fileName);
        File contentFile = new File(directoryPath.toString());
        contentFile.mkdir();
        StringBuilder newFilePath = new StringBuilder(directoryPath.toString()).append("\\").append(tableName).append(fileType).append(".java");
        contentFile = new File(newFilePath.toString());
        if (contentFile.exists()){
            throw new RuntimeException("文件已存在");
        }
        contentFile.createNewFile();
        return contentFile;
    }
}
