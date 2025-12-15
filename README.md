# BaseSetup Spring Security JWT

**BaseSetup** è una repository di base per progetti Java Spring Boot, pensata per essere riutilizzata come starter backend robusto, scalabile e sicuro. Include autenticazione JWT, gestione utenti e ruoli, sicurezza avanzata, mapping automatico con MapStruct e documentazione API integrata.

## 📋 Indice

- [Prerequisiti e Tecnologie](#-prerequisiti-e-tecnologie)
- [Struttura del Progetto](#-struttura-del-progetto)
- [Caratteristiche Principali](#-caratteristiche-principali)
- [Architettura di Sicurezza](#-architettura-di-sicurezza)
- [Gestione Dati e Mapping](#-gestione-dati-e-mapping)
- [Configurazione Dettagliata](#-configurazione-dettagliata)
- [API Endpoints e Swagger](#-api-endpoints-e-swagger)
- [Guida all'Avvio](#-guida-allavvio)
- [Troubleshooting](#-troubleshooting)

---

## 🛠 Prerequisiti e Tecnologie

Questo progetto utilizza tecnologie all'avanguardia per garantire performance e manutenibilità.

### Requisiti di Sistema
- **Java Development Kit (JDK) 21**: Assicurati di avere l'ultima versione di Java installata.
- **Maven 3.8+**: Per la gestione delle dipendenze e il build.
- **MySQL 8.0+**: Database relazionale per la persistenza dei dati.

### Stack Tecnologico
| Tecnologia | Versione | Descrizione |
|------------|----------|-------------|
| **Spring Boot** | 3.5.4 | Framework principale |
| **Spring Security** | 6.x | Gestione autenticazione e autorizzazione |
| **Spring Data JPA** | - | ORM e accesso ai dati |
| **JWT (JJWT)** | 0.12.2 | Token standard per autenticazione stateless |
| **MapStruct** | 1.6.3 | Mapping performante tra Entity e DTO |
| **Lombok** | Latest | Riduzione del codice boilerplate |
| **SpringDoc OpenAPI** | 2.8.5 | Documentazione automatica Swagger UI |
| **BCrypt** | - | Hashing sicuro delle password (via Spring Security) |

---

## 📂 Struttura del Progetto

Il progetto segue una clean architecture per separare le responsabilità e facilitare la manutenzione.

```text
src/main/java/com/giggi/basesetup
├── 📁 controller       # Gestione delle richieste HTTP (REST API)
│   ├── AuthController.java    # Login, Registrazione, Refresh Token
│   └── UtenteController.java  # CRUD Utenti
├── 📁 dto              # Data Transfer Objects (Input/Output API)
│   ├── 📁 request      # DTO per richieste in ingresso (es. LoginRequest)
│   └── 📁 response     # DTO per risposte al client (es. JwtResponse)
├── 📁 entity           # Modelli di persistenza (JPA Entities)
│   ├── Utente.java     # Tabella utenti
│   ├── Role.java       # Tabella ruoli
│   └── RoleName.java   # Enum dei ruoli disponibili
├── 📁 mapper           # Interfacce MapStruct per conversione Entity <-> DTO
├── 📁 repository       # Interfacce Spring Data JPA per accesso al DB
├── 📁 security         # Configurazione Core della sicurezza
│   ├── SecurityConfig.java    # Configurazione FilterChain e Beans
│   ├── 📁 jwt                 # Logica JWT (Generazione, Validazione, Filtri)
│   └── 📁 service             # Implementazione UserDetailsService
├── 📁 service          # Business Logic
│   ├── 📁 impl         # Implementazione dei servizi
│   └── [Interfaces]    # Interfacce dei servizi
└── 📁 swagger          # Configurazione OpenApi/Swagger
```

---

## 🚀 Caratteristiche Principali

- **Autenticazione Stateless**: Utilizzo di JSON Web Tokens (JWT) per scalabilità orizzontale.
- **RBAC (Role-Based Access Control)**: Sistema gerarchico di permessi (USER, ADMIN, MODERATOR).
- **Mapping Automatico**: Conversione pulita ed efficiente tra Entità e DTO grazie a MapStruct.
- **Validazione Dati**: Controlli su input (es. email valida, password robusta) tramite Bean Validation.
- **Sicurezza Avanzata**:
    - Password Hashing con **BCrypt**.
    - Protezione CORS configurabile.
    - Gestione eccezioni di sicurezza centralizzata.
- **Documentazione Live**: Interfaccia Swagger UI per testare le API direttamente dal browser.
- **Actuator**: Endpoint per il monitoraggio dello stato dell'applicazione.

---

## 🔐 Architettura di Sicurezza

### Il Flusso di Autenticazione (JWT)

1. **Login**: L'utente invia credenziali a `/api/auth/signin`.
2. **Verifica**: `AuthenticationManager` valida username e password (confrontando l'hash BCrypt).
3. **Generazione Token**: Se valido, viene generato un JWT firmato contenente:
    - **Subject**: Username
    - **Claims**: ID, Email, Ruoli
    - **Scadenza**: Configurale (default 16 ore)
    - **Firma**: HMAC-SHA256
4. **Utilizzo**: Il client invia il token nell'header `Authorization: Bearer <token>` per ogni richiesta successiva.
5. **Filtro**: `AuthTokenFilter` intercetta la richiesta, valida il token e imposta il contesto di sicurezza.

### Gestione dei Ruoli

I ruoli sono definiti nell'enum `RoleName` e persistiti nel database.
- **ROLE_USER**: Accesso base.
- **ROLE_MODERATOR**: Accesso a funzionalità di moderazione.
- **ROLE_ADMIN**: Accesso completo, inclusa gestione utenti (CRUD).

L'assegnazione avviene in fase di registrazione o tramite API di amministrazione.

---

## 🔄 Gestione Dati e Mapping

Per mantenere pulito il codice, utilizziamo **MapStruct**. Questo evita di esporre le Entità JPA direttamente nelle API.

**Esempio di Flusso:**
1. **Controller** riceve `UtenteCreateRequestDTO`.
2. **Service** chiama `UtenteMapper` per convertirlo in `Utente` (Entity).
3. **Repository** salva l'Entity nel DB.
4. **Service** riceve l'Entity salvata e usa `UtenteMapper` per convertirla in `UtenteFindDTO`.
5. **Controller** restituisce il DTO.

Questo approccio garantisce che password e dati sensibili non vengano mai esposti accidentalmente nelle risposte JSON.

---

## ⚙️ Configurazione Dettagliata

Il file `src/main/resources/application.properties` è il cuore della configurazione.

### Configurazione Server & DB
```properties
spring.application.name=BaseSetup
server.port=8080                    # Porta del server
server.address=0.0.0.0              # Ascolto su tutte le interfacce

# Connessione Database
spring.datasource.url=jdbc:mysql://localhost:3306/YOUR_DATABASE_NAME
spring.datasource.username=root
spring.datasource.password=rootroot

# Configurazione JPA/Hibernate
spring.jpa.show-sql=true            # Mostra le query SQL nei log (utile in dev)
spring.jpa.hibernate.ddl-auto=update # Aggiorna lo schema DB automaticamente
```

### Sicurezza & JWT
```properties
# Chiave segreta per la firma dei token (deve essere lunga e complessa)
spring.app.jwtSecret=mySecretKey123912738aopsgjnspkmndfsopkvajoirjg94gf2opfng2moknm

# Durata del token in millisecondi (es. 57600000 ms = 16 ore)
spring.app.jwtExpirationMs=57600000
```

### Upload File (Multipart)
```properties
# Limiti per l'upload di file
spring.servlet.multipart.max-file-size=200MB
spring.servlet.multipart.max-request-size=200MB
```

### Logging
Il livello di log è configurato per facilitare il debug della sicurezza:
```properties
logging.level.org.springframework.security=DEBUG
logging.level.io.jsonwebtoken=DEBUG
```

---

## 📡 API Endpoints e Swagger

Una volta avviata l'applicazione, la documentazione interattiva è disponibile a:
👉 **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

### Principali Endpoint

| Metodo | Path | Descrizione | Autenticazione |
|--------|------|-------------|----------------|
| `POST` | `/api/auth/signup` | Registrazione nuovo utente | 🔓 Pubblico |
| `POST` | `/api/auth/signin` | Login (restituisce JWT) | 🔓 Pubblico |
| `GET`  | `/api/users` | Lista di tutti gli utenti | 🔒 Admin |
| `GET`  | `/api/users/{id}` | Dettaglio singolo utente | 🔒 Autenticato |
| `PUT`  | `/api/users/{id}` | Modifica utente | 🔒 Autenticato |
| `DELETE`| `/api/users/{id}`| Eliminazione utente | 🔒 Admin |

---

## 🏃‍♂️ Guida all'Avvio

Segui questi passaggi per avviare il progetto in locale.

### 1. Clona la Repository
```bash
git clone <url-repository>
cd BaseSetup
```

### 2. Configura il Database
Crea un database MySQL vuoto (es. `basesetup_db`) e aggiorna `application.properties` con le tue credenziali.

### 3. Build & Run
Puoi avviare l'applicazione usando il wrapper Maven incluso:

**Linux/Mac:**
```bash
./mvnw spring-boot:run
```

**Windows:**
```bash
mvnw.cmd spring-boot:run
```

### 4. Popolazione Iniziale (Opzionale)
Al primo avvio, Hibernate creerà le tabelle. Potrebbe essere necessario inserire manualmente i ruoli se non previsti da uno script di migrazione (es. Flyway) o da un `CommandLineRunner`:
```sql
INSERT INTO roles (name, description) VALUES ('ROLE_USER', 'Standard User');
INSERT INTO roles (name, description) VALUES ('ROLE_ADMIN', 'Administrator');
INSERT INTO roles (name, description) VALUES ('ROLE_MODERATOR', 'Moderator');
```

---

## ❓ Troubleshooting

**Problema: "Java version mismatch"**
Assicurati di avere installato Java 21 e che la variabile `JAVA_HOME` punti alla versione corretta. Se usi una versione precedente, aggiorna il tag `<java.version>` nel `pom.xml`.

**Problema: "Access Denied" (403 Forbidden)**
- Hai incluso il token nell'header? `Authorization: Bearer <tuo_token>`
- Il token è scaduto? Controlla il campo `exp` del JWT (puoi decodificarlo su [jwt.io](https://jwt.io)).
- Hai il ruolo corretto? Alcuni endpoint richiedono `ROLE_ADMIN`.

**Problema: Connessione Database fallita**
- Verifica che il servizio MySQL sia attivo.
- Controlla username, password e URL nel file `application.properties`.
- Verifica che il database specificato nell'URL esista.

---

**Happy Coding!** 🚀
*Per domande o contributi, apri una Issue o una Pull Request.*
