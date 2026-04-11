/**
 * ressource.js
 * Affichage dynamique des champs spécifiques (Livre / Document)
 * selon le <select> choisi dans le formulaire de création/édition.
 */
(function () {
    'use strict';

    var typeSelect      = document.getElementById('typeRessource');
    var livreFields     = document.getElementById('livreFields');
    var documentFields  = document.getElementById('documentFields');

    function showFields(type) {
        if (!livreFields || !documentFields) return;

        if (type === 'Livre') {
            livreFields.classList.remove('hidden');
            documentFields.classList.add('hidden');
        } else if (type === 'Document') {
            livreFields.classList.add('hidden');
            documentFields.classList.remove('hidden');
        } else {
            livreFields.classList.add('hidden');
            documentFields.classList.add('hidden');
        }
    }

    // Édition : currentType est injecté inline dans la JSP
    if (typeof currentType !== 'undefined' && currentType !== '') {
        showFields(currentType);
    }

    if (typeSelect) {
        typeSelect.addEventListener('change', function () {
            showFields(this.value);
        });
        // Applique l'état initial si une valeur est déjà sélectionnée
        showFields(typeSelect.value);
    }
}());
