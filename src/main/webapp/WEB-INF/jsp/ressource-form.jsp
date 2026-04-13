<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty ressource ? 'Ajouter' : 'Modifier'} une ressource – Bibliothèque</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="navbar.jsp" %>

<main class="main-content">
    <div class="page-header">
        <h1>${empty ressource ? 'Ajouter une ressource' : 'Modifier la ressource'}</h1>
        <a href="${pageContext.request.contextPath}/ressources" class="btn btn-secondary">
            &larr; Retour au dashboard
        </a>
    </div>

    <div class="form-card">
        <form action="${pageContext.request.contextPath}/ressources" method="post" id="ressourceForm">
            <input type="hidden" name="action" value="save">
            <c:if test="${not empty ressource}">
                <input type="hidden" name="id" value="${ressource.id}">
            </c:if>

            <!-- Champ Type (uniquement en création) -->
            <c:if test="${empty ressource}">
                <div class="form-group">
                    <label for="typeRessource">Type de ressource <span class="required">*</span></label>
                    <select name="typeRessource" id="typeRessource" class="form-select" required>
                        <option value="">-- Choisir --</option>
                        <option value="Livre">Livre</option>
                        <option value="Document">Document</option>
                    </select>
                </div>
            </c:if>
            <c:if test="${not empty ressource}">
                <input type="hidden" name="typeRessource" value="${ressource.type == 'Livre' ? 'Livre' : 'Document'}">
                <div class="form-group">
                    <label>Type</label>
                    <input type="text" value="${ressource.type}" disabled class="form-input-disabled">
                </div>
            </c:if>

            <!-- Champs communs -->
            <div class="form-group">
                <label for="title">Titre <span class="required">*</span></label>
                <input type="text" id="title" name="title"
                       value="${not empty ressource ? ressource.title : ''}"
                       placeholder="Titre de la ressource" required>
            </div>
            <div class="form-group">
                <label for="auteur">Auteur</label>
                <input type="text" id="auteur" name="auteur"
                       value="${not empty ressource ? ressource.auteur : ''}"
                       placeholder="Nom de l'auteur">
            </div>
            <div class="form-group">
                <label for="description">Description</label>
                <textarea id="description" name="description" rows="3"
                          placeholder="Description courte">${not empty ressource ? ressource.description : ''}</textarea>
            </div>
            <div class="form-row">
                <div class="form-group">
                    <label for="dateParution">Date de parution</label>
                    <input type="date" id="dateParution" name="dateParution"
                           value="${not empty ressource ? ressource.dateParution : ''}">
                </div>
                <div class="form-group">
                    <label for="availableCopies">Exemplaires disponibles</label>
                    <input type="number" id="availableCopies" name="availableCopies"
                           min="0" value="${not empty ressource ? ressource.availableCopies : '1'}">
                </div>
            </div>

            <!-- Champs spécifiques Livre -->
            <div id="livreFields" class="specific-fields hidden">
                <hr class="form-divider">
                <h3 class="fields-subtitle">Informations Livre</h3>
                <div class="form-row">
                    <div class="form-group">
                        <label for="genre">Genre</label>
                        <input type="text" id="genre" name="genre"
                               value="${not empty ressource ? ressource.genre : ''}"
                               placeholder="Ex: Roman, Essai, Science…">
                    </div>
                    <div class="form-group">
                        <label for="edition">Édition</label>
                        <input type="text" id="edition" name="edition"
                               value="${not empty ressource ? ressource.edition : ''}"
                               placeholder="Ex: 3e édition, Poche…">
                    </div>
                </div>
            </div>

            <!-- Champs spécifiques Document -->
            <div id="documentFields" class="specific-fields hidden">
                <hr class="form-divider">
                <h3 class="fields-subtitle">Informations Document</h3>
                <div class="form-group">
                    <label for="typeDocument">Type de document</label>
                    <select id="typeDocument" name="typeDocument" class="form-select">
                        <option value="JOURNAL"   ${not empty ressource && ressource.typeDocument == 'JOURNAL'   ? 'selected' : ''}>Journal</option>
                        <option value="NOUVELLE"  ${not empty ressource && ressource.typeDocument == 'NOUVELLE'  ? 'selected' : ''}>Nouvelle</option>
                        <option value="THESE"     ${not empty ressource && ressource.typeDocument == 'THESE'     ? 'selected' : ''}>Thèse</option>
                        <option value="RAPPORT"   ${not empty ressource && ressource.typeDocument == 'RAPPORT'   ? 'selected' : ''}>Rapport</option>
                    </select>
                </div>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">
                    ${empty ressource ? 'Enregistrer' : 'Mettre à jour'}
                </button>
                <a href="${pageContext.request.contextPath}/ressources" class="btn btn-secondary">Annuler</a>
            </div>
        </form>
    </div>
</main>

<script>
    // Passer le type actuel au JS si en mode édition
    var currentType = "${not empty ressource ? (ressource.type == 'Livre' ? 'Livre' : 'Document') : ''}";
</script>
<script src="${pageContext.request.contextPath}/js/ressource.js"></script>
</body>
</html>
