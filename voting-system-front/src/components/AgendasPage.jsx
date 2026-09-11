import { useEffect, useState } from 'react'
import { agendasApi, assembliesApi } from '../api.js'

const STATE_LABELS = {
  IN_VOTING: { label: 'Em votação', className: 'badge-open' },
  APPROVED: { label: 'Aprovada', className: 'badge-approved' },
  REJECTED: { label: 'Rejeitada', className: 'badge-rejected' },
}

function toIsoLocal(date) {
  const pad = (n) => String(n).padStart(2, '0')
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(
    date.getHours(),
  )}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`
}

export default function AgendasPage() {
  const [agendas, setAgendas] = useState([])
  const [page, setPage] = useState(0)
  const [pageInfo, setPageInfo] = useState({ totalPages: 1, first: true, last: true })
  const [assemblies, setAssemblies] = useState([])
  const [description, setDescription] = useState('')
  const [assemblyId, setAssemblyId] = useState('')
  const [durationMinutes, setDurationMinutes] = useState(1)
  const [loading, setLoading] = useState(false)
  const [message, setMessage] = useState(null)

  async function loadAgendas(pageToLoad) {
    try {
      const result = await agendasApi.list(pageToLoad)
      setAgendas(result.content ?? [])
      setPage(pageToLoad)
      setPageInfo({
        totalPages: result.totalPages ?? 1,
        first: result.first ?? true,
        last: result.last ?? true,
      })
    } catch (err) {
      setMessage({ type: 'error', text: err.message })
    }
  }

  async function loadAssemblies() {
    try {
      const page = await assembliesApi.list()
      setAssemblies(page.content ?? [])
      if (page.content?.length && !assemblyId) {
        setAssemblyId(String(page.content[0].id))
      }
    } catch (err) {
      setMessage({ type: 'error', text: err.message })
    }
  }

  useEffect(() => {
    loadAgendas(0)
    loadAssemblies()
  }, [])

  async function handleSubmit(event) {
    event.preventDefault()
    if (!assemblyId) {
      setMessage({ type: 'error', text: 'Cadastre uma assembleia antes de criar uma pauta.' })
      return
    }
    setLoading(true)
    setMessage(null)
    try {
      const end = toIsoLocal(new Date(Date.now() + Number(durationMinutes) * 60 * 1000))
      await agendasApi.create({
        description,
        assemblyId: Number(assemblyId),
        end,
      })
      setDescription('')
      setMessage({ type: 'success', text: 'Pauta criada e sessão de votação aberta.' })
      loadAgendas(0)
    } catch (err) {
      setMessage({ type: 'error', text: err.message })
    } finally {
      setLoading(false)
    }
  }

  return (
    <section className="page">
      <div className="page-header">
        <h2>Pautas</h2>
        <p>Cadastre uma pauta para abrir sua sessão de votação automaticamente.</p>
      </div>

      <form className="card form" onSubmit={handleSubmit}>
        <div className="field">
          <label htmlFor="agenda-assembly">Assembleia</label>
          <select
            id="agenda-assembly"
            value={assemblyId}
            onChange={(e) => setAssemblyId(e.target.value)}
            required
          >
            <option value="" disabled>
              Selecione uma assembleia
            </option>
            {assemblies.map((assembly) => (
              <option key={assembly.id} value={assembly.id}>
                {assembly.name}
              </option>
            ))}
          </select>
        </div>
        <div className="field">
          <label htmlFor="agenda-description">Descrição da pauta</label>
          <input
            id="agenda-description"
            required
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Ex.: Aprovação do orçamento 2027"
          />
        </div>
        <div className="field">
          <label htmlFor="agenda-duration">Duração da sessão (minutos)</label>
          <input
            id="agenda-duration"
            type="number"
            min="1"
            value={durationMinutes}
            onChange={(e) => setDurationMinutes(e.target.value)}
          />
        </div>
        <button type="submit" disabled={loading}>
          {loading ? 'Abrindo sessão…' : 'Cadastrar pauta e abrir votação'}
        </button>
        {message && <p className={`message message-${message.type}`}>{message.text}</p>}
      </form>

      <div className="card">
        <div className="card-header-row">
          <h3>Pautas cadastradas</h3>
          <button className="secondary" onClick={() => loadAgendas(page)} type="button">
            Atualizar
          </button>
        </div>
        {agendas.length === 0 ? (
          <p className="empty">Nenhuma pauta cadastrada ainda.</p>
        ) : (
          <table>
            <thead>
              <tr>
                <th>Descrição</th>
                <th>Encerra em</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {agendas.map((agenda) => {
                const state = STATE_LABELS[agenda.state] ?? { label: agenda.state, className: '' }
                return (
                  <tr key={agenda.id}>
                    <td>{agenda.description}</td>
                    <td>{new Date(agenda.end).toLocaleString('pt-BR')}</td>
                    <td>
                      <span className={`badge ${state.className}`}>{state.label}</span>
                    </td>
                  </tr>
                )
              })}
            </tbody>
          </table>
        )}
        {agendas.length > 0 && (
          <div className="pagination">
            <button
              className="secondary"
              type="button"
              onClick={() => loadAgendas(page - 1)}
              disabled={pageInfo.first}
            >
              Anterior
            </button>
            <span>
              Página {page + 1} de {pageInfo.totalPages}
            </span>
            <button
              className="secondary"
              type="button"
              onClick={() => loadAgendas(page + 1)}
              disabled={pageInfo.last}
            >
              Próxima
            </button>
          </div>
        )}
      </div>
    </section>
  )
}
