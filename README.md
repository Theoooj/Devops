# README devops

# Tests unitaires

## Introduction

### Ce projet utilise JUnit et Mockito pour réaliser des tests unitaires complets sur les services et contrôleurs de l'application. Les tests visent à garantir le bon fonctionnement de chaque composant individuellement.

## Technologies utilisées

### JUnit : Framework de tests unitaires
### Mockito : Bibliothèque de mocking pour isoler les composants
### Spring Boot Test : Support pour les tests de composants Spring

## Stratégie de Test

### Notre approche de test unitaire se concentre sur :
### Tester chaque méthode individuellement
### Simuler les dépendances externes avec Mockito
### Couvrir les cas nominaux et les cas d'erreur
### Vérifier le comportement attendu pour chaque scénario

## Structure des Tests

### Chaque fichier de test suit un modèle commun :
### Initialisation des données de test
### Configuration des mocks
### Appel de la méthode testée
### Vérification des résultats attendus

## Tests des Services

## UserServiceTest.java

### Fichier de test pour : UserService

### Tests implémentés :

### testGetAllUsers_ReturnsUserList : Teste la récupération de tous les utilisateurs stockés dans la base de données. Le test vérifie que la liste retournée contient les bons utilisateurs et qu'aucune erreur n'est levée.

### testGetUserById_ExistingUser_ReturnsUser : Vérifie que la recherche d'un utilisateur existant par son ID retourne les bonnes informations de l'utilisateur.

### testGetUserById_NonExistingUser_ReturnsEmpty : Vérifie qu'une tentative de récupération d'un utilisateur inexistant retourne un Optional.empty().

### testGetUserByEmail_ExistingUser_ReturnsUser : Teste la recherche d'un utilisateur par son email et vérifie que les données retournées correspondent à celles attendues.

### testCreateUser_NewUser_SavesUser : Teste la création d'un nouvel utilisateur, vérifie que le mot de passe est bien encodé et que l'utilisateur est bien enregistré.

### testCreateUser_ExistingEmail_ThrowsException : Vérifie que l'ajout d'un utilisateur avec une adresse email déjà enregistrée retourne une exception.

### testUpdateUser_ExistingUser_UpdatesUser : Teste la mise à jour d'un utilisateur existant en modifiant ses informations, vérifie que les nouvelles valeurs sont bien enregistrées.

### testUpdateUser_NonExistingUser_ReturnsEmpty : Vérifie que la tentative de mise à jour d'un utilisateur inexistant retourne un Optional.empty().

### testDeleteUser_ExistingUser_DeletesUser : Vérifie que la suppression d'un utilisateur existant fonctionne correctement et que l'utilisateur n'est plus accessible après l'opération.

### testDeleteUser_NonExistingUser_ReturnsFalse : Vérifie que la tentative de suppression d'un utilisateur inexistant retourne false.

## ProductServiceTest.java

### Fichier de test pour : ProductService

### Tests implémentés :

### testFindAll_ReturnsListOfProducts : Teste la récupération de tous les produits enregistrés et vérifie la correspondance des données.

### testFindById_ExistingProduct_ReturnsProduct : Vérifie que la recherche d'un produit existant par ID retourne les bonnes informations.

### testFindById_NonExistingProduct_ReturnsEmptyOptional : Teste qu'une tentative de recherche d'un produit inexistant retourne un Optional.empty().

### testSave_NewProduct_ReturnsSavedProduct : Teste l'ajout d'un nouveau produit et vérifie que toutes les informations sont correctement enregistrées.

### testUpdateProduct_ExistingProduct_UpdatesAndReturnsProduct : Teste la mise à jour d'un produit existant et vérifie que les changements sont bien appliqués.

### testUpdateProduct_NonExistingProduct_ReturnsEmptyOptional : Teste que la mise à jour d'un produit inexistant retourne un Optional.empty().

### testDelete_ExistingProduct_DeletesAndReturnsTrue : Vérifie que la suppression d'un produit existant est bien effectuée.

### testDelete_NonExistingProduct_ReturnsFalse : Vérifie que la tentative de suppression d'un produit inexistant retourne false.

## BasketServiceTest.java

### Fichier de test pour : BasketService

### Tests implémentés :

### testFindAll_ReturnsBasketList : Teste la récupération de tous les paniers disponibles.

### testFindById_ExistingBasket_ReturnsBasket : Vérifie la récupération correcte d'un panier existant.

### testFindById_NonExistingBasket_ReturnsEmpty : Vérifie que la tentative de récupération d'un panier inexistant retourne un Optional.empty().

### testSave_ValidBasket_ReturnsSavedBasket : Teste l'ajout d'un panier et vérifie que toutes les données sont bien sauvegardées.

### testUpdateBasket_ExistingBasket_UpdatesCorrectly : Teste la mise à jour d'un panier existant et vérifie que les changements sont appliqués.

### testDelete_ExistingBasket_ReturnsTrue : Vérifie que la suppression d'un panier existant fonctionne correctement.

### testDelete_NonExistingBasket_ReturnsFalse : Vérifie que la tentative de suppression d'un panier inexistant retourne false.

## Tests des Contrôleurs

## UserControllerTest.java

### Fichier de test pour : UserController

### Tests implémentés :

### testGetAllUsers_ReturnsUserList : Vérifie que l'API /users retourne une liste d'utilisateurs avec le bon format JSON.

### testGetUserById_ExistingUser_ReturnsUser : Vérifie que la requête GET /users/{id} renvoie les informations correctes d'un utilisateur existant.

### testGetUserById_NonExistingUser_ReturnsNotFound : Vérifie que la requête GET /users/{id} retourne 404 Not Found si l'utilisateur n'existe pas.

### testCreateUser_ValidUser_ReturnsCreatedUser : Vérifie que la requête POST /users avec un utilisateur valide retourne 200 OK et l'utilisateur créé.

### testCreateUser_InvalidUser_ReturnsBadRequest : Vérifie que la requête POST /users avec des données invalides retourne 400 Bad Request.

### testDeleteUser_ExistingUser_ReturnsNoContent : Vérifie que DELETE /users/{id} supprime un utilisateur existant et retourne 204 No Content.

## ProductControllerTest.java

### Fichier de test pour : ProductController

### Tests implémentés :

### testGetAllProducts_ReturnsProductList : Vérifie que l'API /products retourne la liste des produits au format JSON.

### testGetProductById_ExistingProduct_ReturnsProduct : Vérifie que GET /products/{id} renvoie les informations correctes d'un produit existant.

### testGetProductById_NonExistingProduct_ReturnsNotFound : Vérifie que GET /products/{id} retourne 404 Not Found pour un produit inexistant.

### testCreateProduct_ValidProduct_ReturnsCreatedProduct : Vérifie que POST /products crée un produit valide et retourne 200 OK.

### testCreateProduct_InvalidData_ReturnsBadRequest : Vérifie que POST /products avec des données invalides retourne 400 Bad Request.

### testDeleteProduct_ExistingProduct_ReturnsNoContent : Vérifie que DELETE /products/{id} supprime un produit et retourne 204 No Content.

## BasketControllerTest.java

### Fichier de test pour : BasketController

### Tests implémentés :

### testGetAllBaskets_ReturnsBasketList : Vérifie que GET /baskets retourne la liste des paniers.

### testGetBasketById_ExistingBasket_ReturnsBasket : Vérifie que GET /baskets/{id} retourne les détails d'un panier existant.

### testGetBasketById_NonExistingBasket_ReturnsNotFound : Vérifie que GET /baskets/{id} retourne 404 Not Found si le panier n'existe pas.

### testCreateBasket_ValidBasket_ReturnsCreatedBasket : Vérifie que POST /baskets crée un panier valide et retourne 200 OK.

### testDeleteBasket_ExistingBasket_ReturnsNoContent : Vérifie que DELETE /baskets/{id} supprime un panier existant et retourne 204 No Content.