<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="fr.turgot.dao.model.User" %>
<%@ page import="fr.turgot.dao.model.Ressource" %>
<%@ page import="fr.turgot.dao.model.Livre" %>
<%@ page import="fr.turgot.dao.model.Document" %>
<%@ page import="java.util.List" %>
<%
    User connectedUser = (User) session.getAttribute("connectedUser");
    List<Ressource> ressources = (List<Ressource>) request.getAttribute("ressources");
    Ressource ressourceAModifier = (Ressource) request.getAttribute("ressourceAModifier");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Gestion des ressources</title>
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
        <h2>Gestion des ressources</h2>

        <%
            String success = (String) request.getAttribute("success");
            String error   = (String) request.getAttribute("error");
            if (success != null) { %> <p class="success"><%= success %></p> <% }
            if (error   != null) { %> <p class="error"><%= error %></p>     <% }
        %>

        <!-- Formulaire d'ajout -->
        <div class="form-section">
            <h3>Ajouter une ressource</h3>
            <form method="post" action="RessourceServlet">
                <input type="hidden" name="action" value="ajouter">

                <div class="form-group">
                    <label>Type de ressource</label>
                    <select name="type" id="typeSelect"
                            onchange="afficherChamps('champsLivreAjout','champsDocumentAjout')">
                        <option value="">-- Choisir --</option>
                        <option value="livre">Livre</option>
                        <option value="document">Document</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Titre</label>
                    <input type="text" name="title" required>
                </div>
                <div class="form-group">
                    <label>Description</label>
                    <textarea name="description"></textarea>
                </div>
                <div class="form-group">
                    <label>Auteur</label>
                    <input type="text" name="auteur">
                </div>
                <div class="form-group">
                    <label>Année de parution</label>
                    <input type="number" name="annee" min="0" max="2100" placeholder="ex: 1897">
                </div>
                <div class="form-group">
                    <label>Mois (optionnel)</label>
                    <input type="number" name="mois" min="1" max="12">
                </div>
                <div class="form-group">
                    <label>Jour (optionnel)</label>
                    <input type="number" name="jour" min="1" max="31">
                </div>
                <div class="form-group">
                    <label>Nombre d'exemplaires</label>
                    <input type="number" name="availableCopies" min="0" value="1" required>
                </div>

                <div class="champs-extra" id="champsLivreAjout">
                    <div class="form-group">
                        <label>Genre</label>
                        <input type="text" name="genre">
                    </div>
                    <div class="form-group">
                        <label>Édition</label>
                        <input type="text" name="edition">
                    </div>
                </div>

                <div class="champs-extra" id="champsDocumentAjout">
                    <div class="form-group">
                        <label>Type de document</label>
                        <select name="typeDoc">
                            <option value="Journal">Journal</option>
                            <option value="Nouvelle">Nouvelle</option>
                            <option value="Thèse">Thèse</option>
                            <option value="Rapport">Rapport</option>
                        </select>
                    </div>
                </div>

                <button type="submit" class="btn btn-primary">Ajouter</button>
            </form>
        </div>

        <!-- Formulaire de modification -->
        <% if (ressourceAModifier != null) {
            boolean estLivre = ressourceAModifier instanceof Livre;
        %>
        <div class="form-modif">
            <h3>Modifier la ressource #<%= ressourceAModifier.getId() %></h3>
            <form method="post" action="RessourceServlet">
                <input type="hidden" name="action" value="modifier">
                <input type="hidden" name="id"     value="<%= ressourceAModifier.getId() %>">
                <input type="hidden" name="type"   value="<%= estLivre ? "livre" : "document" %>">

                <div class="form-group">
                    <label>Titre</label>
                    <input type="text" name="title"
                        value="<%= ressourceAModifier.getTitle() %>" required>
                </div>
                <div class="form-group">
                    <label>Description</label>
                    <textarea name="description"><%= ressourceAModifier.getDescription() != null ? ressourceAModifier.getDescription() : "" %></textarea>
                </div>
                <div class="form-group">
                    <label>Auteur</label>
                    <input type="text" name="auteur"
                        value="<%= ressourceAModifier.getAuteur() != null ? ressourceAModifier.getAuteur() : "" %>">
                </div>
                <div class="form-group">
                    <label>Année</label>
                    <input type="number" name="annee"
                        value="<%= ressourceAModifier.getDateParution() != null ? ressourceAModifier.getDateParution().getYear() : "" %>">
                </div>
                <div class="form-group">
                    <label>Mois</label>
                    <input type="number" name="mois" min="1" max="12"
                        value="<%= ressourceAModifier.getDateParution() != null ? ressourceAModifier.getDateParution().getMonthValue() : "" %>">
                </div>
                <div class="form-group">
                    <label>Jour</label>
                    <input type="number" name="jour" min="1" max="31"
                        value="<%= ressourceAModifier.getDateParution() != null ? ressourceAModifier.getDateParution().getDayOfMonth() : "" %>">
                </div>
                <div class="form-group">
                    <label>Nombre d'exemplaires</label>
                    <input type="number" name="availableCopies" min="0"
                        value="<%= ressourceAModifier.getAvailableCopies() %>" required>
                </div>

                <% if (estLivre) {
                    Livre lm = (Livre) ressourceAModifier; %>
                    <div class="form-group">
                        <label>Genre</label>
                        <input type="text" name="genre"
                            value="<%= lm.getGenre() != null ? lm.getGenre() : "" %>">
                    </div>
                    <div class="form-group">
                        <label>Édition</label>
                        <input type="text" name="edition"
                            value="<%= lm.getEdition() != null ? lm.getEdition() : "" %>">
                    </div>
                <% } else {
                    Document dm = (Document) ressourceAModifier; %>
                    <div class="form-group">
                        <label>Type de document</label>
                        <select name="typeDoc">
                            <option value="Journal"  <%= "Journal".equals(dm.getType())  ? "selected" : "" %>>Journal</option>
                            <option value="Nouvelle" <%= "Nouvelle".equals(dm.getType()) ? "selected" : "" %>>Nouvelle</option>
                            <option value="Thèse"    <%= "Thèse".equals(dm.getType())    ? "selected" : "" %>>Thèse</option>
                            <option value="Rapport"  <%= "Rapport".equals(dm.getType())  ? "selected" : "" %>>Rapport</option>
                        </select>
                    </div>
                <% } %>

                <button type="submit" class="btn btn-primary">Enregistrer</button>
                <a href="RessourceServlet" class="btn btn-secondary">Annuler</a>
            </form>
        </div>
        <% } %>

        <!-- Recherche -->
        <div class="form-section">
            <h3>Rechercher</h3>
            <div class="filtres-wrapper">
                <div class="form-group" style="margin:0">
                    <label>Type</label>
                    <select id="filtreTypeRessource" onchange="filtrerRessources()">
                        <option value="">Tous</option>
                        <option value="Livre">Livres</option>
                        <option value="Document">Documents</option>
                    </select>
                </div>
                <div class="form-group" style="margin:0">
                    <label>Recherche</label>
                    <input type="text" id="filtreRessource" oninput="filtrerRessources()"
                        placeholder="Titre, auteur...">
                </div>
                <button class="btn btn-secondary" onclick="resetFiltresRessources()">
                    Réinitialiser
                </button>
                <a href="<%= request.getContextPath() %>/SuiviServlet"
                class="btn btn-secondary btn-push-right">
                    Suivi emprunts
                </a>
            </div>
        </div>

        <p class="compteur-label">
            <span id="compteurRessources">
                <%= ressources != null ? ressources.size() : 0 %>
            </span> ressource(s) affichée(s)
        </p>

        <h3>Toutes les ressources</h3>
        <div class="table-wrapper">
            <table id="tableRessources">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Type</th>
                        <th>Titre</th>
                        <th>Auteur</th>
                        <th>Date parution</th>
                        <th>Exemplaires</th>
                        <th>Description</th>
                        <th>Détails</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    if (ressources != null) {
                        for (Ressource r : ressources) {
                            boolean estLivre = r instanceof Livre;
                            String typeAffiche = estLivre ? "Livre" : "Document";
                            String details = "";
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
                <tr data-type="<%= typeAffiche %>" data-search="<%= r.getTitle().toLowerCase() %> <%= r.getAuteur() != null ? r.getAuteur().toLowerCase() : "" %>">
                    <td><%= r.getId() %></td>
                    <td>
                        <span class="badge <%= estLivre ? "badge-livre" : "badge-document" %>">
                            <%= typeAffiche %>
                        </span>
                    </td>
                    <td><strong><%= r.getTitle() %></strong></td>
                    <td><%= r.getAuteur()       != null ? r.getAuteur()       : "—" %></td>
                    <td><%= r.getDateParution() != null ? r.getDateParution() : "—" %></td>
                    <td>
                        <% if (r.getAvailableCopies() > 0) { %>
                            <span class="badge badge-livre"><%= r.getAvailableCopies() %></span>
                        <% } else { %>
                            <span class="badge badge-document">0</span>
                        <% } %>
                    </td>
                    <td><%= r.getDescription()  != null ? r.getDescription()  : "—" %></td>
                    <td><%= details.isEmpty()   ? "—" : details %></td>
                    <td class="actions-cell">
                        <form method="post" action="RessourceServlet">
                            <input type="hidden" name="action" value="chargerModif">
                            <input type="hidden" name="id"     value="<%= r.getId() %>">
                            <button type="submit" class="btn btn-primary">Modifier</button>
                        </form>
                        <form method="post" action="RessourceServlet"
                            onsubmit="return confirm('Supprimer cette ressource ?')">
                            <input type="hidden" name="action" value="supprimer">
                            <input type="hidden" name="id"     value="<%= r.getId() %>">
                            <button type="submit" class="btn btn-danger">Supprimer</button>
                        </form>
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
        <a href="<%= request.getContextPath() %>/SuiviServlet" class="btn btn-secondary">
            Suivi des emprunts
        </a>
    </div>

    <script src="<%= request.getContextPath() %>/js/ressource.js"></script>
</body>
</html>