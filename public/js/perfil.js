$(document).ready(function() {
    var username = localStorage.getItem('username');

    if (!username) {
        window.location.href = "login.html";
        return;
    }

    $.ajax({
        url: '/v1/perfil/' + username,
        type: 'GET',
        dataType: 'json',
        success: function(user) {
            $('#usernameDisplay').text(user.username || username);
            $('#monedasVal').text(user.monedas);
            $('#puntosVal').text(user.mejorPuntuacion);
            $('#nombreVal').text(user.nombre || '-');
            $('#apellidoVal').text(user.apellido || '-');
            $('#fechaVal').text(user.fechaNacimiento || '-');
            $('#emailVal').text(user.email || '-');

            if (user.imagenPerfil) {
                $('#profileImage').attr('src', 'img/avatar/' + user.imagenPerfil);
            }
            // LÓGICA DEL CLAN
            var clanName = user.clanNombre;
            var clanImg = user.clanImagen;
            $('#clanBadge').css('display', 'inline-flex');

            if (clanName && clanName !== "Sin Clan" && clanName !== "null") {

                $('#clanNameText').text(clanName);
                if (!clanImg || clanImg === "null") {
                    clanImg = "clan_default.png";
                }
                $('#clanIcon').attr('src', 'img/clan/' + clanImg).show();

            } else {
                $('#clanNameText').text("Sin Clan").css('color', '#AAA');
                $('#clanIcon').hide();
            }
        },
        error: function(xhr) {
            console.error("Error:", xhr);
            $('#usernameDisplay').text("Error");
        }
    });
});