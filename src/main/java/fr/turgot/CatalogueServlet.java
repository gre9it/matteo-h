package fr.turgot;

import java.io.IOException;
import java.util.List;

import fr.turgot.dao.EmpruntDAO;
import fr.turgot.dao.RessourceDAO;
import fr.turgot.dao.model.Ressource;
import fr.turgot.dao.model.User;
import fr.turgot.utils.LinkHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/CatalogueServlet")
public class CatalogueServlet extends HttpServlet {

    private static final String JSP_CATALOGUE = LinkHelper.JSP_ROOT + "catalogue/catalogue.jsp";
    private static final int LIMITE_ETUDIANT = 2;
    private static final int LIMITE_PROFESSEUR = 5;
    private static final int DUREE_JOURS = 14;

    private final RessourceDAO ressourceDAO = new RessourceDAO();
    private final EmpruntDAO empruntDAO = new EmpruntDAO();

    private User getConnectedUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null) ? (User) session.getAttribute("connectedUser") : null;
    }

    private int getLimite(User user) {
        return user.getRole() == User.Role.PROFESSEUR ? LIMITE_PROFESSEUR : LIMITE_ETUDIANT;
    }

    private void chargerPage(HttpServletRequest request, User user) {
        List<Ressource> ressources = ressourceDAO.findAll();
        long enCours = empruntDAO.countEnCours(user.getId());
        int limite = getLimite(user);
        request.setAttribute("ressources", ressources);
        request.setAttribute("enCours", enCours);
        request.setAttribute("limite", limite);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getConnectedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        chargerPage(request, user);
        getServletContext().getRequestDispatcher(JSP_CATALOGUE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getConnectedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        int ressourceId = Integer.parseInt(request.getParameter("ressourceId"));
        long enCours = empruntDAO.countEnCours(user.getId());
        int limite = getLimite(user);

        if (enCours >= limite) {
            request.setAttribute("error",
                    "Limite atteinte (" + limite + " emprunt(s) maximum pour votre rôle)");
        } else {
            Ressource ressource = ressourceDAO.findById(ressourceId);
            if (ressource == null) {
                request.setAttribute("error", "Ressource introuvable");
            } else if (ressource.getAvailableCopies() == 0) {
                request.setAttribute("error", "Plus aucun exemplaire disponible");
            } else {
                boolean empruntOk = empruntDAO.create(user, ressource, DUREE_JOURS);
                boolean borrowOk = ressourceDAO.borrow(ressourceId);
                boolean succes = empruntOk && borrowOk;
                request.setAttribute(succes ? "success" : "error",
                        succes ? "Emprunt enregistré — retour prévu dans " + DUREE_JOURS + " jours"
                                : "Erreur lors de l'emprunt");
            }
        }

        chargerPage(request, user);
        getServletContext().getRequestDispatcher(JSP_CATALOGUE).forward(request, response);
    }
}