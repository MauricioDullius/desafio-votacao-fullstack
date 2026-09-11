import { useEffect, useState } from 'react'
import { assembliesApi } from '../api.js'

function toLocalInputValue(date) {
  const pad = (n) => String(n).padStart(2, '0')
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(
    date.getHours(),
  )}:${pad(date.getMinutes())}`
}

function defaultForm() {
  const start = new Date(Date.now() + 5 * 60 * 1000)
  const end = new Date(Date.now() + 60 * 60 * 1000)
  return { name: '', start: toLocalInputValue(start), end: toLocalInputValue(end) }
}

export default function AssembliesPage() {
  const [assemblies, setAssemblies] = useState([])
  const [page, setPage] = useState(0)
  const [pageInfo, setPageInfo] = useState({ totalPages: 1, first: true, last: true })
  const [form, setForm] = useState(defaultForm)
  const [loading, setLoading] = useState(false)
  const [message, setMessage] = useState(null)

  async function loadAssemblies(pageToLoad) {
    try {
      const result = await assembliesApi.list(pageToLoad)
      setAssemblies(result.content ?? [])
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

  useEffect(() => {
    loadAssemblies(0)
  }, [])

  async function handleSubmit(event) {
    event.preventDefault()
    setLoading(true)
    setMessage(null)
    try {
      await assembliesApi.create(form)
      setForm(defaultForm())
      setMessage({ type: 'success', text: 'Assembleia criada com sucesso.' })
      loadAssemblies(0)
    } catch (err) {
      setMessage({ type: 'error', text: err.message })
    } finally {
      setLoading(false)
    }
  }

  return (
    <section className="page">
      <div className="page-header">
        <h2>Assembleias</h2>
        <p>Uma assembleia agrupa as pautas que serão votadas em uma mesma sessão.</p>
      </div>

      <form className="card form" onSubmit={handleSubmit}>
        <div className="field">
          <label htmlFor="assembly-name">Nome</label>
          <input
            id="assembly-name"
            required
            value={form.name}
            onChange={(e) => setForm({ ...form, name: e.target.value })}
            placeholder="Assembleia Geral Ordinária"
          />
        </div>
        <div className="field-row">
          <div className="field">
            <label htmlFor="assembly-start">Início</label>
            <input
              id="assembly-start"
              type="datetime-local"
              required
              value={form.start}
              onChange={(e) => setForm({ ...form, start: e.target.value })}
            />
          </div>
          <div className="field">
            <label htmlFor="assembly-end">Fim</label>
            <input
              id="assembly-end"
              type="datetime-local"
              required
              value={form.end}
              onChange={(e) => setForm({ ...form, end: e.target.value })}
            />
          </div>
        </div>
        <button type="submit" disabled={loading}>
          {loading ? 'Criando…' : 'Criar assembleia'}
        </button>
        {message && <p className={`message message-${message.type}`}>{message.text}</p>}
      </form>

      <div className="card">
        <h3>Assembleias cadastradas</h3>
        {assemblies.length === 0 ? (
          <p className="empty">Nenhuma assembleia cadastrada ainda.</p>
        ) : (
          <table>
            <thead>
              <tr>
                <th>Nome</th>
                <th>Início</th>
                <th>Fim</th>
              </tr>
            </thead>
            <tbody>
              {assemblies.map((assembly) => (
                <tr key={assembly.id}>
                  <td>{assembly.name}</td>
                  <td>{new Date(assembly.start).toLocaleString('pt-BR')}</td>
                  <td>{new Date(assembly.end).toLocaleString('pt-BR')}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
        {assemblies.length > 0 && (
          <div className="pagination">
            <button
              className="secondary"
              type="button"
              onClick={() => loadAssemblies(page - 1)}
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
              onClick={() => loadAssemblies(page + 1)}
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
