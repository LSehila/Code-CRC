# Calcul et Vérification d'un Code CRC en Java

Ce projet a pour objectif d'implanter un programme Java permettant de calculer et de vérifier des codes CRC (Cyclic Redundancy Check). Il offre à l'utilisateur la possibilité de choisir le polynôme générateur à utiliser pour effectuer les opérations de calcul et de vérification, garantissant ainsi une flexibilité et une compréhension approfondie du processus de détection d'erreurs.

## Description du Projet

Le programme se décline en deux fonctionnalités principales :

1. **Calcul du Code CRC**  
   À partir d'une suite de bits saisie par l'utilisateur, le programme calcule le code CRC à adjoindre au message. Les étapes du calcul sont détaillées pour permettre une meilleure compréhension du processus.

2. **Vérification d'un Message CRC**  
   En saisissant une trame complète (message + code CRC), le programme vérifie l'intégrité du message et détermine s'il contient des erreurs. Les étapes de vérification sont également exposées afin d'expliquer le fonctionnement interne du mécanisme CRC.

Pour chaque opération, l'utilisateur doit sélectionner le polynôme générateur à utiliser, ce qui permet d'adapter le calcul et la vérification à divers scénarios.

## Fonctionnalités

- **Calcul du Code CRC**
  - Saisie d'une suite de bits.
  - Choix du polynôme générateur.
  - Calcul du code CRC et génération du message complet.
  - Affichage détaillé des étapes du calcul.

- **Vérification du Code CRC**
  - Saisie d'une trame complète incluant le code CRC.
  - Choix du polynôme générateur.
  - Vérification de l'intégrité du message.
  - Affichage détaillé des étapes de vérification.

## Prérequis

- **Java JDK** : Assurez-vous d'avoir installé le Java Development Kit pour compiler et exécuter le programme.
- **IDE** : Utilisez un environnement de développement comme Eclipse, IntelliJ IDEA ou NetBeans pour faciliter la gestion du projet.


1. **Cloner le Repository**  
   Clonez le projet sur votre machine en exécutant la commande suivante :
   ```bash
   git clone https://github.com/VotreUtilisateur/CRC-Code-Java.git
