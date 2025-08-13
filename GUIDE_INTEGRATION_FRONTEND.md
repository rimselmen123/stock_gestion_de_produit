# Guide d'Intégration Frontend - Système de Gestion de Stock

## 📋 Table des Matières
1. [Configuration Initiale](#configuration-initiale)
2. [Structure des API](#structure-des-api)
3. [Exemples d'Appels API](#exemples-dappels-api)
4. [Gestion des Erreurs](#gestion-des-erreurs)
5. [Bonnes Pratiques Next.js](#bonnes-pratiques-nextjs)
6. [Types TypeScript](#types-typescript)
7. [Hooks Personnalisés](#hooks-personnalisés)

## 🚀 Configuration Initiale

### Installation des Dépendances
```bash
npm install axios react-hook-form @hookform/resolvers zod zustand
npm install -D @types/node
```

### Configuration Axios
```typescript
// lib/api.ts
import axios from 'axios';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8083/api/v1';

export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
});

// Intercepteur pour la gestion des erreurs
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('API Error:', error.response?.data || error.message);
    return Promise.reject(error);
  }
);
```

## 🔗 Structure des API

### Endpoints Disponibles

#### Units API
- `GET /api/v1/units` - Liste toutes les unités
- `GET /api/v1/units/summary` - Résumé des unités (pour dropdowns)
- `GET /api/v1/units/{id}` - Détails d'une unité
- `POST /api/v1/units` - Créer une unité
- `PUT /api/v1/units/{id}` - Modifier une unité
- `DELETE /api/v1/units/{id}` - Supprimer une unité
- `GET /api/v1/units/search?symbol={symbol}` - Rechercher par symbole

#### Categories API
- `GET /api/v1/categories` - Liste toutes les catégories
- `GET /api/v1/categories/summary` - Résumé des catégories
- `GET /api/v1/categories/branch/{branchId}` - Catégories par branche
- `GET /api/v1/categories/{id}` - Détails d'une catégorie
- `POST /api/v1/categories` - Créer une catégorie
- `PUT /api/v1/categories/{id}` - Modifier une catégorie
- `DELETE /api/v1/categories/{id}` - Supprimer une catégorie

#### Inventory Items API
- `GET /api/v1/inventory-items` - Liste tous les articles
- `GET /api/v1/inventory-items/summary` - Résumé des articles
- `GET /api/v1/inventory-items/{id}` - Détails d'un article
- `GET /api/v1/inventory-items/category/{categoryId}` - Articles par catégorie
- `POST /api/v1/inventory-items` - Créer un article
- `PUT /api/v1/inventory-items/{id}` - Modifier un article
- `DELETE /api/v1/inventory-items/{id}` - Supprimer un article
- `GET /api/v1/inventory-items/search?name={name}` - Rechercher par nom
- `GET /api/v1/inventory-items/low-stock?threshold={threshold}` - Articles en rupture

## 📝 Exemples d'Appels API

### 1. Récupérer la Liste des Unités
```typescript
// services/unitService.ts
import { apiClient } from '@/lib/api';
import { UnitResponseDTO, UnitSummaryDTO } from '@/types/api';

export const unitService = {
  async getAll(): Promise<UnitResponseDTO[]> {
    const response = await apiClient.get<UnitResponseDTO[]>('/units');
    return response.data;
  },

  async getSummary(): Promise<UnitSummaryDTO[]> {
    const response = await apiClient.get<UnitSummaryDTO[]>('/units/summary');
    return response.data;
  },

  async getById(id: string): Promise<UnitResponseDTO> {
    const response = await apiClient.get<UnitResponseDTO>(`/units/${id}`);
    return response.data;
  },

  async create(unit: UnitCreateDTO): Promise<UnitResponseDTO> {
    const response = await apiClient.post<UnitResponseDTO>('/units', unit);
    return response.data;
  },

  async update(id: string, unit: UnitUpdateDTO): Promise<UnitResponseDTO> {
    const response = await apiClient.put<UnitResponseDTO>(`/units/${id}`, unit);
    return response.data;
  },

  async delete(id: string): Promise<void> {
    await apiClient.delete(`/units/${id}`);
  }
};
```

### 2. Créer un Article d'Inventaire
```typescript
// services/inventoryService.ts
export const inventoryService = {
  async createItem(item: InventoryItemCreateDTO): Promise<InventoryItemResponseDTO> {
    try {
      const response = await apiClient.post<InventoryItemResponseDTO>('/inventory-items', item);
      return response.data;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || 'Erreur lors de la création');
      }
      throw error;
    }
  }
};
```

### 3. Composant React avec Gestion d'État
```typescript
// components/CreateInventoryItem.tsx
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';

const createItemSchema = z.object({
  name: z.string().min(2, 'Le nom doit contenir au moins 2 caractères'),
  thresholdQuantity: z.number().min(0, 'La quantité seuil ne peut pas être négative'),
  reorderQuantity: z.number().min(1, 'La quantité de réapprovisionnement doit être au moins 1'),
  unitPurchasePrice: z.number().min(0.01, 'Le prix doit être positif'),
  categoryId: z.string().min(1, 'Veuillez sélectionner une catégorie'),
  unitId: z.string().min(1, 'Veuillez sélectionner une unité'),
});

type CreateItemForm = z.infer<typeof createItemSchema>;

export function CreateInventoryItem() {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm<CreateItemForm>({
    resolver: zodResolver(createItemSchema),
  });

  const onSubmit = async (data: CreateItemForm) => {
    setIsLoading(true);
    setError(null);

    try {
      await inventoryService.createItem(data);
      reset();
      // Redirection ou notification de succès
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Une erreur est survenue');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
          {error}
        </div>
      )}
      
      <div>
        <label htmlFor="name">Nom de l'article</label>
        <input
          {...register('name')}
          type="text"
          className="w-full border rounded px-3 py-2"
        />
        {errors.name && <p className="text-red-500 text-sm">{errors.name.message}</p>}
      </div>

      {/* Autres champs... */}

      <button
        type="submit"
        disabled={isLoading}
        className="bg-blue-500 text-white px-4 py-2 rounded disabled:opacity-50"
      >
        {isLoading ? 'Création...' : 'Créer l\'article'}
      </button>
    </form>
  );
}
```

## ⚠️ Gestion des Erreurs

### Types d'Erreurs API
```typescript
// types/errors.ts
export interface ApiError {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
  validationErrors?: Record<string, string>;
}

export class ApiException extends Error {
  constructor(
    public status: number,
    public apiError: ApiError,
    message?: string
  ) {
    super(message || apiError.message);
    this.name = 'ApiException';
  }
}
```

### Hook de Gestion d'Erreurs
```typescript
// hooks/useErrorHandler.ts
import { useState } from 'react';
import { ApiException } from '@/types/errors';

export function useErrorHandler() {
  const [error, setError] = useState<string | null>(null);

  const handleError = (err: unknown) => {
    if (err instanceof ApiException) {
      if (err.apiError.validationErrors) {
        const validationMessages = Object.values(err.apiError.validationErrors).join(', ');
        setError(`Erreurs de validation: ${validationMessages}`);
      } else {
        setError(err.apiError.message);
      }
    } else if (err instanceof Error) {
      setError(err.message);
    } else {
      setError('Une erreur inattendue est survenue');
    }
  };

  const clearError = () => setError(null);

  return { error, handleError, clearError };
}
```

## 🎯 Bonnes Pratiques Next.js

### 1. Structure des Dossiers
```
src/
├── app/                    # App Router (Next.js 13+)
│   ├── inventory/
│   ├── units/
│   └── categories/
├── components/
│   ├── ui/                 # Composants UI réutilisables
│   ├── forms/              # Formulaires
│   └── tables/             # Tableaux
├── hooks/                  # Hooks personnalisés
├── lib/                    # Utilitaires et configuration
├── services/               # Services API
├── stores/                 # Stores Zustand
└── types/                  # Types TypeScript
```

### 2. Store Zustand pour la Gestion d'État
```typescript
// stores/inventoryStore.ts
import { create } from 'zustand';
import { InventoryItemResponseDTO } from '@/types/api';

interface InventoryStore {
  items: InventoryItemResponseDTO[];
  isLoading: boolean;
  error: string | null;
  
  fetchItems: () => Promise<void>;
  addItem: (item: InventoryItemResponseDTO) => void;
  updateItem: (id: string, item: InventoryItemResponseDTO) => void;
  removeItem: (id: string) => void;
  clearError: () => void;
}

export const useInventoryStore = create<InventoryStore>((set, get) => ({
  items: [],
  isLoading: false,
  error: null,

  fetchItems: async () => {
    set({ isLoading: true, error: null });
    try {
      const items = await inventoryService.getAll();
      set({ items, isLoading: false });
    } catch (error) {
      set({ 
        error: error instanceof Error ? error.message : 'Erreur de chargement',
        isLoading: false 
      });
    }
  },

  addItem: (item) => set((state) => ({ 
    items: [...state.items, item] 
  })),

  updateItem: (id, updatedItem) => set((state) => ({
    items: state.items.map(item => item.id === id ? updatedItem : item)
  })),

  removeItem: (id) => set((state) => ({
    items: state.items.filter(item => item.id !== id)
  })),

  clearError: () => set({ error: null }),
}));
```

## 📊 Types TypeScript

### Types API Complets
```typescript
// types/api.ts

// Unit Types
export interface UnitCreateDTO {
  name: string;
  symbol: string;
}

export interface UnitUpdateDTO {
  name: string;
  symbol: string;
}

export interface UnitResponseDTO {
  id: string;
  name: string;
  symbol: string;
  createdAt: string;
  updatedAt: string;
}

export interface UnitSummaryDTO {
  id: string;
  name: string;
  symbol: string;
}

// Category Types
export interface CategoryCreateDTO {
  name: string;
  description?: string;
  branchId: string;
}

export interface CategoryUpdateDTO {
  name: string;
  description?: string;
}

export interface CategoryResponseDTO {
  id: string;
  name: string;
  description?: string;
  branchId: string;
  createdAt: string;
  updatedAt: string;
}

export interface CategorySummaryDTO {
  id: string;
  name: string;
  branchId: string;
}

// Inventory Item Types
export interface InventoryItemCreateDTO {
  name: string;
  thresholdQuantity: number;
  reorderQuantity: number;
  unitPurchasePrice: number;
  categoryId: string;
  unitId: string;
}

export interface InventoryItemUpdateDTO {
  name: string;
  thresholdQuantity: number;
  reorderQuantity: number;
  unitPurchasePrice: number;
  categoryId: string;
  unitId: string;
}

export interface InventoryItemResponseDTO {
  id: string;
  name: string;
  thresholdQuantity: number;
  reorderQuantity: number;
  unitPurchasePrice: number;
  createdAt: string;
  updatedAt: string;
  category: CategorySummaryDTO;
  unit: UnitSummaryDTO;
  isLowStock: boolean;
}

export interface InventoryItemSummaryDTO {
  id: string;
  name: string;
  unitPurchasePrice: number;
  thresholdQuantity: number;
  categoryName: string;
  unitSymbol: string;
  isLowStock: boolean;
}
```

## 🔧 Hooks Personnalisés

### Hook pour les Opérations CRUD
```typescript
// hooks/useCrud.ts
import { useState, useCallback } from 'react';

interface UseCrudOptions<T> {
  service: {
    getAll: () => Promise<T[]>;
    getById: (id: string) => Promise<T>;
    create: (data: any) => Promise<T>;
    update: (id: string, data: any) => Promise<T>;
    delete: (id: string) => Promise<void>;
  };
}

export function useCrud<T extends { id: string }>({ service }: UseCrudOptions<T>) {
  const [items, setItems] = useState<T[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchAll = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const data = await service.getAll();
      setItems(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erreur de chargement');
    } finally {
      setIsLoading(false);
    }
  }, [service]);

  const create = useCallback(async (data: any) => {
    setIsLoading(true);
    setError(null);
    try {
      const newItem = await service.create(data);
      setItems(prev => [...prev, newItem]);
      return newItem;
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erreur de création');
      throw err;
    } finally {
      setIsLoading(false);
    }
  }, [service]);

  const update = useCallback(async (id: string, data: any) => {
    setIsLoading(true);
    setError(null);
    try {
      const updatedItem = await service.update(id, data);
      setItems(prev => prev.map(item => item.id === id ? updatedItem : item));
      return updatedItem;
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erreur de mise à jour');
      throw err;
    } finally {
      setIsLoading(false);
    }
  }, [service]);

  const remove = useCallback(async (id: string) => {
    setIsLoading(true);
    setError(null);
    try {
      await service.delete(id);
      setItems(prev => prev.filter(item => item.id !== id));
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erreur de suppression');
      throw err;
    } finally {
      setIsLoading(false);
    }
  }, [service]);

  return {
    items,
    isLoading,
    error,
    fetchAll,
    create,
    update,
    remove,
    clearError: () => setError(null),
  };
}
```

## 🚦 Exemples d'Utilisation Complète

### Page de Gestion des Articles
```typescript
// app/inventory/page.tsx
'use client';

import { useEffect } from 'react';
import { useCrud } from '@/hooks/useCrud';
import { inventoryService } from '@/services/inventoryService';
import { InventoryItemResponseDTO } from '@/types/api';

export default function InventoryPage() {
  const {
    items,
    isLoading,
    error,
    fetchAll,
    remove,
    clearError
  } = useCrud<InventoryItemResponseDTO>({
    service: inventoryService
  });

  useEffect(() => {
    fetchAll();
  }, [fetchAll]);

  const handleDelete = async (id: string) => {
    if (confirm('Êtes-vous sûr de vouloir supprimer cet article ?')) {
      try {
        await remove(id);
      } catch (error) {
        // L'erreur est déjà gérée par le hook
      }
    }
  };

  if (isLoading) return <div>Chargement...</div>;

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Gestion des Articles</h1>

      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded mb-4">
          {error}
          <button onClick={clearError} className="ml-2 underline">
            Fermer
          </button>
        </div>
      )}

      <div className="grid gap-4">
        {items.map((item) => (
          <div key={item.id} className="border rounded p-4 flex justify-between items-center">
            <div>
              <h3 className="font-semibold">{item.name}</h3>
              <p className="text-gray-600">
                Prix: {item.unitPurchasePrice}€ |
                Seuil: {item.thresholdQuantity} |
                Catégorie: {item.category.name}
              </p>
              {item.isLowStock && (
                <span className="bg-red-100 text-red-800 px-2 py-1 rounded text-sm">
                  Stock faible
                </span>
              )}
            </div>
            <div className="space-x-2">
              <button className="bg-blue-500 text-white px-3 py-1 rounded">
                Modifier
              </button>
              <button
                onClick={() => handleDelete(item.id)}
                className="bg-red-500 text-white px-3 py-1 rounded"
              >
                Supprimer
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
```

## 📚 Ressources et Documentation

### URLs Importantes
- **API Documentation**: http://localhost:8083/swagger-ui/index.html
- **Health Check**: http://localhost:8083/api/v1/health
- **Actuator**: http://localhost:8083/actuator

### Codes de Statut HTTP
- `200` - Succès
- `201` - Créé avec succès
- `204` - Supprimé avec succès
- `400` - Erreur de validation
- `404` - Ressource non trouvée
- `409` - Conflit (doublon)
- `500` - Erreur serveur

### Conseils de Performance
1. Utilisez les endpoints `/summary` pour les dropdowns
2. Implémentez la pagination pour les grandes listes
3. Mettez en cache les données de référence (unités, catégories)
4. Utilisez React Query ou SWR pour la gestion du cache
5. Implémentez le debouncing pour les recherches

---

**Note**: Cette documentation est basée sur l'API v1.0 du système de gestion de stock. Assurez-vous de vérifier la documentation Swagger pour les dernières mises à jour.
```
