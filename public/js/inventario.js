$(document).ready(function () {
    console.log("Script de inventario.html iniciado.");

    // 1. AUTENTICACIÓN Y CONFIGURACIÓN INICIAL
    const username = localStorage.getItem("username");
    console.log("Usuario recuperado de localStorage:", username);

    if (!username) {
        console.log("Usuario no encontrado, redirigiendo a login.html");
        window.location.href = "login.html";
        return;
    }
    $('#username-display').text(username);

    $('#btn-logout').click(function() {
        localStorage.removeItem("username");
        window.location.href = "index.html";
    });

    // 2. CARGAR MONEDAS (PRIMERO)
    console.log("Iniciando petición AJAX para cargar monedas...");
    $.ajax({
        type: 'GET',
        url: '/v1/shop/monedas/' + username,
        dataType: 'json',
        success: function (response) {
            console.log("Petición de monedas exitosa. Respuesta:", response);
            $('#coins-display').text(response.coins);
            console.log("Monedas actualizadas en la pantalla.");

            // Una vez las monedas están cargadas, cargamos el inventario.
            loadInventory();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.error("Error en la petición AJAX de monedas:", textStatus, errorThrown);
            $('#coins-display').text('Error');
            alert("No se pudo cargar la información del usuario. Revisa la consola para más detalles.");
        }
    });

    // 3. FUNCIÓN PARA CARGAR EL INVENTARIO
    function loadInventory() {
        console.log("Iniciando petición AJAX para cargar inventario...");
        $.ajax({
            type: "GET",
            url: "/v1/shop/inventario/" + encodeURIComponent(username),
            dataType: "json",
            success: function (inventoryItems) {
                console.log("Petición de inventario exitosa. Items:", inventoryItems);

                const container = $("#inventory-container");
                container.empty();

                if (inventoryItems && inventoryItems.length > 0) {

                    inventoryItems.forEach(function (item) {
                        container.append(`
                <div class="col-md-8 mb-3">
                    <div class="card bg-dark text-white border-warning inventory-card">
                        <div class="card-body d-flex align-items-start justify-content-between">

                            <div class="d-flex align-items-center gap-3">
                                ${item.imagen ? `
                                    <img src="${item.imagen}"
                                         class="inventory-item-img">
                                ` : ''}

                                <div>
                                    <div class="pixel-font fs-5">
                                        ${item.nombre || "Sin nombre"}
                                    </div>

                                </div>
                            </div>

                            <div class="pixel-font text-warning fw-bold fs-4">
                                x${item.cantidad || 1}
                            </div>

                        </div>
                    </div>
                </div>
            `);
                    });

                } else {
                    console.log("El inventario está vacío.");
                    $("#empty-message").show();
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error("Error en la petición AJAX de inventario:", textStatus, errorThrown);
                $("#empty-message").text("Error al cargar el inventario.").show();
            }
        });
    }
});
