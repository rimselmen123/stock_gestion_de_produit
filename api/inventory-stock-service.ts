import { InventoryStock, PaginatedResponse, BaseFilters, CreateStockEntryData } from '@/lib/types';
import { apiClient, ServiceError } from './client';
import { API_CONFIG, simulateApiDelay, simulateApiError, getCurrentTimestamp, generateId } from './config';
import { mockInventoryStock, mockInventoryItems } from '@/lib/mock-data';
import { filterBySearch, paginateItems, sortItems } from '@/lib/utils';

class InventoryStockService {
  private readonly endpoint = '/inventory-stock';
  private mockData: InventoryStock[] = [...mockInventoryStock];

  async getAll(filters: BaseFilters): Promise<PaginatedResponse<InventoryStock>> {
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
      if (filters.branch_id) queryParams.append('branch_id', filters.branch_id);
      if (filters.department_id) queryParams.append('department_id', filters.department_id);

      const response = await apiClient.get<PaginatedResponse<InventoryStock>>(
        `${this.endpoint}?${queryParams.toString()}`
      );
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to fetch inventory stock: ${error instanceof Error ? error.message : 'Unknown error'}`,
        500,
        'FETCH_INVENTORY_STOCK_ERROR'
      );
    }
  }

  async getById(id: string): Promise<InventoryStock> {
    if (API_CONFIG.useMockData) {
      return this.getByIdMock(id);
    }
    try {
      const response = await apiClient.get<InventoryStock>(`${this.endpoint}/${id}`);
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to fetch inventory stock: ${error instanceof Error ? error.message : 'Unknown error'}`,
        404,
        'INVENTORY_STOCK_NOT_FOUND'
      );
    }
  }

  async addEntry(data: CreateStockEntryData): Promise<InventoryStock> {
    if (API_CONFIG.useMockData) {
      return this.addEntryMock(data);
    }
    try {
      const response = await apiClient.post<InventoryStock>(`${this.endpoint}/entries`, data);
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to add stock entry: ${error instanceof Error ? error.message : 'Unknown error'}`,
        400,
        'ADD_STOCK_ENTRY_ERROR'
      );
    }
  }

  private async getAllMock(filters: BaseFilters): Promise<PaginatedResponse<InventoryStock>> {
    await simulateApiDelay();
    simulateApiError();
    let filtered = [...this.mockData];

    // Filter by branch_id
    if (filters.branch_id) {
      filtered = filtered.filter(stock => stock.branch_id === filters.branch_id);
    }

    // Filter by department_id (through inventory item)
    if (filters.department_id) {
      filtered = filtered.filter(stock => 
        stock.inventory_item?.department_id === filters.department_id
      );
    }

    if (filters.search) {
      filtered = filterBySearch(filtered, filters.search, []);
      const q = filters.search.toLowerCase();
      filtered = filtered.filter(s => s.inventory_item?.name?.toLowerCase().includes(q));
    }

    if (filters.sort_field && filters.sort_direction) {
      filtered = sortItems(filtered, filters.sort_field as keyof InventoryStock, filters.sort_direction);
    }

    return paginateItems(filtered, filters.page || 1, filters.per_page || 10);
  }

  private async getByIdMock(id: string): Promise<InventoryStock> {
    await simulateApiDelay();
    simulateApiError();
    const stock = this.mockData.find(s => s.id === id);
    if (!stock) {
      throw new ServiceError('Inventory stock not found', 404, 'INVENTORY_STOCK_NOT_FOUND');
    }
    return stock;
  }

  private async addEntryMock(data: CreateStockEntryData): Promise<InventoryStock> {
    await simulateApiDelay();
    simulateApiError();

    const existingIndex = this.mockData.findIndex(s => s.inventory_item_id === data.inventory_item_id);
    const item = mockInventoryItems.find(i => i.id === data.inventory_item_id);

    if (existingIndex >= 0) {
      const current = this.mockData[existingIndex];
      let newQty = current.quantity;
      if (data.transaction_type === 'IN') newQty += data.quantity;
      if (data.transaction_type === 'OUT' || data.transaction_type === 'WASTE') newQty -= data.quantity;
      const updated: InventoryStock = {
        ...current,
        quantity: Math.max(0, newQty),
        unit_purchase_price: data.unit_purchase_price ?? current.unit_purchase_price,
        expiration_date: data.expiration_date ?? current.expiration_date,
        updated_at: getCurrentTimestamp(),
        inventory_item: item ?? current.inventory_item,
      };
      this.mockData[existingIndex] = updated;
      return updated;
    }

    const created: InventoryStock = {
      id: generateId(),
      inventory_item_id: data.inventory_item_id,
      branch_id: data.branch_id,
      quantity: data.quantity,
      unit_purchase_price: data.unit_purchase_price || 0,
      expiration_date: data.expiration_date,
      created_at: getCurrentTimestamp(),
      updated_at: getCurrentTimestamp(),
      inventory_item: item,
    };
    this.mockData.unshift(created);
    return created;
  }

  resetMockData(): void {
    this.mockData = [...mockInventoryStock];
  }
}

export const inventoryStockService = new InventoryStockService();
export { InventoryStockService };


