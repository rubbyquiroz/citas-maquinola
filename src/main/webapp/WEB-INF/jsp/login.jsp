<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión - Clínica</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@600;700&family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
    <link href="<c:url value='/resources/css/custom.css'/>" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container login-wrapper">
        <div class="row justify-content-center w-100">
            <div class="col-md-5">
                <div class="card shadow login-card">
                    <div class="card-body p-4">
                        <div class="login-icon-circle">
                            <i class="bi bi-hospital"></i>
                        </div>
                        <h2 class="card-title text-center mb-1">Clínica - Citas Médicas</h2>
                        <h5 class="text-center text-muted mb-4">Iniciar Sesión</h5>

                        <c:if test="${param.error != null}">
                            <div class="alert alert-danger" role="alert">
                                Email o contraseña incorrectos.
                            </div>
                        </c:if>
                        <c:if test="${param.logout != null}">
                            <div class="alert alert-success" role="alert">
                                Sesión cerrada correctamente.
                            </div>
                        </c:if>

                        <form action="<c:url value='/do-login'/>" method="post">
                            <div class="mb-3">
                                <label for="username" class="form-label"><i class="bi bi-envelope me-1"></i> Email</label>
                                <input type="email" class="form-control" id="username" name="username"
                                       placeholder="tu@email.com" required autofocus>
                            </div>
                            <div class="mb-3">
                                <label for="password" class="form-label"><i class="bi bi-lock me-1"></i> Contraseña</label>
                                <input type="password" class="form-control" id="password" name="password"
                                       placeholder="Contraseña" required>
                            </div>
                            <button type="submit" class="btn btn-primary w-100 btn-lg"><i class="bi bi-box-arrow-in-right me-1"></i> Iniciar Sesión</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
