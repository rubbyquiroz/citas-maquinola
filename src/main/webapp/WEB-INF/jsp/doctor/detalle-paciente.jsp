<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalle Paciente - Clínica</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<c:url value='/resources/css/custom.css'/>" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-success">
        <div class="container">
            <a class="navbar-brand" href="<c:url value='/doctor/dashboard'/>">Clínica - Doctor</a>
            <div class="navbar-nav ms-auto">
                <a href="<c:url value='/doctor/dashboard'/>" class="nav-item nav-link text-light">Mi Agenda</a>
                <form action="<c:url value='/logout'/>" method="post" class="d-inline">
                    <button type="submit" class="btn btn-outline-light btn-sm ms-2">Cerrar Sesión</button>
                </form>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <h2 class="mb-4">Detalle del Paciente</h2>

        <div class="row">
            <div class="col-md-6">
                <div class="card shadow mb-4">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0">Datos del Paciente</h5>
                    </div>
                    <div class="card-body">
                        <table class="table table-borderless">
                            <tr>
                                <th>Nombre:</th>
                                <td>${paciente.nombre}</td>
                            </tr>
                            <tr>
                                <th>Email:</th>
                                <td>${paciente.email}</td>
                            </tr>
                            <tr>
                                <th>Teléfono:</th>
                                <td>${paciente.telefono != null ? paciente.telefono : 'No registrado'}</td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <div class="card shadow">
            <div class="card-header bg-primary text-white">
                <h5 class="mb-0">Historial de Citas</h5>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${empty citas}">
                        <p class="text-muted">Este paciente no tiene citas registradas con usted.</p>
                    </c:when>
                    <c:otherwise>
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>Fecha</th>
                                    <th>Hora</th>
                                    <th>Estado</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="cita" items="${citas}">
                                    <tr>
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
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
                <div class="mt-3">
                    <a href="<c:url value='/doctor/dashboard'/>" class="btn btn-outline-primary">Volver a Mi Agenda</a>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
