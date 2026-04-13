<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="fr.turgot.utils.LinkHelper" %>
<!DOCTYPE html>
<html>
<head>
    <title>Inscription</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <nav class="navbar">
        <h1>Bibliothèque Universitaire</h1>
        <a href="<%= request.getContextPath() %>/index.jsp">Accueil</a>
    </nav>
    <div class="form-auth">
        <h2>Inscription</h2>
        <%
            String error = (String) request.getAttribute("error");
            if (error != null) { %>
                <p class="error"><%= error %></p>
        <% } %>
        <form method="post" action="RegisterServlet">
            <div class="form-group">
                <label>Nom d'utilisateur</label>
                <input type="text" name="username" required>
            </div>
            <div class="form-group">
                <label>Mot de passe</label>
                <input type="password" name="password" required>
            </div>
            <div class="form-group">
                <label>Confirmer le mot de passe</label>
                <input type="password" name="confirm" required>
            </div>
            <button type="submit" class="btn btn-primary">S'inscrire</button>
        </form>
        <div class="link-bottom">
            Déjà inscrit ?
            <a href="<%= LinkHelper.jspLink("authentification/authentification") %>">Se connecter</a>
        </div>
    </div>
</body>
</html>