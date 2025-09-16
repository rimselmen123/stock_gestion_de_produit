import { InventoryItem, CreateInventoryItemData, PaginatedResponse, BaseFilters } from '@/lib/types';
import { apiClient, ServiceError } from './client';
import { API_CONFIG, simulateApiDelay, simulateApiError, getCurrentTimestamp, generateId } from './config';
import { mockInventoryItems, mockCategories, mockUnits } from '@/lib/mock-data';
import { filterBySearch, sortItems, paginateItems } from '@/lib/utils';

// Inventory Items Service Class
class InventoryItemsService {
  private readonly endpoint = '/inventory-items';
  private mockData: InventoryItem[] = [...mockInventoryItems];

  // Get all inventory items with filtering and pagination
  async getAll(filters: BaseFilters): Promise<PaginatedResponse<InventoryItem>> {
    if (API_CONFIG.useMockData) {
      return this.getAllMock(filters);
    }

    try {
      const queryParams = new URLSearchParams();
      if (filters.search) queryParams.append('search', filters.search);
      if (filters.page) queryParams.append('page', filters.page.toString());
      if (filters.per_page) queryParams.append('per_page', filters.per_page.toString());
      if (filters.sort_field) queryParams.append('sort_field', filters.sort_field);
      if (filters.sort_direction) queryParams.append('sort_direction', filters.sort_direction);
      if (filters.department_id) queryParams.append('department_id', filters.department_id);

      const response = await apiClient.get<PaginatedResponse<InventoryItem>>(
        `${this.endpoint}?${queryParams.toString()}`
      );

      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to fetch inventory items: ${error instanceof Error ? error.message : 'Unknown error'}`,
        500,
        'FETCH_INVENTORY_ITEMS_ERROR'
      );
    }
  }

  // Get inventory item by ID
  async getById(id: string): Promise<InventoryItem> {
    if (API_CONFIG.useMockData) {
      return this.getByIdMock(id);
    }

    try {
      const response = await apiClient.get<InventoryItem>(`${this.endpoint}/${id}`);
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to fetch inventory item: ${error instanceof Error ? error.message : 'Unknown error'}`,
        404,
        'INVENTORY_ITEM_NOT_FOUND'
      );
    }
  }

  // Create new inventory item
  async create(data: CreateInventoryItemData): Promise<InventoryItem> {
    if (API_CONFIG.useMockData) {
      return this.createMock(data);
    }

    try {
      const response = await apiClient.post<InventoryItem>(this.endpoint, data);
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to create inventory item: ${error instanceof Error ? error.message : 'Unknown error'}`,
        400,
        'CREATE_INVENTORY_ITEM_ERROR'
      );
    }
  }

  // Update existing inventory item
  async update(id: string, data: CreateInventoryItemData): Promise<InventoryItem> {
    if (API_CONFIG.useMockData) {
      return this.updateMock(id, data);
    }

    try {
      const response = await apiClient.put<InventoryItem>(`${this.endpoint}/${id}`, data);
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to update inventory item: ${error instanceof Error ? error.message : 'Unknown error'}`,
        400,
        'UPDATE_INVENTORY_ITEM_ERROR'
      );
    }
  }

  // Delete inventory item
  async delete(id: string): Promise<void> {
    if (API_CONFIG.useMockData) {
      return this.deleteMock(id);
    }

    try {
      await apiClient.delete(`${this.endpoint}/${id}`);
    } catch (error) {
      throw new ServiceError(
        `Failed to delete inventory item: ${error instanceof Error ? error.message : 'Unknown error'}`,
        400,
        'DELETE_INVENTORY_ITEM_ERROR'
      );
    }
  }

  // Mock implementation methods
  private async getAllMock(filters: BaseFilters): Promise<PaginatedResponse<InventoryItem>> {
    await simulateApiDelay();
    simulateApiError();

    let filteredItems = [...this.mockData];

    // Apply department filter
    if (filters.department_id) {
      filteredItems = filteredItems.filter(item => item.department_id === filters.department_id);
    }

    // Apply search filter
    if (filters.search) {
      filteredItems = filterBySearch(filteredItems, filters.search, ['name']);
    }

    // Apply sorting
    if (filters.sort_field && filters.sort_direction) {
      filteredItems = sortItems(filteredItems, filters.sort_field as keyof InventoryItem, filters.sort_direction);
    }

    // Apply pagination
    return paginateItems(filteredItems, filters.page || 1, filters.per_page || 10);
  }

  private async getByIdMock(id: string): Promise<InventoryItem> {
    await simulateApiDelay();
    simulateApiError();

    const item = this.mockData.find(i => i.id === id);
    if (!item) {
      throw new ServiceError('Inventory item not found', 404, 'INVENTORY_ITEM_NOT_FOUND');
    }
    return item;
  }

  private async createMock(data: CreateInventoryItemData): Promise<InventoryItem> {
    await simulateApiDelay();
    simulateApiError();

    // Find related entities for the response
    const category = mockCategories.find(c => c.id === data.inventory_item_category_id);
    const unit = mockUnits.find(u => u.id === data.unit_id);

    const newItem: InventoryItem = {
      id: generateId(),
      name: data.name,
      inventory_item_category_id: data.inventory_item_category_id,
      unit_id: data.unit_id,
      department_id: data.department_id,
      threshold_quantity: data.threshold_quantity,
      reorder_quantity: data.reorder_quantity,
      branch_id: data.branch_id,
      created_at: getCurrentTimestamp(),
      updated_at: getCurrentTimestamp(),
      // Include relations
      category,
      unit,
    };

    this.mockData.unshift(newItem);
    return newItem;
  }

  private async updateMock(id: string, data: CreateInventoryItemData): Promise<InventoryItem> {
    await simulateApiDelay();
    simulateApiError();

    const index = this.mockData.findIndex(i => i.id === id);
    if (index === -1) {
      throw new ServiceError('Inventory item not found', 404, 'INVENTORY_ITEM_NOT_FOUND');
    }

    // Find related entities for the response
    const category = mockCategories.find(c => c.id === data.inventory_item_category_id);
    const unit = mockUnits.find(u => u.id === data.unit_id);

    const updatedItem: InventoryItem = {
      ...this.mockData[index],
      name: data.name,
      inventory_item_category_id: data.inventory_item_category_id,
      unit_id: data.unit_id,
      department_id: data.department_id,
      threshold_quantity: data.threshold_quantity,
      reorder_quantity: data.reorder_quantity,
      updated_at: getCurrentTimestamp(),
      // Update relations
      category,
      unit,
    };

    this.mockData[index] = updatedItem;
    return updatedItem;
  }

  private async deleteMock(id: string): Promise<void> {
    await simulateApiDelay();
    simulateApiError();

    const index = this.mockData.findIndex(i => i.id === id);
    if (index === -1) {
      throw new ServiceError('Inventory item not found', 404, 'INVENTORY_ITEM_NOT_FOUND');
    }

    this.mockData.splice(index, 1);
  }

  // Reset mock data (useful for testing)
  resetMockData(): void {
    this.mockData = [...mockInventoryItems];
  }
}

// Export singleton instance
export const inventoryItemsService = new InventoryItemsService();
export { InventoryItemsService };
