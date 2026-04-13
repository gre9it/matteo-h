function afficherChamps(idLivre, idDoc) {
    var type = document.getElementById("typeSelect").value;
    document.getElementById(idLivre).style.display = type === "livre"    ? "block" : "none";
    document.getElementById(idDoc).style.display   = type === "document" ? "block" : "none";
}

function filtrerRessources() {
    var texte    = document.getElementById("filtreRessource").value.toLowerCase().trim();
    var type     = document.getElementById("filtreTypeRessource").value;
    var lignes   = document.querySelectorAll("#tableRessources tbody tr");
    var count    = 0;

    lignes.forEach(function(ligne) {
        var search    = (ligne.dataset.search || "").toLowerCase().trim();
        var ligneType = (ligne.dataset.type   || "").trim();
        var texteOk   = !texte || search.includes(texte);
        var typeOk    = !type  || ligneType === type;
        var visible   = texteOk && typeOk;
        ligne.style.display = visible ? "" : "none";
        if (visible) count++;
    });

    document.getElementById("compteurRessources").textContent = count;
}

function resetFiltresRessources() {
    document.getElementById("filtreRessource").value     = "";
    document.getElementById("filtreTypeRessource").value = "";
    filtrerRessources();
}