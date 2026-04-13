package fr.turgot;

import java.io.IOException;

import fr.turgot.dao.UserDAO;
import fr.turgot.utils.LinkHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    public static final String JSP_ROOT_FORM = LinkHelper.JSP_ROOT + "formulaire/";
    public static final String JSP_ROOT_AUTH = LinkHelper.JSP_ROOT + "authentification/";
    private final UserDAO dao = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String login = request.getParameter("username");
        String mdp = request.getParameter("password");
        StringBuilder messageErreur = new StringBuilder();

        if (login == null || login.trim().isEmpty()) {
            messageErreur.append("Login manquant.<br>");
        }
        if (mdp == null || mdp.trim().isEmpty()) {
            messageErreur.append("Mot de passe manquant.<br>");
        }

        if (messageErreur.length() > 0) {
            request.setAttribute("errors", messageErreur.toString());
            getServletContext().getRequestDispatcher(JSP_ROOT_FORM + "register.jsp")
                            .forward(request, response);
            return;
        }

        if (dao.userExists(login)) {
            request.setAttribute("error", "Ce nom d'utilisateur est déjà pris");
            getServletContext().getRequestDispatcher(JSP_ROOT_FORM + "register.jsp")
                            .forward(request, response);
            return;
        }

        boolean success = dao.createUser(login, mdp);
        if (success) {
            request.setAttribute("username", login);
            getServletContext().getRequestDispatcher(JSP_ROOT_FORM + "registersuccess.jsp")
                            .forward(request, response);
        } else {
            request.setAttribute("error", "Erreur lors de la création du compte");
            getServletContext().getRequestDispatcher(JSP_ROOT_FORM + "register.jsp")
                            .forward(request, response);
        }
    }
}