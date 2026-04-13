/**
 * catalogue.js
 * Filtrage dynamique du catalogue (sans rechargement de page).
 * - Filtre par type de ressource (select)
 * - Filtre par texte (titre, auteur, description)
 */
(function () {
    'use strict';

    var searchInput  = document.getElementById('searchInput');
    var typeFilter   = document.getElementById('typeFilter');
    var resetButton  = document.getElementById('resetFilters');
    var rows         = document.querySelectorAll('#catalogueBody .catalogue-row');
    var noResults    = document.getElementById('noResults');

    function normalize(str) {
        if (!str) return '';
        return str.toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '');
    }

    function applyFilters() {
        var search    = normalize(searchInput.value.trim());
        var typeValue = typeFilter.value;
        var visible   = 0;

        rows.forEach(function (row) {
            var type        = row.getAttribute('data-type') || '';
            var title       = normalize(row.getAttribute('data-title'));
            var auteur      = normalize(row.getAttribute('data-auteur'));
            var description = normalize(row.getAttribute('data-description'));

            var matchType = !typeValue || type === typeValue;
            var matchText = !search
                || title.includes(search)
                || auteur.includes(search)
                || description.includes(search);

            if (matchType && matchText) {
                row.style.display = '';
                visible++;
            } else {
                row.style.display = 'none';
            }
        });

        if (noResults) {
            noResults.classList.toggle('hidden', visible > 0);
        }
    }

    if (searchInput) searchInput.addEventListener('input', applyFilters);
    if (typeFilter)  typeFilter.addEventListener('change', applyFilters);

    if (resetButton) {
        resetButton.addEventListener('click', function () {
            searchInput.value = '';
            typeFilter.value  = '';
            applyFilters();
        });
    }
}());
