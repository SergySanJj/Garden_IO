package com.garden;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@WebServlet(name = "IconDipatcher", urlPatterns = {"/icon"})
public class IconDispatcher extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext sc = getServletContext();

        try (InputStream is = sc.getResourceAsStream("/favicon.ico")) {
            // it is the responsibility of the container to close output stream
            try (OutputStream os = resp.getOutputStream()) {
                if (is == null) {
                    resp.setContentType("text/plain");
                    os.write("Failed to send image".getBytes());
                } else {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    resp.setContentType("image/ico");
                    while ((bytesRead = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                }
            }
        }
    }
}
