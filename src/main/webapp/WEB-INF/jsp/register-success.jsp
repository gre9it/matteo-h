<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inscription réussie – Bibliothèque Universitaire</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-page">
<%@ include file="navbar.jsp" %>

<main class="auth-container">
    <div class="auth-card success-card">
        <div class="success-icon">&#10003;</div>
        <div class="auth-header">
            <h1>Compte créé !</h1>
            <p>Bienvenue, <strong>${newUsername}</strong>. Votre compte étudiant a été créé avec succès.</p>
        </div>
        <a href="${pageContext.request.contextPath}/login" class="btn btn-primary btn-full">
            Se connecter maintenant
        </a>
    </div>
</main>
</body>
</html>
