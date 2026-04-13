package fr.turgot;

import fr.turgot.dao.RessourceDAO;
import fr.turgot.dao.model.*;
import fr.turgot.utils.LinkHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

/**
 * Dashboard CRUD des ressources — réservé au BIBLIOTHECAIRE.
 * Actions : list (défaut), add, edit, delete.
 */
@WebServlet("/ressources")
public class RessourceServlet extends HttpServlet {

    private final RessourceDAO ressourceDAO = new RessourceDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = requireBibliothecaire(req, resp);
        if (user == null) return;

        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "add" -> {
                req.getRequestDispatcher(LinkHelper.RESSOURCE_FORM).forward(req, resp);
            }
            case "edit" -> {
                Long id = parseLong(req.getParameter("id"));
                if (id == null) { resp.sendRedirect(req.getContextPath() + "/ressources"); return; }
                Optional<Ressource> opt = ressourceDAO.findById(id);
                if (opt.isEmpty()) { resp.sendRedirect(req.getContextPath() + "/ressources"); return; }
                req.setAttribute("ressource", opt.get());
                req.getRequestDispatcher(LinkHelper.RESSOURCE_FORM).forward(req, resp);
            }
            case "delete" -> {
                Long id = parseLong(req.getParameter("id"));
                if (id != null) ressourceDAO.delete(id);
                resp.sendRedirect(req.getContextPath() + "/ressources");
            }
            default -> {
                List<Ressource> ressources = ressourceDAO.findAll();
                req.setAttribute("ressources", ressources);
                req.getRequestDispatcher(LinkHelper.DASHBOARD_VIEW).forward(req, resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = requireBibliothecaire(req, resp);
        if (user == null) return;

        String action = req.getParameter("action");
        String type   = req.getParameter("typeRessource"); // "Livre" ou "Document"

        String title        = req.getParameter("title");
        String description  = req.getParameter("description");
        String auteur       = req.getParameter("auteur");
        String dateStr      = req.getParameter("dateParution");
        String copiesStr    = req.getParameter("availableCopies");
        String idStr        = req.getParameter("id");

        LocalDate dateParution = null;
        if (dateStr != null && !dateStr.isBlank()) {
            try { dateParution = LocalDate.parse(dateStr); } catch (DateTimeParseException ignored) {}
        }

        int copies = 1;
        try { copies = Integer.parseInt(copiesStr); } catch (NumberFormatException ignored) {}

        if ("save".equals(action)) {
            Long id = parseLong(idStr);

            if (id != null) {
                // Mise à jour
                Optional<Ressource> opt = ressourceDAO.findById(id);
                if (opt.isPresent()) {
                    Ressource r = opt.get();
                    r.setTitle(title);
                    r.setDescription(description);
                    r.setAuteur(auteur);
                    r.setDateParution(dateParution);
                    r.setAvailableCopies(copies);
                    if (r instanceof Livre livre) {
                        livre.setGenre(req.getParameter("genre"));
                        livre.setEdition(req.getParameter("edition"));
                    } else if (r instanceof Document doc) {
                        doc.setTypeDocument(parseTypeDocument(req.getParameter("typeDocument")));
                    }
                    ressourceDAO.update(r);
                }
            } else {
                // Création
                if ("Livre".equals(type)) {
                    Livre livre = new Livre(title, description, dateParution, auteur, copies,
                        req.getParameter("genre"), req.getParameter("edition"));
                    ressourceDAO.save(livre);
                } else if ("Document".equals(type)) {
                    Document doc = new Document(title, description, dateParution, auteur, copies,
                        parseTypeDocument(req.getParameter("typeDocument")));
                    ressourceDAO.save(doc);
                }
            }
        }

        resp.sendRedirect(req.getContextPath() + "/ressources");
    }

    // --- helpers ---

    private User requireBibliothecaire(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("connectedUser") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return null;
        }
        if (user.getRole() != Role.BIBLIOTHECAIRE) {
            req.setAttribute("errorMessage", "Accès réservé aux bibliothécaires.");
            req.getRequestDispatcher(LinkHelper.ERROR_VIEW).forward(req, resp);
            return null;
        }
        return user;
    }

    private Long parseLong(String s) {
        if (s == null || s.isBlank()) return null;
        try { return Long.parseLong(s.trim()); } catch (NumberFormatException e) { return null; }
    }

    private TypeDocument parseTypeDocument(String s) {
        if (s == null) return TypeDocument.RAPPORT;
        try { return TypeDocument.valueOf(s.toUpperCase()); } catch (IllegalArgumentException e) {
            return TypeDocument.RAPPORT;
        }
    }
}
