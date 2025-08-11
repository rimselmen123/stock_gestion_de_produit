# 📚 Stock Management System - Complete API Documentation

## 🌐 Base Information

- **Base URL**: `http://localhost:8083`
- **API Version**: `v1`
- **Content-Type**: `application/json`
- **CORS**: Enabled for all origins (`*`)
- **Authentication**: None required (public API)

---

## 📋 Table of Contents

1. [Brand Management API](#1-brand-management-api)
2. [Unit Management API](#2-unit-management-api)
3. [Category Management API](#3-category-management-api)
4. [Inventory Item Management API](#4-inventory-item-management-api)
5. [Common Response Codes](#5-common-response-codes)
6. [Data Models](#6-data-models)

---

## 1. 🏷️ Brand Management API

### 1.1 Create Brand
- **URL**: `POST /api/v1/brands`
- **Description**: Creates a new brand
- **Request Body**:
```json
{
  "name": "string (required, 2-100 chars)",
  "description": "string (optional, max 500 chars)",
  "imageUrl": "string (optional, max 255 chars)"
}
```
- **Example Request**:
```json
{
  "name": "Apple",
  "description": "Technology company known for innovative products",
  "imageUrl": "https://example.com/apple-logo.png"
}
```
- **Example Response** (201 Created):
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Apple",
  "description": "Technology company known for innovative products",
  "imageUrl": "https://example.com/apple-logo.png",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```
- **Response Codes**:
  - `201`: Brand created successfully
  - `400`: Invalid input data
  - `409`: Brand with same name already exists

### 1.2 Get All Brands
- **URL**: `GET /api/v1/brands`
- **Description**: Retrieves all brands
- **Parameters**: None
- **Example Response** (200 OK):
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Apple",
    "description": "Technology company",
    "imageUrl": "https://example.com/apple-logo.png",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
]
```

### 1.3 Get Brand by ID
- **URL**: `GET /api/v1/brands/{id}`
- **Description**: Retrieves a specific brand by ID
- **Path Parameters**:
  - `id` (string, required): Brand UUID
- **Example Response** (200 OK): Same as create response
- **Response Codes**:
  - `200`: Brand found
  - `404`: Brand not found

### 1.4 Get Brands Summary
- **URL**: `GET /api/v1/brands/summary`
- **Description**: Retrieves lightweight brand list for dropdowns
- **Example Response** (200 OK):
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Apple",
    "imageUrl": "https://example.com/apple-logo.png"
  }
]
```

### 1.5 Search Brands by Name
- **URL**: `GET /api/v1/brands/search?name={name}`
- **Description**: Searches brands by partial name match
- **Query Parameters**:
  - `name` (string, required): Partial name to search
- **Example**: `GET /api/v1/brands/search?name=App`
- **Example Response** (200 OK): Array of matching brands

### 1.6 Update Brand
- **URL**: `PUT /api/v1/brands/{id}`
- **Description**: Updates an existing brand
- **Path Parameters**:
  - `id` (string, required): Brand UUID
- **Request Body**: Same as create request
- **Example Response** (200 OK): Updated brand object
- **Response Codes**:
  - `200`: Brand updated successfully
  - `400`: Invalid input data
  - `404`: Brand not found
  - `409`: Brand with same name already exists

### 1.7 Delete Brand
- **URL**: `DELETE /api/v1/brands/{id}`
- **Description**: Deletes a brand by ID
- **Path Parameters**:
  - `id` (string, required): Brand UUID
- **Response Codes**:
  - `204`: Brand deleted successfully
  - `404`: Brand not found
  - `409`: Cannot delete brand with associated inventory items

---

## 2. 📏 Unit Management API

### 2.1 Create Unit
- **URL**: `POST /api/v1/units`
- **Description**: Creates a new measurement unit
- **Request Body**:
```json
{
  "name": "string (required, 2-50 chars)",
  "symbol": "string (required, 1-10 chars, unique)"
}
```
- **Example Request**:
```json
{
  "name": "Kilogram",
  "symbol": "kg"
}
```
- **Example Response** (201 Created):
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440001",
  "name": "Kilogram",
  "symbol": "kg",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### 2.2 Get All Units
- **URL**: `GET /api/v1/units`
- **Description**: Retrieves all units ordered by name
- **Example Response** (200 OK): Array of unit objects

### 2.3 Get Unit by ID
- **URL**: `GET /api/v1/units/{id}`
- **Description**: Retrieves a specific unit by ID
- **Path Parameters**:
  - `id` (string, required): Unit UUID

### 2.4 Get Units Summary
- **URL**: `GET /api/v1/units/summary`
- **Description**: Retrieves lightweight unit list for dropdowns
- **Example Response** (200 OK):
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "name": "Kilogram",
    "symbol": "kg"
  }
]
```

### 2.5 Search Units by Symbol
- **URL**: `GET /api/v1/units/search?symbol={symbol}`
- **Description**: Searches units by partial symbol match
- **Query Parameters**:
  - `symbol` (string, required): Partial symbol to search

### 2.6 Update Unit
- **URL**: `PUT /api/v1/units/{id}`
- **Description**: Updates an existing unit
- **Request Body**: Same as create request

### 2.7 Delete Unit
- **URL**: `DELETE /api/v1/units/{id}`
- **Description**: Deletes a unit by ID

---

## 3. 📂 Category Management API

### 3.1 Create Category
- **URL**: `POST /api/v1/categories`
- **Description**: Creates a new inventory item category
- **Request Body**:
```json
{
  "name": "string (required, 2-100 chars, unique per branch)",
  "description": "string (optional, max 500 chars)",
  "branchId": "string (required)"
}
```
- **Example Request**:
```json
{
  "name": "Electronics",
  "description": "Electronic devices and components",
  "branchId": "branch-001"
}
```
- **Example Response** (201 Created):
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440002",
  "name": "Electronics",
  "description": "Electronic devices and components",
  "branchId": "branch-001",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### 3.2 Get All Categories
- **URL**: `GET /api/v1/categories`
- **Description**: Retrieves all categories

### 3.3 Get Categories by Branch
- **URL**: `GET /api/v1/categories/branch/{branchId}`
- **Description**: Retrieves all categories for a specific branch
- **Path Parameters**:
  - `branchId` (string, required): Branch identifier

### 3.4 Get Category by ID
- **URL**: `GET /api/v1/categories/{id}`
- **Description**: Retrieves a specific category by ID

### 3.5 Get Categories Summary
- **URL**: `GET /api/v1/categories/summary`
- **Description**: Retrieves lightweight category list
- **Example Response** (200 OK):
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440002",
    "name": "Electronics",
    "branchId": "branch-001"
  }
]
```

### 3.6 Search Categories by Name
- **URL**: `GET /api/v1/categories/search?name={name}&branchId={branchId}`
- **Description**: Searches categories by partial name within a branch
- **Query Parameters**:
  - `name` (string, required): Partial name to search
  - `branchId` (string, required): Branch identifier

### 3.7 Find Category by Exact Name
- **URL**: `GET /api/v1/categories/find?name={name}&branchId={branchId}`
- **Description**: Finds category by exact name within a branch

### 3.8 Get Category Count by Branch
- **URL**: `GET /api/v1/categories/count/branch/{branchId}`
- **Description**: Returns the number of categories in a branch
- **Example Response** (200 OK): `5`

### 3.9 Update Category
- **URL**: `PUT /api/v1/categories/{id}`
- **Description**: Updates an existing category
- **Request Body**:
```json
{
  "name": "string (required, 2-100 chars)",
  "description": "string (optional, max 500 chars)"
}
```
- **Note**: `branchId` cannot be changed after creation

### 3.10 Delete Category
- **URL**: `DELETE /api/v1/categories/{id}`
- **Description**: Deletes a category by ID

---

## 4. 📦 Inventory Item Management API

### 4.1 Create Inventory Item
- **URL**: `POST /api/v1/inventory-items`
- **Description**: Creates a new inventory item with relationships
- **Request Body**:
```json
{
  "name": "string (required, 2-100 chars, unique)",
  "thresholdQuantity": "integer (required, >= 0)",
  "reorderQuantity": "integer (required, >= 1, must be > thresholdQuantity)",
  "unitPurchasePrice": "decimal (required, > 0.01, max 2 decimal places)",
  "categoryId": "string (required, UUID)",
  "unitId": "string (required, UUID)",
  "brandId": "string (optional, UUID)"
}
```
- **Example Request**:
```json
{
  "name": "MacBook Pro 16-inch",
  "thresholdQuantity": 5,
  "reorderQuantity": 20,
  "unitPurchasePrice": 2499.99,
  "categoryId": "550e8400-e29b-41d4-a716-446655440002",
  "unitId": "550e8400-e29b-41d4-a716-446655440001",
  "brandId": "550e8400-e29b-41d4-a716-446655440000"
}
```
- **Example Response** (201 Created):
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440003",
  "name": "MacBook Pro 16-inch",
  "thresholdQuantity": 5,
  "reorderQuantity": 20,
  "unitPurchasePrice": 2499.99,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00",
  "category": {
    "id": "550e8400-e29b-41d4-a716-446655440002",
    "name": "Electronics",
    "branchId": "branch-001"
  },
  "unit": {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "name": "Piece",
    "symbol": "pcs"
  },
  "brand": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Apple",
    "imageUrl": "https://example.com/apple-logo.png"
  },
  "isLowStock": false
}
```
- **Response Codes**:
  - `201`: Inventory item created successfully
  - `400`: Invalid input data (validation errors, reorderQuantity <= thresholdQuantity)
  - `409`: Inventory item with same name already exists

### 4.2 Get All Inventory Items
- **URL**: `GET /api/v1/inventory-items`
- **Description**: Retrieves all inventory items ordered by name
- **Example Response** (200 OK): Array of inventory item objects

### 4.3 Get Inventory Item by ID
- **URL**: `GET /api/v1/inventory-items/{id}`
- **Description**: Retrieves a specific inventory item by ID
- **Path Parameters**:
  - `id` (string, required): Inventory item UUID
- **Response Codes**:
  - `200`: Inventory item found
  - `404`: Inventory item not found

### 4.4 Get Inventory Items Summary
- **URL**: `GET /api/v1/inventory-items/summary`
- **Description**: Retrieves lightweight inventory item list for dropdowns
- **Example Response** (200 OK):
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440003",
    "name": "MacBook Pro 16-inch",
    "unitPurchasePrice": 2499.99,
    "thresholdQuantity": 5,
    "categoryName": "Electronics",
    "unitSymbol": "pcs",
    "brandName": "Apple",
    "isLowStock": false
  }
]
```

### 4.5 Get Inventory Items by Category
- **URL**: `GET /api/v1/inventory-items/category/{categoryId}`
- **Description**: Retrieves all inventory items in a specific category
- **Path Parameters**:
  - `categoryId` (string, required): Category UUID

### 4.6 Search Inventory Items by Name
- **URL**: `GET /api/v1/inventory-items/search?name={name}`
- **Description**: Searches inventory items by partial name match
- **Query Parameters**:
  - `name` (string, required): Partial name to search
- **Example**: `GET /api/v1/inventory-items/search?name=MacBook`

### 4.7 Get Inventory Items by Price Range
- **URL**: `GET /api/v1/inventory-items/price-range?minPrice={min}&maxPrice={max}`
- **Description**: Finds inventory items within a specific price range
- **Query Parameters**:
  - `minPrice` (decimal, required): Minimum price (inclusive)
  - `maxPrice` (decimal, required): Maximum price (inclusive)
- **Example**: `GET /api/v1/inventory-items/price-range?minPrice=100.00&maxPrice=1000.00`

### 4.8 Get Low Stock Inventory Items
- **URL**: `GET /api/v1/inventory-items/low-stock?threshold={threshold}`
- **Description**: Finds inventory items at or below threshold quantity
- **Query Parameters**:
  - `threshold` (integer, optional, default=10): Threshold quantity
- **Example**: `GET /api/v1/inventory-items/low-stock?threshold=5`

### 4.9 Update Inventory Item
- **URL**: `PUT /api/v1/inventory-items/{id}`
- **Description**: Updates an existing inventory item
- **Path Parameters**:
  - `id` (string, required): Inventory item UUID
- **Request Body**: Same as create request
- **Response Codes**:
  - `200`: Inventory item updated successfully
  - `400`: Invalid input data
  - `404`: Inventory item not found
  - `409`: Inventory item with same name already exists

### 4.10 Delete Inventory Item
- **URL**: `DELETE /api/v1/inventory-items/{id}`
- **Description**: Deletes an inventory item by ID
- **Path Parameters**:
  - `id` (string, required): Inventory item UUID
- **Response Codes**:
  - `204`: Inventory item deleted successfully
  - `404`: Inventory item not found

---

## 5. 📊 Common Response Codes

| Code | Description |
|------|-------------|
| `200` | OK - Request successful |
| `201` | Created - Resource created successfully |
| `204` | No Content - Resource deleted successfully |
| `400` | Bad Request - Invalid input data or validation errors |
| `404` | Not Found - Resource not found |
| `409` | Conflict - Resource already exists or constraint violation |
| `500` | Internal Server Error - Server error |

---

## 6. 📋 Data Models

### 6.1 Brand Models

#### BrandCreateDTO / BrandUpdateDTO
```json
{
  "name": "string (required, 2-100 chars)",
  "description": "string (optional, max 500 chars)",
  "imageUrl": "string (optional, max 255 chars)"
}
```

#### BrandResponseDTO
```json
{
  "id": "string (UUID)",
  "name": "string",
  "description": "string",
  "imageUrl": "string",
  "createdAt": "datetime (ISO 8601)",
  "updatedAt": "datetime (ISO 8601)"
}
```

#### BrandSummaryDTO
```json
{
  "id": "string (UUID)",
  "name": "string",
  "imageUrl": "string"
}
```

### 6.2 Unit Models

#### UnitCreateDTO / UnitUpdateDTO
```json
{
  "name": "string (required, 2-50 chars)",
  "symbol": "string (required, 1-10 chars, unique)"
}
```

#### UnitResponseDTO
```json
{
  "id": "string (UUID)",
  "name": "string",
  "symbol": "string",
  "createdAt": "datetime (ISO 8601)",
  "updatedAt": "datetime (ISO 8601)"
}
```

#### UnitSummaryDTO
```json
{
  "id": "string (UUID)",
  "name": "string",
  "symbol": "string"
}
```

### 6.3 Category Models

#### CategoryCreateDTO
```json
{
  "name": "string (required, 2-100 chars, unique per branch)",
  "description": "string (optional, max 500 chars)",
  "branchId": "string (required)"
}
```

#### CategoryUpdateDTO
```json
{
  "name": "string (required, 2-100 chars)",
  "description": "string (optional, max 500 chars)"
}
```

#### CategoryResponseDTO
```json
{
  "id": "string (UUID)",
  "name": "string",
  "description": "string",
  "branchId": "string",
  "createdAt": "datetime (ISO 8601)",
  "updatedAt": "datetime (ISO 8601)"
}
```

#### CategorySummaryDTO
```json
{
  "id": "string (UUID)",
  "name": "string",
  "branchId": "string"
}
```

### 6.4 Inventory Item Models

#### InventoryItemCreateDTO / InventoryItemUpdateDTO
```json
{
  "name": "string (required, 2-100 chars, unique)",
  "thresholdQuantity": "integer (required, >= 0)",
  "reorderQuantity": "integer (required, >= 1, must be > thresholdQuantity)",
  "unitPurchasePrice": "decimal (required, > 0.01, max 2 decimal places)",
  "categoryId": "string (required, UUID)",
  "unitId": "string (required, UUID)",
  "brandId": "string (optional, UUID)"
}
```

#### InventoryItemResponseDTO
```json
{
  "id": "string (UUID)",
  "name": "string",
  "thresholdQuantity": "integer",
  "reorderQuantity": "integer",
  "unitPurchasePrice": "decimal",
  "createdAt": "datetime (ISO 8601)",
  "updatedAt": "datetime (ISO 8601)",
  "category": "CategorySummaryDTO",
  "unit": "UnitSummaryDTO",
  "brand": "BrandSummaryDTO (nullable)",
  "isLowStock": "boolean"
}
```

#### InventoryItemSummaryDTO
```json
{
  "id": "string (UUID)",
  "name": "string",
  "unitPurchasePrice": "decimal",
  "thresholdQuantity": "integer",
  "categoryName": "string",
  "unitSymbol": "string",
  "brandName": "string (nullable)",
  "isLowStock": "boolean"
}
```

---

## 7. 🔗 Relationships

- **InventoryItem** belongs to **Category** (Many-to-One, required)
- **InventoryItem** belongs to **Unit** (Many-to-One, required)
- **InventoryItem** belongs to **Brand** (Many-to-One, optional)
- **Category** is scoped to **Branch** (unique name per branch)

---

## 8. 💡 Usage Tips for Frontend Developers

### 8.1 Recommended API Call Sequence for Creating Inventory Items:
1. Load brands: `GET /api/v1/brands/summary`
2. Load units: `GET /api/v1/units/summary`
3. Load categories for branch: `GET /api/v1/categories/branch/{branchId}`
4. Create inventory item: `POST /api/v1/inventory-items`

### 8.2 Validation Rules to Implement in Frontend:
- **reorderQuantity** must be greater than **thresholdQuantity**
- **unitPurchasePrice** must be positive with max 2 decimal places
- **name** fields must be unique (check with search endpoints)
- **categoryId**, **unitId** must exist (validate with summary endpoints)

### 8.3 Error Handling:
- Always check response status codes
- Parse error messages from response body for validation errors
- Handle 404 errors for resource not found scenarios
- Handle 409 errors for duplicate/constraint violations

### 8.4 Performance Optimization:
- Use summary endpoints for dropdowns and listings
- Use search endpoints for filtering large datasets
- Cache reference data (brands, units, categories) when possible

---

## 9. 🧪 Testing Endpoints

All endpoints have been thoroughly tested and are production-ready. You can test them using:
- **Swagger UI**: `http://localhost:8083/swagger-ui/index.html`
- **Postman**: Import the API endpoints
- **curl**: Command line testing

---

**📝 Document Version**: 1.0
**📅 Last Updated**: January 2024
**👥 Target Audience**: Frontend Development Team
