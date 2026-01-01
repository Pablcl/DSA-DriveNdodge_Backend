$(document).ready(function() {

    // Configurar límites del datepicker
    (function configureDatepickerLimits() {
        const MIN_AGE = 16;
        const MAX_AGE = 122;
        const today = new Date();
        const cutoffMax = new Date();
        cutoffMax.setFullYear(cutoffMax.getFullYear() - MIN_AGE);
        const cutoffMin = new Date();
        cutoffMin.setFullYear(cutoffMin.getFullYear() - MAX_AGE);
        const pad = (n) => n < 10 ? '0' + n : n;
        $('#fechaNacimiento').attr('max', cutoffMax.getFullYear() + '-' + pad(cutoffMax.getMonth()+1) + '-' + pad(cutoffMax.getDate()));
        $('#fechaNacimiento').attr('min', cutoffMin.getFullYear() + '-' + pad(cutoffMin.getMonth()+1) + '-' + pad(cutoffMin.getDate()));
    })();

    $('#register-form input').on('input', function() {
        const id = $(this).attr('id');
        $(this).removeClass('is-invalid');
        $('#' + id + '-error').hide();
        if (id === 'password' || id === 'confirm-password') {
            $('#password-match-message').hide();
        }
    });

    $('#password, #confirm-password').on('input', function() {
        const password = $('#password').val();
        const confirmPassword = $('#confirm-password').val();

        if (password && confirmPassword && password !== confirmPassword) {
            $('#password, #confirm-password').addClass('is-invalid');
            $('#password-match-message').show();
        } else {
            $('#password, #confirm-password').removeClass('is-invalid');
            $('#password-match-message').hide();
        }
    });

    $('#register-form').submit(function(event) {
        event.preventDefault();

        $('#error-message').hide();
        $('#success-message').hide();

        const username = $('#username').val();
        const password = $('#password').val();
        const confirmPassword = $('#confirm-password').val();
        const nombre = $('#nombre').val();
        const apellido = $('#apellido').val();
        const mail = $('#mail').val();
        const fechaNacimiento = $('#fechaNacimiento').val();

        let valid = true;

        if (password !== confirmPassword) {
            $('#password-match-message').text('Las contraseñas no coinciden.').show();
            $('#password, #confirm-password').addClass('is-invalid');
            valid=false;
        }

        const regexUsername = /^[a-zA-Z0-9]+$/;
        const regexNombreApellido = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/;
        const regexEmail = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        const MIN_AGE = 16;
        const MAX_AGE = 122;

        if (!regexUsername.test(username)) { $('#username-error').text('El Usuario solo puede contener letras y números.').show(); $('#username').addClass('is-invalid'); valid=false; }
        if (!regexNombreApellido.test(nombre)) { $('#nombre-error').text('El Nombre no puede contener números ni caracteres especiales.').show(); $('#nombre').addClass('is-invalid'); valid=false; }
        if (!regexNombreApellido.test(apellido)) { $('#apellido-error').text('El Apellido no puede contener números ni caracteres especiales.').show(); $('#apellido').addClass('is-invalid'); valid=false; }
        if (!regexEmail.test(mail)) { $('#mail-error').text('El correo electrónico no tiene un formato válido.').show(); $('#mail').addClass('is-invalid'); valid=false; }

        const birthDate = new Date(fechaNacimiento);
        const today = new Date();
        let age = today.getFullYear() - birthDate.getFullYear();
        const m = today.getMonth() - birthDate.getMonth();
        if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) { age--; }
        if (!fechaNacimiento || birthDate > today || age < MIN_AGE || age > MAX_AGE) {
            $('#fecha-error').text('Fecha de nacimiento inválida.').show(); $('#fechaNacimiento').addClass('is-invalid'); valid=false;
        }

        if(!valid) return;

        $.ajax({
            url: '/v1/auth/register',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ username, password, nombre, apellido, email: mail, fechaNacimiento }),
            success: function(response) {
                $('#success-message').text('¡Registro exitoso! Redirigiendo a la página de inicio de sesión...').show();
                $('#register-form')[0].reset();
                $('#password, #confirm-password').removeClass('is-invalid');
                setTimeout(function() { window.location.href = 'login.html'; }, 1500);
            },
            error: function(jqXHR) {
                let errorMessage = 'Error en el registro. Por favor, inténtalo de nuevo.';
                if (jqXHR.responseText) errorMessage = jqXHR.responseText;
                $('#error-message').text(errorMessage).show();
            }
        });
    });
});
