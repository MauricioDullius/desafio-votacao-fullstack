import { useEffect, useState } from 'react'
import { membersApi } from '../api.js'

const EMPTY_FORM = { name: '', cpf: '' }

export default function MembersPage() {
  const [members, setMembers] = useState([])
  const [form, setForm] = useState(EMPTY_FORM)
  const [loading, setLoading] = useState(false)
  const [message, setMessage] = useState(null)

  async function loadMembers() {
    try {
      const page = await membersApi.list()
      setMembers(page.content ?? [])
    } catch (err) {
      setMessage({ type: 'error', text: err.message })
    }
  }

  useEffect(() => {
    loadMembers()
  }, [])

  async function handleSubmit(event) {
    event.preventDefault()
    setLoading(true)
    setMessage(null)
    try {
      await membersApi.create(form)
      setForm(EMPTY_FORM)
      setMessage({ type: 'success', text: 'Associado cadastrado com sucesso.' })
      loadMembers()
    } catch (err) {
      setMessage({ type: 'error', text: err.message })
    } finally {
      setLoading(false)
    }
  }

  return (
    <section className="page">
      <div className="page-header">
        <h2>Associados</h2>
        <p>Cadastre os associados que poderão votar nas pautas.</p>
      </div>

      <form className="card form" onSubmit={handleSubmit}>
        <div className="field">
          <label htmlFor="name">Nome</label>
          <input
            id="name"
            required
            value={form.name}
            onChange={(e) => setForm({ ...form, name: e.target.value })}
            placeholder="Nome completo"
          />
        </div>
        <div className="field">
          <label htmlFor="cpf">CPF</label>
          <input
            id="cpf"
            required
            value={form.cpf}
            onChange={(e) => setForm({ ...form, cpf: e.target.value })}
            placeholder="Somente números"
          />
        </div>
        <button type="submit" disabled={loading}>
          {loading ? 'Cadastrando…' : 'Cadastrar associado'}
        </button>
        {message && <p className={`message message-${message.type}`}>{message.text}</p>}
        <p className="hint">
          A validação de CPF é simulada: às vezes um CPF válido é recusado aleatoriamente pelo
          serviço fake. Tente novamente se isso acontecer.
        </p>
      </form>

      <div className="card">
        <h3>Associados cadastrados</h3>
        {members.length === 0 ? (
          <p className="empty">Nenhum associado cadastrado ainda.</p>
        ) : (
          <table>
            <thead>
              <tr>
                <th>Nome</th>
                <th>CPF</th>
                <th>Ativo</th>
              </tr>
            </thead>
            <tbody>
              {members.map((member) => (
                <tr key={member.id}>
                  <td>{member.name}</td>
                  <td>{member.cpf}</td>
                  <td>{member.active ? 'Sim' : 'Não'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </section>
  )
}
