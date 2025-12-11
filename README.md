# 📺 API de Cadastro de Séries de TV

API REST desenvolvida em **Java + Spring Boot** para cadastro de séries de TV, como avaliação prática da disciplina.

O projeto implementa operações básicas de CRUD, validações com Bean Validation, documentação com **Swagger (OpenAPI)** e uso de banco de dados relacional (**MySQL**), além de um perfil local com **H2** para facilitar o desenvolvimento.

---

## 🚀 Tecnologias Utilizadas

- Java 17+
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Bean Validation (Jakarta Validation)
- H2 Database (perfil `local`)
- MySQL (perfil padrão)
- Swagger / OpenAPI (springdoc-openapi)
- Docker (para subir o MySQL rapidamente)

---

## 📚 Funcionalidades da API

Cada série possui os seguintes atributos:

- `nome` – Nome da série
- `genero` – Gênero (Ex.: Drama, Comédia, Ficção Científica, etc.)
- `anoLancamento` – Ano de lançamento
- `quantidadeTemporadas` – Quantidade de temporadas
- `classificacaoEnum` – Classificação indicativa (`Livre`, `12+`, `16+`, `18+`)

### ✅ Validações aplicadas (DTO)

As validações são feitas no `DadosSerieDTO` usando Jakarta Validation:

- `nome`: não pode ser vazio (`@NotBlank`)
- `genero`: não pode ser vazio (`@NotBlank`)
- `anoLancamento`:
    - obrigatório (`@NotNull`)
    - mínimo 1900 (`@Min(1900)`)
- `quantidadeTemporadas`:
    - obrigatória (`@NotNull`)
    - deve ser positiva (`@Positive`)
- `classificacaoEnum`:
    - obrigatória (`@NotBlank`)
    - deve ser uma das opções: `Livre`, `12+`, `16+`, `18+` (`@Pattern`)

---

## 🧭 Endpoints Principais

Base URL padrão: `http://localhost:8080`

### 1. Listar todas as séries

**GET** `/series`

---

### 2. Buscar série por ID

**GET** `/series/{serieId}`

- `200 OK`: retorna a série
- `404 Not Found:` série não encontrada

---

### 3. Cadastrar nova série

**POST** `/series`

**Exemplo de body JSON**:
``` json
{
  "nome": "Breaking Bad",
  "genero": "Drama",
  "anoLancamento": 2008,
  "quantidadeTemporadas": 5,
  "classificacaoEnum": "16+"
}
```

- `201 Created`: série cadastrada
- `400 Bad Request`: erro de validação (retorna mensagem com os campos inválidos)

---
### 4. Atualizar série existente

**PUT** `/series/{serieId}`

- Atualiza todos os campos da série
- Mesma estrutura de JSON do POST

---
### 5. Remover série

**DELETE** `/series/{serieId}`

- `204 No Content`: removida com sucesso
- `404 Not Found:` se o ID não existir

---
## 📄 Documentação com Swagger

A API está documentada com **Swagger/OpenAPI**, usando anotações como `@Operation` e `@ApiResponse`.

Após subir a aplicação, a documentação pode ser acessada em:

- **Swagger UI**: <br>
`http://localhost:8080/swagger-ui/index.html`


- **OpenAPI JSON**: <br>
`http://localhost:8080/v3/api-docs`

---

## 🗃️ Banco de Dados & Perfis

O projeto utiliza **dois arquivos de configuração**:

🔹 `application-local.properties` (H2 – desenvolvimento local)

Usado quando o perfil `local` está ativo.

Exemplo de configuração:
```properties
spring.application.name=series-api

spring.datasource.url=jdbc:h2:mem:seriesdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

Para rodar usando esse perfil (H2):
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

H2 Console disponível em:

- `http://localhost:8080/h2-console` <br>
(JDBC URL: jdbc:h2:mem:seriesdb)

---
🔹 `application.properties` **(MySQL – padrão)**

Usado por padrão (sem especificar profile).

Exemplo de configuração:

````properties
spring.application.name=series-api

spring.datasource.url=jdbc:mysql://localhost:3306/seriesdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=seriesuser
spring.datasource.password=seriespass
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
````
---

## 🐬 Subindo o MySQL com Docker

Para facilitar, o banco MySQL pode ser levantado em um container Docker.

### ▶️ Criar e subir o container
```bash
docker run --name mysql-series \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=seriesdb \
  -e MYSQL_USER=seriesuser \
  -e MYSQL_PASSWORD=seriespass \
  -p 3306:3306 \
  -d mysql:8.0
```

### 🔎 Verificar se está rodando
```bash
docker ps
```

### 🧩 Acessar o MySQL dentro do container (opcional)
```bash
docker exec -it mysql-series mysql -useriesuser -pseriespass
```

### ⏹️ Parar o container
```bash
docker stop mysql-series
```

### 🗑️ Remover o container
```bash
docker rm mysql-series
```

---

## ▶️ Como Rodar o Projeto
### 1. Clonar o repositório
```bash
git clone https://github.com/SEU-USUARIO/series-api.git
cd series-api
```

### 2. Rodar com H2 (perfil local)
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

- Banco: H2 em memória
- onsole H2: ´http://localhost:8080/h2-console´

### 3. Rodar com MySQL (perfil padrão)

#### 1. Subir o container MySQL:
```bash
docker run --name mysql-series \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=seriesdb \
  -e MYSQL_USER=seriesuser \
  -e MYSQL_PASSWORD=seriespass \
  -p 3306:3306 \
  -d mysql:8.0
```

#### 2. Rodar a aplicação normalmente:
```bash
mvn spring-boot:run
```

#### 3. Acessar a API:

- `http://localhost:8080/series`
- `http://localhost:8080/swagger-ui.html`

---


## ✅ Tratamento de Erros

- Erros de validação do `@Valid` (ex.: campos obrigatórios, formato inválido) são tratados por um `BindingResult`, retornando:
  - **HTTP 400 (Bad Request)**
  - Corpo com `ValidationErrors` descrevendo os problemas encontrados.

- Erros inesperados são tratados como:
  - **HTTP 500 (Internal Server Error)**
  - Mensagem genérica: `"Erro interno ao processar a requisição"` 

---
## 📝 Observações

- Projeto preparado para demonstração de:
  - Conceitos de API REST com Spring Boot
  - Validação com DTOs
  - Persistência com JPA + banco relacional (H2 / MySQL)
  - Boas práticas básicas de organização (camadas: controller, dto, model, repository, exception handler)
  - Documentação da API com Swagger