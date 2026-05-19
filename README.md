# Desafio Itau - API de Transações e Estatísticas

API REST em Spring Boot para gerenciar transações bancárias e calcular estatísticas em tempo real (últimos 60 segundos).

## 📋 Descrição

Esta aplicação fornece endpoints para:
- **POST /transacao** - Registrar uma nova transação com valor e timestamp
- **GET /transacao** - Listar todas as transações armazenadas
- **GET /transacao/recentes** - Listar transações dos últimos 60 segundos
- **DELETE /transacao** - Limpar todas as transações
- **GET /estatistica** - Obter estatísticas (count, sum, avg, min, max) das transações dos últimos 60s

## 🚀 Tecnologias Utilizadas

- **Java 11+**
- **Spring Boot 3.x**
- **Maven**
- **SLF4J + Logback** (logging)
- **Record (Java 16+)** para DTOs
- **CopyOnWriteArrayList** para thread-safety
- **@Scheduled** para limpeza automática de transações antigas

## ⚙️ Pré-requisitos

- JDK 11+ instalado
- Maven 3.6.3+ instalado
- Git (para versionamento)

## 📦 Instalação

1. Clone o repositório:
```bash
git clone https://github.com/SEU_USUARIO/desafio-itau.git
cd desafio-itau
```

2. Compile a aplicação:
```bash
./mvnw clean compile
```
(No Windows cmd: `mvnw.cmd clean compile`)

3. Rode os testes (se houver):
```bash
./mvnw test
```

## 🏃 Como Executar

### Via Maven:
```bash
./mvnw spring-boot:run
```
(No Windows cmd: `mvnw.cmd spring-boot:run`)

A aplicação iniciará em: **http://localhost:8080**

### Via JAR compilado:
```bash
./mvnw clean package
java -jar target/desafio-itau-0.0.1-SNAPSHOT.jar
```

## 📚 Documentação de Endpoints

### 1. POST /transacao - Registrar Transação

**Descrição**: Cria uma nova transação. O timestamp é normalizado para o momento da requisição se for nulo, futuro ou mais antigo que 60 segundos.

**URL**: `http://localhost:8080/transacao`

**Método**: `POST`

**Headers**:
```
Content-Type: application/json
```

**Body** (JSON):
```json
{
  "valor": 123.45,
  "dataHora": "2026-05-19T19:30:00Z"
}
```

**Response** (201 Created):
```
(vazio - apenas status HTTP 201)
```

**Response** (422 Unprocessable Entity):
```
(em caso de validação falhar - ex.: valor negativo)
```

**Exemplo com curl** (PowerShell - recomendado):
```powershell
$ts = (Get-Date).AddSeconds(-1).ToString("o")
curl -X POST http://localhost:8080/transacao `
  -H "Content-Type: application/json" `
  -d "{`"valor`": 123.45, `"dataHora`": `"$ts`"}" `
  -UseBasicParsing
```

**Exemplo com curl** (Windows cmd):
```cmd
curl -X POST http://localhost:8080/transacao ^
  -H "Content-Type: application/json" ^
  -d "{\"valor\":123.45,\"dataHora\":\"2026-05-19T19:30:00-03:00\"}"
```

---

### 2. GET /transacao - Listar Todas as Transações

**Descrição**: Retorna todas as transações armazenadas em memória (sem filtro de tempo).

**URL**: `http://localhost:8080/transacao`

**Método**: `GET`

**Response** (200 OK):
```json
[
  {
    "valor": 123.45,
    "dataHora": "2026-05-19T19:30:15.123456-03:00"
  },
  {
    "valor": 500.00,
    "dataHora": "2026-05-19T19:30:20.987654-03:00"
  }
]
```

**Exemplo**:
```cmd
curl http://localhost:8080/transacao
```

---

### 3. GET /transacao/recentes - Listar Transações Recentes (últimos 60s)

**Descrição**: Retorna apenas as transações registradas nos últimos 60 segundos (janela deslizante).

**URL**: `http://localhost:8080/transacao/recentes`

**Método**: `GET`

**Response** (200 OK):
```json
[
  {
    "valor": 123.45,
    "dataHora": "2026-05-19T19:30:15.123456-03:00"
  }
]
```

**Exemplo**:
```cmd
curl http://localhost:8080/transacao/recentes
```

---

### 4. GET /estatistica - Obter Estatísticas

**Descrição**: Calcula e retorna estatísticas (count, sum, average, min, max) das transações dos últimos 60 segundos.

**URL**: `http://localhost:8080/estatistica`

**Método**: `GET`

**Response** (200 OK):
```json
{
  "count": 2,
  "avg": 311.725,
  "max": 500.0,
  "min": 123.45,
  "sum": 623.45
}
```

**Campos**:
- `count`: Número de transações na janela
- `sum`: Soma total dos valores
- `avg`: Média aritmética dos valores
- `max`: Valor máximo
- `min`: Valor mínimo

**Exemplo**:
```cmd
curl http://localhost:8080/estatistica
```

**Response quando não há transações recentes**:
```json
{
  "count": 0,
  "avg": 0.0,
  "max": 0.0,
  "min": 0.0,
  "sum": 0.0
}
```

---

### 5. DELETE /transacao - Limpar Todas as Transações

**Descrição**: Remove todas as transações armazenadas (limpa a memória).

**URL**: `http://localhost:8080/transacao`

**Método**: `DELETE`

**Response** (200 OK):
```
(vazio - apenas status HTTP 200)
```

**Exemplo**:
```cmd
curl -X DELETE http://localhost:8080/transacao
```

---

## 📊 Fluxo de Dados

1. **Transação chega** via POST
2. **Validação**:
   - Valor não pode ser negativo
   - Timestamp não pode ser futuro (relativo ao servidor)
   - Valor e timestamp são obrigatórios
3. **Normalização** (opcional):
   - Se timestamp for nulo, futuro ou > 60s no passado → substitui por `now()`
4. **Armazenamento**:
   - Salva na lista thread-safe (CopyOnWriteArrayList)
5. **Limpeza periódica** (a cada 1 segundo):
   - Remove transações com timestamp < now - 60s
6. **Consulta**:
   - `/transacao` → todas as transações
   - `/transacao/recentes` → apenas últimos 60s
   - `/estatistica` → agregações dos últimos 60s

## 🔄 Comportamento de Timestamp

### Normalização Automática

Se o timestamp enviado for:
- **Nulo**: Substituído por `OffsetDateTime.now()` (horário do servidor)
- **Futuro**: Substituído por `OffsetDateTime.now()`
- **Anterior a 60s**: Substituído por `OffsetDateTime.now()`

Esse comportamento garante que transações recém-postadas sempre apareçam nas estatísticas (últimos 60s).

### Exemplo Prático

```bash
# 19:30:00 - Postar transação
curl -X POST http://localhost:8080/transacao \
  -d "{\"valor\":100,\"dataHora\":\"2026-05-19T19:29:50Z\"}"  # 10s atrás da janela
# Resultado: Timestamp normalizado para 19:30:00 (agora) e salvo

# 19:30:05 - Consultar estatística
curl http://localhost:8080/estatistica
# Resultado: count=1, sum=100, avg=100, max=100, min=100
```

## 📝 Logging

A aplicação usa **SLF4J + Logback** para logging. Exemplos de mensagens no console:

```
[TransacaoRepository] Saved: TransacaoDTO[valor=123.45, dataHora=2026-05-19T19:30:15.123456-03:00]
[TransacaoRepository] cleaned: removed 2 old transactions, remaining=1
[EstatisticaService] now=2026-05-19T19:30:20.123456-03:00 cutoff=2026-05-19T19:29:20.123456-03:00 total=1 recent=1
```

Configure o nível de log em `application.properties`:
```properties
logging.level.com.viniciusdev.desafio_itau=INFO
logging.level.root=WARN
```

## 🛠️ Estrutura do Projeto

```
desafio-itau/
├── src/
│   ├── main/
│   │   ├── java/com/viniciusdev/desafio_itau/
│   │   │   ├── DesafioItauApplication.java          # Classe principal com @EnableScheduling
│   │   │   ├── Transacao/
│   │   │   │   ├── controller/TransacaoController.java   # Endpoints
│   │   │   │   ├── service/TransacaoService.java        # Validações
│   │   │   │   ├── repository/TransacaoRepository.java   # Armazenamento + limpeza
│   │   │   │   ├── model/Transacao.java                 # Entity
│   │   │   │   └── dto/TransacaoDTO.java                # Record DTO
│   │   │   └── Estatistica/
│   │   │       ├── Controller/EstatisticaController.java # GET /estatistica
│   │   │       ├── Service/EstatisticaService.java       # Cálculos
│   │   │       ├── Model/EstatisticaModel.java          # Domain
│   │   │       └── dto/EstatisticaDTO.java              # Response DTO
│   │   └── resources/
│   │       └── application.properties               # Configurações
│   └── test/
│       └── java/...                                  # Testes unitários (opcional)
├── pom.xml                                           # Dependências Maven
├── mvnw / mvnw.cmd                                  # Wrappers Maven
├── .gitignore                                        # Arquivos ignorados
└── README.md                                          # Este arquivo
```

## 🧪 Exemplo de Teste Manual

**Passo 1**: Iniciar aplicação
```cmd
mvnw.cmd spring-boot:run
```

**Passo 2**: Limpar transações anteriores (opcional)
```cmd
curl -X DELETE http://localhost:8080/transacao
```

**Passo 3**: Postar 3 transações (PowerShell)
```powershell
# Transação 1
$ts1 = (Get-Date).AddSeconds(-1).ToString("o")
curl -X POST http://localhost:8080/transacao -H "Content-Type: application/json" -d "{\"valor\":100,\"dataHora\":\"$ts1\"}" -UseBasicParsing

# Transação 2 (esperar 1s)
Start-Sleep -Seconds 1
$ts2 = (Get-Date).AddSeconds(-1).ToString("o")
curl -X POST http://localhost:8080/transacao -H "Content-Type: application/json" -d "{\"valor\":200,\"dataHora\":\"$ts2\"}" -UseBasicParsing

# Transação 3
Start-Sleep -Seconds 1
$ts3 = (Get-Date).AddSeconds(-1).ToString("o")
curl -X POST http://localhost:8080/transacao -H "Content-Type: application/json" -d "{\"valor\":300,\"dataHora\":\"$ts3\"}" -UseBasicParsing
```

**Passo 4**: Verificar transações
```cmd
curl http://localhost:8080/transacao
curl http://localhost:8080/transacao/recentes
```

**Passo 5**: Obter estatísticas
```cmd
curl http://localhost:8080/estatistica
```

**Resultado esperado**:
```json
{
  "count": 3,
  "avg": 200.0,
  "max": 300.0,
  "min": 100.0,
  "sum": 600.0
}
```

## 🔐 Considerações de Segurança

- ⚠️ **Não há autenticação** - A aplicação é aberta. Em produção, adicione Spring Security.
- ⚠️ **Dados em memória** - Se a aplicação reiniciar, todos os dados são perdidos. Use banco de dados para persistência.
- ⚠️ **Sem HTTPS** - Configure SSL/TLS em produção.
- ⚠️ **Sem validação de origem** - Considere adicionar CORS se necessário.

## 📦 Build e Deploy

### Compilar JAR
```bash
mvnw clean package
```

### Executar JAR
```bash
java -jar target/desafio-itau-0.0.1-SNAPSHOT.jar
```

### Docker (opcional)

Crie um `Dockerfile`:
```dockerfile
FROM openjdk:11-jre-slim
COPY target/desafio-itau-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build e execute:
```bash
docker build -t desafio-itau:latest .
docker run -p 8080:8080 desafio-itau:latest
```

## 🐛 Troubleshooting

### Erro: "Port 8080 already in use"
Mude a porta em `application.properties`:
```properties
server.port=8081
```

### Transações não aparecem na estatística
1. Verifique se o POST retornou 201 (sucesso)
2. Confirme o timestamp está dentro dos últimos 60s
3. Chame GET /transacao/recentes para listar
4. Verifique os logs da aplicação

### Valores zerados na estatística
- Nenhuma transação recente foi armazenada
- Verifique se as transações foram postadas com `dataHora` válido
- Use GET /transacao e /transacao/recentes para debugar

## 📄 Licença

Projeto desenvolvido como desafio (Itau).

## ✍️ Autor

Desenvolvido por: Vinicius Rodrigues

## 📞 Suporte

Para dúvidas ou issues, abra uma issue no repositório GitHub.

---

**Última atualização**: Maio 2026

