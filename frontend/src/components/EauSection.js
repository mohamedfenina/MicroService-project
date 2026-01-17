import React, { useState, useEffect } from 'react';
import apiService from '../services/apiService';
import './EauSection.css';

/**
 * Eau Section - Manages Réservoirs and Débits Mesurés
 */
function EauSection() {
  const [activeTab, setActiveTab] = useState('reservoirs');
  const [reservoirs, setReservoirs] = useState([]);
  const [debits, setDebits] = useState([]);
  const [pompes, setPompes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showForm, setShowForm] = useState(false);
  const [editingItem, setEditingItem] = useState(null);

  // Form state for Reservoir
  const [reservoirForm, setReservoirForm] = useState({
    nom: '',
    capaciteTotale: '',
    volumeActuel: '',
    localisation: ''
  });

  // Form state for Debit
  const [debitForm, setDebitForm] = useState({
    pompeId: '',
    debit: '',
    unite: 'L/min',
    dateMesure: ''
  });

  useEffect(() => {
    loadData();
    loadPompes();
  }, [activeTab]);

  const loadPompes = async () => {
    try {
      const data = await apiService.getPompes();
      setPompes(data);
    } catch (error) {
      console.error('Error loading pompes:', error);
    }
  };

  const loadData = async () => {
    setLoading(true);
    try {
      if (activeTab === 'reservoirs') {
        const data = await apiService.getReservoirs();
        setReservoirs(data);
      } else {
        const data = await apiService.getDebits();
        setDebits(data);
      }
    } catch (error) {
      console.error('Error loading data:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleAdd = async (e) => {
    e.preventDefault();
    try {
      if (activeTab === 'reservoirs') {
        await apiService.createReservoir(reservoirForm);
      } else {
        await apiService.createDebit(debitForm);
      }
      resetForm();
      loadData();
    } catch (error) {
      console.error('Error creating:', error);
      const errorMsg = error.response?.data?.error || error.response?.data?.message || error.message;
      alert(errorMsg);
    }
  };

  const handleUpdate = async (e) => {
    e.preventDefault();
    try {
      if (activeTab === 'reservoirs') {
        await apiService.updateReservoir(editingItem.id, reservoirForm);
      } else {
        await apiService.updateDebit(editingItem.id, debitForm);
      }
      resetForm();
      loadData();
    } catch (error) {
      console.error('Error updating:', error);
    }
  };

  const handleDelete = async (id) => {
    const itemType = activeTab === 'reservoirs' ? 'réservoir' : 'débit mesuré';
    if (window.confirm(`Êtes-vous sûr de vouloir supprimer ce ${itemType}?`)) {
      try {
        if (activeTab === 'reservoirs') {
          await apiService.deleteReservoir(id);
        } else {
          await apiService.deleteDebit(id);
        }
        loadData();
      } catch (error) {
        console.error('Error deleting:', error);
      }
    }
  };

  const resetForm = () => {
    setReservoirForm({ nom: '', capaciteTotale: '', volumeActuel: '', localisation: '' });
    setDebitForm({ pompeId: '', debit: '', unite: 'L/min', dateMesure: '' });
    setShowForm(false);
    setEditingItem(null);
  };

  const handleEdit = (item) => {
    setEditingItem(item);
    if (activeTab === 'reservoirs') {
      setReservoirForm({
        nom: item.nom,
        capaciteTotale: item.capaciteTotale,
        volumeActuel: item.volumeActuel,
        localisation: item.localisation
      });
    } else {
      setDebitForm({
        pompeId: item.pompeId,
        debit: item.debit,
        unite: item.unite,
        dateMesure: item.dateMesure
      });
    }
    setShowForm(true);
  };

  const calculatePercentage = (volume, capacite) => {
    return capacite > 0 ? ((volume / capacite) * 100).toFixed(1) : 0;
  };

  const getStatusClass = (volume, capacite) => {
    const percentage = calculatePercentage(volume, capacite);
    if (percentage >= 70) return 'status-high';
    if (percentage >= 30) return 'status-medium';
    return 'status-low';
  };

  return (
    <div className="eau-section">
      <div className="section-header">
        <h2>💧 Eau</h2>
        <button className="btn-add" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Annuler' : '+ Ajouter'}
        </button>
      </div>

      <div className="tabs">
        <button 
          className={`tab ${activeTab === 'reservoirs' ? 'active' : ''}`}
          onClick={() => { setActiveTab('reservoirs'); setShowForm(false); }}
        >
          Réservoirs
        </button>
        <button 
          className={`tab ${activeTab === 'debits' ? 'active' : ''}`}
          onClick={() => { setActiveTab('debits'); setShowForm(false); }}
        >
          Débits Mesurés
        </button>
      </div>

      {showForm && activeTab === 'reservoirs' && (
        <div className="form-container">
          <h3>{editingItem ? 'Modifier Réservoir' : 'Nouveau Réservoir'}</h3>
          <form onSubmit={editingItem ? handleUpdate : handleAdd}>
            <input
              type="text"
              placeholder="Nom (ex: Réservoir Nord)"
              value={reservoirForm.nom}
              onChange={(e) => setReservoirForm({ ...reservoirForm, nom: e.target.value })}
              required
            />
            <input
              type="number"
              placeholder="Capacité Totale (L)"
              value={reservoirForm.capaciteTotale}
              onChange={(e) => setReservoirForm({ ...reservoirForm, capaciteTotale: e.target.value })}
              required
            />
            <input
              type="number"
              placeholder="Volume Actuel (L)"
              value={reservoirForm.volumeActuel}
              onChange={(e) => setReservoirForm({ ...reservoirForm, volumeActuel: e.target.value })}
              required
            />
            <input
              type="text"
              placeholder="Localisation (ex: Zone A, Secteur 1)"
              value={reservoirForm.localisation}
              onChange={(e) => setReservoirForm({ ...reservoirForm, localisation: e.target.value })}
              required
            />
            <div className="form-actions">
              <button type="submit" className="btn-save">
                {editingItem ? 'Modifier' : 'Créer'}
              </button>
              <button type="button" className="btn-cancel" onClick={resetForm}>
                Annuler
              </button>
            </div>
          </form>
        </div>
      )}

      {showForm && activeTab === 'debits' && (
        <div className="form-container">
          <h3>{editingItem ? 'Modifier Débit Mesuré' : 'Nouveau Débit Mesuré'}</h3>
          <form onSubmit={editingItem ? handleUpdate : handleAdd}>
            <select
              value={debitForm.pompeId}
              onChange={(e) => setDebitForm({ ...debitForm, pompeId: e.target.value })}
              required
            >
              <option value="">Sélectionner une pompe</option>
              {pompes.map(p => (
                <option key={p.id} value={p.id}>{p.reference}</option>
              ))}
            </select>
            <input
              type="number"
              step="0.01"
              placeholder="Débit (ex: 150)"
              value={debitForm.debit}
              onChange={(e) => setDebitForm({ ...debitForm, debit: e.target.value })}
              required
            />
            <select
              value={debitForm.unite}
              onChange={(e) => setDebitForm({ ...debitForm, unite: e.target.value })}
              required
            >
              <option value="L/min">L/min (Litres par minute)</option>
              <option value="L/h">L/h (Litres par heure)</option>
              <option value="m³/h">m³/h (Mètres cubes par heure)</option>
            </select>
            <input
              type="datetime-local"
              value={debitForm.dateMesure}
              onChange={(e) => setDebitForm({ ...debitForm, dateMesure: e.target.value })}
              required
            />
            <div className="form-actions">
              <button type="submit" className="btn-save">
                {editingItem ? 'Modifier' : 'Créer'}
              </button>
              <button type="button" className="btn-cancel" onClick={resetForm}>
                Annuler
              </button>
            </div>
          </form>
        </div>
      )}

      {loading ? (
        <div className="loading">Chargement...</div>
      ) : activeTab === 'reservoirs' ? (
        <div className="reservoirs-grid">
          {reservoirs.length === 0 ? (
            <div className="empty-state">
              <p>Aucun réservoir disponible</p>
              <button className="btn-add" onClick={() => setShowForm(true)}>
                + Créer le premier réservoir
              </button>
            </div>
          ) : (
            reservoirs.map(reservoir => (
              <div key={reservoir.id} className="reservoir-card">
                <div className="card-header">
                  <h3>{reservoir.nom}</h3>
                  <div className="card-actions">
                    <button className="btn-edit" onClick={() => startEdit(reservoir)}>✏️</button>
                    <button className="btn-delete" onClick={() => handleDelete(reservoir.id)}>🗑️</button>
                  </div>
                </div>
                <div className="card-body">
                  <div className="info-row">
                    <span className="label">Localisation:</span>
                    <span className="value">{reservoir.localisation}</span>
                  </div>
                  <div className="info-row">
                    <span className="label">Capacité:</span>
                    <span className="value">{reservoir.capaciteTotale} L</span>
                  </div>
                  <div className="info-row">
                    <span className="label">Volume:</span>
                    <span className="value">{reservoir.volumeActuel} L</span>
                  </div>
                  
                  <div className="level-indicator">
                    <div className="level-bar">
                      <div
                        className={`level-fill ${getStatusClass(reservoir.volumeActuel, reservoir.capaciteTotale)}`}
                        style={{ width: `${calculatePercentage(reservoir.volumeActuel, reservoir.capaciteTotale)}%` }}
                      ></div>
                    </div>
                    <span className="level-percentage">
                      {calculatePercentage(reservoir.volumeActuel, reservoir.capaciteTotale)}%
                    </span>
                  </div>
                </div>
              </div>
            ))
          )}
        </div>
      ) : (
        <div className="debits-table">
          {debits.length === 0 ? (
            <div className="empty-state">
              <p>Aucun débit mesuré disponible</p>
              <button className="btn-add" onClick={() => setShowForm(true)}>
                + Créer le premier débit mesuré
              </button>
            </div>
          ) : (
            <table className="data-table">
              <thead>
                <tr>
                  <th>Pompe ID</th>
                  <th>Débit</th>
                  <th>Unité</th>
                  <th>Date Mesure</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {debits.map(debit => (
                  <tr key={debit.id}>
                    <td>{debit.pompeId}</td>
                    <td>{debit.debit}</td>
                    <td>{debit.unite}</td>
                    <td>{new Date(debit.dateMesure).toLocaleString('fr-FR')}</td>
                    <td>
                      <button className="btn-edit" onClick={() => handleEdit(debit)}>✏️</button>
                      <button className="btn-delete" onClick={() => handleDelete(debit.id)}>🗑️</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}
    </div>
  );
}

export default EauSection;
