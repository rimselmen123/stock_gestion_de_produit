import { Department, CreateDepartmentData, PaginatedResponse, BaseFilters } from '@/lib/types';
import { apiClient, ServiceError } from './client';
import { API_CONFIG, simulateApiDelay, simulateApiError, getCurrentTimestamp, generateId } from './config';
import { mockDepartments } from '@/lib/mock-data';
import { filterBySearch, sortItems, paginateItems } from '@/lib/utils';

// Departments Service Class
class DepartmentsService {
  private readonly endpoint = '/departments';
  private mockData: Department[] = [...mockDepartments];

  // Get all departments with filtering and pagination
  async getAll(filters: BaseFilters): Promise<PaginatedResponse<Department>> {
    if (API_CONFIG.useMockData) {
      return this.getAllMock(filters);
    }

    try {
      const queryParams = new URLSearchParams();
      if (filters.branch_id) queryParams.append('branch_id', filters.branch_id);
      if (filters.search) queryParams.append('search', filters.search);
      if (filters.page) queryParams.append('page', filters.page.toString());
      if (filters.per_page) queryParams.append('per_page', filters.per_page.toString());
      if (filters.sort_field) queryParams.append('sort_field', filters.sort_field);
      if (filters.sort_direction) queryParams.append('sort_direction', filters.sort_direction);

      console.log('Fetching departments with params:', queryParams.toString());

      const response = await apiClient.get<PaginatedResponse<Department>>(
        `${this.endpoint}?${queryParams.toString()}`
      );

      console.log('API response:', response);

      // API client now handles both wrapped and direct responses
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to fetch departments: ${error instanceof Error ? error.message : 'Unknown error'}`,
        500,
        'FETCH_DEPARTMENTS_ERROR'
      );
    }
  }

  // Get department by ID
  async getById(id: string): Promise<Department> {
    if (API_CONFIG.useMockData) {
      return this.getByIdMock(id);
    }

    try {
      const response = await apiClient.get<Department>(`${this.endpoint}/${id}`);
      console.log('Get department by ID response:', response);
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to fetch department: ${error instanceof Error ? error.message : 'Unknown error'}`,
        404,
        'DEPARTMENT_NOT_FOUND'
      );
    }
  }

  // Create new department
  async create(data: CreateDepartmentData): Promise<Department> {
    if (API_CONFIG.useMockData) {
      return this.createMock(data);
    }

    try {
      console.log('Creating department with data:', data);
      console.log('Sending POST request to:', this.endpoint);
      const response = await apiClient.post<Department>(this.endpoint, data);
      console.log('Create department response:', response);
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to create department: ${error instanceof Error ? error.message : 'Unknown error'}`,
        400,
        'CREATE_DEPARTMENT_ERROR'
      );
    }
  }

  // Update existing department
  async update(id: string, data: CreateDepartmentData): Promise<Department> {
    if (API_CONFIG.useMockData) {
      return this.updateMock(id, data);
    }

    try {
      const response = await apiClient.put<Department>(`${this.endpoint}/${id}`, data);
      console.log('Update department response:', response);
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to update department: ${error instanceof Error ? error.message : 'Unknown error'}`,
        400,
        'UPDATE_DEPARTMENT_ERROR'
      );
    }
  }

  // Delete department
  async delete(id: string): Promise<void> {
    if (API_CONFIG.useMockData) {
      return this.deleteMock(id);
    }

    try {
      await apiClient.delete(`${this.endpoint}/${id}`);
    } catch (error) {
      throw new ServiceError(
        `Failed to delete department: ${error instanceof Error ? error.message : 'Unknown error'}`,
        400,
        'DELETE_DEPARTMENT_ERROR'
      );
    }
  }

  // Mock implementation methods
  private async getAllMock(filters: BaseFilters): Promise<PaginatedResponse<Department>> {
    await simulateApiDelay();
    simulateApiError();

    let filteredDepartments = [...this.mockData];

    // Apply branch filter
    if (filters.branch_id) {
      filteredDepartments = filteredDepartments.filter(department => department.branch_id === filters.branch_id);
    }

    // Apply search filter
    if (filters.search) {
      filteredDepartments = filterBySearch(filteredDepartments, filters.search, ['name', 'description']);
    }

    // Apply sorting
    if (filters.sort_field && filters.sort_direction) {
      filteredDepartments = sortItems(filteredDepartments, filters.sort_field as keyof Department, filters.sort_direction);
    }

    // Apply pagination
    return paginateItems(filteredDepartments, filters.page || 1, filters.per_page || 10);
  }

  private async getByIdMock(id: string): Promise<Department> {
    await simulateApiDelay();
    simulateApiError();

    const department = this.mockData.find(d => d.id === id);
    if (!department) {
      throw new ServiceError('Department not found', 404, 'DEPARTMENT_NOT_FOUND');
    }
    return department;
  }

  private async createMock(data: CreateDepartmentData): Promise<Department> {
    await simulateApiDelay();
    simulateApiError();

    const newDepartment: Department = {
      id: generateId(),
      name: data.name,
      description: data.description,
      branch_id: data.branch_id,
      created_at: getCurrentTimestamp(),
      updated_at: getCurrentTimestamp(),
    };

    this.mockData.unshift(newDepartment);
    return newDepartment;
  }

  private async updateMock(id: string, data: CreateDepartmentData): Promise<Department> {
    await simulateApiDelay();
    simulateApiError();

    const index = this.mockData.findIndex(d => d.id === id);
    if (index === -1) {
      throw new ServiceError('Department not found', 404, 'DEPARTMENT_NOT_FOUND');
    }

    const updatedDepartment: Department = {
      ...this.mockData[index],
      branch_id: data.branch_id,
      name: data.name,
      description: data.description,
      updated_at: getCurrentTimestamp(),
    };

    this.mockData[index] = updatedDepartment;
    return updatedDepartment;
  }

  private async deleteMock(id: string): Promise<void> {
    await simulateApiDelay();
    simulateApiError();

    const index = this.mockData.findIndex(d => d.id === id);
    if (index === -1) {
      throw new ServiceError('Department not found', 404, 'DEPARTMENT_NOT_FOUND');
    }

    this.mockData.splice(index, 1);
  }

  // Reset mock data (useful for testing)
  resetMockData(): void {
    this.mockData = [...mockDepartments];
  }
}

// Export singleton instance
export const departmentsService = new DepartmentsService();
export { DepartmentsService };
