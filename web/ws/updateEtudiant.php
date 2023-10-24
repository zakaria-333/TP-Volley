<?php

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    include_once '../racine.php';
    include_once RACINE . '/service/EtudiantService.php';
    include_once RACINE . '/classes/Etudiant.php';
    update();
}

function update() {
    extract($_POST);
    $es = new EtudiantService();
    $etudiant = $es->findById($id);
    $etudiant->setNom($nom);
    $etudiant->setSexe($sexe);
    $etudiant->setPrenom($prenom);
    $etudiant->setVille($ville);
    $es->update($etudiant);
    header('Content-type: application/json');
    echo json_encode($es->findAllApi());
}
