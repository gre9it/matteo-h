<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Erreur – Bibliothèque Universitaire</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-page">
<%@ include file="navbar.jsp" %>
<main class="auth-container">
    <div class="auth-card error-card">
        <div class="error-icon">&#9888;</div>
        <h1>Accès refusé</h1>
        <p class="muted">${not empty errorMessage ? errorMessage : 'Vous n\'êtes pas autorisé à accéder à cette page.'}</p>
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Retour à l'accueil</a>
    </div>
</main>
</body>
</html>
