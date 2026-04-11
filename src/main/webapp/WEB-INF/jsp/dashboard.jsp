<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard – Bibliothèque Universitaire</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="navbar.jsp" %>

<main class="main-content">
    <div class="page-header">
        <h1>Dashboard Bibliothécaire</h1>
        <a href="${pageContext.request.contextPath}/ressources?action=add" class="btn btn-primary">
            + Ajouter une ressource
        </a>
    </div>

    <div class="table-wrapper">
        <table class="data-table">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Type</th>
                    <th>Titre</th>
                    <th>Auteur</th>
                    <th>Parution</th>
                    <th>Exemplaires</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="r" items="${ressources}">
                    <tr>
                        <td class="muted">${r.id}</td>
                        <td><span class="type-badge type-${r.type}">${r.type}</span></td>
                        <td>${r.title}</td>
                        <td>${r.auteur}</td>
                        <td>
                            <c:if test="${not empty r.dateParution}">
                                <fmt:formatDate value="${r.dateParution}" pattern="dd/MM/yyyy" type="date"/>
                            </c:if>
                            <c:if test="${empty r.dateParution}">—</c:if>
                        </td>
                        <td>
                            <span class="copies-badge ${r.availableCopies > 0 ? 'copies-ok' : 'copies-empty'}">
                                ${r.availableCopies}
                            </span>
                        </td>
                        <td class="action-cell">
                            <a href="${pageContext.request.contextPath}/ressources?action=edit&id=${r.id}"
                               class="btn btn-sm btn-secondary">Modifier</a>
                            <a href="${pageContext.request.contextPath}/ressources?action=delete&id=${r.id}"
                               class="btn btn-sm btn-danger"
                               onclick="return confirm('Supprimer « ${r.title} » ?')">Supprimer</a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty ressources}">
                    <tr>
                        <td colspan="7" class="text-center muted">Aucune ressource enregistrée.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</main>
</body>
</html>
