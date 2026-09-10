import { useEffect, useState } from 'react'
import { agendasApi, votesApi } from '../api.js'

export default function VotePage() {
  const [agendas, setAgendas] = useState([])
  const [agendaId, setAgendaId] = useState('')
  const [memberCpf, setMemberCpf] = useState('')
  const [vote, setVote] = useState('YES')
  const [loading, setLoading] = useState(false)
  const [message, setMessage] = useState(null)

  async function loadOpenAgendas() {
    try {
      const page = await agendasApi.list()
      const open = (page.content ?? []).filter((agenda) => agenda.state === 'IN_VOTING')
      setAgendas(open)
      setAgendaId((current) => {
        if (current && open.some((agenda) => String(agenda.id) === current)) return current
        return open[0] ? String(open[0].id) : ''
      })
    } catch (err) {
      setMessage({ type: 'error', text: err.message })
    }
  }

  useEffect(() => {
    loadOpenAgendas()
    const interval = setInterval(loadOpenAgendas, 10000)
    return () => clearInterval(interval)
  }, [])

  async function handleSubmit(event) {
    event.preventDefault()
    if (!agendaId) {
      setMessage({ type: 'error', text: 'Não há pautas em votação no momento.' })
      return
    }
    setLoading(true)
    setMessage(null)
    try {
      await votesApi.create({ agendaId: Number(agendaId), memberCpf, vote })
      setMemberCpf('')
      setMessage({ type: 'success', text: 'Voto registrado com sucesso.' })
      loadOpenAgendas()
    } catch (err) {
      setMessage({ type: 'error', text: err.message })
    } finally {
      setLoading(false)
    }
  }

  return (
    <section className="page">
      <div className="page-header">
        <h2>Votar</h2>
        <p>Cada associado pode votar apenas uma vez em cada pauta, enquanto a sessão estiver aberta.</p>
      </div>

      <form className="card form" onSubmit={handleSubmit}>
        {agendas.length === 0 ? (
          <p className="empty">Nenhuma pauta em votação no momento. Cadastre uma pauta para começar.</p>
        ) : (
          <>
            <div className="field">
              <label htmlFor="vote-agenda">Pauta</label>
              <select id="vote-agenda" value={agendaId} onChange={(e) => setAgendaId(e.target.value)}>
                {agendas.map((agenda) => (
                  <option key={agenda.id} value={agenda.id}>
                    {agenda.description}
                  </option>
                ))}
              </select>
            </div>
            <div className="field">
              <label htmlFor="vote-cpf">CPF do associado</label>
              <input
                id="vote-cpf"
                required
                value={memberCpf}
                onChange={(e) => setMemberCpf(e.target.value)}
                placeholder="Somente números"
              />
            </div>
            <div className="field">
              <span className="field-label">Voto</span>
              <div className="vote-choice">
                <label className={vote === 'YES' ? 'choice choice-active' : 'choice'}>
                  <input
                    type="radio"
                    name="vote"
                    value="YES"
                    checked={vote === 'YES'}
                    onChange={() => setVote('YES')}
                  />
                  Sim
                </label>
                <label className={vote === 'NO' ? 'choice choice-active' : 'choice'}>
                  <input
                    type="radio"
                    name="vote"
                    value="NO"
                    checked={vote === 'NO'}
                    onChange={() => setVote('NO')}
                  />
                  Não
                </label>
              </div>
            </div>
            <button type="submit" disabled={loading}>
              {loading ? 'Registrando…' : 'Confirmar voto'}
            </button>
          </>
        )}
        {message && <p className={`message message-${message.type}`}>{message.text}</p>}
      </form>
    </section>
  )
}
