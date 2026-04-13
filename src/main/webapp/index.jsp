<%@ page import="fr.turgot.utils.LinkHelper" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<h1%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Bibliothèque Universitaire</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <nav class="navbar">
        <h1>Bibliothèque Universitaire</h1>
    </nav>
    <div class="card">
        <h2>Bienvenue</h2>
        <p>Accédez à votre espace personnel</p>
        <a href="<%= LinkHelper.jspLink("authentification/authentification") %>" class="btn btn-primary">
            Se connecter
        </a>
        <br><br>
        <a href="<%= LinkHelper.jspLink("formulaire/register") %>" class="btn btn-secondary">
            S'inscrire
        </a>
    </div>
</body>
</html>