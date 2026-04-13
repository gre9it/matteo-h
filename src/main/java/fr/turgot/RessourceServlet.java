package fr.turgot;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import fr.turgot.dao.RessourceDAO;
import fr.turgot.dao.model.Document;
import fr.turgot.dao.model.Livre;
import fr.turgot.dao.model.Ressource;
import fr.turgot.dao.model.User;
import fr.turgot.utils.LinkHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/RessourceServlet")
public class RessourceServlet extends HttpServlet {

    private static final String JSP_RESSOURCE = LinkHelper.JSP_ROOT + "ressources/ressource.jsp";
    private final RessourceDAO dao = new RessourceDAO();

    private boolean isBibliothecaire(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("connectedUser") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return false;
        }
        if (user.getRole() != User.Role.BIBLIOTHECAIRE) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé aux bibliothécaires");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isBibliothecaire(request, response))
            return;

        List<Ressource> ressources = dao.findAll();
        request.setAttribute("ressources", ressources);
        getServletContext().getRequestDispatcher(JSP_RESSOURCE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isBibliothecaire(request, response))
            return;

        String action = request.getParameter("action");

        switch (action) {

            case "ajouter" -> {
                String type = request.getParameter("type");
                String title = request.getParameter("title");
                String description = request.getParameter("description");
                String auteur = request.getParameter("auteur");
                LocalDate date = parseDate(request);
                String copiesStr = request.getParameter("availableCopies");
                int copies = (copiesStr != null && !copiesStr.isEmpty())
                        ? Integer.parseInt(copiesStr)
                        : 1;

                boolean succes;
                if ("livre".equals(type)) {
                    Livre livre = new Livre();
                    livre.setTitle(title);
                    livre.setDescription(description);
                    livre.setAuteur(auteur);
                    livre.setDateParution(date);
                    livre.setAvailableCopies(copies);
                    livre.setGenre(request.getParameter("genre"));
                    livre.setEdition(request.getParameter("edition"));
                    succes = dao.createLivre(livre);
                } else {
                    Document doc = new Document();
                    doc.setTitle(title);
                    doc.setDescription(description);
                    doc.setAuteur(auteur);
                    doc.setDateParution(date);
                    doc.setAvailableCopies(copies);
                    doc.setType(request.getParameter("typeDoc"));
                    succes = dao.createDocument(doc);
                }
                request.setAttribute(succes ? "success" : "error",
                        succes ? "Ressource ajoutée" : "Erreur lors de l'ajout");
            }

            case "chargerModif" -> {
                int id = Integer.parseInt(request.getParameter("id"));
                Ressource r = dao.findById(id);
                request.setAttribute("ressourceAModifier", r);
            }

            case "modifier" -> {
                int id = Integer.parseInt(request.getParameter("id"));
                String type = request.getParameter("type");
                LocalDate date = parseDate(request);
                String copiesStr = request.getParameter("availableCopies");
                int copies = (copiesStr != null && !copiesStr.isEmpty())
                        ? Integer.parseInt(copiesStr)
                        : 0;

                boolean succes = dao.update(id, type,
                        request.getParameter("title"),
                        request.getParameter("description"),
                        request.getParameter("auteur"),
                        date, copies,
                        request.getParameter("genre"),
                        request.getParameter("edition"),
                        request.getParameter("typeDoc"));

                request.setAttribute(succes ? "success" : "error",
                        succes ? "Ressource modifiée" : "Erreur lors de la modification");
            }

            case "supprimer" -> {
                int id = Integer.parseInt(request.getParameter("id"));
                boolean success = dao.delete(id);
                request.setAttribute(success ? "success" : "error",
                        success ? "Ressource supprimée" : "Erreur lors de la suppression");
            }

            default -> {
                request.setAttribute("error", "Action inconnue");
            }
        }

        request.setAttribute("ressources", dao.findAll());
        getServletContext().getRequestDispatcher(JSP_RESSOURCE).forward(request, response);
    }

    private LocalDate parseDate(HttpServletRequest request) {
        String anneeStr = request.getParameter("annee");
        if (anneeStr == null || anneeStr.isEmpty())
            return null;
        int annee = Integer.parseInt(anneeStr);
        String moisStr = request.getParameter("mois");
        String jourStr = request.getParameter("jour");
        int mois = (moisStr != null && !moisStr.isEmpty()) ? Integer.parseInt(moisStr) : 1;
        int jour = (jourStr != null && !jourStr.isEmpty()) ? Integer.parseInt(jourStr) : 1;
        return LocalDate.of(annee, mois, jour);
    }
}