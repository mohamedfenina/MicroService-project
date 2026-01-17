import React, { useState } from 'react';
import './App.css';
import Sidebar from './components/Sidebar';
import EnergieSection from './components/EnergieSection';
import EauSection from './components/EauSection';

/**
 * Main Application Component
 * Single-page application with sidebar navigation
 */
function App() {
  const [activeSection, setActiveSection] = useState('energie');

  const renderSection = () => {
    switch (activeSection) {
      case 'energie':
        return <EnergieSection />;
      case 'eau':
        return <EauSection />;
      default:
        return <EnergieSection />;
    }
  };

  return (
    <div className="App">
      <Sidebar onNavigate={setActiveSection} activeSection={activeSection} />
      
      <div className="main-content">
        <header className="app-header">
          <h1>🌾 Système de Gestion d'Irrigation</h1>
          <p className="subtitle">Architecture Microservices - API Gateway</p>
        </header>

        <main className="app-main">
          {renderSection()}
        </main>

        <footer className="app-footer">
          <p>Développé avec Spring Boot, Spring Cloud, Docker & React</p>
        </footer>
      </div>
    </div>
  );
}

export default App;
