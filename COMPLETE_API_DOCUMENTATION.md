# 📋 Stock Management API - Complete Documentation

**Version:** 1.0  
**Base URL:** `http://localhost:8080/api`  
**Content-Type:** `application/json`

---

## 📖 Table of Contents

1. [Overview](#overview)
2. [Common Response Formats](#common-response-formats)
3. [HTTP Status Codes](#http-status-codes)
4. [Units API](#units-api)
5. [Categories API](#categories-api)
6. [Inventory Items API](#inventory-items-api)
7. [Error Handling](#error-handling)
8. [Testing Examples](#testing-examples)

---

## Overview

This API provides complete CRUD (Create, Read, Update, Delete) operations for managing inventory system entities including Units, Categories, and Inventory Items. All endpoints follow RESTful conventions with consistent request/response formats.

### Key Features
- ✅ Full CRUD operations for all entities
- ✅ Pagination and search functionality
- ✅ Sorting capabilities
- ✅ Comprehensive validation
- ✅ Proper error handling
- ✅ Foreign key relationship management

---

## Common Response Formats

### Success Response Structure
```json
{
  "data": {}, // Entity data or array of entities
  "message": "Operation successful",
  "success": true
}
```

### Paginated Response Structure
```json
{
  "data": [], // Array of entities
  "pagination": {
    "current_page": 1,
    "per_page": 5,
    "total": 50,
    "last_page": 10
  }
}
```

### Error Response Structure
```json
{
  "error": {
    "message": "Error description",
    "code": "ERROR_CODE",
    "details": {}
  },
  "success": false
}
```

---

## HTTP Status Codes

| Code | Description |
|------|-------------|
| 200 | OK - Request successful |
| 201 | Created - Resource created successfully |
| 400 | Bad Request - Invalid request data |
| 404 | Not Found - Resource not found |
| 422 | Unprocessable Entity - Validation errors |
| 500 | Internal Server Error - Server error |

---

# Units API

Units represent measurement units for inventory items (kg, pieces, liters, etc.).

## Entity Structure

```json
{
  "id": "string (UUID)",
  "name": "string",
  "symbol": "string",
  "created_at": "2024-01-15T10:30:00Z",
  "updated_at": "2024-01-15T10:30:00Z"
}
```

## CRUD Operations

### 📖 READ - Get All Units
**Endpoint:** `GET /api/units`  
**Type:** READ (List with pagination)

#### Query Parameters
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| search | string | No | - | Search in name and symbol fields |
| page | integer | No | 1 | Page number (1-based) |
| per_page | integer | No | 5 | Items per page (max 100) |
| sort_field | string | No | created_at | Field to sort by |
| sort_direction | string | No | desc | Sort direction (asc/desc) |

#### Example Request
```bash
GET /api/units?search=kg&page=1&per_page=10&sort_field=name&sort_direction=asc
```

#### Example Response (200)
```json
{
  "data": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "name": "Kilogram",
      "symbol": "kg",
      "created_at": "2024-01-15T10:30:00Z",
      "updated_at": "2024-01-15T10:30:00Z"
    }
  ],
  "pagination": {
    "current_page": 1,
    "per_page": 10,
    "total": 1,
    "last_page": 1
  }
}
```

### 📖 READ - Get Single Unit
**Endpoint:** `GET /api/units/{id}`  
**Type:** READ (Single entity)

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Unit UUID |

#### Example Request
```bash
GET /api/units/550e8400-e29b-41d4-a716-446655440000
```

#### Example Response (200)
```json
{
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Kilogram",
    "symbol": "kg",
    "created_at": "2024-01-15T10:30:00Z",
    "updated_at": "2024-01-15T10:30:00Z"
  },
  "success": true
}
```

#### Error Response (404)
```json
{
  "error": {
    "message": "Unit not found",
    "code": "UNIT_NOT_FOUND"
  },
  "success": false
}
```

### ✏️ CREATE - Create New Unit
**Endpoint:** `POST /api/units`  
**Type:** CREATE

#### Request Payload
```json
{
  "name": "string",    // Required, max 255 chars
  "symbol": "string"   // Required, max 10 chars, must be unique
}
```

#### Validation Rules
- `name`: Required, string, max 255 characters
- `symbol`: Required, string, max 10 characters, must be unique

#### Example Request
```bash
POST /api/units
Content-Type: application/json

{
  "name": "Kilogram",
  "symbol": "kg"
}
```

#### Example Response (201)
```json
{
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Kilogram",
    "symbol": "kg",
    "created_at": "2024-01-15T10:30:00Z",
    "updated_at": "2024-01-15T10:30:00Z"
  },
  "message": "Unit created successfully",
  "success": true
}
```

#### Validation Error Response (422)
```json
{
  "error": {
    "message": "Validation failed",
    "code": "VALIDATION_ERROR",
    "details": {
      "name": ["Unit name is required"],
      "symbol": ["Unit symbol must not exceed 10 characters"]
    }
  },
  "success": false
}
```

### ✏️ UPDATE - Update Existing Unit
**Endpoint:** `PUT /api/units/{id}`  
**Type:** UPDATE

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Unit UUID |

#### Request Payload
```json
{
  "name": "string",    // Required, max 255 chars
  "symbol": "string"   // Required, max 10 chars, must be unique
}
```

#### Example Request
```bash
PUT /api/units/550e8400-e29b-41d4-a716-446655440000
Content-Type: application/json

{
  "name": "Updated Kilogram",
  "symbol": "kg"
}
```

#### Example Response (200)
```json
{
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Updated Kilogram",
    "symbol": "kg",
    "created_at": "2024-01-15T10:30:00Z",
    "updated_at": "2024-01-15T11:30:00Z"
  },
  "message": "Unit updated successfully",
  "success": true
}
```

### 🗑️ DELETE - Delete Unit
**Endpoint:** `DELETE /api/units/{id}`  
**Type:** DELETE

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Unit UUID |

#### Example Request
```bash
DELETE /api/units/550e8400-e29b-41d4-a716-446655440000
```

#### Example Response (200)
```json
{
  "message": "Unit deleted successfully",
  "success": true
}
```

#### Constraint Error Response (400)
```json
{
  "error": {
    "message": "Cannot delete Unit with id 550e8400-e29b-41d4-a716-446655440000: Unit is referenced by inventory items",
    "code": "DELETE_CONSTRAINT"
  },
  "success": false
}
```

---

# Categories API

Categories represent inventory item categories (Meat & Poultry, Dairy, etc.).

## Entity Structure

```json
{
  "id": "string (UUID)",
  "name": "string",
  "branch_id": "string",
  "created_at": "2024-01-15T10:30:00Z",
  "updated_at": "2024-01-15T10:30:00Z"
}
```

## CRUD Operations

### 📖 READ - Get All Categories
**Endpoint:** `GET /api/categories`  
**Type:** READ (List with pagination)

#### Query Parameters
Same as Units API (search, page, per_page, sort_field, sort_direction)

#### Example Request
```bash
GET /api/categories?search=meat&page=1&per_page=5
```

#### Example Response (200)
```json
{
  "data": [
    {
      "id": "660e8400-e29b-41d4-a716-446655440001",
      "name": "Meat & Poultry",
      "branch_id": "default-branch",
      "created_at": "2024-01-15T10:30:00Z",
      "updated_at": "2024-01-15T10:30:00Z"
    }
  ],
  "pagination": {
    "current_page": 1,
    "per_page": 5,
    "total": 1,
    "last_page": 1
  }
}
```

### 📖 READ - Get Single Category
**Endpoint:** `GET /api/categories/{id}`  
**Type:** READ (Single entity)

#### Example Response (200)
```json
{
  "data": {
    "id": "660e8400-e29b-41d4-a716-446655440001",
    "name": "Meat & Poultry",
    "branch_id": "default-branch",
    "created_at": "2024-01-15T10:30:00Z",
    "updated_at": "2024-01-15T10:30:00Z"
  },
  "success": true
}
```

### ✏️ CREATE - Create New Category
**Endpoint:** `POST /api/categories`  
**Type:** CREATE

#### Request Payload
```json
{
  "name": "string"    // Required, max 255 chars
}
```

#### Example Request
```bash
POST /api/categories
Content-Type: application/json

{
  "name": "Meat & Poultry"
}
```

#### Example Response (201)
```json
{
  "data": {
    "id": "660e8400-e29b-41d4-a716-446655440001",
    "name": "Meat & Poultry",
    "branch_id": "default-branch",
    "created_at": "2024-01-15T10:30:00Z",
    "updated_at": "2024-01-15T10:30:00Z"
  },
  "message": "Category created successfully",
  "success": true
}
```

### ✏️ UPDATE - Update Existing Category
**Endpoint:** `PUT /api/categories/{id}`  
**Type:** UPDATE

#### Request Payload
```json
{
  "name": "string"    // Required, max 255 chars
}
```

#### Example Response (200)
```json
{
  "data": {
    "id": "660e8400-e29b-41d4-a716-446655440001",
    "name": "Updated Meat & Poultry",
    "branch_id": "default-branch",
    "created_at": "2024-01-15T10:30:00Z",
    "updated_at": "2024-01-15T11:30:00Z"
  },
  "message": "Category updated successfully",
  "success": true
}
```

### 🗑️ DELETE - Delete Category
**Endpoint:** `DELETE /api/categories/{id}`  
**Type:** DELETE

#### Example Response (200)
```json
{
  "message": "Category deleted successfully",
  "success": true
}
```

---

# Inventory Items API

Inventory items represent products/items in the inventory system.

## Entity Structure

```json
{
  "id": "string (UUID)",
  "name": "string",
  "inventory_item_category_id": "string (UUID)",
  "unit_id": "string (UUID)",
  "threshold_quantity": 10,
  "reorder_quantity": 50,
  "created_at": "2024-01-15T10:30:00Z",
  "updated_at": "2024-01-15T10:30:00Z",
  "category": {
    "id": "string (UUID)",
    "name": "string",
    "branch_id": "string",
    "created_at": "2024-01-15T10:30:00Z",
    "updated_at": "2024-01-15T10:30:00Z"
  },
  "unit": {
    "id": "string (UUID)",
    "name": "string",
    "symbol": "string",
    "created_at": "2024-01-15T10:30:00Z",
    "updated_at": "2024-01-15T10:30:00Z"
  }
}
```

## CRUD Operations

### 📖 READ - Get All Inventory Items
**Endpoint:** `GET /api/inventory-items`  
**Type:** READ (List with pagination)

#### Query Parameters
Same as Units API (search, page, per_page, sort_field, sort_direction)

#### Example Request
```bash
GET /api/inventory-items?search=chicken&page=1&per_page=5&sort_field=name&sort_direction=asc
```

#### Example Response (200)
```json
{
  "data": [
    {
      "id": "770e8400-e29b-41d4-a716-446655440002",
      "name": "Chicken Breast",
      "inventory_item_category_id": "660e8400-e29b-41d4-a716-446655440001",
      "unit_id": "550e8400-e29b-41d4-a716-446655440000",
      "threshold_quantity": 5,
      "reorder_quantity": 20,
      "created_at": "2024-01-15T10:30:00Z",
      "updated_at": "2024-01-15T10:30:00Z",
      "category": {
        "id": "660e8400-e29b-41d4-a716-446655440001",
        "name": "Meat & Poultry",
        "branch_id": "default-branch",
        "created_at": "2024-01-15T10:30:00Z",
        "updated_at": "2024-01-15T10:30:00Z"
      },
      "unit": {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "name": "Kilogram",
        "symbol": "kg",
        "created_at": "2024-01-15T10:30:00Z",
        "updated_at": "2024-01-15T10:30:00Z"
      }
    }
  ],
  "pagination": {
    "current_page": 1,
    "per_page": 5,
    "total": 1,
    "last_page": 1
  }
}
```

### 📖 READ - Get Single Inventory Item
**Endpoint:** `GET /api/inventory-items/{id}`  
**Type:** READ (Single entity with relations)

#### Example Response (200)
```json
{
  "data": {
    "id": "770e8400-e29b-41d4-a716-446655440002",
    "name": "Chicken Breast",
    "inventory_item_category_id": "660e8400-e29b-41d4-a716-446655440001",
    "unit_id": "550e8400-e29b-41d4-a716-446655440000",
    "threshold_quantity": 5,
    "reorder_quantity": 20,
    "created_at": "2024-01-15T10:30:00Z",
    "updated_at": "2024-01-15T10:30:00Z",
    "category": {
      "id": "660e8400-e29b-41d4-a716-446655440001",
      "name": "Meat & Poultry",
      "branch_id": "default-branch",
      "created_at": "2024-01-15T10:30:00Z",
      "updated_at": "2024-01-15T10:30:00Z"
    },
    "unit": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "name": "Kilogram",
      "symbol": "kg",
      "created_at": "2024-01-15T10:30:00Z",
      "updated_at": "2024-01-15T10:30:00Z"
    }
  },
  "success": true
}
```

### ✏️ CREATE - Create New Inventory Item
**Endpoint:** `POST /api/inventory-items`  
**Type:** CREATE

#### Request Payload
```json
{
  "name": "string",                        // Required, max 255 chars
  "inventory_item_category_id": "string",  // Required, valid category UUID
  "unit_id": "string",                     // Required, valid unit UUID
  "threshold_quantity": 10,                // Required, positive integer
  "reorder_quantity": 50                   // Required, positive integer
}
```

#### Validation Rules
- `name`: Required, string, max 255 characters
- `inventory_item_category_id`: Required, must exist in categories table
- `unit_id`: Required, must exist in units table
- `threshold_quantity`: Required, positive number
- `reorder_quantity`: Required, positive number

#### Example Request
```bash
POST /api/inventory-items
Content-Type: application/json

{
  "name": "Chicken Breast",
  "inventory_item_category_id": "660e8400-e29b-41d4-a716-446655440001",
  "unit_id": "550e8400-e29b-41d4-a716-446655440000",
  "threshold_quantity": 5,
  "reorder_quantity": 20
}
```

#### Example Response (201)
```json
{
  "data": {
    "id": "770e8400-e29b-41d4-a716-446655440002",
    "name": "Chicken Breast",
    "inventory_item_category_id": "660e8400-e29b-41d4-a716-446655440001",
    "unit_id": "550e8400-e29b-41d4-a716-446655440000",
    "threshold_quantity": 5,
    "reorder_quantity": 20,
    "created_at": "2024-01-15T10:30:00Z",
    "updated_at": "2024-01-15T10:30:00Z",
    "category": {
      "id": "660e8400-e29b-41d4-a716-446655440001",
      "name": "Meat & Poultry",
      "branch_id": "default-branch",
      "created_at": "2024-01-15T10:30:00Z",
      "updated_at": "2024-01-15T10:30:00Z"
    },
    "unit": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "name": "Kilogram",
      "symbol": "kg",
      "created_at": "2024-01-15T10:30:00Z",
      "updated_at": "2024-01-15T10:30:00Z"
    }
  },
  "message": "Inventory item created successfully",
  "success": true
}
```

#### Foreign Key Validation Error (422)
```json
{
  "error": {
    "message": "Validation failed",
    "code": "VALIDATION_ERROR",
    "details": {
      "inventory_item_category_id": ["Selected category does not exist"],
      "unit_id": ["Selected unit does not exist"]
    }
  },
  "success": false
}
```

### ✏️ UPDATE - Update Existing Inventory Item
**Endpoint:** `PUT /api/inventory-items/{id}`  
**Type:** UPDATE

#### Request Payload
Same as CREATE payload

#### Example Request
```bash
PUT /api/inventory-items/770e8400-e29b-41d4-a716-446655440002
Content-Type: application/json

{
  "name": "Updated Chicken Breast",
  "inventory_item_category_id": "660e8400-e29b-41d4-a716-446655440001",
  "unit_id": "550e8400-e29b-41d4-a716-446655440000",
  "threshold_quantity": 10,
  "reorder_quantity": 30
}
```

#### Example Response (200)
```json
{
  "data": {
    "id": "770e8400-e29b-41d4-a716-446655440002",
    "name": "Updated Chicken Breast",
    "inventory_item_category_id": "660e8400-e29b-41d4-a716-446655440001",
    "unit_id": "550e8400-e29b-41d4-a716-446655440000",
    "threshold_quantity": 10,
    "reorder_quantity": 30,
    "created_at": "2024-01-15T10:30:00Z",
    "updated_at": "2024-01-15T11:30:00Z",
    "category": {
      "id": "660e8400-e29b-41d4-a716-446655440001",
      "name": "Meat & Poultry",
      "branch_id": "default-branch",
      "created_at": "2024-01-15T10:30:00Z",
      "updated_at": "2024-01-15T10:30:00Z"
    },
    "unit": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "name": "Kilogram",
      "symbol": "kg",
      "created_at": "2024-01-15T10:30:00Z",
      "updated_at": "2024-01-15T10:30:00Z"
    }
  },
  "message": "Inventory item updated successfully",
  "success": true
}
```

### 🗑️ DELETE - Delete Inventory Item
**Endpoint:** `DELETE /api/inventory-items/{id}`  
**Type:** DELETE

#### Example Response (200)
```json
{
  "message": "Inventory item deleted successfully",
  "success": true
}
```

---

## Error Handling

### Common Error Codes

| Code | Description | HTTP Status |
|------|-------------|-------------|
| `VALIDATION_ERROR` | Request validation failed | 422 |
| `UNIT_NOT_FOUND` | Unit with specified ID not found | 404 |
| `CATEGORY_NOT_FOUND` | Category with specified ID not found | 404 |
| `INVENTORY_ITEM_NOT_FOUND` | Inventory item with specified ID not found | 404 |
| `FOREIGN_KEY_CONSTRAINT` | Referenced entity does not exist | 422 |
| `DELETE_CONSTRAINT` | Cannot delete entity due to references | 400 |
| `INTERNAL_SERVER_ERROR` | Unexpected server error | 500 |

### Validation Error Format
```json
{
  "error": {
    "message": "Validation failed",
    "code": "VALIDATION_ERROR",
    "details": {
      "field_name": [
        "First validation error message",
        "Second validation error message"
      ],
      "another_field": [
        "Error message for another field"
      ]
    }
  },
  "success": false
}
```

---

## Testing Examples

### Complete CRUD Flow Example

#### 1. Create a Unit
```bash
curl -X POST http://localhost:8080/api/units \
  -H "Content-Type: application/json" \
  -d '{"name": "Kilogram", "symbol": "kg"}'
```

#### 2. Create a Category
```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{"name": "Meat & Poultry"}'
```

#### 3. Create an Inventory Item
```bash
curl -X POST http://localhost:8080/api/inventory-items \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Chicken Breast",
    "inventory_item_category_id": "CATEGORY_UUID_HERE",
    "unit_id": "UNIT_UUID_HERE",
    "threshold_quantity": 5,
    "reorder_quantity": 20
  }'
```

#### 4. Get All Inventory Items
```bash
curl http://localhost:8080/api/inventory-items
```

#### 5. Update the Inventory Item
```bash
curl -X PUT http://localhost:8080/api/inventory-items/ITEM_UUID_HERE \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Premium Chicken Breast",
    "inventory_item_category_id": "CATEGORY_UUID_HERE",
    "unit_id": "UNIT_UUID_HERE",
    "threshold_quantity": 10,
    "reorder_quantity": 30
  }'
```

#### 6. Delete the Inventory Item
```bash
curl -X DELETE http://localhost:8080/api/inventory-items/ITEM_UUID_HERE
```

### Search and Pagination Examples

#### Search Units
```bash
curl "http://localhost:8080/api/units?search=kg"
```

#### Paginated Categories
```bash
curl "http://localhost:8080/api/categories?page=2&per_page=10"
```

#### Sorted Inventory Items
```bash
curl "http://localhost:8080/api/inventory-items?sort_field=name&sort_direction=asc"
```

---

## Summary

This API provides complete CRUD functionality for:

### **Units** 🏷️
- **CREATE**: Add new measurement units
- **READ**: List/search units with pagination
- **UPDATE**: Modify existing units
- **DELETE**: Remove units (with constraint checking)

### **Categories** 📂
- **CREATE**: Add new item categories
- **READ**: List/search categories with pagination
- **UPDATE**: Modify existing categories
- **DELETE**: Remove categories (with constraint checking)

### **Inventory Items** 📦
- **CREATE**: Add new inventory items with foreign key validation
- **READ**: List/search items with nested category/unit data
- **UPDATE**: Modify existing items with validation
- **DELETE**: Remove items (with constraint checking)

All operations include proper validation, error handling, and maintain referential integrity through foreign key constraints.

**API is ready for production use and frontend integration!** 🚀