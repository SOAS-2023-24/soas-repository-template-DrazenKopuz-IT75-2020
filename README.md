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

- ### Get User by email

- **URL:** `http://localhost:8765/users/user`
- **Method:** `GET`

- ### Create a New User

- **URL:** `http://localhost:8765/users/newUser`
- **Method:** `POST`
- **Headers:** `Authorization` required.

- ### Create a New Admin

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

- ## CURRENCY EXCHANGE SERVICE

### Get Exchange Rate

- **URL:** `http://localhost:8765/currency-exchange`
- **Method:** `GET`
- **Parameters:**
  - `from` (query parameter) - The currency code of the source currency.
  - `to` (query parameter) - The currency code of the target currency.
- **Response:** Returns the exchange rate from the `from` currency to the `to` currency.


- ## BANK ACCOUNT SERVICE

### Get All Bank Accounts

- **URL:** `http://localhost:8765/bank-account`
- **Method:** `GET`
- **Response:** Returns a list of all bank accounts.

### Get Bank Account by Email

- **URL:** `http://localhost:8765/bank-account/{email}`
- **Method:** `GET`
- **Path Parameters:**
  - `email` - The email of the user whose bank account details are to be fetched.
- **Response:** Returns the bank account details for the specified email.

### Create a New Bank Account

- **URL:** `http://localhost:8765/bank-account`
- **Method:** `POST`
- **Headers:** Authorization required.
- **Request Body:** BankAccountDto - The details of the bank account to be created.
- **Response:** Returns the response for the bank account creation request.

### Update Bank Account

- **URL:** `http://localhost:8765/bank-account/{email}`
- **Method:** `PUT`
- **Path Parameters:**
  - `email` - The email of the bank account to be updated.
- **Headers:** Authorization required.
- **Request Body:** BankAccountDto - The updated bank account details.
- **Response:** Returns the response for the bank account update request.

### Delete Bank Account

- **URL:** `http://localhost:8765/bank-account/{email}`
- **Method:** `DELETE`
- **Path Parameters:**
  - `email` - The email of the bank account to be deleted.
- **Response:** Deletes the bank account for the specified email.

### Update Bank Account Balances

- **URL:** `http://localhost:8765/bank-account`
- **Method:** `PUT`
- **Request Parameters:**
  - `email` - The email of the bank account to be updated.
  - `from` - The currency code of the source currency.
  - `to` - The currency code of the target currency.
  - `quantity` - The quantity of currency to be updated.
  - `quantityConverted` - The total amount to be updated.
- **Response:** Returns the response for the bank account balance update request.
