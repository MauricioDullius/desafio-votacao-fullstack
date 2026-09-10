const BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

async function request(path, options = {}) {
  const response = await fetch(`${BASE_URL}${path}`, {
    headers: { 'Content-Type': 'application/json' },
    ...options,
  })

  const rawBody = await response.text()
  let body = null
  if (rawBody) {
    try {
      body = JSON.parse(rawBody)
    } catch {
      body = rawBody
    }
  }

  if (!response.ok) {
    const message =
      typeof body === 'string'
        ? body
        : body && typeof body === 'object'
        ? Object.values(body).join(', ')
        : `Erro ${response.status}`
    throw new Error(message || `Erro ${response.status}`)
  }

  return body
}

export const membersApi = {
  list: (page = 0, size = 30) => request(`/api/member/v1?page=${page}&size=${size}`),
  create: (member) => request('/api/member/v1', { method: 'POST', body: JSON.stringify(member) }),
}

export const assembliesApi = {
  list: (page = 0, size = 30) => request(`/api/assembly/v1?page=${page}&size=${size}`),
  create: (assembly) => request('/api/assembly/v1', { method: 'POST', body: JSON.stringify(assembly) }),
}

export const agendasApi = {
  list: (page = 0, size = 30) => request(`/api/agenda/v1?page=${page}&size=${size}`),
  create: (agenda) => request('/api/agenda/v1', { method: 'POST', body: JSON.stringify(agenda) }),
}

export const votesApi = {
  create: (vote) => request('/api/vote/v1', { method: 'POST', body: JSON.stringify(vote) }),
}
