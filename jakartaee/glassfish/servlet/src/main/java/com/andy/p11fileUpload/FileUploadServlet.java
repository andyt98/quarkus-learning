package com.andy.p11fileUpload;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/upload")
@MultipartConfig
public class FileUploadServlet extends HttpServlet {
    private final static Logger LOGGER = Logger.getLogger(FileUploadServlet.class.getCanonicalName());
    private static final String DEFAULT_UPLOAD_DIR = "/tmp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("""
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>File Upload Example</title>
                    </head>
                    <body>
                        <h2>Upload a File</h2>
                        <form method="POST" action="upload" enctype="multipart/form-data">
                            File:
                            <input type="file" name="file" id="file" /> <br/>
                            Destination:
                            <input type="text" value="%s" name="destination"/>
                            <br/>
                            <input type="submit" value="Upload" name="upload" id="upload" />
                        </form>
                    </body>
                    </html>
                """.formatted(DEFAULT_UPLOAD_DIR));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Create path components to save the file
        final String path = request.getParameter("destination");
        final String uploadDir = (path != null && !path.isEmpty()) ? path : DEFAULT_UPLOAD_DIR;
        final Part filePart = request.getPart("file");
        final String fileName = getFileName(filePart);

        try (PrintWriter writer = response.getWriter();
             OutputStream out = new FileOutputStream(uploadDir + File.separator + fileName);
             InputStream fileContent = filePart.getInputStream()) {

            int read;
            final byte[] bytes = new byte[1024];

            while ((read = fileContent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            writer.println("New file " + fileName + " created at " + uploadDir);
            LOGGER.log(Level.INFO, "File {0} being uploaded to {1}",
                    new Object[]{fileName, uploadDir});
        } catch (Exception e) {
            // Handle exceptions
            LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}",
                    new Object[]{e.getMessage()});
        }
    }

    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}
