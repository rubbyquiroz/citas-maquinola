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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@600;700&family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
    <link href="<c:url value='/resources/css/custom.css'/>" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="<c:url value='/paciente/dashboard'/>"><i class="bi bi-hospital"></i> Clínica</a>
            <div class="navbar-nav ms-auto">
                <a href="<c:url value='/paciente/dashboard'/>" class="nav-item nav-link text-light"><i class="bi bi-speedometer2 me-1"></i>Dashboard</a>
                <form action="<c:url value='/logout'/>" method="post" class="d-inline">
                    <button type="submit" class="btn btn-outline-light btn-sm ms-2"><i class="bi bi-box-arrow-right"></i> Cerrar Sesión</button>
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

        <div class="page-header">
            <h2><i class="bi bi-calendar-plus"></i> Agendar Nueva Cita</h2>
        </div>

        <div class="card shadow">
            <div class="card-body">
                <form action="<c:url value='/paciente/agendar'/>" method="post">
                    <div class="mb-3">
                        <label for="doctorId" class="form-label"><i class="bi bi-person-badge me-1"></i> Seleccione un Doctor</label>
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
                        <label for="fecha" class="form-label"><i class="bi bi-calendar-event me-1"></i> Fecha</label>
                        <input type="date" class="form-control" id="fecha" name="fecha" required
                               min="<%= java.time.LocalDate.now() %>">
                    </div>

                    <div class="mb-3">
                        <label for="hora" class="form-label"><i class="bi bi-clock me-1"></i> Hora</label>
                        <select class="form-select" id="hora" name="hora" required>
                            <option value="">-- Seleccione --</option>
                            <c:forEach var="h" items="${horarios}">
                                <option value="${h}">${h}</option>
                            </c:forEach>
                        </select>
                        <small class="text-muted">Horarios disponibles: Lunes a Viernes, 9:00 - 17:00 (turnos de 30 min)</small>
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn btn-success"><i class="bi bi-check-circle me-1"></i> Confirmar Cita</button>
                        <a href="<c:url value='/paciente/dashboard'/>" class="btn btn-secondary">Cancelar</a>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
