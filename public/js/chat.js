$(document).ready(function() {

    if (typeof window.showToast !== 'function') {
        window.showToast = function(type, message) {
            const map = { success: 'toast-success', danger: 'toast-danger', warning: 'toast-warning' };
            const cls = map[type] || 'toast-info';
            const toastId = 'toast-' + Date.now();

            const toastHtml = `
                <div id="${toastId}" class="toast align-items-center ${cls} text-white mb-2" role="alert" aria-live="assertive" aria-atomic="true">
                    <div class="d-flex"><div class="toast-body pixel-font">${message}</div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button></div>
                </div>`;

            // Añadir al contenedor (asegurando que existe)
            if ($('#toast-container').length === 0) {
                $('body').append('<div id="toast-container" class="toast-container position-fixed bottom-0 end-0 p-3" style="z-index: 2050;"></div>');
            }

            const $el = $(toastHtml).appendTo('#toast-container');
            const bsToast = new bootstrap.Toast(document.getElementById(toastId), {delay: 4000});
            $el.on('hidden.bs.toast', function () { $el.remove(); });
            bsToast.show();
        };
    }

    // ABRIR / CERRAR CHAT
    window.toggleChat = function() {
        const chat = $('#chat-window');
        if (chat.is(':visible')) {
            chat.fadeOut(200);
        } else {
            chat.fadeIn(200).css('display', 'flex');
            $('#chat-input').focus();
        }
    };

    // ENVIAR MENSAJE
    $('#btn-chat-send').click(sendMessage);
    $('#chat-input').keypress(function(e) { if (e.which == 13) sendMessage(); });

    function sendMessage() {
        var input = $('#chat-input');
        var text = input.val().trim();

        if (!text) return;

        // Pintar usuario
        addMessage(text, 'user');
        input.val('');

        // Loading temporal
        var loadingId = "temp-" + Date.now();
        addMessage("Procesando...", 'ai', loadingId);

        // AJAX al Backend
        $.ajax({
            url: '/v1/chat',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ "prompt": text }),
            timeout: 60000,
            success: function(response) {
                $('#' + loadingId).remove();

                var aiText = response.message || response.prompt || response.response || "Sistemas Online.";
                addMessage(aiText, 'ai');
            },
            error: function(xhr) {
                $('#' + loadingId).remove();
                console.error("Error Chat:", xhr);

                showToast('danger', 'Error: Chat IA no responde.');
                addMessage("Error de conexión.", 'ai');
            }
        });
    }

    // PINTAR BURBUJA
    function addMessage(text, type, id = null) {
        var container = $('#chat-messages');
        var html = `<div class="message ${type}" ${id ? `id="${id}"` : ''}>
                        <div class="bubble">${text}</div>
                    </div>`;

        container.append(html);
        container.animate({ scrollTop: container.prop("scrollHeight") }, 300);
    }
});