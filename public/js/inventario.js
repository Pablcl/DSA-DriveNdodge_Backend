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
                if (inventoryItems && inventoryItems.length > 0) {
                    let html = "";
                    inventoryItems.forEach(function (item) {
                        html += `
                        <tr>
                            <td>${item.nombre || "Sin nombre"}</td>
                            <td>${item.precio || "0"}</td>
                            <td>${item.cantidad || "1"}</td>
                        </tr>
                    `;
                    });
                    $("#inventory-body").html(html);
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
