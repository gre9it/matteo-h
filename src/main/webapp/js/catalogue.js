function filtrer() {
    var type   = document.getElementById("filtreType").value.toLowerCase();
    var texte  = document.getElementById("filtreTexte").value.toLowerCase();
    var lignes = document.querySelectorAll("#tableRessources tbody tr");
    var count  = 0;

    lignes.forEach(function(ligne) {
        var typeOk  = !type  || ligne.dataset.type.toLowerCase() === type;
        var texteOk = !texte || ligne.dataset.search.includes(texte);
        var visible = typeOk && texteOk;
        ligne.style.display = visible ? "" : "none";
        if (visible) count++;
    });

    document.getElementById("compteur").textContent = count;
}

function resetFiltres() {
    document.getElementById("filtreType").value  = "";
    document.getElementById("filtreTexte").value = "";
    filtrer();
}