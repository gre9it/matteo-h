<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="fr.turgot.dao.model.User" %>
<%@ page import="fr.turgot.dao.model.Emprunt" %>
<%@ page import="fr.turgot.dao.model.Livre" %>
<%@ page import="java.util.List" %>
<%
    User connectedUser  = (User) session.getAttribute("connectedUser");
    List<Emprunt> emprunts = (List<Emprunt>) request.getAttribute("emprunts");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Suivi des emprunts</title>
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

    <div class="container-wide">
        <h2>Suivi des emprunts</h2>

        <%
            String success = (String) request.getAttribute("success");
            String error   = (String) request.getAttribute("error");
            if (success != null) { %> <p class="success"><%= success %></p> <% }
            if (error   != null) { %> <p class="error"><%= error %></p>     <% }
        %>

        <!-- Filtres -->
        <div class="form-section">
            <h3>Filtrer</h3>
            <div class="filtres-wrapper">
                <div>
                    <label class="filtre-label">Statut</label>
                    <select id="filtreStatut" onchange="filtrerEmprunts()">
                        <option value="">Tous</option>
                        <option value="En cours">En cours</option>
                        <option value="En retard">En retard</option>
                        <option value="Rendu">Rendu</option>
                    </select>
                </div>
                <div>
                    <label class="filtre-label">Recherche</label>
                    <input type="text" id="filtreTexte" oninput="filtrerEmprunts()"
                    placeholder="Utilisateur, ressource...">
                </div>
                <button class="btn btn-secondary" onclick="resetFiltresEmprunts()">
                    Réinitialiser
                </button>
                <a href="<%= request.getContextPath() %>/RessourceServlet"
                class="btn btn-secondary btn-push-right">
                    Gérer les ressources
                </a>
            </div>
        </div>

        <p class="compteur-label">
            <span id="compteurEmprunts">
                <%= emprunts != null ? emprunts.size() : 0 %>
            </span> emprunt(s) affiché(s)
        </p>

        <div class="table-wrapper">
            <table id="tableSuivi">
                <thead>
                    <tr>
                        <th>Utilisateur</th>
                        <th>Rôle</th>
                        <th>Ressource</th>
                        <th>Type</th>
                        <th>Emprunté le</th>
                        <th>Retour prévu</th>
                        <th>Retour effectif</th>
                        <th>Statut</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    if (emprunts != null) {
                        for (Emprunt e : emprunts) {
                            boolean estLivre = e.getRessource() instanceof Livre;
                            String statut;
                            String statutCss;
                            if (!e.isOngoing()) {
                                statut    = "Rendu";
                                statutCss = "badge-document";
                            } else if (e.isLate()) {
                                statut    = "En retard";
                                statutCss = "badge-retard";
                            } else {
                                statut    = "En cours";
                                statutCss = "badge-livre";
                            }
                %>
                <tr data-statut="<%= statut %>" data-search="<%= e.getUser().getUsername().toLowerCase() %> <%= e.getRessource().getTitle().toLowerCase() %>">
                    <td><strong><%= e.getUser().getUsername() %></strong></td>
                    <td>
                        <span class="badge <%= e.getUser().getRole() == User.Role.PROFESSEUR ? "badge-document" : "badge-livre" %>">
                            <%= e.getUser().getRole() %>
                        </span>
                    </td>
                    <td><%= e.getRessource().getTitle() %></td>
                    <td>
                        <span class="badge <%= estLivre ? "badge-livre" : "badge-document" %>">
                            <%= estLivre ? "Livre" : "Document" %>
                        </span>
                    </td>
                    <td><%= e.getDateEmprunt() %></td>
                    <td><%= e.getDateRetourPrevue() %></td>
                    <td><%= e.getDateRetourEffective() != null ? e.getDateRetourEffective() : "—" %></td>
                    <td><span class="badge <%= statutCss %>"><%= statut %></span></td>
                    <td>
                        <% if (e.isOngoing()) { %>
                        <form method="post" action="SuiviServlet"
                            onsubmit="return confirm('Forcer le retour de cet emprunt ?')">
                            <input type="hidden" name="action"    value="forcer_retour">
                            <input type="hidden" name="empruntId" value="<%= e.getId() %>">
                            <button type="submit" class="btn btn-danger">Forcer retour</button>
                        </form>
                        <% } else { %>
                            —
                        <% } %>
                    </td>
                </tr>
                <%
                        }
                    }
                %>
                </tbody>
            </table>
        </div>

        <br>
        <a href="<%= request.getContextPath() %>/RessourceServlet" class="btn btn-secondary">
            Gérer les ressources
        </a>
    </div>

    <script>
    function filtrerEmprunts() {
        var statut = document.getElementById("filtreStatut").value;
        var texte  = document.getElementById("filtreTexte").value.toLowerCase().trim();
        var lignes = document.querySelectorAll("#tableSuivi tbody tr");
        var count  = 0;

        lignes.forEach(function(ligne) {
            var ligneStatut = (ligne.dataset.statut || "").trim();
            var search      = (ligne.dataset.search || "").toLowerCase().trim();
            var statutOk    = !statut || ligneStatut === statut;
            var texteOk     = !texte  || search.includes(texte);
            var visible     = statutOk && texteOk;
            ligne.style.display = visible ? "" : "none";
            if (visible) count++;
        });

        document.getElementById("compteurEmprunts").textContent = count;
    }

    function resetFiltresEmprunts() {
        document.getElementById("filtreStatut").value = "";
        document.getElementById("filtreTexte").value  = "";
        filtrerEmprunts();
    }
    </script>
</body>
</html>