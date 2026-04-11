<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<nav class="navbar">
    <div class="navbar-brand">
        <span class="navbar-logo">&#128218;</span>
        <span class="navbar-title">Bibliothèque Universitaire Turgot</span>
    </div>
    <div class="navbar-links">
        <c:if test="${not empty sessionScope.connectedUser}">
            <span class="navbar-user">
                &#128100; ${sessionScope.connectedUser.username}
                <span class="role-badge role-${sessionScope.connectedUser.role}">
                    ${sessionScope.connectedUser.role}
                </span>
            </span>
            <c:choose>
                <c:when test="${sessionScope.connectedUser.role == 'BIBLIOTHECAIRE'}">
                    <a href="${pageContext.request.contextPath}/ressources" class="nav-link">Dashboard</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/catalogue" class="nav-link">Catalogue</a>
                </c:otherwise>
            </c:choose>
            <a href="${pageContext.request.contextPath}/logout" class="nav-link nav-logout">Déconnexion</a>
        </c:if>
        <c:if test="${empty sessionScope.connectedUser}">
            <a href="${pageContext.request.contextPath}/login" class="nav-link">Connexion</a>
            <a href="${pageContext.request.contextPath}/register" class="nav-link">Inscription</a>
        </c:if>
    </div>
</nav>
