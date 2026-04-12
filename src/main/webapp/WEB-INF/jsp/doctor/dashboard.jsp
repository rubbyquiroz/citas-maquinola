<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Doctor - Clínica</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<c:url value='/resources/css/custom.css'/>" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-success">
        <div class="container">
            <a class="navbar-brand" href="<c:url value='/doctor/dashboard'/>">Clínica - Doctor</a>
            <div class="navbar-nav ms-auto">
                <span class="nav-item nav-link text-light">Dr. ${doctor.nombre}</span>
                <form action="<c:url value='/logout'/>" method="post" class="d-inline">
                    <button type="submit" class="btn btn-outline-light btn-sm ms-2">Cerrar Sesión</button>
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

        <h2 class="mb-4">Mi Agenda de Citas</h2>

        <div class="card shadow">
            <div class="card-header bg-success text-white">
                <h5 class="mb-0">Citas Programadas</h5>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${empty citas}">
                        <p class="text-muted text-center">No tiene citas programadas.</p>
                    </c:when>
                    <c:otherwise>
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>Paciente</th>
                                    <th>Fecha</th>
                                    <th>Hora</th>
                                    <th>Estado</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="cita" items="${citas}">
                                    <tr>
                                        <td>
                                            <a href="<c:url value='/doctor/paciente/${cita.pacienteId}'/>">
                                                ${cita.nombrePaciente}
                                            </a>
                                        </td>
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
                                            <div class="btn-group btn-group-sm" role="group">
                                                <c:if test="${cita.estado eq 'PENDIENTE'}">
                                                    <form action="<c:url value='/doctor/confirmar-cita'/>" method="post" class="d-inline">
                                                        <input type="hidden" name="citaId" value="${cita.id}">
                                                        <button type="submit" class="btn btn-success">Confirmar</button>
                                                    </form>
                                                </c:if>
                                                <c:if test="${cita.estado eq 'PENDIENTE' || cita.estado eq 'CONFIRMADA'}">
                                                    <form action="<c:url value='/doctor/cancelar-cita'/>" method="post" class="d-inline">
                                                        <input type="hidden" name="citaId" value="${cita.id}">
                                                        <button type="submit" class="btn btn-danger"
                                                                onclick="return confirm('¿Está seguro de cancelar esta cita?')">
                                                            Cancelar
                                                        </button>
                                                    </form>
                                                </c:if>
                                                <a href="<c:url value='/doctor/paciente/${cita.pacienteId}'/>" class="btn btn-outline-primary">
                                                    Ver Paciente
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
