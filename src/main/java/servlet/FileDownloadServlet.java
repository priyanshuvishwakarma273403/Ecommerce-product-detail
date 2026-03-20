package servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;

@WebServlet("/download")
public class FileDownloadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException{
        String fileName = req.getParameter("file");

        if(fileName == null || fileName.contains("..")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid file name");
            return;
        }

        String filePath = getServletContext().getRealPath("/uploads/" + fileName);
        File file = new File(filePath);
        if(!file.exists()){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
            return;
        }

        resp.setContentType(getServletContext().getMimeType(fileName));
        resp.setContentLengthLong(file.length());
        resp.setHeader("Content-Disposition",
                "attachment; filename=\"" + fileName + "\"");

        try(InputStream in = new FileInputStream(file);
            OutputStream out = resp.getOutputStream()){
            byte[] buffer = new byte[4096];

            int byteRead;
            while((byteRead = in.read(buffer)) != -1){
                out.write(buffer, 0, byteRead);
            }
        }
    }
}
