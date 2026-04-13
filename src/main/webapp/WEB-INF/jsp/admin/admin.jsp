<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="fr.turgot.dao.model.User" %>
<%@ page import="java.util.List" %>
<%
    User connectedUser = (User) session.getAttribute("connectedUser");
    List<User> users   = (List<User>) request.getAttribute("users");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Administration</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <nav class="navbar">
        <h1>Bibliothèque Universitaire</h1>
        <div class="navbar-user">
            <span>Admin : <%= connectedUser.getUsername() %></span>
            <a href="<%= request.getContextPath() %>/DeconnexionServlet" class="btn-deconnexion">
                Se déconnecter
            </a>
        </div>
    </nav>

    <div class="container">
        <h2>Gestion des utilisateurs</h2>

        <%
            String success = (String) request.getAttribute("success");
            String error   = (String) request.getAttribute("error");
            if (success != null) { %> <p class="success"><%= success %></p> <% }
            if (error   != null) { %> <p class="error"><%= error %></p>     <% }
        %>
        <div>
            <div class="form-group" style="margin:0">
                    <label>Recherche</label>
                    <input type="text" id="filtreTexte" oninput="filtrer()"
                        placeholder="Titre, auteur...">
                </div>
                <button class="btn btn-secondary" onclick="resetFiltres()">Réinitialiser</button>
        </div>

        <div class="table-wrapper">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nom d'utilisateur</th>
                        <th>Rôle actuel</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    if (users != null) {
                        for (User u : users) {
                            boolean estAdmin = u.getRole() == User.Role.ADMIN;
                %>
                <tr>
                    <td><%= u.getId() %></td>
                    <td><strong><%= u.getUsername() %></strong></td>
                    <td>
                        <span class="badge
                            <%= u.getRole() == User.Role.ETUDIANT       ? "badge-livre"     :
                                u.getRole() == User.Role.PROFESSEUR     ? "badge-document"  :
                                u.getRole() == User.Role.BIBLIOTHECAIRE ? "badge-livre"     : "" %>">
                            <%= u.getRole() %>
                        </span>
                    </td>
                    <td>
                        <% if (!estAdmin) { %>
                        <div style="display:flex; gap:8px; align-items:center; flex-wrap:wrap;">

                            <!-- Changer le rôle -->
                            <form method="post" action="AdminServlet"
                                style="display:flex; gap:8px; align-items:center;"
                                onsubmit="return confirm('Changer le rôle de <%= u.getUsername() %> ?')">
                                <input type="hidden" name="action" value="changerRole">
                                <input type="hidden" name="userId" value="<%= u.getId() %>">
                                <select name="role">
                                    <option value="ETUDIANT"
                                        <%= u.getRole() == User.Role.ETUDIANT ? "selected" : "" %>>
                                        Étudiant
                                    </option>
                                    <option value="PROFESSEUR"
                                        <%= u.getRole() == User.Role.PROFESSEUR ? "selected" : "" %>>
                                        Professeur
                                    </option>
                                    <option value="BIBLIOTHECAIRE"
                                        <%= u.getRole() == User.Role.BIBLIOTHECAIRE ? "selected" : "" %>>
                                        Bibliothécaire
                                    </option>
                                </select>
                                <button type="submit" class="btn btn-primary">Appliquer</button>
                            </form>

                            <!-- Supprimer -->
                            <form method="post" action="AdminServlet"
                                onsubmit="return confirm('Supprimer <%= u.getUsername() %> définitivement ?')">
                                <input type="hidden" name="action" value="supprimer">
                                <input type="hidden" name="userId" value="<%= u.getId() %>">
                                <button type="submit" class="btn btn-danger">Supprimer</button>
                            </form>

                        </div>
                        <% } else { %>
                            <span style="color:var(--grey-dark); font-size:0.85rem;">Non modifiable</span>
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
    </div>
    <script src="<%= request.getContextPath() %>/js/admin.js"></script>
</body>
</html>