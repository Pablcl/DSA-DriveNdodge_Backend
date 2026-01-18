$(document).ready(function () {

    let username = localStorage.getItem("username");
    if (!username) {
        window.location.href = "index.html";
        return;
    }

    $('#username-display').text(username);
    loadUserCoins();
    loadEventos();

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

    function loadEventos() {
        $.ajax({
            type: 'GET',
            url: '/v1/eventos/list',
            dataType: 'json',
            success: function (eventos) {

                let container = $('#eventos-container');
                container.empty();

                if (!eventos || eventos.length === 0) {
                    container.html(
                        '<p class="text-center pixel-font">No hay eventos disponibles.</p>'
                    );
                    return;
                }

                eventos.forEach(evento => {
                    container.append(`
                    <div class="col-md-3 mb-4">
                        <div class="card h-100 card-shop border-warning bg-dark text-light evento-card"
                             data-id="${evento.id}">
                            <img src="${evento.imagen}"
                                 class="card-img-top evento-img"
                                 alt="Evento">
                        </div>
                    </div>
                `);
                });

                $('.evento-card').click(function () {
                    const eventId = $(this).data('id');
                    window.location.href = `detail_event.html?id=${eventId}`;
                });


            },
            error: function () {
                $('#eventos-container').html(
                    '<p class="text-center pixel-font text-warning">Error cargando los eventos.</p>'
                );
            }
        });
    }

});
