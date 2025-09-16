import { InventoryMovement, InventoryMovementFilters, PaginatedResponse, CreateStockEntryData } from '@/lib/types';
import { apiClient, ServiceError } from './client';
import { API_CONFIG, simulateApiDelay, simulateApiError, getCurrentTimestamp } from './config';
import { mockInventoryMovements } from '@/lib/mock-data';
import { filterBySearch, paginateItems, sortItems } from '@/lib/utils';

class InventoryMovementsService {
  private readonly endpoint = '/inventory-movements';
  private mockData: InventoryMovement[] = [...mockInventoryMovements];

  async getAll(filters: InventoryMovementFilters): Promise<PaginatedResponse<InventoryMovement>> {
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
      if (filters.transaction_type) queryParams.append('transaction_type', filters.transaction_type);
      if (filters.category) queryParams.append('category', filters.category);
      if (filters.date_range) queryParams.append('date_range', filters.date_range);

      const response = await apiClient.get<PaginatedResponse<InventoryMovement>>(
        `${this.endpoint}?${queryParams.toString()}`
      );
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to fetch inventory movements: ${error instanceof Error ? error.message : 'Unknown error'}`,
        500,
        'FETCH_INVENTORY_MOVEMENTS_ERROR'
      );
    }
  }

  async getById(id: string): Promise<InventoryMovement> {
    if (API_CONFIG.useMockData) {
      return this.getByIdMock(id);
    }
    try {
      const response = await apiClient.get<InventoryMovement>(`${this.endpoint}/${id}`);
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to fetch inventory movement: ${error instanceof Error ? error.message : 'Unknown error'}`,
        404,
        'INVENTORY_MOVEMENT_NOT_FOUND'
      );
    }
  }

  async create(data: CreateStockEntryData): Promise<InventoryMovement> {
    if (API_CONFIG.useMockData) {
      return this.createMock(data);
    }
    try {
      const response = await apiClient.post<InventoryMovement>(this.endpoint, data);
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to create inventory movement: ${error instanceof Error ? error.message : 'Unknown error'}`,
        400,
        'CREATE_INVENTORY_MOVEMENT_ERROR'
      );
    }
  }

  async update(id: string, data: Partial<CreateStockEntryData>): Promise<InventoryMovement> {
    if (API_CONFIG.useMockData) {
      return this.updateMock(id, data);
    }
    try {
      const response = await apiClient.put<InventoryMovement>(`${this.endpoint}/${id}`, data);
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to update inventory movement: ${error instanceof Error ? error.message : 'Unknown error'}`,
        400,
        'UPDATE_INVENTORY_MOVEMENT_ERROR'
      );
    }
  }

  // Mock implementations
  private async getAllMock(filters: InventoryMovementFilters): Promise<PaginatedResponse<InventoryMovement>> {
    await simulateApiDelay();
    simulateApiError();

    let filtered = [...this.mockData];

    // Filter by branch_id
    if (filters.branch_id) {
      filtered = filtered.filter(movement => movement.branch_id === filters.branch_id);
    }

    // Filter by department_id (through inventory item)
    if (filters.department_id) {
      filtered = filtered.filter(movement => 
        movement.inventory_item?.department_id === filters.department_id
      );
    }

    if (filters.search) {
      filtered = filterBySearch(
        filtered,
        filters.search,
        ['notes']
      ).filter(m => {
        const inventoryItemName = m.inventory_item?.name?.toLowerCase() || '';
        const supplierName = m.supplier?.name?.toLowerCase() || '';
        const q = filters.search!.toLowerCase();
        return inventoryItemName.includes(q) || supplierName.includes(q) || true;
      });
    }

    if (filters.transaction_type) {
      filtered = filtered.filter(m => m.transaction_type === filters.transaction_type);
    }

    if (filters.category) {
      filtered = filtered.filter(m => m.inventory_item?.inventory_item_category_id === filters.category);
    }

    if (filters.date_range) {
      const today = new Date();
      const filterDate = new Date(today);
      switch (filters.date_range) {
        case 'today':
          filterDate.setHours(0, 0, 0, 0);
          break;
        case 'week':
          filterDate.setDate(today.getDate() - 7);
          break;
        case 'month':
          filterDate.setMonth(today.getMonth() - 1);
          break;
        default:
          filterDate.setFullYear(1970);
      }
      filtered = filtered.filter(m => new Date(m.created_at) >= filterDate);
    }

    if (filters.sort_field && filters.sort_direction) {
      filtered = sortItems(filtered, filters.sort_field as keyof InventoryMovement, filters.sort_direction);
    }

    return paginateItems(filtered, filters.page || 1, filters.per_page || 10);
  }

  private async getByIdMock(id: string): Promise<InventoryMovement> {
    await simulateApiDelay();
    simulateApiError();
    const movement = this.mockData.find(m => m.id === id);
    if (!movement) {
      throw new ServiceError('Inventory movement not found', 404, 'INVENTORY_MOVEMENT_NOT_FOUND');
    }
    return movement;
  }

  private async createMock(data: CreateStockEntryData): Promise<InventoryMovement> {
    await simulateApiDelay();
    simulateApiError();
    const newMovement: InventoryMovement = {
      id: `${Date.now()}`,
      inventory_item_id: data.inventory_item_id,
      branch_id: data.branch_id,
      transaction_type: data.transaction_type,
      quantity: data.quantity,
      unit_purchase_price: data.unit_purchase_price,
      supplier_id: data.supplier_id,
      destination_branch_id: data.destination_branch_id,
      waste_reason: data.waste_reason,
      notes: data.notes,
      expiration_date: data.expiration_date,
      created_at: getCurrentTimestamp(),
      updated_at: getCurrentTimestamp(),
    };
    this.mockData.unshift(newMovement);
    return newMovement;
  }

  private async updateMock(id: string, data: Partial<CreateStockEntryData>): Promise<InventoryMovement> {
    await simulateApiDelay();
    simulateApiError();
    const index = this.mockData.findIndex(m => m.id === id);
    if (index === -1) {
      throw new ServiceError('Inventory movement not found', 404, 'INVENTORY_MOVEMENT_NOT_FOUND');
    }
    const updated: InventoryMovement = {
      ...this.mockData[index],
      quantity: data.quantity ?? this.mockData[index].quantity,
      unit_purchase_price: data.unit_purchase_price ?? this.mockData[index].unit_purchase_price,
      supplier_id: data.supplier_id ?? this.mockData[index].supplier_id,
      destination_branch_id: data.destination_branch_id ?? this.mockData[index].destination_branch_id,
      waste_reason: data.waste_reason ?? this.mockData[index].waste_reason,
      notes: data.notes ?? this.mockData[index].notes,
      expiration_date: data.expiration_date ?? this.mockData[index].expiration_date,
      updated_at: getCurrentTimestamp(),
    };
    this.mockData[index] = updated;
    return updated;
  }

  resetMockData(): void {
    this.mockData = [...mockInventoryMovements];
  }
}

export const inventoryMovementsService = new InventoryMovementsService();
export { InventoryMovementsService };


