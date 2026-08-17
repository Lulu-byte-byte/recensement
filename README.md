 Backend — API de Recensement (Spring Boot)

API REST exposant 4 endpoints pour gérer les ménages recensés et calculer
8 statistiques nationales.

## Stack

- Java 17, Spring Boot 3.3
- Spring Data JPA, Spring Web, Bean Validation
- PostgreSQL
- Lombok
- Springdoc OpenAPI (Swagger)

## Endpoints

| Méthode | Endpoint                    | Rôle                                  |
|---------|------------------------------|----------------------------------------|
| POST    | `/api/menages`               | Créer un ménage                        |
| GET     | `/api/menages`                | Lister tous les ménages                |
| DELETE  | `/api/menages/{id}`           | Supprimer un ménage                    |
| GET     | `/api/menages/statistiques`   | Récupérer les 8 statistiques           |

Documentation interactive une fois lancé : `http://localhost:8080/swagger-ui.html`

## 1. Prérequis

- JDK 17+ installé (`java -version`)
- Maven (ou utilise le wrapper `./mvnw` si tu en ajoutes un — sinon
  installe Maven : `sudo apt install maven` / `brew install maven`)
- Un compte [Render](https://render.com) (gratuit)
- [Postman](https://www.postman.com/) pour tester l'API

## 2. Créer la base de données PostgreSQL sur Render

1. Connecte-toi sur [dashboard.render.com](https://dashboard.render.com)
2. Clique sur **New +** → **PostgreSQL**
3. Donne un nom (ex : `recensement-db`), choisis la région, le plan **Free**
4. Clique sur **Create Database** et attends le statut **Available**
5. Dans la page de la base, note les informations de connexion, en particulier :
   - **Hostname**
   - **Port** (5432 par défaut)
   - **Database**
   - **Username**
   - **Password**
   - Ou directement l'**External Database URL** (au format
     `postgres://user:password@host:port/dbname`)

⚠️ Spring Boot attend une URL **JDBC**, pas l'URL brute de Render.
Construis-la ainsi à partir des informations ci-dessus :

```
jdbc:postgresql://<Hostname>:<Port>/<Database>
```

## 3. Configurer les variables d'environnement en local

Ne mets **jamais** d'identifiants en dur dans le code. Le fichier
`application.properties` lit déjà 3 variables d'environnement :

| Variable            | Valeur                                              |
|----------------------|-----------------------------------------------------|
| `DATABASE_URL`       | `jdbc:postgresql://<host>:<port>/<database>`         |
| `DATABASE_USERNAME`  | le username Render                                   |
| `DATABASE_PASSWORD`  | le password Render                                   |

### Sous Linux / macOS (terminal)

```bash
export DATABASE_URL="jdbc:postgresql://<host>:5432/<database>"
export DATABASE_USERNAME="<username>"
export DATABASE_PASSWORD="<password>"
```

### Sous Windows (PowerShell)

```powershell
$env:DATABASE_URL="jdbc:postgresql://<host>:5432/<database>"
$env:DATABASE_USERNAME="<username>"
$env:DATABASE_PASSWORD="<password>"
```

### Dans IntelliJ IDEA / Eclipse

Ouvre la configuration d'exécution (Run Configuration) de
`RecensementApplication` → onglet **Environment variables** → ajoute
les 3 variables ci-dessus.

## 4. Lancer l'application en local

```bash
cd backend
mvn spring-boot:run
```

L'application démarre sur `http://localhost:8080`. Les tables sont
créées automatiquement grâce à `spring.jpa.hibernate.ddl-auto=update`.

## 5. Tester avec Postman

Importe les 4 requêtes suivantes dans Postman (ou crée une collection) :

- `POST http://localhost:8080/api/menages`
  ```json
  {
    "chefMenage": "Jean Mballa",
    "zone": "Yaoundé - Mfoundi",
    "nombrePersonnes": 5,
    "ageMoyen": 28,
    "typeLogement": "Maison"
  }
  ```
- `GET http://localhost:8080/api/menages`
- `DELETE http://localhost:8080/api/menages/1`
- `GET http://localhost:8080/api/menages/statistiques`

Vérifie aussi les cas d'erreur (champ manquant, valeurs négatives,
suppression d'un id inexistant → doit renvoyer 404 avec un message clair).

## 6. Déployer le backend sur Render

Deux options : avec Docker (recommandée, fiable) ou en natif.

### Option A — avec Docker (recommandée)

Le `Dockerfile` fourni à la racine de `backend/` construit et lance
l'application automatiquement, sans configuration supplémentaire.

1. Pousse d'abord le code sur GitHub (voir section 7 ci-dessous)
2. Sur Render : **New +** → **Web Service**
3. Connecte ton repository GitHub
4. Render détecte le `Dockerfile` → choisis **Docker** comme environnement
5. Renseigne le **Root Directory** : `backend`
6. Dans **Environment Variables**, ajoute les 3 variables (mêmes noms
   qu'en local) avec les vraies valeurs de connexion à ta base Render
7. Clique sur **Create Web Service**
8. Une fois déployé, récupère l'URL publique (ex :
   `https://recensement-backend.onrender.com`)

### Option B — sans Docker (build natif)

1. **New +** → **Web Service**, connecte le repo, Root Directory : `backend`
2. **Environment** : `Java`
3. **Build Command** : `mvn clean package -DskipTests`
4. **Start Command** : `java -jar target/recensement-1.0.0.jar`
5. Ajoute les mêmes 3 variables d'environnement
6. Déploie et récupère l'URL publique

## 7. Publier le code sur GitHub

Depuis la racine du dossier `backend/` (ou du projet complet) :

```bash
git init
git add .
git commit -m "Backend recensement - API Spring Boot"
git branch -M main
git remote add origin https://github.com/<ton-utilisateur>/<ton-repo>.git
git push -u origin main
```

Le fichier `.gitignore` fourni exclut déjà `target/`, les fichiers IDE
et les fichiers `.env` — tes identifiants ne seront jamais versionnés.

## 8. Re-tester après déploiement

Refais tous les tests Postman, cette fois sur l'URL publique Render
(`https://recensement-backend.onrender.com/api/menages`, etc.) avant de
passer au frontend.

> Note : sur le plan gratuit de Render, le service se met en veille
> après quelques minutes d'inactivité. Le premier appel après une
> période d'inactivité peut prendre 30 à 60 secondes.
