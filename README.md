# Raízes do Nordeste — API Back-end

API REST desenvolvida como projeto multidisciplinar da trilha Back-end da UNINTER.
Simula o sistema de pedidos de uma rede de lanchonetes nordestina com múltiplos canais de atendimento (APP, TOTEM, BALCÃO, PICKUP, WEB).

---

## Requisitos

| Ferramenta | Versão |
|------------|--------|
| Java | 21 (Temurin/Adoptium) |
| Maven | 3.9+ |
| MongoDB | 7.0 |

---

## 1. Instalação das dependências

### Java 21

**Mac (via Homebrew):**
```bash
brew install --cask temurin@21
```

Verifique a instalação:
```bash
java -version
# Deve aparecer: openjdk version "21.x.x"
```

**Windows:**
1. Acesse https://adoptium.net
2. Baixe o instalador **Temurin 21 (LTS)** para Windows
3. Execute o instalador e siga os passos (Next → Next → Finish)
4. Abra o Prompt de Comando e verifique:
```bash
java -version
```

---

### Maven 3.9+

**Mac (via Homebrew):**
```bash
brew install maven
```

Verifique:
```bash
mvn -version
# Deve aparecer: Apache Maven 3.9.x e Java version: 21
```

Se aparecer uma versão diferente do Java, force o Java 21:
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
mvn -version
```

**Windows:**
1. Acesse https://maven.apache.org/download.cgi
2. Baixe o **Binary zip archive** (ex: apache-maven-3.9.x-bin.zip)
3. Extraia o conteúdo em `C:\maven`
4. Adicione `C:\maven\bin` na variável de ambiente PATH:
   - Pesquise "Variáveis de Ambiente" no menu Iniciar
   - Em "Variáveis do Sistema", clique em PATH → Editar → Novo
   - Digite `C:\maven\bin` e salve
5. Abra um novo Prompt de Comando e verifique:
```bash
mvn -version
```

---

### MongoDB 7.0

**Mac (via Homebrew):**
```bash
brew tap mongodb/brew
brew install mongodb-community@7.0
```

Inicie o MongoDB:
```bash
brew services start mongodb-community@7.0
```

Verifique se está rodando:
```bash
brew services list
# mongodb-community@7.0 deve aparecer com status "started"
```

**Windows:**
1. Acesse https://www.mongodb.com/try/download/community
2. Selecione: Version **7.0**, Platform **Windows**, Package **msi**
3. Baixe e execute o instalador
4. Marque a opção **"Install MongoDB as a Service"** durante a instalação
5. O MongoDB iniciará automaticamente com o Windows

---

## 2. Clonar o repositório

```bash
git clone https://github.com/IsaqueNogueira/raizes-do-nordeste.git
cd raizes-do-nordeste
```

---

## 3. Variáveis de ambiente

O projeto já possui valores padrão configurados em `src/main/resources/application.properties`. Nenhuma configuração adicional é necessária para rodar localmente.

Se quiser personalizar, use o `.env.example` como referência:

```properties
# Servidor
server.port=8080

# MongoDB
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=raizesdonordeste

# JWT
jwt.secret=raizesdonordeste2026chaveSecretaSuperSeguraParaJWT
jwt.expiration=86400000
```

> O banco `raizesdonordeste` é criado automaticamente pelo MongoDB na primeira execução. Não há migrations — o Spring Data MongoDB cria as collections automaticamente.

---

## 4. Instalar dependências do projeto

```bash
./mvnw clean install -DskipTests
```

**Windows:**
```bash
mvnw.cmd clean install -DskipTests
```

---

## 5. Iniciar a API

Certifique-se de que o MongoDB está rodando antes de iniciar a API.

```bash
./mvnw spring-boot:run
```

**Windows:**
```bash
mvnw.cmd spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

---

## 6. Documentação Swagger

Após iniciar a API, acesse no navegador:

```
http://localhost:8080/swagger-ui.html
```

Para autenticar no Swagger:
1. Faça login via `POST /auth/login` e copie o `accessToken`
2. Clique no botão **Authorize** (cadeado) no topo da página
3. Digite `Bearer SEU_TOKEN` e clique em Authorize

Definição OpenAPI (JSON):
```
http://localhost:8080/api-docs
```

---

## 7. Coleção Postman

O arquivo `postman_collection.json` na raiz do projeto contém todos os testes organizados.

**Como importar:**
1. Abra o Postman
2. Clique em **Import**
3. Selecione o arquivo `postman_collection.json`

**Ordem sugerida de execução:**
1. Registrar Admin
2. Login Admin *(token salvo automaticamente)*
3. Criar Unidade
4. Criar Produto
5. Adicionar Estoque
6. Registrar Cliente
7. Login Cliente *(token salvo automaticamente)*
8. Criar Pedido
9. Consultar Fidelidade
10. Filtrar Pedidos por Canal

**Como rodar todos os testes:**
1. Clique nos três pontinhos da collection
2. Clique em **Run collection**
3. Clique em **Run Raízes do Nordeste API**

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
| POST | /pedidos | CLIENTE/ATENDENTE/ADMIN | Criar pedido com pagamento mock |
| GET | /pedidos | ADMIN/GERENTE/COZINHA/ATENDENTE | Listar pedidos com filtros |
| PATCH | /pedidos/{id}/status | ADMIN/GERENTE/COZINHA/ATENDENTE | Atualizar status do pedido |
| GET | /fidelidade/{clienteId} | ADMIN/CLIENTE | Consultar pontos de fidelidade |

---

## Fluxo crítico

Pedido → Pagamento Mock → Atualização de Status → Fidelidade

1. Cliente faz pedido informando `canalPedido` (APP, TOTEM, BALCAO, PICKUP, WEB)
2. Sistema valida estoque por unidade
3. Pagamento mock é processado automaticamente por forma de pagamento:
   - PIX: aprovado até R$ 2.000
   - Cartão: aprovado até R$ 1.000 (+ 10% de chance de recusa por saldo)
   - Dinheiro: aprovado até R$ 500
4. Status do pedido é atualizado automaticamente (PAGAMENTO_APROVADO ou CANCELADO)
5. Pontos de fidelidade são creditados se pagamento aprovado

---

## Segurança e LGPD

- Senhas armazenadas com BCrypt (hash — nunca em texto puro)
- Autenticação via JWT Bearer Token com expiração de 24 horas
- Autorização por perfis: ADMIN, GERENTE, CLIENTE, COZINHA, ATENDENTE
- Senha nunca retornada nas respostas da API
- Consentimento LGPD obrigatório no cadastro
- Dados pessoais coletados apenas para autenticação e fidelização

---

## Estrutura do projeto

```
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
```

---

## Autor

Isaque Paixão Nogueira — RU: 4757111
UNINTER — Projeto Multidisciplinar Trilha Back-end 2026