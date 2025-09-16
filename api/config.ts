// API Configuration
export const API_CONFIG = {
  baseUrl: process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080/api',
  useMockData: process.env.NEXT_PUBLIC_USE_MOCK_DATA === 'true',
  timeout: parseInt(process.env.NEXT_PUBLIC_API_TIMEOUT || '10000'),
  retryAttempts: parseInt(process.env.NEXT_PUBLIC_API_RETRY_ATTEMPTS || '3'),
  retryDelay: parseInt(process.env.NEXT_PUBLIC_API_RETRY_DELAY || '1000'),
  
  
  // Mock data settings
    // Mock data settings - should be disabled in production
  mockApiDelayMin: parseInt(process.env.NEXT_PUBLIC_MOCK_API_DELAY_MIN || '300'),
  mockApiDelayMax: parseInt(process.env.NEXT_PUBLIC_MOCK_API_DELAY_MAX || '1000'),
  mockErrorRate: parseFloat(process.env.NEXT_PUBLIC_MOCK_ERROR_RATE || '0.0'),
};

// API Response Types (matching Spring Boot format)
export interface ApiResponse<T> {
  data: T;
  message?: string;
  success: boolean;
  timestamp: string;
}

export interface ApiError {
  error: string;
  message: string;
  status: number;
  timestamp: string;
  path?: string;
}

export interface PaginatedApiResponse<T> {
  data: T[];
  pagination: {
    page: number;
    per_page: number;
    total: number;
    total_pages: number;
    has_next: boolean;
    has_prev: boolean;
  };
  message?: string;
  success: boolean;
  timestamp: string;
}

// Service Error Class
export class ServiceError extends Error {
  constructor(
    message: string,
    public status: number = 500,
    public code?: string
  ) {
    super(message);
    this.name = 'ServiceError';
  }
}

// Utility function to simulate API delay for mock data
export const simulateApiDelay = async (
  minDelay: number = API_CONFIG.mockApiDelayMin,
  maxDelay: number = API_CONFIG.mockApiDelayMax
): Promise<void> => {
  const delay = Math.random() * (maxDelay - minDelay) + minDelay;
  await new Promise(resolve => setTimeout(resolve, delay));
};

// Utility function to simulate API errors
export const simulateApiError = (): void => {
  if (Math.random() < API_CONFIG.mockErrorRate) {
    throw new ServiceError('Simulated API error', 500, 'MOCK_ERROR');
  }
};

// Generate current timestamp in ISO format
export const getCurrentTimestamp = (): string => {
  return new Date().toISOString();
};

// Generate unique ID for mock data
export const generateId = (): string => {
  return Math.random().toString(36).substring(2, 11);
};
