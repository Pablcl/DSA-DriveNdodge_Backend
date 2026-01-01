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
                let html = "";
                data.forEach(function (user, index) {
                    html += `
                        <tr>
                            <td>${index + 1}</td>
                            <td>${user.username}</td>
                            <td>${user.nombre || ""}</td>
                            <td>${user.mejorPuntuacion}</td>
                        </tr>
                    `;
                });
                $("#ranking-body").html(html);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error("Error cargando el ranking:", textStatus, errorThrown);
                alert("Error al cargar el ranking");
            }
        });
    }

});
