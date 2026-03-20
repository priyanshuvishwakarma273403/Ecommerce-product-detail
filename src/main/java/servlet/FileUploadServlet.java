package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@WebServlet("/upload")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB- threshold before writing to disk
        maxFileSize = 5*1024*1024, //5MB-maxsingle file size
        maxRequestSize = 20 * 1024 * 1024 //20MB-maxtotal request size
        )

public class FileUploadServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads   ";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/upload.jsp").forward(req,resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Create upload directory
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
        File uploadDir = new File(uploadPath);
        if(!uploadDir.exists()){
            uploadDir.mkdirs();
        }

        try{
            Part filePart = req.getPart("file");
            if(filePart == null || filePart.getSize() == 0){
                req.setAttribute("error","Please select a file to upload.");
                req.getRequestDispatcher("/upload.jsp").forward(req,resp);
                return;
            }

            String originalName = Paths.get(filePart.getSubmittedFileName())
                    .getFileName().toString();

            String contentType = filePart.getContentType();
            if(!isAllowedType(contentType)){
                req.setAttribute("error",
                        "File type not allowed. Only images, PDF, and documents are accepted.");
                req.getRequestDispatcher("/upload.jsp").forward(req, resp);
                return;
            }

            String extension = originalName.substring(originalName.lastIndexOf("."));
            String uniqueName = UUID.randomUUID().toString() + extension;

            String filePath = uploadPath + File.separator + originalName;
//            filePath.write(filePath);


            String description =  req.getParameter("description");

            // Set success attributes
            req.setAttribute("message", "File uploaded successfully!");
            req.setAttribute("fileName", originalName);
            req.setAttribute("savedName", uniqueName);
            req.setAttribute("fileSize", formatFileSize(filePart.getSize()));
            req.setAttribute("contentType", contentType);
            req.setAttribute("fileUrl",
                    req.getContextPath() + "/" + UPLOAD_DIR + "/" + uniqueName);
        }

        catch (IllegalArgumentException e){
            req.setAttribute("error", "File too large. Maximum size is 5MB.");
        }
        catch (Exception e){
            req.setAttribute("error", "Upload failed: " + e.getMessage());
        }
        req.getRequestDispatcher("/upload.jsp").forward(req,resp);
    }

    private boolean isAllowedType(String contentType){
        return contentType != null && (
                contentType.startsWith("image/") ||
                        contentType.equals("application/pdf") ||
                        contentType.equals("application/msword") ||
                        contentType.equals("application/vnd.openxmlformats- officedocument.wordprocessingml.document") ||
                                contentType.equals("text/plain")
                        );
    }

    private String formatFileSize(long bytes){
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024));
    }
}
