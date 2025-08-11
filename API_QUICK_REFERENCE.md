# 🚀 Stock Management API - Quick Reference

## 📍 Base URL: `http://localhost:8083`

---

## 🏷️ BRANDS (`/api/v1/brands`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/brands` | Create brand |
| `GET` | `/api/v1/brands` | Get all brands |
| `GET` | `/api/v1/brands/{id}` | Get brand by ID |
| `GET` | `/api/v1/brands/summary` | Get brands summary |
| `GET` | `/api/v1/brands/search?name={name}` | Search brands |
| `PUT` | `/api/v1/brands/{id}` | Update brand |
| `DELETE` | `/api/v1/brands/{id}` | Delete brand |

---

## 📏 UNITS (`/api/v1/units`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/units` | Create unit |
| `GET` | `/api/v1/units` | Get all units |
| `GET` | `/api/v1/units/{id}` | Get unit by ID |
| `GET` | `/api/v1/units/summary` | Get units summary |
| `GET` | `/api/v1/units/search?symbol={symbol}` | Search by symbol |
| `PUT` | `/api/v1/units/{id}` | Update unit |
| `DELETE` | `/api/v1/units/{id}` | Delete unit |

---

## 📂 CATEGORIES (`/api/v1/categories`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/categories` | Create category |
| `GET` | `/api/v1/categories` | Get all categories |
| `GET` | `/api/v1/categories/{id}` | Get category by ID |
| `GET` | `/api/v1/categories/branch/{branchId}` | Get by branch |
| `GET` | `/api/v1/categories/summary` | Get categories summary |
| `GET` | `/api/v1/categories/search?name={name}&branchId={branchId}` | Search categories |
| `GET` | `/api/v1/categories/find?name={name}&branchId={branchId}` | Find by exact name |
| `GET` | `/api/v1/categories/count/branch/{branchId}` | Count by branch |
| `PUT` | `/api/v1/categories/{id}` | Update category |
| `DELETE` | `/api/v1/categories/{id}` | Delete category |

---

## 📦 INVENTORY ITEMS (`/api/v1/inventory-items`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/inventory-items` | Create inventory item |
| `GET` | `/api/v1/inventory-items` | Get all items |
| `GET` | `/api/v1/inventory-items/{id}` | Get item by ID |
| `GET` | `/api/v1/inventory-items/summary` | Get items summary |
| `GET` | `/api/v1/inventory-items/category/{categoryId}` | Get by category |
| `GET` | `/api/v1/inventory-items/search?name={name}` | Search by name |
| `GET` | `/api/v1/inventory-items/price-range?minPrice={min}&maxPrice={max}` | Filter by price |
| `GET` | `/api/v1/inventory-items/low-stock?threshold={threshold}` | Get low stock items |
| `PUT` | `/api/v1/inventory-items/{id}` | Update item |
| `DELETE` | `/api/v1/inventory-items/{id}` | Delete item |

---

## 📋 Quick Create Examples

### Create Brand
```json
POST /api/v1/brands
{
  "name": "Apple",
  "description": "Technology company",
  "imageUrl": "https://example.com/logo.png"
}
```

### Create Unit
```json
POST /api/v1/units
{
  "name": "Kilogram",
  "symbol": "kg"
}
```

### Create Category
```json
POST /api/v1/categories
{
  "name": "Electronics",
  "description": "Electronic devices",
  "branchId": "branch-001"
}
```

### Create Inventory Item
```json
POST /api/v1/inventory-items
{
  "name": "MacBook Pro",
  "thresholdQuantity": 5,
  "reorderQuantity": 20,
  "unitPurchasePrice": 2499.99,
  "categoryId": "category-uuid",
  "unitId": "unit-uuid",
  "brandId": "brand-uuid"
}
```

---

## ⚠️ Important Validation Rules

1. **reorderQuantity** > **thresholdQuantity** (required)
2. **unitPurchasePrice** > 0.01 (required)
3. **Brand names** must be unique globally
4. **Unit symbols** must be unique globally
5. **Category names** must be unique per branch
6. **Inventory item names** must be unique globally

---

## 🔗 Recommended API Call Flow

### For Creating Inventory Items:
1. `GET /api/v1/brands/summary` → Load brands dropdown
2. `GET /api/v1/units/summary` → Load units dropdown  
3. `GET /api/v1/categories/branch/{branchId}` → Load categories for branch
4. `POST /api/v1/inventory-items` → Create item

### For Listings/Tables:
- Use `/summary` endpoints for better performance
- Use search endpoints for filtering
- Cache reference data when possible

---

## 📊 Response Status Codes

| Code | Meaning |
|------|---------|
| `200` | Success |
| `201` | Created |
| `204` | Deleted |
| `400` | Bad Request / Validation Error |
| `404` | Not Found |
| `409` | Conflict / Duplicate |

---

## 🧪 Testing

- **Swagger UI**: `http://localhost:8083/swagger-ui/index.html`
- **All endpoints tested and working** ✅
- **CORS enabled** for frontend integration ✅

---

**Total Endpoints**: 32 REST APIs  
**All entities**: Brand, Unit, Category, InventoryItem  
**Status**: Production Ready 🚀
