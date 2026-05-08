# Raízes do Nordeste — API Back-end

API REST desenvolvida como projeto multidisciplinar da trilha Back-end da UNINTER.
Simula o sistema de pedidos de uma rede de lanchonetes nordestina com múltiplos canais de atendimento.

## Tecnologias

- Java 21
- Spring Boot 3.5.14
- Spring Security + JWT
- MongoDB
- Swagger/OpenAPI (springdoc 2.8.6)
- Maven

## Pré-requisitos

- Java 21 instalado
- MongoDB rodando na porta 27017
- Maven 3.9+

## Como rodar

**1. Clone o repositório**

    git clone https://github.com/IsaqueNogueira/raizes-do-nordeste.git
    cd raizes-do-nordeste

**2. Inicie o MongoDB**

    brew services start mongodb-community@7.0

**3. Compile o projeto**

    ./mvnw clean install -DskipTests

**4. Inicie a API**

    ./mvnw spring-boot:run

## Documentação Swagger

    http://localhost:8080/swagger-ui.html

## OpenAPI JSON

    http://localhost:8080/api-docs

## Coleção Postman

O arquivo `postman_collection.json` na raiz do projeto contém todos os testes organizados.

Ordem sugerida de execução:
1. Registrar Admin
2. Login Admin
3. Criar Unidade
4. Criar Produto
5. Adicionar Estoque
6. Registrar Cliente
7. Login Cliente
8. Criar Pedido
9. Consultar Fidelidade
10. Filtrar Pedidos por Canal

## Endpoints principais

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | /auth/register | Cadastro de usuário |
| POST | /auth/login | Login e geração de token JWT |
| GET | /unidades | Listar unidades ativas |
| POST | /unidades | Criar unidade (ADMIN) |
| GET | /produtos | Listar produtos por unidade |
| POST | /produtos | Criar produto (ADMIN/GERENTE) |
| POST | /estoque/movimentacao | Movimentar estoque (ADMIN/GERENTE) |
| GET | /estoque/unidade/{id} | Consultar estoque por unidade |
| POST | /pedidos | Criar pedido com pagamento mock |
| GET | /pedidos | Listar pedidos com filtros |
| PATCH | /pedidos/{id}/status | Atualizar status do pedido |
| GET | /fidelidade/{clienteId} | Consultar pontos de fidelidade |

## Fluxo crítico

Pedido → Pagamento Mock → Atualização de Status → Fidelidade

1. Cliente faz pedido informando canalPedido (APP, TOTEM, BALCAO, PICKUP, WEB)
2. Sistema valida estoque por unidade
3. Pagamento mock é processado (valores acima de R$ 1.000 são recusados)
4. Status do pedido é atualizado automaticamente
5. Pontos de fidelidade são creditados se pagamento aprovado

## Segurança e LGPD

- Senhas armazenadas com BCrypt
- Autenticação via JWT (Bearer Token)
- Autorização por perfis: ADMIN, GERENTE, CLIENTE, COZINHA, ATENDENTE
- Senha nunca retornada nas respostas da API
- Consentimento LGPD obrigatório no cadastro
- Dados pessoais coletados apenas para finalidade de autenticação e fidelização

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

## Autor

Isaque Paixão Nogueira — RU: 4757111
UNINTER — Projeto Multidisciplinar Trilha Back-end 2026