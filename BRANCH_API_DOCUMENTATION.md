# 🏢 Branch Management API

## 📋 **Vue d'ensemble**

L'API de gestion des branches fournit une interface REST complète pour gérer les succursales de l'entreprise avec des fonctionnalités professionnelles avancées.

### ✨ **Fonctionnalités Principales**

- ✅ **CRUD Complet** : Création, lecture, mise à jour, suppression
- ✅ **Recherche Avancée** : Filtres multiples, pagination, tri
- ✅ **Soft Delete** : Désactivation au lieu de suppression physique  
- ✅ **Codes d'Intégration** : Codes alphanumériques uniques pour les intégrations
- ✅ **Validation Métier** : Contrôles de dépendances et unicité
- ✅ **Endpoints Utilitaires** : Dropdowns, autocomplete, statistiques

## 🛠️ **Endpoints Disponibles**

### **CRUD Operations**
```http
POST   /api/branches              # Créer une nouvelle branche
GET    /api/branches/{id}         # Obtenir une branche par ID
PUT    /api/branches/{id}         # Mettre à jour une branche
DELETE /api/branches/{id}         # Supprimer une branche (hard delete)
```

### **Recherche et Filtrage**
```http
GET /api/branches                          # Liste avec filtres avancés
GET /api/branches/search/name              # Recherche par nom (autocomplete)
GET /api/branches/by-name/{name}           # Obtenir par nom exact
GET /api/branches/by-code/{code}           # Obtenir par code exact
```

### **Endpoints Utilitaires**
```http
GET /api/branches/dropdown/active          # Branches actives pour dropdown
GET /api/branches/dropdown/all             # Toutes les branches pour admin
```

### **Opérations Métier**
```http
PATCH /api/branches/{id}/soft-delete       # Désactivation (soft delete)
PATCH /api/branches/{id}/reactivate        # Réactivation
```

### **Validation**
```http
GET /api/branches/exists/name/{name}       # Vérifier si nom existe
GET /api/branches/exists/code/{code}       # Vérifier si code existe  
GET /api/branches/{id}/can-delete          # Vérifier si suppression possible
```

### **Statistiques**
```http
GET /api/branches/stats/count              # Statistiques générales
```

## 📊 **Exemples d'Utilisation**

### **1. Créer une Branche**
```json
POST /api/branches
{
  "name": "Succursale Paris",
  "description": "75001 Paris",
  "code": "PAR01",
  "isActive": true
}
```

### **2. Recherche avec Filtres**
```http
GET /api/branches?search=paris&isActive=true&page=0&perPage=10&sortField=name&sortDirection=asc
```

### **3. Réponse Type**
```json
{
  "success": true,
  "message": "Branch created successfully",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Succursale Paris",
    "description": "75001 Paris",
    "code": "PAR01",
    "isActive": true,
    "createdAt": "2025-09-10T10:30:00Z",
    "updatedAt": "2025-09-10T10:30:00Z",
    "departmentsCount": 0,
    "movementsCount": 0
  }
}
```

## 🏗️ **Architecture Technique**

### **Stack Technologique**
- **Framework** : Spring Boot 3.x
- **Base de données** : PostgreSQL avec indexes optimisés
- **Validation** : Jakarta Bean Validation
- **Mapping** : MapStruct
- **Documentation** : OpenAPI 3.0 / Swagger UI
- **Tests** : JUnit 5, MockMvc

### **Patterns Implémentés**
- **Repository Pattern** : Couche d'accès aux données
- **Service Layer** : Logique métier centralisée
- **DTO Pattern** : Séparation des couches de données
- **Builder Pattern** : Construction d'objets fluide
- **Soft Delete** : Préservation des données historiques

### **Sécurité et Performance**
- ✅ **Indexes de base de données** pour les performances
- ✅ **Validation stricte** des données d'entrée
- ✅ **Gestion des exceptions** centralisée
- ✅ **Pagination** pour les gros volumes
- ✅ **CORS** configuré pour les intégrations frontend

## 📖 **Documentation Swagger**

L'API est entièrement documentée avec Swagger UI.

**Accès local** : `http://localhost:8080/swagger-ui.html`

### **Configuration OpenAPI**
- **Version API** : 1.0.0
- **Titre** : Stock Management API
- **Description** : API professionnelle de gestion de stock
- **Serveurs** : Développement (localhost) et Production

## 🧪 **Tests**

### **Tests d'Intégration**
```bash
mvn test
```

Les tests couvrent :
- ✅ Tous les endpoints REST
- ✅ Validation des données
- ✅ Gestion des erreurs
- ✅ Logique métier
- ✅ Pagination et filtrage

## 🚀 **Déploiement**

### **Prérequis**
- Java 21+
- PostgreSQL 12+
- Maven 3.8+

### **Variables d'Environnement**
```bash
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=postgresql://localhost:5432/stock_db
DATABASE_USERNAME=stock_user
DATABASE_PASSWORD=secure_password
```

### **Commandes**
```bash
# Compilation
mvn clean compile

# Tests
mvn test  

# Package
mvn clean package

# Lancement
java -jar target/stock-0.0.1-SNAPSHOT.jar
```

## 📈 **Roadmap**

### **Prochaines Fonctionnalités**
- [ ] **Cache Redis** pour les performances
- [ ] **Events système** pour l'audit
- [ ] **Export/Import** CSV/Excel
- [ ] **API de recherche avancée** avec Elasticsearch
- [ ] **Métriques** avec Micrometer/Prometheus

---

**Créé par** : Équipe de développement Stock Management  
**Version** : 1.0.0  
**Dernière mise à jour** : 10 septembre 2025
