# Sistema de Votação — Front-end

Front-end simples em React (Vite) para o `voting-system`. Sem roteador, sem
gerenciador de estado externo — só componentes com `useState`/`useEffect`
consumindo a API REST diretamente via `fetch`.

## Telas

- **Votar** — lista as pautas com sessão aberta (`state = IN_VOTING`) e
  permite registrar o voto (Sim/Não) de um associado pelo CPF.
- **Pautas** — cadastra uma pauta vinculada a uma assembleia, já abrindo a
  sessão de votação (duração configurável em minutos, 1 minuto por padrão).
  A lista mostra o status calculado pelo backend (Em votação / Aprovada /
  Rejeitada).
- **Assembleias** — cadastra e lista assembleias (nome, início, fim).
- **Associados** — cadastra e lista associados (nome, CPF). A validação de
  CPF do backend é simulada (`FakeCpfValidatorClient`) e pode recusar um CPF
  válido aleatoriamente — nesse caso, basta tentar novamente.

## Pré-requisitos

- Node.js 18+
- O backend (`voting-system`) rodando em `http://localhost:8080`

## Como executar

```bash
npm install
npm run dev
```

A aplicação sobe em `http://localhost:5173`. O backend já está configurado
(`CorsConfig`) para aceitar chamadas desse endereço em desenvolvimento.

Se o backend rodar em outra porta/host, defina a variável de ambiente
`VITE_API_URL` antes de subir o front, por exemplo:

```bash
VITE_API_URL=http://localhost:9090 npm run dev
```

## Build de produção

```bash
npm run build
```

Gera os arquivos estáticos em `dist/`.
