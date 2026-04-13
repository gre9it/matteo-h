<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="fr.turgot.dao.model.User" %>
<%@ page import="fr.turgot.dao.model.Emprunt" %>
<%@ page import="fr.turgot.dao.model.Livre" %>
<%@ page import="java.util.List" %>
<%
    User connectedUser = (User) session.getAttribute("connectedUser");
    List<Emprunt> emprunts = (List<Emprunt>) request.getAttribute("emprunts");
    long enCours = (long) request.getAttribute("enCours");
    int  limite  = (int)  request.getAttribute("limite");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Mes emprunts</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <nav class="navbar">
        <h1>Bibliothèque Universitaire</h1>
        <div class="navbar-user">
            <span>Bonjour, <%= connectedUser.getUsername() %></span>
            <a href="<%= request.getContextPath() %>/DeconnexionServlet" class="btn-deconnexion">
                Se déconnecter
            </a>
        </div>
    </nav>

    <div class="container">
        <h2>Mes emprunts</h2>

        <%
            String success = (String) request.getAttribute("success");
            String error   = (String) request.getAttribute("error");
            if (success != null) { %> <p class="success"><%= success %></p> <% }
            if (error   != null) { %> <p class="error"><%= error %></p>     <% }
        %>

        <!-- Quota -->
        <div class="form-section" style="margin-bottom:24px;">
            <p>
                Emprunts en cours : <strong><%= enCours %> / <%= limite %></strong>
                &nbsp;—&nbsp;
                <a href="<%= request.getContextPath() %>/CatalogueServlet">
                    Emprunter depuis le catalogue
                </a>
            </p>
        </div>

        <!-- Historique -->
        <div class="table-wrapper">
            <table>
                <thead>
                    <tr>
                        <th>Ressource</th>
                        <th>Type</th>
                        <th>Emprunté le</th>
                        <th>Retour prévu</th>
                        <th>Statut</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    if (emprunts != null && !emprunts.isEmpty()) {
                        for (Emprunt e : emprunts) {
                            boolean estLivre = e.getRessource() instanceof Livre;
                            String statut;
                            String statutCss;
                            if (!e.isOngoing()) {
                                statut    = "Rendu";
                                statutCss = "badge-document";
                            } else if (e.isLate()) {
                                statut    = "En retard";
                                statutCss = "badge";
                            } else {
                                statut    = "En cours";
                                statutCss = "badge-livre";
                            }
                %>
                    <tr>
                        <td><strong><%= e.getRessource().getTitle() %></strong></td>
                        <td>
                            <span class="badge <%= estLivre ? "badge-livre" : "badge-document" %>">
                                <%= estLivre ? "Livre" : "Document" %>
                            </span>
                        </td>
                        <td><%= e.getDateEmprunt() %></td>
                        <td><%= e.getDateRetourPrevue() %></td>
                        <td><span class="badge <%= statutCss %>"><%= statut %></span></td>
                        <td>
                            <% if (e.isOngoing()) { %>
                            <form method="post" action="EmpruntServlet"
                                onsubmit="return confirm('Confirmer le retour ?')">
                                <input type="hidden" name="action"    value="retourner">
                                <input type="hidden" name="empruntId" value="<%= e.getId() %>">
                                <button type="submit" class="btn btn-secondary">Retourner</button>
                            </form>
                            <% } else { %>
                                —
                            <% } %>
                        </td>
                    </tr>
                <%
                        }
                    } else { %>
                        <tr>
                            <td colspan="6" style="text-align:center; color:var(--grey-dark);">
                                Aucun emprunt pour le moment.
                            </td>
                        </tr>
                <% } %>
                </tbody>
            </table>
        </div>

        <br>
        <a href="<%= request.getContextPath() %>/CatalogueServlet" class="btn btn-secondary">
            Voir le catalogue
        </a>
    </div>
</body>
</html>