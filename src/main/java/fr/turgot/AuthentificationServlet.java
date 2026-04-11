package fr.turgot;

import fr.turgot.dao.UserDAO;
import fr.turgot.dao.model.User;
import fr.turgot.utils.LinkHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/login")
public class AuthentificationServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Si déjà connecté, rediriger
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("connectedUser") != null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        req.getRequestDispatcher(LinkHelper.LOGIN_VIEW).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            req.setAttribute("errorMessage", "Veuillez renseigner tous les champs.");
            req.getRequestDispatcher(LinkHelper.LOGIN_VIEW).forward(req, resp);
            return;
        }

        Optional<User> optUser = userDAO.findByUsername(username.trim());

        if (optUser.isEmpty() || !BCrypt.checkpw(password, optUser.get().getPassword())) {
            req.setAttribute("errorMessage", "Identifiants incorrects.");
            req.setAttribute("lastUsername", username);
            req.getRequestDispatcher(LinkHelper.LOGIN_VIEW).forward(req, resp);
            return;
        }

        User user = optUser.get();
        HttpSession session = req.getSession(true);
        session.setAttribute("connectedUser", user);
        session.setMaxInactiveInterval(30 * 60); // 30 minutes

        resp.sendRedirect(req.getContextPath() + "/");
    }
}
