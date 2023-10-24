<?php

include_once '../racine.php';
include_once RACINE . '/service/EtudiantService.php';
include_once RACINE . '/classes/Etudiant.php';
extract($_GET);
$es = new EtudiantService();
$etudiant = $es->findById($id);
$etudiant->setNom($nom);
$etudiant->setSexe($sexe);
$etudiant->setPrenom($prenom);
$etudiant->setVille($ville);
$es->update($etudiant);
header("location:../index.php");

