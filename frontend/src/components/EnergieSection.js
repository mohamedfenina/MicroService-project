import React, { useState, useEffect } from 'react';
import apiService from '../services/apiService';
import './EnergieSection.css';

/**
 * Énergie Section - Manages Pompes and Consommations
 */
function EnergieSection() {
  const [activeTab, setActiveTab] = useState('pompes');
  const [pompes, setPompes] = useState([]);
  const [consommations, setConsommations] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showForm, setShowForm] = useState(false);
  const [editingItem, setEditingItem] = useState(null);

  // Form state for Pompe
  const [pompeForm, setPompeForm] = useState({
    reference: '',
    puissance: '',
    statut: 'ACTIVE',
    dateMiseEnService: ''
  });

  // Form state for Consommation
  const [consommationForm, setConsommationForm] = useState({
    pompeId: '',
    energieUtilisee: '',
    duree: '',
    dateMesure: ''
  });

  const loadData = async () => {
    setLoading(true);
    try {
      if (activeTab === 'pompes') {
        const data = await apiService.getPompes();
        setPompes(data);
      } else {
        const data = await apiService.getConsommations();
        setConsommations(data);
      }
    } catch (error) {
      console.error('Error loading data:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [activeTab]);

  const handleAddPompe = async (e) => {
    e.preventDefault();
    try {
      await apiService.createPompe(pompeForm);
      resetForm();
      loadData();
    } catch (error) {
      console.error('Error creating pompe:', error);
    }
  };

  const handleUpdatePompe = async (e) => {
    e.preventDefault();
    try {
      await apiService.updatePompe(editingItem.id, pompeForm);
      resetForm();
      loadData();
    } catch (error) {
      console.error('Error updating pompe:', error);
    }
  };

  const handleDeletePompe = async (id) => {
    if (window.confirm('Êtes-vous sûr de vouloir supprimer cette pompe?')) {
      try {
        await apiService.deletePompe(id);
        loadData();
      } catch (error) {
        console.error('Error deleting pompe:', error);
      }
    }
  };

  const handleAddConsommation = async (e) => {
    e.preventDefault();
    try {
      await apiService.createConsommation(consommationForm);
      resetForm();
      loadData();
    } catch (error) {
      console.error('Error creating consommation:', error);
    }
  };

  const resetForm = () => {
    setPompeForm({ reference: '', puissance: '', statut: 'ACTIVE', dateMiseEnService: '' });
    setConsommationForm({ pompeId: '', energieUtilisee: '', duree: '', dateMesure: '' });
    setShowForm(false);
    setEditingItem(null);
  };

  const startEdit = (item) => {
    setEditingItem(item);
    setPompeForm({
      reference: item.reference,
      puissance: item.puissance,
      statut: item.statut,
      dateMiseEnService: item.dateMiseEnService
    });
    setShowForm(true);
  };

  return (
    <div className="energie-section">
      <div className="section-header">
        <h2>⚡ Énergie</h2>
        <button className="btn-add" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Annuler' : '+ Ajouter'}
        </button>
      </div>

      <div className="tabs">
        <button 
          className={`tab ${activeTab === 'pompes' ? 'active' : ''}`}
          onClick={() => { setActiveTab('pompes'); setShowForm(false); }}
        >
          Pompes
        </button>
        <button 
          className={`tab ${activeTab === 'consommations' ? 'active' : ''}`}
          onClick={() => { setActiveTab('consommations'); setShowForm(false); }}
        >
          Consommations Électriques
        </button>
      </div>

      {showForm && activeTab === 'pompes' && (
        <div className="form-container">
          <h3>{editingItem ? 'Modifier Pompe' : 'Nouvelle Pompe'}</h3>
          <form onSubmit={editingItem ? handleUpdatePompe : handleAddPompe}>
            <input
              type="text"
              placeholder="Référence (ex: PUMP-001)"
              value={pompeForm.reference}
              onChange={(e) => setPompeForm({ ...pompeForm, reference: e.target.value })}
              required
            />
            <input
              type="number"
              placeholder="Puissance (kW)"
              value={pompeForm.puissance}
              onChange={(e) => setPompeForm({ ...pompeForm, puissance: e.target.value })}
              required
            />
            <select
              value={pompeForm.statut}
              onChange={(e) => setPompeForm({ ...pompeForm, statut: e.target.value })}
            >
              <option value="ACTIVE">Active</option>
              <option value="INACTIVE">Inactive</option>
              <option value="MAINTENANCE">Maintenance</option>
            </select>
            <input
              type="date"
              value={pompeForm.dateMiseEnService}
              onChange={(e) => setPompeForm({ ...pompeForm, dateMiseEnService: e.target.value })}
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

      {showForm && activeTab === 'consommations' && (
        <div className="form-container">
          <h3>Nouvelle Consommation</h3>
          <form onSubmit={handleAddConsommation}>
            <select
              value={consommationForm.pompeId}
              onChange={(e) => setConsommationForm({ ...consommationForm, pompeId: e.target.value })}
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
              placeholder="Énergie Utilisée (kWh)"
              value={consommationForm.energieUtilisee}
              onChange={(e) => setConsommationForm({ ...consommationForm, energieUtilisee: e.target.value })}
              required
            />
            <input
              type="number"
              step="0.01"
              placeholder="Durée (heures)"
              value={consommationForm.duree}
              onChange={(e) => setConsommationForm({ ...consommationForm, duree: e.target.value })}
              required
            />
            <input
              type="datetime-local"
              value={consommationForm.dateMesure}
              onChange={(e) => setConsommationForm({ ...consommationForm, dateMesure: e.target.value })}
              required
            />
            <div className="form-actions">
              <button type="submit" className="btn-save">Créer</button>
              <button type="button" className="btn-cancel" onClick={resetForm}>Annuler</button>
            </div>
          </form>
        </div>
      )}

      {loading ? (
        <div className="loading">Chargement...</div>
      ) : (
        <div className="data-container">
          {activeTab === 'pompes' && (
            <table className="data-table">
              <thead>
                <tr>
                  <th>Référence</th>
                  <th>Puissance (kW)</th>
                  <th>Statut</th>
                  <th>Energy Status</th>
                  <th>Date de Mise en Service</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {pompes.length === 0 ? (
                  <tr><td colSpan="6" className="empty">Aucune pompe disponible</td></tr>
                ) : (
                  pompes.map(pompe => (
                    <tr key={pompe.id}>
                      <td>{pompe.reference}</td>
                      <td>{pompe.puissance}</td>
                      <td><span className={`status ${pompe.statut.toLowerCase()}`}>{pompe.statut}</span></td>
                      <td>
                        <span className={`status ${pompe.energyStatus === 'Overconsumption' ? 'excessive' : 'normal'}`}>
                          {pompe.energyStatus || 'Normal'}
                        </span>
                      </td>
                      <td>{pompe.dateMiseEnService}</td>
                      <td className="actions">
                        <button className="btn-edit" onClick={() => startEdit(pompe)}>✏️</button>
                        <button className="btn-delete" onClick={() => handleDeletePompe(pompe.id)}>🗑️</button>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          )}

          {activeTab === 'consommations' && (
            <table className="data-table">
              <thead>
                <tr>
                  <th>Pompe ID</th>
                  <th>Énergie (kWh)</th>
                  <th>Durée (h)</th>
                  <th>Date Mesure</th>
                  <th>Statut</th>
                </tr>
              </thead>
              <tbody>
                {consommations.length === 0 ? (
                  <tr><td colSpan="5" className="empty">Aucune consommation enregistrée</td></tr>
                ) : (
                  consommations.map(conso => (
                    <tr key={conso.id}>
                      <td>{conso.pompeId}</td>
                      <td>{conso.energieUtilisee}</td>
                      <td>{conso.duree}</td>
                      <td>{new Date(conso.dateMesure).toLocaleString()}</td>
                      <td>
                        <span className={`status ${conso.energieUtilisee >= 150 ? 'excessive' : 'normal'}`}>
                          {conso.energieUtilisee >= 150 ? 'Excessive' : 'Normal'}
                        </span>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          )}
        </div>
      )}
    </div>
  );
}

export default EnergieSection;
