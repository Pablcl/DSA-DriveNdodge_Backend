$(document).ready(function() {
    let username = localStorage.getItem("username");
    if (!username) { window.location.href = "index.html"; return; }
    $('#username-display').text(username);
    loadUserCoins();
    loadShopItems();

    $('#btn-logout').click(function(e) { e.preventDefault(); localStorage.clear(); window.location.href = "index.html"; });

    function loadUserCoins() {
        $.ajax({ type: 'GET', url: '/v1/shop/monedas/' + username, dataType: 'json',
            success: function(response) { $('#coins-display').text(response.coins); },
            error: function() { showToast('warning', 'Error cargando monedas.'); }
        });
    }

    function loadShopItems() {
        $.ajax({
            type: 'GET', url: '/v1/shop/items', dataType: 'json',
            success: function(items) {
                let container = $('#shop-items-container'); container.empty();
                if (!items || items.length === 0) { container.html('<p class="text-center">No hay items disponibles.</p>'); return; }
                items.forEach(item => {
                    container.append(`
                        <div class="col-md-4 mb-4">
                            <div class="card h-100 card-shop border-warning bg-dark text-light">
                                <img src="${item.imagen}" class="card-img-top" alt="${item.nombre}">
                                <div class="card-body text-center">
                                    <h5 class="card-title text-gold">${item.nombre}</h5>
                                    <p class="card-text">${item.descripcion}</p>
                                    <p class="text-warning fw-bold">💰 ${item.precio}</p>
                                    <button class="btn btn-buy" data-item='${JSON.stringify(item)}'>Comprar</button>
                                </div>
                            </div>
                        </div>
                    `);
                });
                $('.btn-buy').click(function() {
                    const item = $(this).data('item');
                    $('#confirm-message').text(`¿Deseas comprar ${item.nombre} por ${item.precio} monedas?`);
                    const modal = new bootstrap.Modal(document.getElementById('confirmModal'));
                    modal.show();
                    $('#confirm-yes').off('click').on('click', function() { buyItem(item, modal); });
                });
            },
            error: function() { showToast('warning','Error cargando la tienda.'); }
        });
    }

    function buyItem(item, modal) {
        $.ajax({
            type: 'POST', url: '/v1/shop/buy', contentType: 'application/json',
            data: JSON.stringify({ username, itemId: item.id }),
            success: function(resp) { modal.hide(); showToast('success', `¡Has comprado ${item.nombre}!`); loadUserCoins(); },
            error: function(xhr) { modal.hide(); showToast('warning', xhr.responseText || 'Error al comprar el item.'); }
        });
    }

    function showToast(type, message) {
        const toastId = 'toast-' + Date.now();
        const toastHtml = `<div id="${toastId}" class="toast align-items-center text-bg-${type} border-0" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body">${message}</div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>`;
        $('#toast-container').append(toastHtml);
        const toastEl = document.getElementById(toastId);
        const toast = new bootstrap.Toast(toastEl, { delay: 3000 });
        toast.show();
        toastEl.addEventListener('hidden.bs.toast', function () { $(this).remove(); });
    }
});
