package fr.turgot;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/page")
public class MainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getParameter("path"); // chemin relatif, ex: authentification/login
        if (path == null || path.isEmpty()) {
            path = "authentification/login"; // page par défaut
        }
        String jspPath = "/WEB-INF/jsp/" + path + ".jsp";
        request.getRequestDispatcher(jspPath).forward(request, response);
    }
}