<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="fr.turgot.dao.model.User" %>
<%@ page import="fr.turgot.dao.model.Ressource" %>
<%@ page import="fr.turgot.dao.model.Livre" %>
<%@ page import="fr.turgot.dao.model.Document" %>
<%@ page import="java.util.List" %>
<%
    User connectedUser  = (User)           session.getAttribute("connectedUser");
    List<Ressource> ressources = (List<Ressource>) request.getAttribute("ressources");
    long enCours = (long) request.getAttribute("enCours");
    int  limite  = (int)  request.getAttribute("limite");
    boolean peutEmprunter = connectedUser.getRole() != User.Role.BIBLIOTHECAIRE && enCours < limite;
%>
<!DOCTYPE html>
<html>
<head>
    <title>Catalogue</title>
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
        <h2>Catalogue des ressources</h2>

        <%
            String success = (String) request.getAttribute("success");
            String error   = (String) request.getAttribute("error");
            if (success != null) { %> <p class="success"><%= success %></p> <% }
            if (error   != null) { %> <p class="error"><%= error %></p>     <% }
        %>

        <% if (connectedUser.getRole() != User.Role.BIBLIOTHECAIRE) { %>
        <div class="form-section quota-section">
            <p>
                Emprunts en cours : <strong><%= enCours %> / <%= limite %></strong>
                &nbsp;—&nbsp;
                <a href="<%= request.getContextPath() %>/EmpruntServlet">Voir mes emprunts</a>
            </p>
        </div>
        <% } %>

        <!-- Filtres -->
        <div class="form-section">
            <h3>Filtrer</h3>
            <div class="filtres-wrapper">
                <div class="form-group" style="margin:0">
                    <label>Type</label>
                    <select id="filtreType" onchange="filtrer()">
                        <option value="">Tous</option>
                        <option value="Livre">Livres</option>
                        <option value="Document">Documents</option>
                    </select>
                </div>
                <div class="form-group" style="margin:0">
                    <label>Recherche</label>
                    <input type="text" id="filtreTexte" oninput="filtrer()"
                        placeholder="Titre, auteur...">
                </div>
                <button class="btn btn-secondary" onclick="resetFiltres()">Réinitialiser</button>
            </div>
        </div>

        <p class="compteur-label">
            <span id="compteur"><%= ressources != null ? ressources.size() : 0 %></span> ressource(s) affichée(s)
        </p>

        <div class="table-wrapper">
            <table id="tableRessources">
                <thead>
                    <tr>
                        <th>Type</th>
                        <th>Titre</th>
                        <th>Auteur</th>
                        <th>Date de parution</th>
                        <th>Description</th>
                        <th>Détails</th>
                        <th>Exemplaires</th>
                        <% if (connectedUser.getRole() != User.Role.BIBLIOTHECAIRE) { %>
                        <th>Emprunter</th>
                        <% } %>
                    </tr>
                </thead>
                <tbody>
                <%
                    if (ressources != null) {
                        for (Ressource r : ressources) {
                            boolean estLivre = r instanceof Livre;
                            String typeStr   = estLivre ? "Livre" : "Document";
                            String details   = "";
                            if (estLivre) {
                                Livre l = (Livre) r;
                                details = (l.getGenre()   != null ? "Genre : " + l.getGenre() : "")
                                        + (l.getGenre() != null && l.getEdition() != null ? " — " : "")
                                        + (l.getEdition() != null ? "Éd. " + l.getEdition() : "");
                            } else {
                                Document d = (Document) r;
                                details = d.getType() != null ? d.getType() : "—";
                            }
                %>
                    <tr data-type="<%= typeStr %>"
                        data-search="<%= r.getTitle().toLowerCase() %> <%= r.getAuteur() != null ? r.getAuteur().toLowerCase() : "" %>">
                        <td>
                            <span class="badge <%= estLivre ? "badge-livre" : "badge-document" %>">
                                <%= typeStr %>
                            </span>
                        </td>
                        <td><strong><%= r.getTitle() %></strong></td>
                        <td><%= r.getAuteur()       != null ? r.getAuteur()       : "—" %></td>
                        <td><%= r.getDateParution() != null ? r.getDateParution() : "—" %></td>
                        <td><%= r.getDescription()  != null ? r.getDescription()  : "—" %></td>
                        <td><%= details.isEmpty()   ? "—" : details %></td>
                        <td>
                            <% if (r.getAvailableCopies() > 0) { %>
                                <span class="badge badge-livre"><%= r.getAvailableCopies() %> dispo.</span>
                            <% } else { %>
                                <span class="badge badge-document">Indisponible</span>
                            <% } %>
                        </td>
                        <% if (connectedUser.getRole() != User.Role.BIBLIOTHECAIRE) { %>
                        <td>
                            <% if (peutEmprunter && r.getAvailableCopies() > 0) { %>
                                <form method="post" action="CatalogueServlet">
                                    <input type="hidden" name="ressourceId" value="<%= r.getId() %>">
                                    <button type="submit" class="btn btn-primary">Emprunter</button>
                                </form>
                            <% } else if (r.getAvailableCopies() == 0) { %>
                                <span class="txt-indisponible">Indisponible</span>
                            <% } else { %>
                                <span class="txt-indisponible">Limite atteinte</span>
                            <% } %>
                        </td>
                        <% } %>
                    </tr>
                <%
                        }
                    }
                %>
                </tbody>
            </table>
        </div>

        <br>
        <% if (connectedUser.getRole() != User.Role.BIBLIOTHECAIRE) { %>
            <a href="<%= request.getContextPath() %>/EmpruntServlet" class="btn btn-secondary">
                Mes emprunts
            </a>
        <% } %>
    </div>

    <script src="<%= request.getContextPath() %>/js/catalogue.js"></script>
</body>
</html>