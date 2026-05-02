'use strict';

document.addEventListener('DOMContentLoaded', function () {

  /* Auto-cerrar alertas de éxito e info a los 5 s */
  document.querySelectorAll('.alert-success, .alert-info').forEach(function (el) {
    setTimeout(function () {
      var a = bootstrap.Alert.getOrCreateInstance(el);
      a.close();
    }, 5000);
  });

  /* Tooltips Bootstrap */
  document.querySelectorAll('[data-bs-toggle="tooltip"]').forEach(function (el) {
    new bootstrap.Tooltip(el, { trigger: 'hover' });
  });

  /* Resaltar nav-link activo */
  var path = window.location.pathname;
  document.querySelectorAll('.navbar-nav .nav-link').forEach(function (link) {
    try {
      if (link.href && path.startsWith(new URL(link.href, window.location.origin).pathname)) {
        link.classList.add('active', 'fw-semibold');
      }
    } catch (e) {}
  });

  /* Filtro en tiempo real sobre tablas */
  var searchInput = document.getElementById('tablaFiltro');
  if (searchInput) {
    searchInput.addEventListener('input', function () {
      var q = this.value.toLowerCase();
      document.querySelectorAll('#tablaFiltrable tbody tr').forEach(function (row) {
        row.style.display = row.textContent.toLowerCase().includes(q) ? '' : 'none';
      });
    });
  }

  /* Spinner en botones submit */
  document.querySelectorAll('form').forEach(function (form) {
    form.addEventListener('submit', function () {
      var btn = form.querySelector('button[type="submit"]');
      if (btn && !btn.dataset.noSpinner) {
        btn.disabled = true;
        var orig = btn.innerHTML;
        btn.innerHTML = '<span class="spinner-border spinner-border-sm me-1" role="status"></span>' + orig;
        setTimeout(function () { btn.disabled = false; btn.innerHTML = orig; }, 8000);
      }
    });
  });

  /* Contador de caracteres en textareas con maxlength */
  document.querySelectorAll('textarea[maxlength]').forEach(function (ta) {
    var max  = parseInt(ta.getAttribute('maxlength'));
    var hint = document.createElement('small');
    hint.className = 'text-muted d-block text-end';
    hint.textContent = '0 / ' + max;
    ta.parentNode.insertBefore(hint, ta.nextSibling);
    ta.addEventListener('input', function () {
      hint.textContent = this.value.length + ' / ' + max;
    });
  });

});

/* ── Utilidades globales ──────────────────────────────────── */
var SIGPROD = {

  /**
   * Toast en esquina superior derecha
   * @param {string} msg
   * @param {string} type  success | danger | warning | info
   */
  toast: function (msg, type) {
    type = type || 'success';
    var icons = { success:'check-circle', danger:'exclamation-triangle',
                  warning:'exclamation-circle', info:'info-circle' };
    var container = document.getElementById('sp-toast-container');
    if (!container) {
      container = document.createElement('div');
      container.id = 'sp-toast-container';
      container.style.cssText = 'position:fixed;top:1rem;right:1rem;z-index:9999;min-width:260px';
      document.body.appendChild(container);
    }
    var div = document.createElement('div');
    div.className = 'toast align-items-center text-bg-' + type + ' border-0 show mb-2';
    div.setAttribute('role', 'alert');
    div.innerHTML = '<div class="d-flex">' +
      '<div class="toast-body"><i class="bi bi-' + (icons[type]||'info-circle') + ' me-1"></i>' + msg + '</div>' +
      '<button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>' +
      '</div>';
    container.appendChild(div);
    setTimeout(function () { div.remove(); }, 4500);
  },

  /** Formatea número como moneda COP */
  formatCOP: function (n) {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency', currency: 'COP', maximumFractionDigits: 0
    }).format(n);
  }
};
