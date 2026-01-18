$(document).ready(function () {

    let username = localStorage.getItem("username");
    if (!username) {
        window.location.href = "index.html";
        return;
    }

    $('#username-display').text(username);
    loadUserCoins();

    const eventId = parseInt(
        new URLSearchParams(window.location.search).get('id')
    );

    if (!eventId) {
        window.location.href = "eventos.html";
        return;
    }

    loadEventoDesdeLista(eventId);

    $('#btn-logout').click(function (e) {
        e.preventDefault();
        localStorage.clear();
        window.location.href = "index.html";
    });

    function loadUserCoins() {
        $.ajax({
            type: 'GET',
            url: '/v1/shop/monedas/' + username,
            dataType: 'json',
            success: function (response) {
                $('#coins-display').text(response.coins);
            }
        });
    }

    function loadEventoDesdeLista(eventoId) {
        $.ajax({
            type: 'GET',
            url: '/v1/eventos/list',
            dataType: 'json',
            success: function (eventos) {

                const evento = eventos.find(e => e.id === eventoId);

                if (!evento) {
                    $('#evento-detalles').text('Evento no encontrado.');
                    return;
                }

                $('#evento-imagen').attr('src', evento.imagen);
                $('#evento-nombre').text(evento.nombre);
                $('#evento-descripcion').text(evento.descripcion);


                $('#evento-detalles').html(`
                    <p class="text-light pixel-font">
                        Prepárate para vivir este evento y demostrar que eres
                        capaz de dominar la carretera en cualquier entorno.
                    </p>
                `);
            },
            error: function () {
                $('#evento-detalles').text('Error cargando el evento.');
            }
        });
    }

});
