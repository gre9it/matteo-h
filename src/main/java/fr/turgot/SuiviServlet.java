package fr.turgot;

import java.io.IOException;
import java.util.List;

import fr.turgot.dao.EmpruntDAO;
import fr.turgot.dao.model.Emprunt;
import fr.turgot.dao.model.User;
import fr.turgot.utils.LinkHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/SuiviServlet")
public class SuiviServlet extends HttpServlet {

    private static final String JSP_SUIVI = LinkHelper.JSP_ROOT + "suivi/suivi.jsp";
    private final EmpruntDAO empruntDAO = new EmpruntDAO();

    private User getConnectedUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null) ? (User) session.getAttribute("connectedUser") : null;
    }

    private boolean isBibliothecaire(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User user = getConnectedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return false;
        }
        if (user.getRole() != User.Role.BIBLIOTHECAIRE) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;
    }

    private void chargerPage(HttpServletRequest request) {
        List<Emprunt> emprunts = empruntDAO.findAll();
        request.setAttribute("emprunts", emprunts);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isBibliothecaire(request, response)) return;
        chargerPage(request);
        getServletContext().getRequestDispatcher(JSP_SUIVI).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isBibliothecaire(request, response)) return;

        String action = request.getParameter("action");

        if ("forcer_retour".equals(action)) {
            int empruntId = Integer.parseInt(request.getParameter("empruntId"));
            boolean succes = empruntDAO.forcerRetour(empruntId);
            request.setAttribute(succes ? "success" : "error",
                succes ? "Retour enregistré" : "Erreur lors du retour");
        }

        chargerPage(request);
        getServletContext().getRequestDispatcher(JSP_SUIVI).forward(request, response);
    }
}