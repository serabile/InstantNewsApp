## Introduction

**InstantNews** affichant les actualités en utilisant l'API [NewsAPI](https://newsapi.org/).

## Fonctionnalités

- Afficher la liste des actualités à la une (titre + image)
- Voir le détail d'une actualité (titre, image, description, lien vers l'article complet)
- Actualités selon le pays du téléphone (France, US, etc.)
- Ouvrir l'article complet dans le navigateur
- Rafraîchir les actualités manuellement
- Gestion des erreurs avec écran de retry
- Support thème clair/sombre

---

## Architecture

### Clean Architecture + MVVM

```
UI --> Domain <-- Data
```

Utilisation du pattern MVVM 

En plus MVI est utilisé en parti pour gérer les actions utilisateur via des **Intent**

### Structure Modulaire

- `:app` : Point d'entrée et gestion de navigation
- `:domain` : logique métier (useCases, ...)
- `:data` : Accès aux données et repositories
- `:designsystem` : Composants design system et theming
- `:features:topnews` : Les screens de News list et détails avec compose


### Pourquoi ce choix ?

1. **Séparation des responsabilités** : Chaque couche a un rôle bien défini
2. **Testabilité** : Facilite les tests unitaires
3. **Maintenabilité** : Le code est organisé et facile à faire évoluer
4. **Scalabilité** : Ajouter de nouvelles fonctionnalités sans impacter l'existant
5. **Indépendance des frameworks** : Le domaine ne dépend d'aucun framework Android (pure Kotlin)

---

## Stack Technique

- **Jetpack Compose** pour un UI déclarative pour un développement rapide et dynamique
- **Retrofit** pour les appels API
- **Coil** pour l'affichage d'images
- **Hilt** pour l'injection de dépendances
- **Kotlin Coroutines** pour les opérations asynchrones
- **Timber** pour le logging
- **Spotless** pour le formatage du code
- **JUnit** + **MockK** pour les tests unitaires
- **Robolectric** pour les tests UI sur la JVM
---

## Configuration API

L'application utilise l'API NewsAPI :
- **Base URL** : `https://newsapi.org/v2/`
- **Endpoint** : `GET /top-headlines`
- **Documentation** : https://newsapi.org/docs

> **Note** : Vu que la clé API ne doit **jamais** être commitée dans le code source. Il faut utiliser `local.properties`.

### Ajouter la clé API dans `local.properties` :
   ```properties
   NEWS_API_KEY=votre_news_api_key
   ```

> **Note** : Idéalement l'utilisation d'un backend proxy est recommandé pour la securité de la clé.

---

## Améliorations futures

- Charger plus d'articles au scroll (Pagination)
- Offline-first (Room + Flows)
- Sauvegarder des articles en favoris
- Rechercher dans les actualités
- Notifications: Breaking news en push
- Firebase Analytics pour tracking
- CI/CD
- Deep linking

---

## Comment lancer le projet

1. Cloner le repository
2. Ajouter la clé API dans `local.properties` :
   ```properties
   NEWS_API_KEY=votre_news_api_key
   ```
3. Synchroniser Gradle
4. Lancer l'application

### Commandes utiles

```bash
# Vérifier le formatage
./gradlew spotlessCheck

# Appliquer le formatage automatique
./gradlew spotlessApply

```

---

## License

Evaluation technique pour Instant System

---