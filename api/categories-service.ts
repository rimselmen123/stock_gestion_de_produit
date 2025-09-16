import { InventoryItemCategory, CreateInventoryItemCategoryData, PaginatedResponse, BaseFilters } from '@/lib/types';
import { apiClient, ServiceError } from './client';
import { API_CONFIG, simulateApiDelay, simulateApiError, getCurrentTimestamp, generateId } from './config';
import { mockCategories } from '@/lib/mock-data';
import { filterBySearch, sortItems, paginateItems } from '@/lib/utils';

// Categories Service Class
class CategoriesService {
  private readonly endpoint = '/categories';
  private mockData: InventoryItemCategory[] = [...mockCategories];

  // Get all categories with filtering and pagination
  async getAll(filters: BaseFilters): Promise<PaginatedResponse<InventoryItemCategory>> {
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

      const response = await apiClient.get<PaginatedResponse<InventoryItemCategory>>(
        `${this.endpoint}?${queryParams.toString()}`
      );

      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to fetch categories: ${error instanceof Error ? error.message : 'Unknown error'}`,
        500,
        'FETCH_CATEGORIES_ERROR'
      );
    }
  }

  // Get category by ID
  async getById(id: string): Promise<InventoryItemCategory> {
    if (API_CONFIG.useMockData) {
      return this.getByIdMock(id);
    }

    try {
      const response = await apiClient.get<InventoryItemCategory>(`${this.endpoint}/${id}`);
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to fetch category: ${error instanceof Error ? error.message : 'Unknown error'}`,
        404,
        'CATEGORY_NOT_FOUND'
      );
    }
  }

  // Create new category
  async create(data: CreateInventoryItemCategoryData): Promise<InventoryItemCategory> {
    if (API_CONFIG.useMockData) {
      return this.createMock(data);
    }

    try {
      const response = await apiClient.post<InventoryItemCategory>(this.endpoint, data);
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to create category: ${error instanceof Error ? error.message : 'Unknown error'}`,
        400,
        'CREATE_CATEGORY_ERROR'
      );
    }
  }

  // Update existing category
  async update(id: string, data: CreateInventoryItemCategoryData): Promise<InventoryItemCategory> {
    if (API_CONFIG.useMockData) {
      return this.updateMock(id, data);
    }

    try {
      const response = await apiClient.put<InventoryItemCategory>(`${this.endpoint}/${id}`, data);
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to update category: ${error instanceof Error ? error.message : 'Unknown error'}`,
        400,
        'UPDATE_CATEGORY_ERROR'
      );
    }
  }

  // Delete category
  async delete(id: string): Promise<void> {
    if (API_CONFIG.useMockData) {
      return this.deleteMock(id);
    }

    try {
      await apiClient.delete(`${this.endpoint}/${id}`);
    } catch (error) {
      throw new ServiceError(
        `Failed to delete category: ${error instanceof Error ? error.message : 'Unknown error'}`,
        400,
        'DELETE_CATEGORY_ERROR'
      );
    }
  }

  // Mock implementation methods
  private async getAllMock(filters: BaseFilters): Promise<PaginatedResponse<InventoryItemCategory>> {
    await simulateApiDelay();
    simulateApiError();

    let filteredCategories = [...this.mockData];

    // Apply department filter
    if (filters.department_id) {
      filteredCategories = filteredCategories.filter(category => category.department_id === filters.department_id);
    }

    // Apply search filter
    if (filters.search) {
      filteredCategories = filterBySearch(filteredCategories, filters.search, ['name']);
    }

    // Apply sorting
    if (filters.sort_field && filters.sort_direction) {
      filteredCategories = sortItems(filteredCategories, filters.sort_field as keyof InventoryItemCategory, filters.sort_direction);
    }

    // Apply pagination
    return paginateItems(filteredCategories, filters.page || 1, filters.per_page || 10);
  }

  private async getByIdMock(id: string): Promise<InventoryItemCategory> {
    await simulateApiDelay();
    simulateApiError();

    const category = this.mockData.find(c => c.id === id);
    if (!category) {
      throw new ServiceError('Category not found', 404, 'CATEGORY_NOT_FOUND');
    }
    return category;
  }

  private async createMock(data: CreateInventoryItemCategoryData): Promise<InventoryItemCategory> {
    await simulateApiDelay();
    simulateApiError();

    const newCategory: InventoryItemCategory = {
      id: generateId(),
      name: data.name,
      department_id: data.department_id,
      branch_id: data.branch_id,
      created_at: getCurrentTimestamp(),
      updated_at: getCurrentTimestamp(),
    };

    this.mockData.unshift(newCategory);
    return newCategory;
  }

  private async updateMock(id: string, data: CreateInventoryItemCategoryData): Promise<InventoryItemCategory> {
    await simulateApiDelay();
    simulateApiError();

    const index = this.mockData.findIndex(c => c.id === id);
    if (index === -1) {
      throw new ServiceError('Category not found', 404, 'CATEGORY_NOT_FOUND');
    }

    const updatedCategory: InventoryItemCategory = {
      ...this.mockData[index],
      name: data.name,
      department_id: data.department_id,
      branch_id: data.branch_id,
      updated_at: getCurrentTimestamp(),
    };

    this.mockData[index] = updatedCategory;
    return updatedCategory;
  }

  private async deleteMock(id: string): Promise<void> {
    await simulateApiDelay();
    simulateApiError();

    const index = this.mockData.findIndex(c => c.id === id);
    if (index === -1) {
      throw new ServiceError('Category not found', 404, 'CATEGORY_NOT_FOUND');
    }

    this.mockData.splice(index, 1);
  }

  // Reset mock data (useful for testing)
  resetMockData(): void {
    this.mockData = [...mockCategories];
  }
}

// Export singleton instance
export const categoriesService = new CategoriesService();
export { CategoriesService };
