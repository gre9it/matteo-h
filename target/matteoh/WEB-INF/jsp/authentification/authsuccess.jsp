<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="fr.turgot.dao.model.User" %>
<%
    User connectedUser = (User) session.getAttribute("connectedUser");
    if (connectedUser == null) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Connexion réussie</title>
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
    <div class="card">
        <h2>Connexion réussie</h2>
        <p>Bienvenue <strong><%= connectedUser.getUsername() %></strong></p>
        <p>Rôle : <strong><%= connectedUser.getRole() %></strong></p>

        <% if (connectedUser.getRole() == User.Role.ADMIN) { %>
            <a href="<%= request.getContextPath() %>/AdminServlet" class="btn btn-primary">
                Gérer les utilisateurs
            </a>

        <% } else if (connectedUser.getRole() == User.Role.BIBLIOTHECAIRE) { %>
            <a href="<%= request.getContextPath() %>/RessourceServlet" class="btn btn-primary">
                Gérer les ressources
            </a>
            <br><br>
            <a href="<%= request.getContextPath() %>/SuiviServlet" class="btn btn-primary">
                Suivi des emprunts
            </a>

        <% } else { %>
            <a href="<%= request.getContextPath() %>/CatalogueServlet" class="btn btn-primary">
                Voir le catalogue
            </a>
            <br><br>
            <a href="<%= request.getContextPath() %>/EmpruntServlet" class="btn btn-primary">
                Mes emprunts
            </a>
        <% } %>

        <br><br>
        <a href="<%= request.getContextPath() %>/index.jsp" class="btn btn-secondary">
            Retour à l'accueil
        </a>
    </div>
</body>
</html>