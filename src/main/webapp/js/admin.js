function filtrer() {
    var type   = user.getElementById("filtreType").value.toLowerCase();
    var texte  = user.getElementById("filtreTexte").value.toLowerCase();
    var lignes = user.querySelectorAll("#tableRessources tbody tr");
    var count  = 0;

    lignes.forEach(function(ligne) {
        var typeOk  = !type  || ligne.dataset.type.toLowerCase() === type;
        var texteOk = !texte || ligne.dataset.search.includes(texte);
        var visible = typeOk && texteOk;
        ligne.style.display = visible ? "" : "none";
        if (visible) count++;
    });

    user.getElementById("compteur").textContent = count;
}

function resetFiltres() {
    user.getElementById("filtreType").value  = "";
    user.getElementById("filtreTexte").value = "";
    filtrer();
}

/*à revoir*/