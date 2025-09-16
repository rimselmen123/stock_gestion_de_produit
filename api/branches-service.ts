import { Branch, CreateBranchData, PaginatedResponse, BaseFilters } from '@/lib/types';
import { apiClient, ServiceError } from './client';
import { API_CONFIG, simulateApiDelay, simulateApiError, getCurrentTimestamp, generateId } from './config';
import { mockBranches } from '@/lib/mock-data';
import { filterBySearch, sortItems, paginateItems } from '@/lib/utils';

// Branches Service Class
class BranchesService {
  private readonly endpoint = '/branches';
  private mockData: Branch[] = [...mockBranches];

  // Get all branches with filtering and pagination
  async getAll(filters: BaseFilters): Promise<PaginatedResponse<Branch>> {
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

      const response = await apiClient.get<PaginatedResponse<Branch>>(
        `${this.endpoint}?${queryParams.toString()}`
      );

      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to fetch branches: ${error instanceof Error ? error.message : 'Unknown error'}`,
        500,
        'FETCH_BRANCHES_ERROR'
      );
    }
  }

  // Get branch by ID
  async getById(id: string): Promise<Branch> {
    if (API_CONFIG.useMockData) {
      return this.getByIdMock(id);
    }

    try {
      const response = await apiClient.get<Branch>(`${this.endpoint}/${id}`);
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to fetch branch: ${error instanceof Error ? error.message : 'Unknown error'}`,
        404,
        'BRANCH_NOT_FOUND'
      );
    }
  }

  // Create new branch
  async create(data: CreateBranchData): Promise<Branch> {
    if (API_CONFIG.useMockData) {
      return this.createMock(data);
    }

    try {
      const response = await apiClient.post<Branch>(this.endpoint, data);
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to create branch: ${error instanceof Error ? error.message : 'Unknown error'}`,
        400,
        'CREATE_BRANCH_ERROR'
      );
    }
  }

  // Update existing branch
  async update(id: string, data: CreateBranchData): Promise<Branch> {
    if (API_CONFIG.useMockData) {
      return this.updateMock(id, data);
    }

    try {
      const response = await apiClient.put<Branch>(`${this.endpoint}/${id}`, data);
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to update branch: ${error instanceof Error ? error.message : 'Unknown error'}`,
        400,
        'UPDATE_BRANCH_ERROR'
      );
    }
  }

  // Delete branch
  async delete(id: string): Promise<void> {
    if (API_CONFIG.useMockData) {
      return this.deleteMock(id);
    }

    try {
      await apiClient.delete(`${this.endpoint}/${id}`);
    } catch (error) {
      throw new ServiceError(
        `Failed to delete branch: ${error instanceof Error ? error.message : 'Unknown error'}`,
        400,
        'DELETE_BRANCH_ERROR'
      );
    }
  }

  // Mock implementation methods
  private async getAllMock(filters: BaseFilters): Promise<PaginatedResponse<Branch>> {
    await simulateApiDelay();
    simulateApiError();

    let filteredBranches = [...this.mockData];

    // Apply search filter
    if (filters.search) {
      filteredBranches = filterBySearch(filteredBranches, filters.search, ['name', 'description']);
    }

    // Apply sorting
    if (filters.sort_field && filters.sort_direction) {
      filteredBranches = sortItems(filteredBranches, filters.sort_field as keyof Branch, filters.sort_direction);
    }

    // Apply pagination
    return paginateItems(filteredBranches, filters.page || 1, filters.per_page || 10);
  }

  private async getByIdMock(id: string): Promise<Branch> {
    await simulateApiDelay();
    simulateApiError();

    const branch = this.mockData.find(b => b.id === id);
    if (!branch) {
      throw new ServiceError('Branch not found', 404, 'BRANCH_NOT_FOUND');
    }
    return branch;
  }

  private async createMock(data: CreateBranchData): Promise<Branch> {
    await simulateApiDelay();
    simulateApiError();

    const newBranch: Branch = {
      id: generateId(),
      name: data.name,
      description: data.description,
      created_at: getCurrentTimestamp(),
      updated_at: getCurrentTimestamp(),
    };

    this.mockData.unshift(newBranch);
    return newBranch;
  }

  private async updateMock(id: string, data: CreateBranchData): Promise<Branch> {
    await simulateApiDelay();
    simulateApiError();

    const index = this.mockData.findIndex(b => b.id === id);
    if (index === -1) {
      throw new ServiceError('Branch not found', 404, 'BRANCH_NOT_FOUND');
    }

    const updatedBranch: Branch = {
      ...this.mockData[index],
      name: data.name,
      description: data.description,
      updated_at: getCurrentTimestamp(),
    };

    this.mockData[index] = updatedBranch;
    return updatedBranch;
  }

  private async deleteMock(id: string): Promise<void> {
    await simulateApiDelay();
    simulateApiError();

    const index = this.mockData.findIndex(b => b.id === id);
    if (index === -1) {
      throw new ServiceError('Branch not found', 404, 'BRANCH_NOT_FOUND');
    }

    this.mockData.splice(index, 1);
  }

  // Reset mock data (useful for testing)
  resetMockData(): void {
    this.mockData = [...mockBranches];
  }
}

// Export singleton instance
export const branchesService = new BranchesService();
export { BranchesService };