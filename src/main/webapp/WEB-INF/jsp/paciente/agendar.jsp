<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Agendar Cita - Clínica</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<c:url value='/resources/css/custom.css'/>" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="<c:url value='/paciente/dashboard'/>">Clínica</a>
            <div class="navbar-nav ms-auto">
                <a href="<c:url value='/paciente/dashboard'/>" class="nav-item nav-link text-light">Dashboard</a>
                <form action="<c:url value='/logout'/>" method="post" class="d-inline">
                    <button type="submit" class="btn btn-outline-light btn-sm ms-2">Cerrar Sesión</button>
                </form>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <c:if test="${error != null}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <h2 class="mb-4">Agendar Nueva Cita</h2>

        <div class="card shadow">
            <div class="card-body">
                <form action="<c:url value='/paciente/agendar'/>" method="post">
                    <div class="mb-3">
                        <label for="doctorId" class="form-label">Seleccione un Doctor</label>
                        <select class="form-select" id="doctorId" name="doctorId" required>
                            <option value="">-- Seleccione --</option>
                            <c:forEach var="doc" items="${doctores}">
                                <option value="${doc.id}">
                                    ${doc.nombre} - ${doc.especialidad}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="fecha" class="form-label">Fecha</label>
                        <input type="date" class="form-control" id="fecha" name="fecha" required
                               min="<%= java.time.LocalDate.now() %>">
                    </div>

                    <div class="mb-3">
                        <label for="hora" class="form-label">Hora</label>
                        <select class="form-select" id="hora" name="hora" required>
                            <option value="">-- Seleccione --</option>
                            <c:forEach var="h" items="${horarios}">
                                <option value="${h}">${h}</option>
                            </c:forEach>
                        </select>
                        <small class="text-muted">Horarios disponibles: Lunes a Viernes, 9:00 - 17:00 (turnos de 30 min)</small>
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn btn-success">Confirmar Cita</button>
                        <a href="<c:url value='/paciente/dashboard'/>" class="btn btn-secondary">Cancelar</a>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
