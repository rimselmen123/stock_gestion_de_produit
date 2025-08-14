# Stock API – Short Reference

- Base URL: http://localhost:8083
- All payloads: application/json
- Validation: Bean Validation (errors via global handler)

## Common Wrappers
- Headers: `Content-Type: application/json`
- Paginated: `PaginatedResponse<T> = { data: T[], pagination: { current_page, per_page, total, last_page } }`
- Single (wrapped): `ApiResponse<T> = { data, message?, success: true }`

Example (paginated):
```json
{
  "data": [ { "id": "...", "name": "kg", "symbol": "kg" } ],
  "pagination": { "current_page": 1, "per_page": 5, "total": 12, "last_page": 3 }
}
```

---

## Units
Base path: `/api/units`

- GET `/api/units`
  - Query: `search?`, `name?`, `symbol?`, `created_from?`, `created_to?`, `updated_from?`, `updated_to?`, `page=1`, `per_page=5`, `sort_field=created_at`, `sort_direction=desc`
  - 200 → `PaginatedResponse<UnitResponseDTO>`
- GET `/api/units/{id}`
  - 200 → `ApiResponse<UnitResponseDTO>`
- POST `/api/units`
  - Body UnitCreateDTO
  - 201 → `ApiResponse<UnitResponseDTO>`
- PUT `/api/units/{id}`
  - Body UnitUpdateDTO
  - 200 → `ApiResponse<UnitResponseDTO>`
- DELETE `/api/units/{id}`
  - 200 → `ApiResponse<void>`

DTOs
- UnitCreateDTO / UnitUpdateDTO
  - `{ name: string(<=255, required), symbol: string(<=10, required) }`
- UnitResponseDTO
  - `{ id: string, name: string, symbol: string, created_at: string, updated_at: string }`

---

## Categories
Base path: `/api/categories`

- GET `/api/categories`
  - Query: `search?`, `name?`, `branch_id?`, `created_from?`, `created_to?`, `updated_from?`, `updated_to?`, `page=1`, `per_page=5`, `sort_field=created_at`, `sort_direction=desc`
  - 200 → `PaginatedResponse<CategoryResponseDTO>`
- GET `/api/categories/{id}`
  - 200 → `ApiResponse<CategoryResponseDTO>`
- POST `/api/categories`
  - Body CategoryCreateDTO
  - 201 → `ApiResponse<CategoryResponseDTO>`
- PUT `/api/categories/{id}`
  - Body CategoryUpdateDTO
  - 200 → `ApiResponse<CategoryResponseDTO>`
- DELETE `/api/categories/{id}`
  - 200 → `ApiResponse<void>`

DTOs
- CategoryCreateDTO / CategoryUpdateDTO
  - `{ name: string(<=255, required) }`
- CategoryResponseDTO
  - `{ id: string, name: string, branch_id: string, created_at: string, updated_at: string }`

---

## Inventory Items
Base path: `/api/inventory-items`

- GET `/api/inventory-items`
  - Query: `search?`, `name?`, `category_id?`, `unit_id?`, `min_threshold?`, `max_threshold?`, `min_reorder?`, `max_reorder?`, `created_from?`, `created_to?`, `updated_from?`, `updated_to?`, `page=1`, `per_page=5`, `sort_field=created_at`, `sort_direction=desc`
  - 200 → `PaginatedResponse<InventoryItemResponseDTO>`
- GET `/api/inventory-items/{id}`
  - 200 → `InventoryItemResponseDTO` (note: not wrapped)
- POST `/api/inventory-items`
  - Body InventoryItemCreateDTO
  - 201 → `InventoryItemResponseDTO`
- PUT `/api/inventory-items/{id}`
  - Body InventoryItemUpdateDTO
  - 200 → `InventoryItemResponseDTO`
- DELETE `/api/inventory-items/{id}`
  - 200 → empty body

DTOs
- InventoryItemCreateDTO / InventoryItemUpdateDTO
  - `{
      name: string(<=255, required),
      inventory_item_category_id: string(required),
      unit_id: string(required),
      threshold_quantity: number>0(required),
      reorder_quantity: number>0(required)
    }`
- InventoryItemResponseDTO
  - `{
      id: string,
      name: string,
      inventory_item_category_id: string,
      unit_id: string,
      threshold_quantity: number,
      reorder_quantity: number,
      created_at: string,
      updated_at: string,
      category: CategoryResponseDTO,
      unit: UnitResponseDTO
    }`

---

## Health
Base path: `/api/v1/health`

- GET `/api/v1/health` → `{ status, timestamp, application, profile }`
- GET `/api/v1/health/info` → app/system/db details
- GET `/api/v1/health/db` → db connectivity details (200 or 503)

---

## Notes
- Actual base paths use `/api/...` (no `/api/v1`) for business resources per controllers: `UnitController`, `CategoryController`, `InventoryItemController`.
- Health endpoints keep `/api/v1/health`.
- Errors conform to global exception handler (validation, not found, FK constraints).

---

# Detailed Endpoint Specs (for QA/testing)

This section formalizes each endpoint with method, description, payloads, common status codes, and minimal curl examples.

## Error Schema (global)
On error (4xx/5xx), the body is:
```
{
  "timestamp": "2025-08-14T22:12:33.123",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/units",
  "validationErrors": { "name": "must not be blank" }
}
```

## Units
Base: `/api/units`

- GET `/api/units`
  - Desc: List with filters + pagination
  - Query: `search?`, `name?`, `symbol?`, `created_from?`, `created_to?`, `updated_from?`, `updated_to?`, `page=1`, `per_page=5`, `sort_field=created_at`, `sort_direction=desc`
  - 200: `PaginatedResponse<UnitResponseDTO>`
  - curl:
    ```bash
    curl "http://localhost:8083/api/units?page=1&per_page=5"
    ```
- GET `/api/units/{id}`
  - 200: `ApiResponse<UnitResponseDTO>` | 404
  - curl:
    ```bash
    curl http://localhost:8083/api/units/UUID
    ```
- POST `/api/units`
  - Body UnitCreateDTO: `{ "name": "Kilogram", "symbol": "kg" }`
  - 201: `ApiResponse<UnitResponseDTO>` | 400
  - curl:
    ```bash
    curl -X POST http://localhost:8083/api/units \
      -H "Content-Type: application/json" \
      -d '{"name":"Kilogram","symbol":"kg"}'
    ```
- PUT `/api/units/{id}`
  - Body UnitUpdateDTO: `{ "name": "Gram", "symbol": "g" }`
  - 200: `ApiResponse<UnitResponseDTO>` | 400 | 404
  - curl:
    ```bash
    curl -X PUT http://localhost:8083/api/units/UUID \
      -H "Content-Type: application/json" \
      -d '{"name":"Gram","symbol":"g"}'
    ```
- DELETE `/api/units/{id}`
  - 200: `ApiResponse<void>` | 404 | 409 (constraint)
  - curl:
    ```bash
    curl -X DELETE http://localhost:8083/api/units/UUID
    ```

## Categories
Base: `/api/categories`

- GET `/api/categories`
  - Desc: List with filters + pagination
  - Query: `search?`, `name?`, `branch_id?`, `created_from?`, `created_to?`, `updated_from?`, `updated_to?`, `page=1`, `per_page=5`, `sort_field=created_at`, `sort_direction=desc`
  - 200: `PaginatedResponse<CategoryResponseDTO>`
- GET `/api/categories/{id}`
  - 200: `ApiResponse<CategoryResponseDTO>` | 404
- POST `/api/categories`
  - Body CategoryCreateDTO: `{ "name": "Beverages" }`
  - 201: `ApiResponse<CategoryResponseDTO>` | 400
- PUT `/api/categories/{id}`
  - Body CategoryUpdateDTO: `{ "name": "Snacks" }`
  - 200: `ApiResponse<CategoryResponseDTO>` | 400 | 404
- DELETE `/api/categories/{id}`
  - 200: `ApiResponse<void>` | 404 | 409

Minimal curls
```bash
curl "http://localhost:8083/api/categories?page=1&per_page=5"
curl -X POST http://localhost:8083/api/categories -H "Content-Type: application/json" -d '{"name":"Beverages"}'
```

## Inventory Items
Base: `/api/inventory-items`

- GET `/api/inventory-items`
  - Desc: List with advanced filters + pagination
  - Query: `search?`, `name?`, `category_id?`, `unit_id?`, `min_threshold?`, `max_threshold?`, `min_reorder?`, `max_reorder?`, `created_from?`, `created_to?`, `updated_from?`, `updated_to?`, `page=1`, `per_page=5`, `sort_field=created_at`, `sort_direction=desc`
  - 200: `PaginatedResponse<InventoryItemResponseDTO>`
- GET `/api/inventory-items/{id}`
  - 200: `InventoryItemResponseDTO` | 404
- POST `/api/inventory-items`
  - Body InventoryItemCreateDTO:
    ```json
    {
      "name": "Sugar",
      "inventory_item_category_id": "UUID",
      "unit_id": "UUID",
      "threshold_quantity": 10,
      "reorder_quantity": 50
    }
    ```
  - 201: `InventoryItemResponseDTO` | 400
- PUT `/api/inventory-items/{id}`
  - Body InventoryItemUpdateDTO: same fields as create
  - 200: `InventoryItemResponseDTO` | 400 | 404
- DELETE `/api/inventory-items/{id}`
  - 200: empty body | 404 | 409

Minimal curls
```bash
curl "http://localhost:8083/api/inventory-items?page=1&per_page=5"
curl -X POST http://localhost:8083/api/inventory-items -H "Content-Type: application/json" -d '{"name":"Sugar","inventory_item_category_id":"UUID","unit_id":"UUID","threshold_quantity":10,"reorder_quantity":50}'
```

## Health
Base: `/api/v1/health`

- GET `/api/v1/health`
  - 200: `{ status, timestamp, application, profile }`
- GET `/api/v1/health/info`
  - 200: application/system/db details
- GET `/api/v1/health/db`
  - 200: db details | 503: `{ status: "DOWN", error, timestamp }`

Notes
- No auth layer present in controllers; CORS enabled `@CrossOrigin(origins = "*")`.
- Keep using `/api/...` (no version) for Units/Categories/Items; only Health is `/api/v1/health`.
