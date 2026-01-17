// API Gateway base URL
const API_GATEWAY_URL = 'http://localhost:8080';

// API endpoints
export const ENDPOINTS = {
  // Energy Service endpoints
  PUMPS: `${API_GATEWAY_URL}/api/energy/pompes`,
  CONSUMPTION: `${API_GATEWAY_URL}/api/energy/consommations`,
  
  // Water Service endpoints
  RESERVOIRS: `${API_GATEWAY_URL}/api/water/reservoirs`,
  
  // Eureka dashboard (for reference)
  EUREKA: 'http://localhost:8761'
};

// API service configuration
export const API_CONFIG = {
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json'
  }
};
