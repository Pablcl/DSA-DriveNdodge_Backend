$(document).ready(function () {

    let username = localStorage.getItem("username");
    if (!username) {
        window.location.href = "index.html";
        return;
    }

    $('#username-display').text(username);
    loadUserCoins();

    $('#preview-clan-img').attr('src', $('#clan-imagen').val());

    $('#clan-imagen').on('change', function () {
        $('#preview-clan-img').attr('src', $(this).val());
    });

    $('#btn-logout').click(function (e) {
        e.preventDefault();
        localStorage.clear();
        window.location.href = "index.html";
    });

    $('#form-crear-clan').submit(function (e) {
        e.preventDefault();
        crearClan();
    });
});

function loadUserCoins() {
    $.ajax({
        type: 'GET',
        url: '/v1/shop/monedas/' + localStorage.getItem("username"),
        dataType: 'json',
        success: function (response) {
            $('#coins-display').text(response.coins);
        }
    });
}

function crearClan() {
    let currentUsername = localStorage.getItem("username");
    const clan = {
        nombre: $('#clan-nombre').val().trim(),
        descripcion: $('#clan-descripcion').val().trim(),
        imagen: $('#clan-imagen').val(),
        username: currentUsername
    };

    $.ajax({
        type: 'POST',
        url: '/v1/clan/create',
        contentType: 'application/json',
        data: JSON.stringify(clan),
        success: function () {
            window.location.href = 'clanes.html';
        },
        error: function (xhr) {
            let msg = "Error al crear el clan";
            if (xhr.responseJSON && xhr.responseJSON.message) {
                msg = xhr.responseJSON.message;
            }
            $('#mensaje-error').text(msg).removeClass('d-none');
        }
    });
}
