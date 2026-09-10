# 🗳️ Sistema de Votação

Plataforma para gerenciamento de assembleias, pautas e votação de associados no cooperativismo — API REST em Java/Spring Boot e front-end em React.

---

## 📋 Sumário

- [📌 Sobre o Projeto](#-sobre-o-projeto)
- [✅ Funcionalidades](#-funcionalidades)
- [📐 Regras de Negócio](#-regras-de-negócio)
- [⚙️ Tecnologias Utilizadas](#️-tecnologias-utilizadas)
- [📁 Estrutura do Repositório](#-estrutura-do-repositório)
- [🚀 Como Executar o Projeto](#-como-executar-o-projeto)
- [🔍 Documentação Swagger](#-documentação-swagger)
- [🏗️ Arquitetura e Boas Práticas](#️-arquitetura-e-boas-práticas)
- [🔢 Versionamento da API](#-versionamento-da-api)
- [⚡ Performance](#-performance)
- [🪪 Validação de CPF (Client Fake)](#-validação-de-cpf-client-fake)
- [🧪 Testes](#-testes)
- [📄 Licença](#-licença)

---

## 📌 Sobre o Projeto

No cooperativismo, cada associado possui um voto e as decisões são tomadas em assembleias, por votação. Este projeto implementa uma solução completa para gerenciar e participar dessas sessões: cadastro de pautas, abertura de sessão de votação com tempo determinado, registro de votos únicos por associado e apuração automática do resultado — através de uma API REST consumida por um front-end web.

O repositório contém dois projetos independentes:

| Projeto | Descrição |
|---|---|
| [`voting-system`](./voting-system) | API REST em Java 21 + Spring Boot |
| [`voting-system-front`](./voting-system-front) | Front-end em React + Vite |

---

## ✅ Funcionalidades

- Cadastro, atualização e listagem de **associados**
- Criação e gerenciamento de **assembleias**
- Cadastro de **pautas**, com abertura automática da sessão de votação
- Registro de **votos** (Sim/Não) e apuração do resultado
- Validação de dados e regras de negócio, com tratamento de erros consistente
- Documentação interativa via Swagger
- Testes unitários e de integração
- Front-end web para operar todo o fluxo (associados, assembleias, pautas e votação)

---

## 📐 Regras de Negócio

- Uma pauta pertence a uma assembleia e, ao ser cadastrada, já abre sua sessão de votação.
- A sessão fica aberta pelo tempo informado na criação da pauta, ou por **1 minuto**, caso nenhum valor seja informado.
- Cada associado é identificado pelo seu CPF e pode votar **apenas uma vez por pauta**, enquanto a sessão estiver aberta. Votos após o encerramento da sessão são rejeitados.
- O resultado da pauta (`IN_VOTING`, `APPROVED`, `REJECTED`) é **calculado dinamicamente**: enquanto a data de encerramento não chega, a pauta está em votação; depois disso, o resultado é obtido por contagem de votos Sim/Não diretamente no banco — nenhum resultado é persistido antecipadamente.
- O cadastro de um associado passa por uma validação (simulada) de CPF antes de ser concluído.

---

## ⚙️ Tecnologias Utilizadas

### Backend

| Finalidade              | Tecnologia                 |
|------------------------|----------------------------|
| Linguagem              | Java 21                    |
| Framework Principal    | Spring Boot 3.2.5          |
| Build Tool             | Gradle                     |
| Migração de Banco      | Flyway                     |
| Documentação           | SpringDoc / Swagger        |
| Persistência           | Spring Data JPA            |
| Testes                 | JUnit, Mockito, MockMvc    |
| Mapeamento de Objetos  | MapStruct                  |
| Banco de Dados         | MySQL                      |
| Logs                   | SLF4J (LoggerFactory)      |

### Frontend

| Finalidade              | Tecnologia                 |
|------------------------|----------------------------|
| Biblioteca UI          | React 18                   |
| Build Tool / Dev Server| Vite                       |
| Comunicação com a API  | `fetch` nativo (sem cliente HTTP externo) |
| Estado / Navegação     | `useState`/`useEffect` (sem Redux ou React Router) |

---

## 📁 Estrutura do Repositório

```
desafio-votacao/
├── voting-system/            # API REST
│   └── src/
│       ├── main/
│       │   └── java/br.com.db.system.votingsystem.v1/
│       │       ├── client/          # Cliente fake de validação de CPF
│       │       ├── config/          # Configurações (OpenAPI, CORS)
│       │       ├── controller/      # Controladores REST
│       │       │   └── doc/         # Interfaces de documentação Swagger
│       │       ├── dto/             # Data Transfer Objects
│       │       ├── exception/       # Exceções customizadas e handler global
│       │       ├── mapper/          # MapStruct mappers
│       │       ├── model/
│       │       │   ├── entity/      # Entidades JPA
│       │       │   └── enums/       # Enums do domínio
│       │       ├── repository/      # Repositórios JPA
│       │       ├── service/         # Regras de negócio
│       │       └── util/            # Classes utilitárias
│       └── test/                    # Testes unitários e de integração
│
└── voting-system-front/      # Front-end
    └── src/
        ├── api.js                   # Cliente HTTP para a API
        ├── App.jsx                  # Navegação por abas
        └── components/              # Telas: Votar, Pautas, Assembleias, Associados
```

---

## 🚀 Como Executar o Projeto

### Backend (`voting-system`)

**Pré-requisitos:** Java 21, MySQL, Gradle ou suporte via IDE.

Defina a conexão com o banco em `voting-system/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/voting_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

> ⚠️ O Flyway cria o schema automaticamente com base nos arquivos `.sql` em `src/main/resources/db/migration`.

Execute:

```bash
cd voting-system
./gradlew bootRun
```

A API sobe em `http://localhost:8080`.

### Frontend (`voting-system-front`)

**Pré-requisitos:** Node.js 18+, e o backend rodando em `http://localhost:8080`.

```bash
cd voting-system-front
npm install
npm run dev
```

O front sobe em `http://localhost:5173` e já está liberado no CORS do backend para desenvolvimento. Caso o backend rode em outro endereço, defina a variável `VITE_API_URL`:

```bash
VITE_API_URL=http://localhost:9090 npm run dev
```

Mais detalhes das telas e do fluxo de uso estão no [README do front-end](./voting-system-front/README.md).

---

## 🔍 Documentação Swagger

Com o backend em execução, a documentação interativa da API fica disponível em:

http://localhost:8080/swagger-ui/index.html

---

## 🏗️ Arquitetura e Boas Práticas

- Padrão REST para definição dos endpoints, com separação clara entre controller, service, repository, DTO e mapper.
- Validações de dados aplicadas tanto na camada de DTOs (Bean Validation) quanto na de serviços (regras de negócio).
- Tratamento consistente de exceções via `@ControllerAdvice`, incluindo um handler genérico para erros inesperados e um handler específico para violações de integridade do banco (ex.: voto duplicado), evitando vazar detalhes internos ao cliente.
- Flyway para versionamento e migração do banco de dados.
- MapStruct para mapeamento entre entidades e DTOs.
- Adoção de práticas de **Clean Code**, seguindo o padrão arquitetural **MVC** e os princípios **SOLID**.
- Front-end desacoplado da API, comunicando-se exclusivamente via HTTP/JSON — pode ser hospedado e escalado de forma independente do backend.

---

## 🔢 Versionamento da API

A aplicação segue a estratégia de versionamento via **URI**, utilizando o prefixo `v1` nos endpoints (ex.: `/api/member/v1`). Essa abordagem torna a versão explícita na própria URL, é simples de documentar no Swagger e permite introduzir uma futura `v2` para um recurso específico sem impactar os demais nem quebrar clientes já integrados com a `v1`.

---

## ⚡ Performance

Para lidar com cenários de grande volume (centenas de milhares de votos), a aplicação adota duas medidas principais:

- **Paginação (`Pageable`)** em todos os endpoints de listagem, limitando o carregamento em memória e facilitando a escalabilidade.
- **Apuração por contagem no banco:** o resultado de uma pauta (`APPROVED`/`REJECTED`) é calculado com queries `COUNT` (`countByAgendaIdAndVote`) direto no `VoteRepository`, em vez de carregar a coleção inteira de votos de uma pauta para a memória da aplicação. Isso mantém o custo de listar/consultar pautas constante independentemente de quantos votos elas já receberam.

---

## 🪪 Validação de CPF (Client Fake)

Para fins de desenvolvimento e testes, a validação do CPF é simulada por `FakeCpfValidatorClient`, sem consultar nenhum serviço externo real:

```json
// CPF válido para votar
{
    "status": "ABLE_TO_VOTE"
}
// CPF válido, mas não habilitado a votar
{
    "status": "UNABLE_TO_VOTE"
}
```

Quando o CPF é considerado inválido, a API retorna HTTP 404. Essa validação é executada no cadastro/atualização de associados, e por ser aleatória, um mesmo CPF pode ser aceito em uma tentativa e recusado em outra — nesse caso, basta tentar novamente.

---

## 🧪 Testes

Os testes estão localizados em `voting-system/src/test`:

- **Testes de unidade:** services, mappers, entidades, exception handler e utilitários
- **Testes de integração:** controllers com MockMvc

Execute com:

```bash
cd voting-system
./gradlew test
```

---

## 📄 Licença

Este projeto está licenciado sob a licença Apache 2.0.
