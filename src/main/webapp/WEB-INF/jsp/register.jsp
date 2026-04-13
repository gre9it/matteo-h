<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inscription – Bibliothèque Universitaire</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-page">
<%@ include file="navbar.jsp" %>

<main class="auth-container">
    <div class="auth-card">
        <div class="auth-header">
            <h1>Inscription</h1>
            <p>Créez votre compte étudiant</p>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">${errorMessage}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="post" class="auth-form">
            <div class="form-group">
                <label for="username">Nom d'utilisateur <span class="required">*</span></label>
                <input type="text" id="username" name="username"
                       value="${not empty lastUsername ? lastUsername : ''}"
                       placeholder="Choisissez un identifiant" required autofocus>
            </div>
            <div class="form-group">
                <label for="password">Mot de passe <span class="required">*</span></label>
                <input type="password" id="password" name="password"
                       placeholder="Au moins 6 caractères" required minlength="6">
            </div>
            <div class="form-group">
                <label for="confirmPassword">Confirmer le mot de passe <span class="required">*</span></label>
                <input type="password" id="confirmPassword" name="confirmPassword"
                       placeholder="Répétez le mot de passe" required>
            </div>
            <hr class="form-divider">
            <div class="form-group">
                <label for="numeroEtudiant">Numéro étudiant</label>
                <input type="text" id="numeroEtudiant" name="numeroEtudiant"
                       placeholder="Ex: 20240001">
            </div>
            <div class="form-group">
                <label for="filiere">Filière</label>
                <input type="text" id="filiere" name="filiere"
                       placeholder="Ex: Informatique, Droit...">
            </div>
            <button type="submit" class="btn btn-primary btn-full">Créer mon compte</button>
        </form>

        <div class="auth-footer">
            <p>Déjà inscrit ? <a href="${pageContext.request.contextPath}/login">Se connecter</a></p>
        </div>
    </div>
</main>
</body>
</html>
