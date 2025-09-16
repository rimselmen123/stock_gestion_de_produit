import { Supplier, CreateSupplierData, PaginatedResponse, BaseFilters, Document, SupplierAdditionalInfo } from '@/lib/types';
import { SupplierFormData } from '@/lib/schemas';
import { apiClient, ServiceError } from './client';
import { API_CONFIG, simulateApiDelay, simulateApiError, getCurrentTimestamp, generateId } from './config';
import { mockSuppliers } from '@/lib/mock-data';
import { filterBySearch, sortItems, paginateItems } from '@/lib/utils';

class SuppliersService {
  private readonly endpoint = '/suppliers';
  private mockData: Supplier[] = [...mockSuppliers];

  async getAll(filters: BaseFilters): Promise<PaginatedResponse<Supplier>> {
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

      const response = await apiClient.get<PaginatedResponse<Supplier>>(
        `${this.endpoint}?${queryParams.toString()}`
      );

      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to fetch suppliers: ${error instanceof Error ? error.message : 'Unknown error'}`,
        500,
        'FETCH_SUPPLIERS_ERROR'
      );
    }
  }

  async getById(id: string): Promise<Supplier> {
    if (API_CONFIG.useMockData) {
      return this.getByIdMock(id);
    }

    try {
      const response = await apiClient.get<Supplier>(`${this.endpoint}/${id}`);
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to fetch supplier: ${error instanceof Error ? error.message : 'Unknown error'}`,
        404,
        'SUPPLIER_NOT_FOUND'
      );
    }
  }

  async create(data: SupplierFormData): Promise<Supplier> {
    if (API_CONFIG.useMockData) {
      return this.createMock(data);
    }

    try {
      // Handle file uploads for documents
      const processedData = await this.processSupplierData(data);
      const response = await apiClient.post<Supplier>(this.endpoint, processedData);
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to create supplier: ${error instanceof Error ? error.message : 'Unknown error'}`,
        400,
        'CREATE_SUPPLIER_ERROR'
      );
    }
  }

  async update(id: string, data: SupplierFormData): Promise<Supplier> {
    if (API_CONFIG.useMockData) {
      return this.updateMock(id, data);
    }

    try {
      // Handle file uploads for documents
      const processedData = await this.processSupplierData(data);
      const response = await apiClient.put<Supplier>(`${this.endpoint}/${id}`, processedData);
      return response.data;
    } catch (error) {
      throw new ServiceError(
        `Failed to update supplier: ${error instanceof Error ? error.message : 'Unknown error'}`,
        400,
        'UPDATE_SUPPLIER_ERROR'
      );
    }
  }

  async delete(id: string): Promise<void> {
    if (API_CONFIG.useMockData) {
      return this.deleteMock(id);
    }

    try {
      await apiClient.delete(`${this.endpoint}/${id}`);
    } catch (error) {
      throw new ServiceError(
        `Failed to delete supplier: ${error instanceof Error ? error.message : 'Unknown error'}`,
        400,
        'DELETE_SUPPLIER_ERROR'
      );
    }
  }

  private async getAllMock(filters: BaseFilters): Promise<PaginatedResponse<Supplier>> {
    await simulateApiDelay();
    simulateApiError();

    let filtered = [...this.mockData];

    if (filters.search) {
      filtered = filterBySearch(filtered, filters.search, ['name', 'email', 'phone', 'address', 'description']);
    }

    if (filters.sort_field && filters.sort_direction) {
      filtered = sortItems(filtered, filters.sort_field as keyof Supplier, filters.sort_direction);
    }

    return paginateItems(filtered, filters.page || 1, filters.per_page || 10);
  }

  private async getByIdMock(id: string): Promise<Supplier> {
    await simulateApiDelay();
    simulateApiError();

    const supplier = this.mockData.find(s => s.id === id);
    if (!supplier) {
      throw new ServiceError('Supplier not found', 404, 'SUPPLIER_NOT_FOUND');
    }
    return supplier;
  }

  private async processSupplierData(data: SupplierFormData): Promise<CreateSupplierData> {
    const processedData: CreateSupplierData = {
      branch_id: data.branch_id,
      name: data.name,
      email: data.email,
      phone: data.phone,
      address: data.address,
      description: data.description,
    };

    // Process additional info if present
    if (data.additional_info) {
      const additionalInfo = { ...data.additional_info };
      
      // Process documents - upload files and get URLs
      if (additionalInfo.documents && additionalInfo.documents.length > 0) {
        const uploadedDocuments = await Promise.all(
          additionalInfo.documents.map(async (doc: Document) => {
            const uploadedUrl = await this.uploadDocument(doc.file);
            return {
              name: doc.name,
              url: uploadedUrl as string,
              type: doc.type,
              category: doc.category,
            };
          })
        );
        (additionalInfo as SupplierAdditionalInfo).documents = uploadedDocuments;
      }

      processedData.additional_info = additionalInfo;
    }

    return processedData;
  }

  private async uploadDocument(file: File): Promise<string> {
    // In a real implementation, this would upload the file to a storage service
    // For now, we'll simulate with a mock URL
    await simulateApiDelay();
    return `https://storage.example.com/documents/${file.name}`;
  }

  private async createMock(data: SupplierFormData): Promise<Supplier> {
    await simulateApiDelay();
    simulateApiError();

    const newSupplier: Supplier = {
      id: generateId(),
      branch_id: data.branch_id,
      name: data.name,
      email: data.email,
      phone: data.phone,
      address: data.address,
      description: data.description,
      additional_info: data.additional_info as SupplierAdditionalInfo,
      created_at: getCurrentTimestamp(),
      updated_at: getCurrentTimestamp(),
    };

    this.mockData.unshift(newSupplier);
    return newSupplier;
  }

  private async updateMock(id: string, data: SupplierFormData): Promise<Supplier> {
    await simulateApiDelay();
    simulateApiError();

    const index = this.mockData.findIndex(s => s.id === id);
    if (index === -1) {
      throw new ServiceError('Supplier not found', 404, 'SUPPLIER_NOT_FOUND');
    }

    const updated: Supplier = {
      ...this.mockData[index],
      name: data.name,
      email: data.email,
      phone: data.phone,
      address: data.address,
      description: data.description,
      additional_info: data.additional_info as SupplierAdditionalInfo,
      updated_at: getCurrentTimestamp(),
    };

    this.mockData[index] = updated;
    return updated;
  }

  private async deleteMock(id: string): Promise<void> {
    await simulateApiDelay();
    simulateApiError();

    const index = this.mockData.findIndex(s => s.id === id);
    if (index === -1) {
      throw new ServiceError('Supplier not found', 404, 'SUPPLIER_NOT_FOUND');
    }

    this.mockData.splice(index, 1);
  }

  resetMockData(): void {
    this.mockData = [...mockSuppliers];
  }
}

export const suppliersService = new SuppliersService();
export { SuppliersService };


