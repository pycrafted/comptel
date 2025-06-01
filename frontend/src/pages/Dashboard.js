import React from 'react';

const Dashboard = () => {

  const months = ['JAN', 'FEV', 'MARS', 'AVRIL', 'MAI', 'JUIN', 'JUIL', 'AOUT', 'SEP', 'OCT', 'NOV', 'DEC'];
  
  const chartLabels = [
    'Jan 2024', 'Fév 2024', 'Mar 2024', 'Avr 2024', 'Mai 2024', 'Jun 2024',
    'Jul 2024', 'Aoû 2024', 'Sep 2024', 'Oct 2024', 'Nov 2024', 'Déc 2024'
  ];

  return (
    <div style={{ backgroundColor: '#f5f5f5', minHeight: '100vh', padding: '20px' }}>
      {/* Header */}
      <div style={{ 
        display: 'grid', 
        gridTemplateColumns: 'repeat(4, 1fr)', 
        gap: '20px', 
        marginBottom: '30px',
        maxWidth: '1200px',
        margin: '0 auto 30px auto'
      }}>
        <div style={{
          backgroundColor: 'white',
          padding: '30px 20px',
          borderRadius: '8px',
          textAlign: 'center',
          boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
          border: '1px solid #e0e0e0'
        }}>
          <h3 style={{
            margin: '0 0 10px 0',
            fontSize: '14px',
            fontWeight: 'bold',
            color: '#666',
            textTransform: 'uppercase'
          }}>MENSUEL</h3>
          <p style={{
            margin: '0',
            fontSize: '24px',
            fontWeight: 'bold',
            color: '#333'
          }}>0 FCFA</p>
        </div>

        <div style={{
          backgroundColor: 'white',
          padding: '30px 20px',
          borderRadius: '8px',
          textAlign: 'center',
          boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
          border: '1px solid #e0e0e0'
        }}>
          <h3 style={{
            margin: '0 0 10px 0',
            fontSize: '14px',
            fontWeight: 'bold',
            color: '#666',
            textTransform: 'uppercase'
          }}>DE</h3>
          <p style={{
            margin: '0',
            fontSize: '24px',
            fontWeight: 'bold',
            color: '#333'
          }}>0 Clients</p>
        </div>

        <div style={{
          backgroundColor: 'white',
          padding: '30px 20px',
          borderRadius: '8px',
          textAlign: 'center',
          boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
          border: '1px solid #e0e0e0'
        }}>
          <h3 style={{
            margin: '0 0 10px 0',
            fontSize: '14px',
            fontWeight: 'bold',
            color: '#666',
            textTransform: 'uppercase'
          }}>MOYEN</h3>
          <p style={{
            margin: '0',
            fontSize: '24px',
            fontWeight: 'bold',
            color: '#333'
          }}>0 FCFA</p>
        </div>

        <div style={{
          backgroundColor: 'white',
          padding: '30px 20px',
          borderRadius: '8px',
          textAlign: 'center',
          boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
          border: '1px solid #e0e0e0'
        }}>
          <h3 style={{
            margin: '0 0 10px 0',
            fontSize: '14px',
            fontWeight: 'bold',
            color: '#666',
            textTransform: 'uppercase'
          }}>D'AFFAIRES</h3>
          <p style={{
            margin: '0',
            fontSize: '24px',
            fontWeight: 'bold',
            color: '#333'
          }}>0 FCFA</p>
        </div>
      </div>

      {/* Tableau mensuel */}
      <div style={{
        backgroundColor: 'white',
        borderRadius: '8px',
        marginBottom: '30px',
        overflow: 'hidden',
        boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
        maxWidth: '1200px',
        margin: '0 auto 30px auto'
      }}>
        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead>
            <tr style={{ backgroundColor: '#4a5568' }}>
              <th style={{
                padding: '15px',
                color: 'white',
                fontWeight: 'bold',
                textAlign: 'center',
                fontSize: '14px'
              }}></th>
              {months.map((month, index) => (
                <th key={index} style={{
                  padding: '15px',
                  color: 'white',
                  fontWeight: 'bold',
                  textAlign: 'center',
                  fontSize: '14px'
                }}>{month}</th>
              ))}
            </tr>
          </thead>
          <tbody>
            <tr style={{ borderBottom: '1px solid #e0e0e0' }}>
              <td style={{
                padding: '15px',
                fontWeight: 'bold',
                backgroundColor: '#4a5568',
                color: 'white',
                textAlign: 'center'
              }}>C.A</td>
              {months.map((_, index) => (
                <td key={index} style={{
                  padding: '15px',
                  textAlign: 'center',
                  color: '#e53e3e',
                  fontWeight: 'bold'
                }}>0</td>
              ))}
            </tr>
            <tr>
              <td style={{
                padding: '15px',
                fontWeight: 'bold',
                backgroundColor: '#4a5568',
                color: 'white',
                textAlign: 'center'
              }}>DÉPENSE</td>
              {months.map((_, index) => (
                <td key={index} style={{
                  padding: '15px',
                  textAlign: 'center',
                  color: '#38a169',
                  fontWeight: 'bold'
                }}>0</td>
              ))}
            </tr>
          </tbody>
        </table>
      </div>

      {/* Graphiques */}
      <div style={{
        display: 'grid',
        gridTemplateColumns: '1fr 1fr',
        gap: '20px',
        maxWidth: '1200px',
        margin: '0 auto'
      }}>
        {/* Graphique Chiffre d'affaires */}
        <div style={{
          backgroundColor: 'white',
          borderRadius: '8px',
          overflow: 'hidden',
          boxShadow: '0 2px 8px rgba(0,0,0,0.1)'
        }}>
          <div style={{
            backgroundColor: '#4a5568',
            color: 'white',
            padding: '15px',
            textAlign: 'center',
            fontWeight: 'bold'
          }}>
            Graphique du chiffre d'affaires
          </div>
          <div style={{ padding: '20px', height: '300px', position: 'relative' }}>
            {/* Grille du graphique */}
            <svg width="100%" height="100%" style={{ position: 'absolute', top: '20px', left: '20px' }}>
              {/* Lignes horizontales */}
              {[0, 1, 2, 3, 4].map((i) => (
                <g key={i}>
                  <line 
                    x1="50" 
                    y1={50 + i * 50} 
                    x2="450" 
                    y2={50 + i * 50} 
                    stroke="#e0e0e0" 
                    strokeWidth="1"
                  />
                  <text 
                    x="30" 
                    y={55 + i * 50} 
                    fontSize="12" 
                    fill="#666" 
                    textAnchor="end"
                  >
                    {2 - i * 0.5} FCFA
                  </text>
                </g>
              ))}
              {/* Lignes verticales */}
              {chartLabels.map((label, i) => (
                <g key={i}>
                  <line 
                    x1={50 + i * 33} 
                    y1="50" 
                    x2={50 + i * 33} 
                    y2="250" 
                    stroke="#e0e0e0" 
                    strokeWidth="1"
                  />
                  <text 
                    x={50 + i * 33} 
                    y="270" 
                    fontSize="10" 
                    fill="#666" 
                    textAnchor="middle" 
                    transform={`rotate(-45, ${50 + i * 33}, 270)`}
                  >
                    {label}
                  </text>
                </g>
              ))}
            </svg>
            {/* Axe Y */}
            <div style={{
              position: 'absolute',
              left: '5px',
              top: '50%',
              transform: 'rotate(-90deg)',
              transformOrigin: 'center',
              fontSize: '12px',
              color: '#666',
              fontWeight: 'bold'
            }}>
              Chiffre d'affaires
            </div>
          </div>
        </div>

        {/* Graphique Dépenses */}
        <div style={{
          backgroundColor: 'white',
          borderRadius: '8px',
          overflow: 'hidden',
          boxShadow: '0 2px 8px rgba(0,0,0,0.1)'
        }}>
          <div style={{
            backgroundColor: '#4a5568',
            color: 'white',
            padding: '15px',
            textAlign: 'center',
            fontWeight: 'bold'
          }}>
            Graphique des dépenses mensuelles
          </div>
          <div style={{ padding: '20px', height: '300px', position: 'relative' }}>
            {/* Grille du graphique */}
            <svg width="100%" height="100%" style={{ position: 'absolute', top: '20px', left: '20px' }}>
              {/* Lignes horizontales */}
              {[0, 1, 2, 3, 4].map((i) => (
                <g key={i}>
                  <line 
                    x1="50" 
                    y1={50 + i * 50} 
                    x2="450" 
                    y2={50 + i * 50} 
                    stroke="#e0e0e0" 
                    strokeWidth="1"
                  />
                  <text 
                    x="30" 
                    y={55 + i * 50} 
                    fontSize="12" 
                    fill="#666" 
                    textAnchor="end"
                  >
                    {2 - i * 0.5} FCFA
                  </text>
                </g>
              ))}
              {/* Lignes verticales */}
              {chartLabels.map((label, i) => (
                <g key={i}>
                  <line 
                    x1={50 + i * 33} 
                    y1="50" 
                    x2={50 + i * 33} 
                    y2="250" 
                    stroke="#e0e0e0" 
                    strokeWidth="1"
                  />
                  <text 
                    x={50 + i * 33} 
                    y="270" 
                    fontSize="10" 
                    fill="#666" 
                    textAnchor="middle" 
                    transform={`rotate(-45, ${50 + i * 33}, 270)`}
                  >
                    {label}
                  </text>
                </g>
              ))}
            </svg>
            {/* Axe Y */}
            <div style={{
              position: 'absolute',
              left: '5px',
              top: '50%',
              transform: 'rotate(-90deg)',
              transformOrigin: 'center',
              fontSize: '12px',
              color: '#666',
              fontWeight: 'bold'
            }}>
              Dépenses mensuelles
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;