<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="fr.turgot.utils.LinkHelper" %>
<!DOCTYPE html>
<html>
<head>
    <title>Connexion</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <nav class="navbar">
        <h1>Bibliothèque Universitaire</h1>
        <a href="<%= request.getContextPath() %>/index.jsp">Accueil</a>
    </nav>
    <div class="form-auth">
        <h2>Connexion</h2>
        <%
            String error = (String) request.getAttribute("error");
            if (error != null) { %>
                <p class="error"><%= error %></p>
        <% } %>
        <form method="post" action="AuthentificationServlet">
            <div class="form-group">
                <label>Nom d'utilisateur</label>
                <input type="text" name="username" required>
            </div>
            <div class="form-group">
                <label>Mot de passe</label>
                <input type="password" name="password" required>
            </div>
            <button type="submit" class="btn btn-primary">Se connecter</button>
        </form>
        <div class="link-bottom">
            Pas encore de compte ?
            <a href="<%= LinkHelper.jspLink("formulaire/register") %>">S'inscrire</a>
        </div>
    </div>
</body>
</html>