package fr.turgot;

import fr.turgot.dao.UserDAO;
import fr.turgot.dao.model.Etudiant;
import fr.turgot.utils.LinkHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(LinkHelper.REGISTER_VIEW).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username       = req.getParameter("username");
        String password       = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String numeroEtudiant = req.getParameter("numeroEtudiant");
        String filiere        = req.getParameter("filiere");

        // Validation basique
        if (isBlank(username) || isBlank(password) || isBlank(confirmPassword)) {
            req.setAttribute("errorMessage", "Tous les champs obligatoires doivent être remplis.");
            req.getRequestDispatcher(LinkHelper.REGISTER_VIEW).forward(req, resp);
            return;
        }

        if (!password.equals(confirmPassword)) {
            req.setAttribute("errorMessage", "Les mots de passe ne correspondent pas.");
            req.setAttribute("lastUsername", username);
            req.getRequestDispatcher(LinkHelper.REGISTER_VIEW).forward(req, resp);
            return;
        }

        if (password.length() < 6) {
            req.setAttribute("errorMessage", "Le mot de passe doit contenir au moins 6 caractères.");
            req.setAttribute("lastUsername", username);
            req.getRequestDispatcher(LinkHelper.REGISTER_VIEW).forward(req, resp);
            return;
        }

        if (userDAO.usernameExists(username.trim())) {
            req.setAttribute("errorMessage", "Ce nom d'utilisateur est déjà pris.");
            req.setAttribute("lastUsername", username);
            req.getRequestDispatcher(LinkHelper.REGISTER_VIEW).forward(req, resp);
            return;
        }

        // Hashage BCrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

        Etudiant etudiant = new Etudiant(
            username.trim(),
            hashedPassword,
            numeroEtudiant != null ? numeroEtudiant.trim() : "",
            filiere        != null ? filiere.trim()        : ""
        );

        userDAO.save(etudiant);

        // Forward (pas redirect) vers la vue de succès
        req.setAttribute("newUsername", username.trim());
        req.getRequestDispatcher(LinkHelper.REGISTER_SUCCESS).forward(req, resp);
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
