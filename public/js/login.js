$(document).ready(function() {
    $('#login-form').submit(function(event) {
        event.preventDefault();

        // Ocultar mensajes previos
        $('#error-message').hide();
        $('#success-message').hide();

        const username = $('#username').val();
        const password = $('#password').val();

        // Petición AJAX al backend
        $.ajax({
            url: '/v1/auth/login',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ username: username, password: password }),
            success: function(response) {
                $('#success-message').text('¡Inicio de sesión exitoso! Redirigiendo...').show();
                $('#login-form')[0].reset();
                localStorage.setItem("username", username);

                setTimeout(function() {
                    window.location.href = 'shop.html';
                }, 1500);
            },
            error: function(jqXHR) {
                let errorMessage = 'Error al iniciar sesión. Usuario o contraseña incorrectos.';
                if (jqXHR.responseText) {
                    errorMessage = jqXHR.responseText;
                }
                $('#error-message').text(errorMessage).show();
            }
        });
    });
});
