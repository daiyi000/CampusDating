package com.campusdating.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import jakarta.servlet.http.Part;

/**
 * 文件上传工具类
 * 处理文件上传相关功能
 */
public class FileUploadUtil {

    // 允许的图片类型
    private static final String[] ALLOWED_IMAGE_TYPES = {"image/jpeg", "image/png", "image/gif", "image/jpg"};
    
    // 最大文件大小（5MB）
    private static final int MAX_FILE_SIZE = 5 * 1024 * 1024;
    
    // 上传文件的基础目录
    private static final String UPLOAD_BASE_DIR = "uploads";
    
    // 头像目录
    private static final String AVATAR_DIR = "avatars";
    
    // 活动图片目录
    private static final String EVENT_IMAGE_DIR = "events";
    
    // 默认头像
    private static final String DEFAULT_AVATAR = "default_avatar.png";
    
    /**
     * 保存上传的文件
     * @param part 文件Part
     * @param uploadPath 保存路径
     * @return 保存后的文件名，失败返回null
     */
    public static String saveFile(Part part, String uploadPath) {
        try {
            // 创建上传目录
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 生成唯一文件名
            String fileName = getUniqueFileName(getFileName(part));
            String filePath = uploadPath + File.separator + fileName;
            
            // 保存文件
            try (InputStream input = part.getInputStream();
                 FileOutputStream output = new FileOutputStream(filePath)) {
                
                byte[] buffer = new byte[1024];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
            }
            
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 上传头像
     * @param part 文件Part
     * @param realPath Web应用的实际路径
     * @return 保存后的头像URL，失败返回默认头像
     */
    public static String uploadAvatar(Part part, String realPath) {
        // 验证文件
        if (!isValidImageFile(part)) {
            return DEFAULT_AVATAR;
        }
        
        // 构建上传路径
        String uploadPath = realPath + File.separator + UPLOAD_BASE_DIR + File.separator + AVATAR_DIR;
        
        // 保存文件
        String fileName = saveFile(part, uploadPath);
        if (fileName == null) {
            return DEFAULT_AVATAR;
        }
        
        // 返回头像URL
        return UPLOAD_BASE_DIR + "/" + AVATAR_DIR + "/" + fileName;
    }
    
    /**
     * 上传活动图片
     * @param part 文件Part
     * @param realPath Web应用的实际路径
     * @return 保存后的图片URL，失败返回null
     */
    public static String uploadEventImage(Part part, String realPath) {
        // 验证文件
        if (!isValidImageFile(part)) {
            return null;
        }
        
        // 构建上传路径
        String uploadPath = realPath + File.separator + UPLOAD_BASE_DIR + File.separator + EVENT_IMAGE_DIR;
        
        // 保存文件
        String fileName = saveFile(part, uploadPath);
        if (fileName == null) {
            return null;
        }
        
        // 返回图片URL
        return UPLOAD_BASE_DIR + "/" + EVENT_IMAGE_DIR + "/" + fileName;
    }
    
    /**
     * 验证是否为有效的图片文件
     * @param part 文件Part
     * @return 如果是有效的图片文件返回true
     */
    public static boolean isValidImageFile(Part part) {
        // 检查是否有文件
        if (part == null || part.getSize() == 0) {
            return false;
        }
        
        // 检查文件大小
        if (part.getSize() > MAX_FILE_SIZE) {
            return false;
        }
        
        // 检查文件类型
        String contentType = part.getContentType();
        for (String type : ALLOWED_IMAGE_TYPES) {
            if (type.equals(contentType)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 从Part获取文件名
     * @param part 文件Part
     * @return 文件名
     */
    public static String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] elements = contentDisposition.split(";");
        
        for (String element : elements) {
            if (element.trim().startsWith("filename")) {
                return element.substring(element.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        
        return "unknown";
    }
    
    /**
     * 生成唯一文件名
     * @param originalFileName 原始文件名
     * @return 唯一文件名
     */
    public static String getUniqueFileName(String originalFileName) {
        // 获取文件扩展名
        String extension = "";
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFileName.substring(dotIndex);
        }
        
        // 生成UUID作为文件名
        return UUID.randomUUID().toString() + extension;
    }
    
    /**
     * 删除文件
     * @param filePath 文件路径
     * @return 成功返回true，失败返回false
     */
    public static boolean deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }
        
        File file = new File(filePath);
        return file.exists() && file.isFile() && file.delete();
    }
    
    /**
     * 获取文件扩展名
     * @param fileName 文件名
     * @return 文件扩展名
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        
        return "";
    }
    
    /**
     * 检查文件扩展名是否允许
     * @param fileName 文件名
     * @param allowedExtensions 允许的扩展名数组
     * @return 如果扩展名允许返回true
     */
    public static boolean isAllowedExtension(String fileName, String[] allowedExtensions) {
        String extension = getFileExtension(fileName);
        
        for (String allowedExtension : allowedExtensions) {
            if (extension.equals(allowedExtension.toLowerCase())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 创建目录
     * @param directoryPath 目录路径
     * @return 成功返回true，失败返回false
     */
    public static boolean createDirectory(String directoryPath) {
        if (directoryPath == null || directoryPath.isEmpty()) {
            return false;
        }
        
        File directory = new File(directoryPath);
        return directory.exists() || directory.mkdirs();
    }
}