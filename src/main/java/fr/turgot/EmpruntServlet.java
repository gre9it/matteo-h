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

@WebServlet("/EmpruntServlet")
public class EmpruntServlet extends HttpServlet {

    private static final String JSP_EMPRUNT      = LinkHelper.JSP_ROOT + "emprunt/emprunt.jsp";
    private static final int    LIMITE_ETUDIANT  = 2;
    private static final int    LIMITE_PROFESSEUR = 5;

    private final EmpruntDAO empruntDAO = new EmpruntDAO();

    private User getConnectedUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null) ? (User) session.getAttribute("connectedUser") : null;
    }

    private int getLimite(User user) {
        return user.getRole() == User.Role.PROFESSEUR ? LIMITE_PROFESSEUR : LIMITE_ETUDIANT;
    }

    private void chargerPage(HttpServletRequest request, User user) {
        List<Emprunt> emprunts = empruntDAO.findByUser(user.getId());
        long enCours           = empruntDAO.countEnCours(user.getId());
        int  limite            = getLimite(user);
        request.setAttribute("emprunts", emprunts);
        request.setAttribute("enCours",  enCours);
        request.setAttribute("limite",   limite);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getConnectedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        if (user.getRole() == User.Role.BIBLIOTHECAIRE) {
            response.sendRedirect(request.getContextPath() + "/RessourceServlet");
            return;
        }
        chargerPage(request, user);
        getServletContext().getRequestDispatcher(JSP_EMPRUNT).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getConnectedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("retourner".equals(action)) {
            int empruntId  = Integer.parseInt(request.getParameter("empruntId"));
            boolean succes = empruntDAO.retourner(empruntId, user.getId());
            request.setAttribute(succes ? "success" : "error",
                succes ? "Retour enregistré" : "Erreur lors du retour");
        }

        chargerPage(request, user);
        getServletContext().getRequestDispatcher(JSP_EMPRUNT).forward(request, response);
    }
}