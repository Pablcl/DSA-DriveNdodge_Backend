$(document).ready(function () {

    let username = localStorage.getItem("username");
    if (!username) {
        window.location.href = "index.html";
        return;
    }

    $('#username-display').text(username);

    loadUserCoins();
    cargarRankingClanes();

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
        success: function (response) {
            $('#coins-display').text(response.coins);
        },
        error: function () {
            $('#coins-display').text('---');
        }
    });
}

function cargarRankingClanes() {
    $.ajax({
        type: 'GET',
        url: '/v1/clan/ranking',
        dataType: 'json',
        success: function (ranking) {
            renderRanking(ranking);
        },
        error: function () {
            $('#clan-ranking-container').html(
                '<p class="text-danger text-center pixel-font">Error al cargar el ranking de clanes</p>'
            );
        }
    });
}

function renderRanking(ranking) {
    const container = $('#clan-ranking-container');
    container.empty();

    if (!ranking || ranking.length === 0) {
        container.html('<p class="text-muted text-center pixel-font">No hay clanes todavía</p>');
        return;
    }

    ranking.forEach((clan, index) => {
        container.append(`
            <div class="col-md-8 mb-3">
                <div class="card bg-dark text-white border-warning clan-ranking-card"
                     onclick="irADetalleClan('${clan.nombre}')">
                    <div class="card-body d-flex align-items-center justify-content-between">

                        <div class="d-flex align-items-center gap-3">
                            <span class="text-gold fw-bold pixel-font">
                                #${index + 1}
                            </span>

                            <img src="${clan.imagen}"
                                 alt="Imagen ${clan.nombre}"
                                 class="clan-ranking-avatar"
                                 onerror="this.src='img/clan/clan_default.png'">

                            <span class="pixel-font">
                                ${clan.nombre}
                            </span>
                        </div>

                        <div class="pixel-font text-warning fw-bold">
                            ${clan.puntosTotales} pts
                        </div>

                    </div>
                </div>
            </div>
        `);
    });
}

function irADetalleClan(nombreClan) {
    window.location.href = `detail_clan.html?clan=${encodeURIComponent(nombreClan)}`;
}


