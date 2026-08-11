# Inventory API

Uma API REST desenvolvida com **Java** e **Spring Boot** para gerenciamento de products, controle de estoque e histórico de movimentações.

O objetivo deste projeto é consolidar os conhecimentos adquiridos em Java e Spring Boot por meio do desenvolvimento de uma aplicação organizada em camadas, simulando um sistema de estoque utilizado em empresas.

---

# Tecnologias

- Java
- Spring Boot
- Spring Web
- Maven
- JUnit 5
- Mockito

---

# Funcionalidades

### Produtos

- Cadastrar product
- Listar products
- Buscar product por ID
- Buscar product por nome
- Atualizar product
- Remover product

### Estoque

- Registrar entrada de products
- Registrar saída de products
- Consultar estoque

### Movimentações

- Registrar automaticamente entradas e saídas
- Consultar histórico completo
- Consultar histórico por product

---

# Regras de Negócio

- Não permitir nome vazio.
- Não permitir preço menor ou igual a zero.
- Não permitir entradas ou saídas com quantidade menor ou igual a zero.
- Não permitir estoque negativo.
- Não permitir atualizar ou remover products inexistentes.
- Registrar toda movimentação realizada.

---

# Estrutura do Projeto

```text
src
└── main
    └── java
        └── com.seuusuario.inventoryapi
            ├── controller
            ├── service
            ├── dto
            ├── mapper
            ├── model
            └── exception
```

---

# Arquitetura

O projeto segue o padrão MVC.

```text
Cliente
   │
   ▼
Controller
   │
   ▼
Service
   │
   ▼
Model
```

---

# Endpoints

## Produtos

| Método | Endpoint | Descrição |
|---------|----------|-----------|
| POST | `/products` | Cadastrar product |
| GET | `/products` | Listar products |
| GET | `/products/{id}` | Buscar product por ID |
| GET | `/products/busca` | Buscar product por nome |
| PUT | `/products/{id}` | Atualizar product |
| DELETE | `/products/{id}` | Remover product |

## Estoque

| Método | Endpoint | Descrição |
|---------|----------|-----------|
| POST | `/estoque/entrada/{id}` | Registrar entrada |
| POST | `/estoque/saida/{id}` | Registrar saída |
| GET | `/estoque` | Consultar estoque |

## Movimentações

| Método | Endpoint | Descrição |
|---------|----------|-----------|
| GET | `/movimentacoes` | Histórico completo |
| GET | `/movimentacoes/{produtoId}` | Histórico de um product |

---

# Exemplo de Requisição

### Cadastro de Produto

```http
POST /products
```

```json
{
  "nome": "Mouse Gamer",
  "descricao": "Mouse RGB com 6 botões",
  "preco": 149.90
}
```

### Resposta

```json
{
  "id": 1,
  "nome": "Mouse Gamer",
  "descricao": "Mouse RGB com 6 botões",
  "preco": 149.90,
  "quantidade": 0
}
```

---

# Testes

Os testes unitários serão desenvolvidos utilizando **JUnit 5** e **Mockito**, com foco na validação das regras de negócio da camada de serviço.

---

# Próximas Melhorias

- Persistência com MySQL
- Spring Data JPA
- Bean Validation
- Swagger/OpenAPI
- Docker
- Spring Security + JWT
- Paginação e filtros
- Deploy da aplicação
