import { API_CONFIG, ApiResponse, ApiError, ServiceError } from './config';

// HTTP Methods
type HttpMethod = 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH';

// Request Options
interface RequestOptions {
  method?: HttpMethod;
  headers?: Record<string, string>;
  body?: Record<string, unknown> | FormData | string | null;
  timeout?: number;
}

// Base API Client Class
class ApiClient {
  private baseUrl: string;
  private defaultHeaders: Record<string, string>;

  constructor(baseUrl: string = API_CONFIG.baseUrl) {
    this.baseUrl = baseUrl;
    this.defaultHeaders = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    };
  }

  // Set authorization header
  setAuthToken(token: string): void {
    this.defaultHeaders['Authorization'] = `Bearer ${token}`;
  }

  // Remove authorization header
  clearAuthToken(): void {
    delete this.defaultHeaders['Authorization'];
  }

  // Make HTTP request with retry logic
  async request<T>(
    endpoint: string,
    options: RequestOptions = {}
  ): Promise<ApiResponse<T>> {
    const url = `${this.baseUrl}${endpoint}`;
    const {
      method = 'GET',
      headers = {},
      body,
      timeout = API_CONFIG.timeout,
    } = options;

    const requestHeaders = {
      ...this.defaultHeaders,
      ...headers,
    };

    const requestOptions: RequestInit = {
      method,
      headers: requestHeaders,
      body: body ? JSON.stringify(body) : undefined,
    };

    // Add timeout
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), timeout);
    requestOptions.signal = controller.signal;

    let lastError: Error | null = null;

    // Retry logic
    for (let attempt = 1; attempt <= API_CONFIG.retryAttempts; attempt++) {
      try {
        const response = await fetch(url, requestOptions);
        clearTimeout(timeoutId);

        if (!response.ok) {
          const errorData: ApiError = await response.json().catch(() => ({
            error: 'Unknown Error',
            message: `HTTP ${response.status}: ${response.statusText}`,
            status: response.status,
            timestamp: new Date().toISOString(),
          }));

          throw new ServiceError(
            errorData.message || `HTTP ${response.status}`,
            response.status,
            errorData.error
          );
        }

        const rawData = await response.json();
        console.log('Raw response data:', rawData);

        // Handle both wrapped (ApiResponse<T>) and direct responses
        // If the response has a 'data' property, it's wrapped
        // If not, it's direct data from the backend
        let data: ApiResponse<T>;

        if (rawData && typeof rawData === 'object' && 'data' in rawData && 'pagination' in rawData) {
          // Direct response from backend (PaginatedResponse format)
          console.log('Detected direct backend response format');
          data = {
            data: rawData,
            success: true,
            message: 'Success',
            timestamp: new Date().toISOString(),
          } as ApiResponse<T>;
        } else if (rawData && typeof rawData === 'object' && 'success' in rawData) {
          // Already wrapped in ApiResponse format
          console.log('Detected wrapped ApiResponse format');
          data = rawData as ApiResponse<T>;
        } else {
          // Direct entity response (for single items)
          console.log('Detected direct entity response');
          data = {
            data: rawData,
            success: true,
            message: 'Success',
            timestamp: new Date().toISOString(),
          } as ApiResponse<T>;
        }

        console.log('Processed response data:', data);
        return data;

      } catch (error) {
        clearTimeout(timeoutId);
        lastError = error as Error;

        // Don't retry on client errors (4xx)
        if (error instanceof ServiceError && error.status >= 400 && error.status < 500) {
          throw error;
        }

        // Don't retry on last attempt
        if (attempt === API_CONFIG.retryAttempts) {
          break;
        }

        // Wait before retry
        await new Promise(resolve => 
          setTimeout(resolve, API_CONFIG.retryDelay * attempt)
        );
      }
    }

    // If we get here, all retries failed
    throw lastError instanceof ServiceError
      ? lastError
      : new ServiceError(
          lastError?.message || 'Network request failed',
          0,
          'NETWORK_ERROR'
        );
  }

  // Convenience methods
  async get<T>(endpoint: string, headers?: Record<string, string>): Promise<ApiResponse<T>> {
    return this.request<T>(endpoint, { method: 'GET', headers });
  }

  async post<T>(
    endpoint: string,
    body?: Record<string, unknown> | FormData | string | null,
    headers?: Record<string, string>
  ): Promise<ApiResponse<T>> {
    return this.request<T>(endpoint, { method: 'POST', body, headers });
  }

  async put<T>(
    endpoint: string,
    body?: Record<string, unknown> | FormData | string | null,
    headers?: Record<string, string>
  ): Promise<ApiResponse<T>> {
    return this.request<T>(endpoint, { method: 'PUT', body, headers });
  }

  async patch<T>(
    endpoint: string,
    body?: Record<string, unknown> | FormData | string | null,
    headers?: Record<string, string>
  ): Promise<ApiResponse<T>> {
    return this.request<T>(endpoint, { method: 'PATCH', body, headers });
  }

  async delete<T>(endpoint: string, headers?: Record<string, string>): Promise<ApiResponse<T>> {
    return this.request<T>(endpoint, { method: 'DELETE', headers });
  }
}

// Export singleton instance
export const apiClient = new ApiClient();
export { ApiClient, ServiceError };
