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

function showToast(type, message) {
    const toastId = 'toast-' + Date.now();
    const toastHtml = `
    <div id="${toastId}" class="toast align-items-center text-bg-${type} border-0" role="alert" aria-live="assertive" aria-atomic="true">
      <div class="d-flex">
        <div class="toast-body">${message}</div>
        <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
      </div>
    </div>`;
    $('#toast-container').append(toastHtml);

    const toastEl = document.getElementById(toastId);
    const toast = new bootstrap.Toast(toastEl, { delay: 3000 });
    toast.show();

    toastEl.addEventListener('hidden.bs.toast', function () { $(this).remove(); });
}



$(document).ready(function() {
    var username = localStorage.getItem('username');
    if (!username) {
        showToast('warning', 'No hay sesión iniciada. Redirigiendo al login...');
        setTimeout(() => window.location.href = "login.html", 1200);
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
            showToast('warning', 'Error al cargar los datos del perfil. Código: ' + xhr.status);        }
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
                showToast('success', '¡Perfil actualizado correctamente!');
                setTimeout(() => window.location.href = "perfil.html", 1200);
            },
            error: function(xhr) {
                console.error("Error actualizando:", xhr);
                showToast('warning', xhr.responseText || 'Error al guardar los cambios. Asegúrate de que los datos son válidos.');
            }
        });
    });

    $('#btnCancelar').click(function() {
        window.location.href = "perfil.html";
    });
});
