<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Catalogue – Bibliothèque Universitaire</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="navbar.jsp" %>

<main class="main-content">
    <div class="page-header">
        <h1>Catalogue des ressources</h1>
        <p class="page-subtitle">
            ${ressources.size()} ressource(s) disponible(s)
        </p>
    </div>

    <!-- Barre de filtres -->
    <div class="filter-bar">
        <div class="filter-group">
            <label for="searchInput">Recherche</label>
            <input type="text" id="searchInput" placeholder="Titre, auteur, description…" class="filter-input">
        </div>
        <div class="filter-group">
            <label for="typeFilter">Type</label>
            <select id="typeFilter" class="filter-select">
                <option value="">Tous les types</option>
                <option value="Livre">Livre</option>
                <option value="JOURNAL">Journal</option>
                <option value="NOUVELLE">Nouvelle</option>
                <option value="THESE">Thèse</option>
                <option value="RAPPORT">Rapport</option>
            </select>
        </div>
        <button id="resetFilters" class="btn btn-secondary">Réinitialiser</button>
    </div>

    <!-- Table des ressources -->
    <div class="table-wrapper">
        <table class="data-table" id="catalogueTable">
            <thead>
                <tr>
                    <th>Type</th>
                    <th>Titre</th>
                    <th>Auteur</th>
                    <th>Date de parution</th>
                    <th>Détails</th>
                    <th>Exemplaires disponibles</th>
                </tr>
            </thead>
            <tbody id="catalogueBody">
                <c:forEach var="r" items="${ressources}">
                    <tr class="catalogue-row"
                        data-type="${r.type}"
                        data-title="${r.title}"
                        data-auteur="${r.auteur}"
                        data-description="${r.description}">
                        <td>
                            <span class="type-badge type-${r.type}">${r.type}</span>
                        </td>
                        <td class="resource-title">${r.title}</td>
                        <td>${r.auteur}</td>
                        <td>
                            <fmt:formatDate value="${r.dateParution}" pattern="dd/MM/yyyy" type="date"/>
                            <c:if test="${empty r.dateParution}">—</c:if>
                        </td>
                        <td class="resource-details">
                            <c:choose>
                                <c:when test="${r['class'].simpleName == 'Livre'}">
                                    Genre : ${r.genre}<br>Édition : ${r.edition}
                                </c:when>
                                <c:otherwise>
                                    ${r.description}
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="copies-cell">
                            <span class="copies-badge ${r.availableCopies > 0 ? 'copies-ok' : 'copies-empty'}">
                                ${r.availableCopies}
                            </span>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty ressources}">
                    <tr id="emptyRow">
                        <td colspan="6" class="text-center muted">Aucune ressource dans le catalogue.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
        <div id="noResults" class="no-results hidden">Aucune ressource ne correspond aux filtres sélectionnés.</div>
    </div>
</main>

<script src="${pageContext.request.contextPath}/js/catalogue.js"></script>
</body>
</html>
