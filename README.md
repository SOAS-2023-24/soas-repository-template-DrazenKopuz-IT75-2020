# Aplikacija za razmenu običnih (fiat) i crypto valuta

Ova aplikacija omogućava razmenu fiat i kripto valuta koristeći mikroservisnu arhitekturu. Aplikacija je implementirana koristeći Java programski jezik, Maven za upravljanje zavisnostima, Docker za kontejnerizaciju, i H2 kao in-memory bazu podataka.

## KORISNICI I KREDENCIJALI

- **admin@uns.ac.rs** - Admin korisnik sa lozinkom 'password' i rolom 'ADMIN'.
- **user@uns.ac.rs** - Obični korisnik sa lozinkom 'password' i rolom 'USER'.
- **owner@uns.ac.rs** - Vlasnik sa lozinkom 'password' i rolom 'OWNER'.


- ## USERS SERVICE
- 
- ### Get All Users

- **URL:** `http://localhost:8765/users`
- **Method:** `GET`

- - ### Get User by email

- **URL:** `http://localhost:8765/users/user`
- **Method:** `GET`

- ### Create a New User

- **URL:** `http://localhost:8765/users/newUser`
- **Method:** `POST`
- **Headers:** `Authorization` required.

- - ### Create a New Admin

- **URL:** `http://localhost:8765/users/newAdmin`
- **Method:** `POST`
- **Headers:** `Authorization` required.

- ### Update a User

- **URL:** `http://localhost:8765/users`
- **Method:** `PUT`
- **Headers:** `Authorization` required.

- ### Delete a User

- **URL:** `http://localhost:8765/users`
- **Method:** `DELETE`
- **Headers:** `Authorization` required.
