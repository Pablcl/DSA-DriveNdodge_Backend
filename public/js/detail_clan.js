let CURRENT_USER = null;

$(document).ready(function () {

    const username = localStorage.getItem("username");
    if (!username) {
        window.location.href = "index.html";
        return;
    }

    $('#username-display').text(username);
    loadUserCoins();

    const params = new URLSearchParams(window.location.search);
    const clanName = params.get('clan');

    if (!clanName) {
        window.location.href = "clanes.html";
        return;
    }

    loadClanInfo(clanName);
    loadClanMembers(clanName);

    $.ajax({
        url: '/v1/perfil/' + username,
        type: 'GET',
        dataType: 'json',
        success: function (user) {
            CURRENT_USER = user;

            if (user.clanNombre === clanName) {
                $('#btn-unirse-clan')
                    .prop('disabled', true)
                    .text('Ya eres miembro');

                $('#btn-salir-clan').removeClass('d-none');
            }
        }
    });

    $('#btn-unirse-clan').click(function () {
        unirseClan(clanName, username);
    });

    $('#btn-salir-clan').click(function () {
        salirClan(clanName);
    });

    $('#btn-logout').click(function (e) {
        e.preventDefault();
        localStorage.clear();
        window.location.href = "index.html";
    });
});

function loadUserCoins() {
    $.ajax({
        type: 'GET',
        url: '/v1/shop/monedas/' + localStorage.getItem("username"),
        dataType: 'json',
        success: r => $('#coins-display').text(r.coins),
        error: () => $('#coins-display').text('---')
    });
}

function loadClanInfo(clanName) {
    $.ajax({
        type: 'GET',
        url: '/v1/clan/all',
        dataType: 'json',
        success: function (clanes) {
            const clan = clanes.find(c => c.nombre === clanName);
            if (!clan) {
                window.location.href = "clanes.html";
                return;
            }

            $('#clan-nombre').text(`🛡️ ${clan.nombre}`);
            $('#clan-descripcion').text(clan.descripcion);
            $('#clan-imagen').attr('src', clan.imagen);
        }
    });
}

function loadClanMembers(clanName) {
    $.ajax({
        type: 'GET',
        url: `/v1/clan/${clanName}/members`,
        dataType: 'json',
        success: function (miembros) {
            const container = $('#clan-miembros');
            container.empty();

            if (!miembros || miembros.length === 0) {
                container.html('<p class="text-muted text-center pixel-font">Este clan no tiene miembros</p>');
                return;
            }

            miembros.forEach(u => {
                const isSelf = CURRENT_USER && u.username === CURRENT_USER.username;

                container.append(`
                    <div class="col-md-3 mb-3 text-center">
                        <img src="img/avatar/${u.imagenPerfil}"
                             class="clan-member-avatar ${isSelf ? 'clan-member-self' : ''}">
                        <div class="pixel-font text-light">
                            ${u.username}${isSelf ? ' (Tú)' : ''}
                        </div>
                    </div>
                `);
            });
        }
    });
}

function showInfoModal(message) {
    $('#info-modal-message').text(message);
    const modal = new bootstrap.Modal(document.getElementById('infoModal'));
    modal.show();
}


function unirseClan(clanName, username) {
    $.ajax({
        type: 'PUT',
        url: `/v1/clan/join/${clanName}`,
        contentType: 'application/json',
        data: JSON.stringify({ username }),
        success: function () {
            $('#btn-unirse-clan')
                .prop('disabled', true)
                .text('Ya eres miembro');

            $('#btn-salir-clan').removeClass('d-none');
            loadClanMembers(clanName);
        },
        error: function (xhr) {
            if (xhr.status === 409 && xhr.responseJSON) {
                showInfoModal(xhr.responseJSON.message);
            } else {
                showInfoModal("No se pudo unir al clan en este momento.");
            }
        }

    });
}


function salirClan(clanName) {
    $.ajax({
        type: 'PUT',
        url: `/v1/clan/leave`,
        contentType: 'application/json',
        data: JSON.stringify({ username: CURRENT_USER.username }),
        success: function () {
            window.location.replace("clanes.html");
        },
        error: function () {
            showInfoModal("Error al salir del clan.");
        }
    });
}
