# Test technique - Leboncoin

## Sommaire
1. Instructions
2. Le code et son organisation
---

## Instructions
L'application doit charger une liste d'albums à partir [de ce point d'API REST](https://static.leboncoin.fr/img/shared/technical-test.json).

Les données chargées doivent être affichées à l'utilisateur et persister à fin d'être disponibles dans les conditions suivantes :
- accès _offline_;
- changement de configuration (rotation, langue, écran _splité_, etc.); 
- redémarrage de l'application ou du téléphone;

Version Android minimale à supporter : Android 4.4 _KitKat_ (SDK 19).

## Le code et son organisation

### Langage
L'application sera intégralement codée en Kotlin et se reposera sur ses _coroutines_ :
- Kotlin, car ce langage est plus clair et plus agréable à utiliser que Java 7 & 8.
- Les coroutines, car elles permettent une gestion de l'asynchrone plus maintenable et plus sûre.

### Android Studio
Un Android Studio avec version minimale `3.6.3` est nécessaire pour la compilation du projet.

### Versioning
Le code est disponible et est versionnée dans [ce dépôt Github](https://github.com/ricard-v/test-leboncoin) qui utilise GitFlow pour son organisation.

### Approche SOLID
D'une manière générale, toutes les classes respecteront, au mieux, le principe du SOLID.

### Front-End
L'application utilise le pattern MVVM à l'aide de la stack AndroidX (Jetpack - Architecture Components) :
- `Lifecycle`: pour la gestion et le respect des cycles de vie de l'app et de ses vues (Activities & Fragments);
- `ViewModel` et `LiveData`: pour une gestion propre des données en-dehors de la vue, en respect de son cycle de vie et ainsi permettre le DataBinding;
- `Navigation`: pour simplifier la navigation dans l'app avec ses Fragments;
- `Room`: pour la sauvegarde des données chargées dans une base de données locale;

### Back-End
Pour la partie communication avec le point d'API REST, l'application utilise `Retrofit 2` qui se chargera de récupérer les données proprement.

### Architecture du projet Android
Le projet est composé de trois modules:
1. le module `app` qui contient toute la partie applicative, à savoir les vues qui seront affichées (UI /UX).
2. le module `services` servant à communiquer avec le point d'API et de stocker les données chargées. Ce module reste indépendant.
3. le module `common` qui contient principalement du code dit utilitaire. Ce module reste indépendant.

Une telle modularisation facilite la maintenance du code, par la séparation des logiques, et une compilation plus rapide avec Gradle.

### Autres librairies / dépendances
L'application fera appel aux librairies suivantes pour son bon fonctionnement :
1. `Dagger 2` pour l'injection des APIs du module`service` comme dépendances dans les parties du module `app` concernées.
2. `Glide` pour le chargement et la mise en cache des images de façon asynchrone.

---
> Création: 05.05.20
> Dernière édition: 05.05.20
> Auteur: Vincent Ricard - ricard@intechinfo.fr
