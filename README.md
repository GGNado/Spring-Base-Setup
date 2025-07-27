# BaseSetup

**BaseSetup** è una repository di base per progetti Java Spring Boot, pensata per essere riutilizzata come starter backend con autenticazione JWT, gestione utenti e ruoli, sicurezza avanzata e API documentate.

---

## Indice

- [Caratteristiche](#caratteristiche)
- [Architettura](#architettura)
- [Flusso di autenticazione JWT](#flusso-di-autenticazione-jwt)
- [Gestione utenti e ruoli](#gestione-utenti-e-ruoli)
- [Sicurezza e CORS](#sicurezza-e-cors)
- [Endpoints principali](#endpoints-principali)
- [Configurazione](#configurazione)
- [Dipendenze](#dipendenze)
- [Estensioni e personalizzazioni](#estensioni-e-personalizzazioni)
- [Avvio rapido](#avvio-rapido)
- [Documentazione API](#documentazione-api)
- [Struttura del progetto](#struttura-del-progetto)

---

## Caratteristiche

- **Spring Boot 3.5+** con Maven
- **Autenticazione JWT** stateless
- **Gestione utenti e ruoli** (USER, ADMIN, MODERATOR)
- **Registrazione, login, refresh token**
- **Controllo accessi per endpoint**
- **CORS configurato**
- **Swagger/OpenAPI** per la documentazione API
- **Password cifrate con BCrypt**
- **Entity e DTO mappati con MapStruct**
- **Gestione errori strutturata (JSON)**
- **Test di base inclusi**

---

## Architettura

- **Controller**: gestiscono le richieste REST (autenticazione, utenti)
- **Service**: logica di business (registrazione, login, CRUD utenti)
- **Repository**: accesso ai dati tramite JPA
- **Security**: configurazione Spring Security, filtri JWT, gestione ruoli
- **DTO/Mapper**: conversione tra entity e oggetti di trasferimento dati
- **Entity**: modelli JPA per utenti e ruoli

---

## Flusso di autenticazione JWT

1. **Registrazione (`POST /api/auth/signup`)**
    - L'utente invia username, email, password, ruoli.
    - Il sistema verifica unicità username/email.
    - Password cifrata con BCrypt.
    - Ruolo di default: `ROLE_USER` se non specificato.
    - Utente salvato nel DB.

2. **Login (`POST /api/auth/signin`)**
    - L'utente invia username/email e password.
    - Autenticazione tramite `AuthenticationManager`.
    - Se le credenziali sono corrette, viene generato un JWT con info utente e ruoli.
    - Il token viene restituito al frontend.

3. **Utilizzo del token**
    - Il JWT va inviato nell'header `Authorization: Bearer <token>` per ogni richiesta protetta.
    - Il filtro `AuthTokenFilter` intercetta la richiesta, valida il token e imposta l'autenticazione.
    - Se il token è valido, l'utente accede agli endpoint protetti in base ai ruoli.
    - Se il token è scaduto/non valido, viene restituito errore 401 con JSON dettagliato.

4. **Refresh token**
    - Endpoint dedicato per rigenerare il token JWT se necessario.

---

## Gestione utenti e ruoli

- **Ruoli disponibili**: `ROLE_USER`, `ROLE_ADMIN`, `ROLE_MODERATOR`
- **Assegnazione ruoli**: in fase di registrazione, oppure tramite API/admin
- **Controllo accessi**: configurato in `SecurityConfig` tramite regole granulari
- **Entity Utente**: include flag di abilitazione, scadenza, lock, ruoli (ManyToMany)
- **UserDetailsService**: carica utente da DB per autenticazione

---

## Sicurezza e CORS

- **Stateless**: nessuna sessione, solo JWT
- **CORS**: configurato per frontend (localhost, dominio custom)
- **CSRF**: disabilitato per API REST
- **Gestione errori**: risposta JSON dettagliata su errori di autenticazione/autorizzazione

---

## Endpoints principali

| Metodo | Endpoint                | Descrizione                        | Accesso         |
|--------|-------------------------|------------------------------------|-----------------|
| POST   | `/api/auth/signup`      | Registrazione utente               | Pubblico        |
| POST   | `/api/auth/signin`      | Login utente (JWT)                 | Pubblico        |
| GET    | `/api/utentes`          | Lista utenti                       | Solo USER       |
| GET    | `/api/users`            | Lista utenti                       | Solo ADMIN      |
| POST   | `/api/users`            | Crea utente                        | Solo ADMIN      |
| DELETE | `/api/users/{id}`       | Elimina utente                     | Solo ADMIN      |
| GET    | `/actuator/health`      | Health check                       | Pubblico        |
| GET    | `/swagger-ui.html`      | Documentazione API                 | Pubblico        |

---

## Configurazione

1. **Database**: configura le credenziali in `src/main/resources/application.properties`
2. **Ruoli**: assicurati che i ruoli siano presenti nel DB (`ROLE_USER`, `ROLE_ADMIN`, `ROLE_MODERATOR`)
3. **CORS**: aggiorna gli origin consentiti in `SecurityConfig` per la produzione
4. **JWT**: configura secret e scadenza in `application.properties`

---

## Dipendenze

- `spring-boot-starter-web`
- `spring-boot-starter-security`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-validation`
- `spring-boot-starter-actuator`
- `mysql-connector-j`
- `jjwt` (JSON Web Token)
- `lombok`, `mapstruct`
- `springdoc-openapi`
- `bcrypt`
- Test: `spring-boot-starter-test`, `spring-security-test`

---

## Estensioni e personalizzazioni

- **Aggiunta di nuovi ruoli**: estendi l'entity `Role` e aggiorna la logica di assegnazione
- **Gestione profili utente**: aggiungi campi custom all'entity `Utente`
- **Refresh token**: implementa endpoint e logica per rinnovo token
- **Gestione password dimenticata**: aggiungi endpoint per reset password
- **Audit log**: integra log delle operazioni utente
- **Rate limiting**: aggiungi protezione contro brute force

---

## Avvio rapido

1. Clona la repo
2. Configura il database in `application.properties`
3. Avvia l'applicazione con `mvn spring-boot:run`
4. Accedi a Swagger su `/swagger-ui.html` per testare le API

---

## Documentazione API

- **Swagger/OpenAPI**: tutte le API sono documentate e testabili via interfaccia web
- **Response standard**: tutte le risposte di errore sono in formato JSON strutturato

---

## Struttura del progetto

```
src/
 └── main/
     ├── java/com/giggi/basesetup/
     │    ├── controller/      # REST API
     │    ├── entity/          # Modelli JPA
     │    ├── security/        # JWT, filtri, config
     │    ├── service/         # Business logic
     │    ├── repository/      # Accesso dati
     │    ├── mapper/          # MapStruct DTO <-> Entity
     └── resources/
          └── application.properties
```

---

**Questa base ti permette di partire subito con un backend sicuro, scalabile e facilmente estendibile, pronto per essere integrato con qualsiasi frontend moderno!**