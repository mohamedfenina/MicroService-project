import React, { useState, useEffect } from 'react';
import { fetchData } from '../services/apiService';
import { ENDPOINTS } from '../config/api';
import './PumpsList.css';

/**
 * Component to display list of pumps from Energy Service
 */
const PumpsList = () => {
  const [pumps, setPumps] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    loadPumps();
  }, []);

  const loadPumps = async () => {
    setLoading(true);
    setError(null);
    
    const result = await fetchData(ENDPOINTS.PUMPS);
    
    if (result.success) {
      setPumps(result.data);
    } else {
      setError(result.error);
    }
    
    setLoading(false);
  };

  const getStatusColor = (statut) => {
    switch(statut?.toUpperCase()) {
      case 'ACTIVE':
      case 'ACTIF':
        return '#4caf50';
      case 'INACTIVE':
      case 'INACTIF':
        return '#f44336';
      case 'MAINTENANCE':
        return '#ff9800';
      default:
        return '#9e9e9e';
    }
  };

  if (loading) {
    return (
      <div className="pumps-container">
        <h2>⚡ Pompes Électriques</h2>
        <div className="loading">Chargement des pompes...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="pumps-container">
        <h2>⚡ Pompes Électriques</h2>
        <div className="error">
          <p>Erreur: {error}</p>
          <button onClick={loadPumps} className="retry-btn">Réessayer</button>
        </div>
      </div>
    );
  }

  return (
    <div className="pumps-container">
      <div className="header">
        <h2>⚡ Pompes Électriques</h2>
        <button onClick={loadPumps} className="refresh-btn">🔄 Actualiser</button>
      </div>
      
      {pumps.length === 0 ? (
        <div className="empty-state">
          <p>Aucune pompe disponible</p>
          <small>Les pompes apparaîtront ici une fois créées via l'API</small>
        </div>
      ) : (
        <div className="pumps-grid">
          {pumps.map((pump) => (
            <div key={pump.id} className="pump-card">
              <div className="pump-header">
                <h3>{pump.reference}</h3>
                <span 
                  className="status-badge" 
                  style={{ backgroundColor: getStatusColor(pump.statut) }}
                >
                  {pump.statut}
                </span>
              </div>
              <div className="pump-details">
                <div className="detail-row">
                  <span className="label">Puissance:</span>
                  <span className="value">{pump.puissance} kW</span>
                </div>
                <div className="detail-row">
                  <span className="label">Mise en service:</span>
                  <span className="value">
                    {pump.dateMiseEnService 
                      ? new Date(pump.dateMiseEnService).toLocaleDateString('fr-FR')
                      : 'N/A'
                    }
                  </span>
                </div>
                <div className="detail-row">
                  <span className="label">ID:</span>
                  <span className="value">#{pump.id}</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
      
      <div className="info-footer">
        <small>📡 Connecté via API Gateway: {ENDPOINTS.PUMPS}</small>
      </div>
    </div>
  );
};

export default PumpsList;
