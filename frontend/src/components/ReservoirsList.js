import React, { useState, useEffect } from 'react';
import { fetchData } from '../services/apiService';
import { ENDPOINTS } from '../config/api';
import './ReservoirsList.css';

/**
 * Component to display list of reservoirs from Water Service
 */
const ReservoirsList = () => {
  const [reservoirs, setReservoirs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    loadReservoirs();
  }, []);

  const loadReservoirs = async () => {
    setLoading(true);
    setError(null);
    
    const result = await fetchData(ENDPOINTS.RESERVOIRS);
    
    if (result.success) {
      setReservoirs(result.data);
    } else {
      setError(result.error);
    }
    
    setLoading(false);
  };

  const calculatePercentage = (current, capacity) => {
    if (!capacity || capacity === 0) return 0;
    return Math.min(100, Math.round((current / capacity) * 100));
  };

  const getLevelColor = (percentage) => {
    if (percentage >= 70) return '#4caf50'; // Green - Good
    if (percentage >= 40) return '#ff9800'; // Orange - Medium
    return '#f44336'; // Red - Low
  };

  if (loading) {
    return (
      <div className="reservoirs-container">
        <h2>💧 Réservoirs d'Eau</h2>
        <div className="loading">Chargement des réservoirs...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="reservoirs-container">
        <h2>💧 Réservoirs d'Eau</h2>
        <div className="error">
          <p>Erreur: {error}</p>
          <button onClick={loadReservoirs} className="retry-btn">Réessayer</button>
        </div>
      </div>
    );
  }

  return (
    <div className="reservoirs-container">
      <div className="header">
        <h2>💧 Réservoirs d'Eau</h2>
        <button onClick={loadReservoirs} className="refresh-btn">🔄 Actualiser</button>
      </div>
      
      {reservoirs.length === 0 ? (
        <div className="empty-state">
          <p>Aucun réservoir disponible</p>
          <small>Les réservoirs apparaîtront ici une fois créés via l'API</small>
        </div>
      ) : (
        <div className="reservoirs-grid">
          {reservoirs.map((reservoir) => {
            const percentage = calculatePercentage(reservoir.niveauActuel, reservoir.capacite);
            const levelColor = getLevelColor(percentage);
            
            return (
              <div key={reservoir.id} className="reservoir-card">
                <div className="reservoir-header">
                  <h3>{reservoir.nom}</h3>
                  <span className="location">📍 {reservoir.localisation || 'N/A'}</span>
                </div>
                
                <div className="reservoir-level">
                  <div className="level-bar-container">
                    <div 
                      className="level-bar" 
                      style={{ 
                        width: `${percentage}%`,
                        backgroundColor: levelColor
                      }}
                    >
                      <span className="level-text">{percentage}%</span>
                    </div>
                  </div>
                </div>
                
                <div className="reservoir-details">
                  <div className="detail-row">
                    <span className="label">Niveau actuel:</span>
                    <span className="value">{reservoir.niveauActuel?.toLocaleString('fr-FR')} L</span>
                  </div>
                  <div className="detail-row">
                    <span className="label">Capacité max:</span>
                    <span className="value">{reservoir.capacite?.toLocaleString('fr-FR')} L</span>
                  </div>
                  <div className="detail-row">
                    <span className="label">ID:</span>
                    <span className="value">#{reservoir.id}</span>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      )}
      
      <div className="info-footer">
        <small>📡 Connecté via API Gateway: {ENDPOINTS.RESERVOIRS}</small>
      </div>
    </div>
  );
};

export default ReservoirsList;
