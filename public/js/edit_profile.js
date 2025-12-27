var selectedAvatarFile = "avatar_default.webp"; //Default

function openAvatarModal() {
    $('#avatarModal').fadeIn();
}

function closeAvatarModal() {
    $('#avatarModal').fadeOut();
}

function selectAvatar(imageName) {
    selectedAvatarFile = imageName;

    $('#profileImage').attr('src', 'img/avatar/' + imageName);

    closeAvatarModal();
}


$(document).ready(function() {
    var username = localStorage.getItem('username');
    if (!username) {
        alert("No hay sesión iniciada. Redirigiendo al login...");
        window.location.href = "login.html";
        return;
    }

    $.ajax({
        url: '/v1/perfil/' + username,
        type: 'GET',
        dataType: 'json',
        success: function(user) {
            $('#nombre').val(user.nombre);
            $('#apellido').val(user.apellido);
            $('#email').val(user.email);
            $('#fecha').val(user.fechaNacimiento);
            if (user.imagenPerfil) {
                selectedAvatarFile = user.imagenPerfil;
                $('#profileImage').attr('src', 'img/avatar/' + user.imagenPerfil);
            }
        },
        error: function(xhr) {
            console.error("Error cargando perfil:", xhr);
            alert("Error al cargar los datos del perfil. Código: " + xhr.status);
        }
    });

    $('#btnGuardar').click(function() {
        // Objeto JSON a enviar
        var updatedUser = {
            nombre: $('#nombre').val(),
            apellido: $('#apellido').val(),
            email: $('#email').val(),
            fechaNacimiento: $('#fecha').val(),
            imagenPerfil: selectedAvatarFile
        };

        $.ajax({
            url: '/v1/perfil/' + username,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(updatedUser),
            success: function(response) {
                alert("¡Perfil actualizado correctamente!");
                window.location.href = "perfil.html"; // Volver a la pantalla de perfil
            },
            error: function(xhr) {
                console.error("Error actualizando:", xhr);
                alert("Error al guardar los cambios. Asegúrate de que los datos son válidos.");
            }
        });
    });

    $('#btnCancelar').click(function() {
        window.location.href = "perfil.html";
    });
});