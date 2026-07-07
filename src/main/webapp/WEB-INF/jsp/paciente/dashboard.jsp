<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Clínica</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@600;700&family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
    <link href="<c:url value='/resources/css/custom.css'/>" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="<c:url value='/paciente/dashboard'/>"><i class="bi bi-hospital"></i> Clínica</a>
            <div class="navbar-nav ms-auto align-items-center">
                <span class="nav-item nav-link text-light"><i class="bi bi-person-circle me-1"></i>Hola, ${paciente.nombre}</span>
                <form action="<c:url value='/logout'/>" method="post" class="d-inline">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                    <button type="submit" class="btn btn-outline-light btn-sm ms-2"><i class="bi bi-box-arrow-right"></i> Cerrar Sesión</button>
                </form>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <c:if test="${success != null}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${success}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${error != null}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <div class="page-header">
            <h2><i class="bi bi-calendar2-check"></i> Mi Dashboard</h2>
            <a href="<c:url value='/paciente/agendar'/>" class="btn btn-success btn-lg">
                <i class="bi bi-plus-circle me-1"></i> Agendar Nueva Cita
            </a>
        </div>

        <div class="card shadow">
            <div class="card-header bg-primary text-white">
                <h5 class="mb-0"><i class="bi bi-clock-history"></i> Mis Citas Pendientes</h5>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${empty citas}">
                        <div class="empty-state">
                            <i class="bi bi-calendar-x"></i>
                            No tiene citas pendientes. ¡Agende una nueva!
                        </div>
                    </c:when>
                    <c:otherwise>
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>Doctor</th>
                                    <th>Fecha</th>
                                    <th>Hora</th>
                                    <th>Estado</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="cita" items="${citas}">
                                    <tr>
                                        <td>${cita.nombreDoctor}</td>
                                        <td>${cita.fecha}</td>
                                        <td>${cita.hora}</td>
                                        <td>
                                            <span class="badge
                                                <c:choose>
                                                    <c:when test="${cita.estado eq 'PENDIENTE'}">bg-warning text-dark</c:when>
                                                    <c:when test="${cita.estado eq 'CONFIRMADA'}">bg-success</c:when>
                                                    <c:when test="${cita.estado eq 'CANCELADA'}">bg-danger</c:when>
                                                    <c:otherwise>bg-secondary</c:otherwise>
                                                </c:choose>">
                                                ${cita.estado}
                                            </span>
                                        </td>
                                        <td>
                                            <c:if test="${cita.estado eq 'PENDIENTE'}">
                                                <form action="<c:url value='/paciente/cancelar-cita'/>" method="post" class="d-inline">
                                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                                    <input type="hidden" name="citaId" value="${cita.id}">
                                                    <button type="submit" class="btn btn-sm btn-danger"
                                                            onclick="return confirm('¿Está seguro de cancelar esta cita?')">
                                                        Cancelar
                                                    </button>
                                                </form>
                                            </c:if>
                                            <c:if test="${cita.estado ne 'PENDIENTE'}">
                                                <span class="text-muted">-</span>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        <div class="mt-3">
                            <a href="<c:url value='/paciente/mis-citas'/>" class="btn btn-outline-primary">
                                <i class="bi bi-list-ul me-1"></i> Ver Todas Mis Citas
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
