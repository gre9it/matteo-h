package fr.turgot;

import fr.turgot.dao.RessourceDAO;
import fr.turgot.dao.model.Ressource;
import fr.turgot.dao.model.Role;
import fr.turgot.dao.model.User;
import fr.turgot.utils.LinkHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Accessible aux rôles ETUDIANT et PROFESSEUR.
 * Affiche le catalogue en lecture seule.
 */
@WebServlet("/catalogue")
public class CatalogueServlet extends HttpServlet {

    private final RessourceDAO ressourceDAO = new RessourceDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = getConnectedUser(req, resp);
        if (user == null) return;

        // Le bibliothécaire n'accède pas au catalogue : il a son dashboard
        if (user.getRole() == Role.BIBLIOTHECAIRE) {
            resp.sendRedirect(req.getContextPath() + "/ressources");
            return;
        }

        List<Ressource> ressources = ressourceDAO.findAll();
        req.setAttribute("ressources", ressources);
        req.getRequestDispatcher(LinkHelper.CATALOGUE_VIEW).forward(req, resp);
    }

    private User getConnectedUser(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("connectedUser") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
        return user;
    }
}
