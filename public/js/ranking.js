$(document).ready(function () {

    const username = localStorage.getItem("username");
    if (!username) {
        window.location.href = "login.html";
        return;
    }
    $('#username-display').text(username);

    $('#btn-logout').click(function(e) {
        e.preventDefault();
        localStorage.clear();
        window.location.href = "index.html";
    });

    loadUserCoins();
    loadRanking();

    function loadUserCoins() {
        $.ajax({
            type: 'GET',
            url: '/v1/shop/monedas/' + username,
            dataType: 'json',
            success: function (response) {
                $('#coins-display').text(response.coins);
            },
            error: function () {
                console.log("Error cargando monedas");
            }
        });
    }

    function loadRanking() {
        $.ajax({
            type: "GET",
            url: "/v1/ranking/lista",
            dataType: "json",
            success: function (data) {
                renderPlayerRanking(data);
            },
            error: function () {
                $("#player-ranking-container").html(
                    '<p class="text-danger text-center pixel-font">Error al cargar el ranking</p>'
                );
            }
        });
    }

    function renderPlayerRanking(ranking) {
        const container = $("#player-ranking-container");
        container.empty();

        if (!ranking || ranking.length === 0) {
            container.html('<p class="text-muted text-center pixel-font">No hay jugadores todavía</p>');
            return;
        }

        ranking.forEach((player, index) => {
            container.append(`
            <div class="col-md-8 mb-3">
                <div class="card bg-dark text-white border-warning clan-ranking-card">
                    <div class="card-body d-flex align-items-center justify-content-between">

                        <div class="d-flex align-items-center gap-3">
                            <span class="text-gold fw-bold pixel-font fs-4">
                                #${index + 1}
                            </span>

                            <img src="${player.imagen || 'img/avatar/default_avatar.png'}"
                                 class="player-ranking-avatar"
                                 onerror="this.src='img/avatar/default_avatar.png'">

                            <div class="pixel-font fs-5">
                                <div>${player.username}</div>
                                <div class="text-muted fs-6">${player.nombre || ""}</div>
                            </div>
                        </div>

                        <div class="pixel-font text-warning fw-bold fs-4">
                            ${player.mejorPuntuacion} pts
                        </div>

                    </div>
                </div>
            </div>
        `);
        });
    }


});
