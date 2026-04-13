package fr.turgot;

import java.io.IOException;
import java.util.List;

import fr.turgot.dao.UserDAO;
import fr.turgot.dao.model.User;
import fr.turgot.utils.LinkHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {

    private static final String JSP_ADMIN = LinkHelper.JSP_ROOT + "admin/admin.jsp";
    private final UserDAO userDAO = new UserDAO();

    private User getConnectedUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null) ? (User) session.getAttribute("connectedUser") : null;
    }

    private boolean isAdmin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User user = getConnectedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return false;
        }
        if (user.getRole() != User.Role.ADMIN) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé aux administrateurs");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request, response)) return;

        List<User> users = userDAO.findAll();
        request.setAttribute("users", users);
        getServletContext().getRequestDispatcher(JSP_ADMIN).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request, response)) return;

        String action = request.getParameter("action");

        if ("supprimer".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            User cible = userDAO.findById(userId);
            if (cible == null) {
                request.setAttribute("error", "Utilisateur introuvable");
            } else if (cible.getRole() == User.Role.ADMIN) {
                request.setAttribute("error", "Impossible de supprimer un administrateur");
            } else {
                boolean succes = userDAO.deleteUser(userId);
                request.setAttribute(succes ? "success" : "error",
                    succes ? "Utilisateur supprimé" : "Erreur lors de la suppression");
            }

        } else if ("changerRole".equals(action)) {
            int userId            = Integer.parseInt(request.getParameter("userId"));
            String roleStr        = request.getParameter("role");
            User.Role nouveauRole = User.Role.valueOf(roleStr);
            User cible            = userDAO.findById(userId);

            if (cible != null && cible.getRole() == User.Role.ADMIN) {
                request.setAttribute("error", "Impossible de modifier le rôle d'un administrateur");
            } else {
                boolean succes = userDAO.changeRole(userId, nouveauRole);
                request.setAttribute(succes ? "success" : "error",
                    succes ? "Rôle mis à jour avec succès" : "Erreur lors de la mise à jour");
            }
        }

        List<User> users = userDAO.findAll();
        request.setAttribute("users", users);
        getServletContext().getRequestDispatcher(JSP_ADMIN).forward(request, response);
    }
}