import { useState } from 'react'
import MembersPage from './components/MembersPage.jsx'
import AssembliesPage from './components/AssembliesPage.jsx'
import AgendasPage from './components/AgendasPage.jsx'
import VotePage from './components/VotePage.jsx'

const TABS = [
  { id: 'vote', label: 'Votar', component: VotePage },
  { id: 'agendas', label: 'Pautas', component: AgendasPage },
  { id: 'assemblies', label: 'Assembleias', component: AssembliesPage },
  { id: 'members', label: 'Associados', component: MembersPage },
]

export default function App() {
  const [activeTab, setActiveTab] = useState('vote')

  const ActiveComponent = TABS.find((tab) => tab.id === activeTab).component

  return (
    <div className="app">
      <header className="app-header">
        <div className="brand">
          <span className="brand-mark">V</span>
          <div>
            <h1>Sistema de Votação</h1>
            <p>Assembleias e pautas do cooperativismo</p>
          </div>
        </div>
        <nav className="tabs">
          {TABS.map((tab) => (
            <button
              key={tab.id}
              className={tab.id === activeTab ? 'tab tab-active' : 'tab'}
              onClick={() => setActiveTab(tab.id)}
            >
              {tab.label}
            </button>
          ))}
        </nav>
      </header>

      <main className="app-main">
        <ActiveComponent />
      </main>
    </div>
  )
}
