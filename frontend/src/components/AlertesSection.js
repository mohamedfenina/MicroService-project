import React from 'react';
import './AlertesSection.css';

/**
 * Alertes Section - Displays overconsumption alerts
 * Note: This is a placeholder as alert functionality may need backend implementation
 */
function AlertesSection() {
  // Placeholder data - would come from backend in production
  const alerts = [];

  return (
    <div className="alertes-section">
      <div className="section-header">
        <h2>⚠️ Alertes</h2>
      </div>

      <div className="alerts-info">
        <p className="info-message">
          Cette section affichera les alertes de surconsommation électrique lorsque disponibles.
        </p>
        <p className="info-note">
          Les alertes sont générées automatiquement via RabbitMQ lorsqu'une consommation 
          dépasse le seuil défini (150 kWh par défaut).
        </p>
      </div>

      {alerts.length === 0 ? (
        <div className="empty-state">
          <div className="empty-icon">🔔</div>
          <h3>Aucune alerte active</h3>
          <p>Aucune surconsommation détectée pour le moment</p>
        </div>
      ) : (
        <div className="alerts-list">
          {alerts.map((alert, index) => (
            <div key={index} className="alert-card">
              <div className="alert-icon">⚠️</div>
              <div className="alert-content">
                <h4>{alert.title}</h4>
                <p>{alert.message}</p>
                <span className="alert-time">{alert.timestamp}</span>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default AlertesSection;
