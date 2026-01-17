import axios from 'axios';

const API_BASE = 'http://localhost:8080/api';

/**
 * API Service for all backend communication via API Gateway
 */
const apiService = {
  // ===== POMPES (Energy Service) =====
  getPompes: async () => {
    const response = await axios.get(`${API_BASE}/energy/pompes`);
    return response.data;
  },

  createPompe: async (pompe) => {
    const response = await axios.post(`${API_BASE}/energy/pompes`, pompe);
    return response.data;
  },

  updatePompe: async (id, pompe) => {
    const response = await axios.put(`${API_BASE}/energy/pompes/${id}`, pompe);
    return response.data;
  },

  deletePompe: async (id) => {
    await axios.delete(`${API_BASE}/energy/pompes/${id}`);
  },

  // ===== CONSOMMATIONS (Energy Service) =====
  getConsommations: async () => {
    const response = await axios.get(`${API_BASE}/energy/consommations`);
    return response.data;
  },

  createConsommation: async (consommation) => {
    const response = await axios.post(`${API_BASE}/energy/consommations`, consommation);
    return response.data;
  },

  // ===== RÉSERVOIRS (Water Service) =====
  getReservoirs: async () => {
    const response = await axios.get(`${API_BASE}/water/reservoirs`);
    return response.data;
  },

  createReservoir: async (reservoir) => {
    const response = await axios.post(`${API_BASE}/water/reservoirs`, reservoir);
    return response.data;
  },

  updateReservoir: async (id, reservoir) => {
    const response = await axios.put(`${API_BASE}/water/reservoirs/${id}`, reservoir);
    return response.data;
  },

  deleteReservoir: async (id) => {
    await axios.delete(`${API_BASE}/water/reservoirs/${id}`);
  },

  // ===== DÉBITS MESURÉS (Water Service) =====
  getDebits: async () => {
    const response = await axios.get(`${API_BASE}/water/debits`);
    return response.data;
  },

  createDebit: async (debit) => {
    const response = await axios.post(`${API_BASE}/water/debits`, debit);
    return response.data;
  },

  updateDebit: async (id, debit) => {
    const response = await axios.put(`${API_BASE}/water/debits/${id}`, debit);
    return response.data;
  },

  deleteDebit: async (id) => {
    await axios.delete(`${API_BASE}/water/debits/${id}`);
  }
};

export default apiService;
