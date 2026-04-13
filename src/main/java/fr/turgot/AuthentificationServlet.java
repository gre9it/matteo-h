package fr.turgot;

import java.io.IOException;

import fr.turgot.dao.UserDAO;
import fr.turgot.dao.model.User;
import fr.turgot.utils.LinkHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/AuthentificationServlet")
public class AuthentificationServlet extends HttpServlet {

    public static final String JSP_ROOT_AUTH = LinkHelper.JSP_ROOT + "authentification/";
    private final UserDAO dao = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        getServletContext()
            .getRequestDispatcher(JSP_ROOT_AUTH + "authentification.jsp")
            .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String login = request.getParameter("username");
        String mdp = request.getParameter("password");

        StringBuilder messageErreur = new StringBuilder();

        // Validation des champs
        if (login == null || login.trim().isEmpty()) {
            messageErreur.append("Login manquant.<br>");
        }
        if (mdp == null || mdp.trim().isEmpty()) {
            messageErreur.append("Mot de passe manquant.<br>");
        }

        if (messageErreur.length() > 0) {
            request.setAttribute("errors", messageErreur.toString());
            getServletContext().getRequestDispatcher(JSP_ROOT_AUTH + "authechec.jsp").forward(request, response);
        } else {
            
            User user = dao.authenticate(login, mdp);

            if (user == null) {
                messageErreur.append("Login ou mot de passe incorrect");
                request.setAttribute("error", messageErreur.toString());
                getServletContext().getRequestDispatcher(JSP_ROOT_AUTH + "authentification.jsp").forward(request, response);
            } else {
                HttpSession session = request.getSession(true);
                session.setAttribute("connectedUser", user);
                getServletContext().getRequestDispatcher(JSP_ROOT_AUTH + "authsuccess.jsp").forward(request, response);
            }
        }
    }

}
