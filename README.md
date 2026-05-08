# Raízes do Nordeste — API Back-end

API REST desenvolvida como projeto multidisciplinar da trilha Back-end da UNINTER.
Simula o sistema de pedidos de uma rede de lanchonetes nordestina com múltiplos canais de atendimento (APP, TOTEM, BALCÃO, PICKUP, WEB).

---

## Requisitos

| Ferramenta | Versão |
|------------|--------|
| Java | 21 |
| Maven | 3.9+ |
| MongoDB | 7.0 |

---

## Variáveis de ambiente

O projeto possui valores padrão em `src/main/resources/application.properties`.
Para customizar, copie o `.env.example` e ajuste conforme necessário:

    # Servidor
    server.port=8080

    # MongoDB
    spring.data.mongodb.host=localhost
    spring.data.mongodb.port=27017
    spring.data.mongodb.database=raizesdonordeste

    # JWT
    jwt.secret=raizesdonordeste2026chaveSecretaSuperSeguraParaJWT
    jwt.expiration=86400000

---

## Como instalar e rodar

**1. Clone o repositório**

    git clone https://github.com/IsaqueNogueira/raizes-do-nordeste.git
    cd raizes-do-nordeste

**2. Inicie o MongoDB**

    brew services start mongodb-community@7.0

O banco `raizesdonordeste` é criado automaticamente pelo MongoDB na primeira execução.
Não há migrations — o Spring Data MongoDB cria as collections automaticamente.

**3. Instale as dependências**

    ./mvnw clean install -DskipTests

**4. Inicie a API**

    ./mvnw spring-boot:run

A API estará disponível em `http://localhost:8080`

---

## Documentação Swagger

Acesse após iniciar a API:

    http://localhost:8080/swagger-ui.html

Definição OpenAPI (JSON):

    http://localhost:8080/api-docs

---

## Coleção Postman

O arquivo `postman_collection.json` na raiz do projeto contém todos os testes organizados.

Ordem sugerida de execução:
1. Registrar Admin
2. Login Admin (token salvo automaticamente)
3. Criar Unidade
4. Criar Produto
5. Adicionar Estoque
6. Registrar Cliente
7. Login Cliente (token salvo automaticamente)
8. Criar Pedido
9. Consultar Fidelidade
10. Filtrar Pedidos por Canal

---

## Como rodar os testes

Os testes da coleção Postman cobrem os cenários obrigatórios do roteiro.
Para rodar via Postman Runner:

1. Abre o Postman
2. Clica nos três pontinhos da collection
3. Clica em **Run collection**
4. Clica em **Run Raízes do Nordeste API**

---

## Endpoints principais

| Método | Rota | Permissão | Descrição |
|--------|------|-----------|-----------|
| POST | /auth/register | Público | Cadastro de usuário |
| POST | /auth/login | Público | Login e geração de token JWT |
| GET | /unidades | Público | Listar unidades ativas |
| POST | /unidades | ADMIN | Criar unidade |
| GET | /produtos | Público | Listar produtos por unidade |
| POST | /produtos | ADMIN/GERENTE | Criar produto |
| POST | /estoque/movimentacao | ADMIN/GERENTE | Movimentar estoque |
| GET | /estoque/unidade/{id} | ADMIN/GERENTE | Consultar estoque por unidade |
| POST | /pedidos | CLIENTE/ATENDENTE | Criar pedido com pagamento mock |
| GET | /pedidos | ADMIN/GERENTE/COZINHA | Listar pedidos com filtros |
| PATCH | /pedidos/{id}/status | ADMIN/GERENTE/COZINHA | Atualizar status do pedido |
| GET | /fidelidade/{clienteId} | ADMIN/CLIENTE | Consultar pontos de fidelidade |

---

## Fluxo crítico

Pedido → Pagamento Mock → Atualização de Status → Fidelidade

1. Cliente faz pedido informando canalPedido (APP, TOTEM, BALCAO, PICKUP, WEB)
2. Sistema valida estoque por unidade
3. Pagamento mock é processado automaticamente
4. Valores acima de R$ 1.000 são recusados pelo mock
5. Status do pedido é atualizado automaticamente
6. Pontos de fidelidade são creditados se pagamento aprovado

---

## Segurança e LGPD

- Senhas armazenadas com BCrypt
- Autenticação via JWT Bearer Token
- Autorização por perfis: ADMIN, GERENTE, CLIENTE, COZINHA, ATENDENTE
- Senha nunca retornada nas respostas da API
- Consentimento LGPD obrigatório no cadastro
- Dados pessoais coletados apenas para autenticação e fidelização

---

## Estrutura do projeto

    src/main/java/com/raizesdonordeste/
    ├── domain/
    │   ├── model/          # Entidades do domínio
    │   └── repository/     # Interfaces de repositório
    ├── application/
    │   ├── dto/            # Objetos de transferência
    │   └── service/        # Regras de negócio
    ├── infrastructure/
    │   ├── security/       # JWT e configuração de segurança
    │   └── mock/           # Serviço de pagamento simulado
    └── api/
        ├── controller/     # Endpoints REST
        └── exception/      # Tratamento global de erros

---

## Autor

Isaque Paixão Nogueira — RU: 4757111
UNINTER — Projeto Multidisciplinar Trilha Back-end 2026