<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="fr.turgot.utils.LinkHelper" %>
<!DOCTYPE html>
<html>
<head>
    <title>Inscription réussie</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <nav class="navbar">
        <h1>Bibliothèque Universitaire</h1>
    </nav>
    <div class="card">
        <h2>Inscription réussie !</h2>
        <p>Bienvenue <strong><%= request.getAttribute("username") %></strong></p>
        <p>Votre compte a été créé avec le rôle Étudiant.</p>
        <a href="<%= request.getContextPath() %>/AuthentificationServlet"
        class="btn btn-primary">
            Se connecter
        </a>
    </div>
</body>
</html>