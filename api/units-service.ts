import { Unit, CreateUnitData, PaginatedResponse, BaseFilters } from '@/lib/types';
import { apiClient, ServiceError } from './client';
import { API_CONFIG, simulateApiDelay, simulateApiError, getCurrentTimestamp, generateId } from './config';
import { mockUnits } from '@/lib/mock-data';
import { filterBySearch, sortItems, paginateItems } from '@/lib/utils';

// Units Service Class
class UnitsService {
  private readonly endpoint = '/units';
  private mockData: Unit[] = [...mockUnits];

  // Get all units with filtering and pagination
  async getAll(filters: BaseFilters): Promise<PaginatedResponse<Unit>> {
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

      console.log('Fetching units with params:', queryParams.toString());

      const response = await apiClient.get<PaginatedResponse<Unit>>(
        `${this.endpoint}?${queryParams.toString()}`
      );

      console.log('API response:', response);

      // API client now handles both wrapped and direct responses
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to fetch units: ${error instanceof Error ? error.message : 'Unknown error'}`,
        500,
        'FETCH_UNITS_ERROR'
      );
    }
  }

  // Get unit by ID
  async getById(id: string): Promise<Unit> {
    if (API_CONFIG.useMockData) {
      return this.getByIdMock(id);
    }

    try {
      const response = await apiClient.get<Unit>(`${this.endpoint}/${id}`);
      console.log('Get unit by ID response:', response);
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to fetch unit: ${error instanceof Error ? error.message : 'Unknown error'}`,
        404,
        'UNIT_NOT_FOUND'
      );
    }
  }

  // Create new unit
  async create(data: CreateUnitData): Promise<Unit> {
    if (API_CONFIG.useMockData) {
      return this.createMock(data);
    }

    try {
      console.log('Creating unit with data:', data);
      console.log('Sending POST request to:', this.endpoint);
      const response = await apiClient.post<Unit>(this.endpoint, data);
      console.log('Create unit response:', response);
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to create unit: ${error instanceof Error ? error.message : 'Unknown error'}`,
        400,
        'CREATE_UNIT_ERROR'
      );
    }
  }

  // Update existing unit
  async update(id: string, data: CreateUnitData): Promise<Unit> {
    if (API_CONFIG.useMockData) {
      return this.updateMock(id, data);
    }

    try {
      const response = await apiClient.put<Unit>(`${this.endpoint}/${id}`, data);
      console.log('Update unit response:', response);
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to update unit: ${error instanceof Error ? error.message : 'Unknown error'}`,
        400,
        'UPDATE_UNIT_ERROR'
      );
    }
  }

  // Delete unit
  async delete(id: string): Promise<void> {
    if (API_CONFIG.useMockData) {
      return this.deleteMock(id);
    }

    try {
      await apiClient.delete(`${this.endpoint}/${id}`);
    } catch (error) {
      throw new ServiceError(
        `Failed to delete unit: ${error instanceof Error ? error.message : 'Unknown error'}`,
        400,
        'DELETE_UNIT_ERROR'
      );
    }
  }

  // Mock implementation methods
  private async getAllMock(filters: BaseFilters): Promise<PaginatedResponse<Unit>> {
    await simulateApiDelay();
    simulateApiError();

    let filteredUnits = [...this.mockData];

    // Apply department filter
    if (filters.department_id) {
      filteredUnits = filteredUnits.filter(unit => unit.department_id === filters.department_id);
    }

    // Apply search filter
    if (filters.search) {
      filteredUnits = filterBySearch(filteredUnits, filters.search, ['name', 'symbol']);
    }

    // Apply sorting
    if (filters.sort_field && filters.sort_direction) {
      filteredUnits = sortItems(filteredUnits, filters.sort_field as keyof Unit, filters.sort_direction);
    }

    // Apply pagination
    return paginateItems(filteredUnits, filters.page || 1, filters.per_page || 10);
  }

  private async getByIdMock(id: string): Promise<Unit> {
    await simulateApiDelay();
    simulateApiError();

    const unit = this.mockData.find(u => u.id === id);
    if (!unit) {
      throw new ServiceError('Unit not found', 404, 'UNIT_NOT_FOUND');
    }
    return unit;
  }

  private async createMock(data: CreateUnitData): Promise<Unit> {
    await simulateApiDelay();
    simulateApiError();

    const newUnit: Unit = {
      id: generateId(),
      branch_id: data.branch_id,
      name: data.name,
      symbol: data.symbol,
      department_id: data.department_id,
      created_at: getCurrentTimestamp(),
      updated_at: getCurrentTimestamp(),
    };

    this.mockData.unshift(newUnit);
    return newUnit;
  }

  private async updateMock(id: string, data: CreateUnitData): Promise<Unit> {
    await simulateApiDelay();
    simulateApiError();

    const index = this.mockData.findIndex(u => u.id === id);
    if (index === -1) {
      throw new ServiceError('Unit not found', 404, 'UNIT_NOT_FOUND');
    }

    const updatedUnit: Unit = {
      ...this.mockData[index],
      branch_id: data.branch_id,
      name: data.name,
      symbol: data.symbol,
      department_id: data.department_id,
      updated_at: getCurrentTimestamp(),
    };

    this.mockData[index] = updatedUnit;
    return updatedUnit;
  }

  private async deleteMock(id: string): Promise<void> {
    await simulateApiDelay();
    simulateApiError();

    const index = this.mockData.findIndex(u => u.id === id);
    if (index === -1) {
      throw new ServiceError('Unit not found', 404, 'UNIT_NOT_FOUND');
    }

    this.mockData.splice(index, 1);
  }

  // Reset mock data (useful for testing)
  resetMockData(): void {
    this.mockData = [...mockUnits];
  }
}

// Export singleton instance
export const unitsService = new UnitsService();
export { UnitsService };
