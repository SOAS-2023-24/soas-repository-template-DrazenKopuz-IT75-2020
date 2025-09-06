# Aplikacija za razmenu običnih (fiat) i crypto valuta

Ova aplikacija omogućava razmenu fiat i kripto valuta koristeći mikroservisnu arhitekturu. Aplikacija je implementirana koristeći Java programski jezik, Maven za upravljanje zavisnostima, Docker za kontejnerizaciju, i H2 kao in-memory bazu podataka.

## KORISNICI I KREDENCIJALI

- **admin@uns.ac.rs** - Admin korisnik sa lozinkom 'password' i rolom 'ADMIN'.
- **user@uns.ac.rs** - Obični korisnik sa lozinkom 'password' i rolom 'USER'.
- **owner@uns.ac.rs** - Vlasnik sa lozinkom 'password' i rolom 'OWNER'.


- ## USERS SERVICE
  
- ### Get All Users

- **URL:** `http://localhost:8765/users`
- **Method:** `GET`

- ### Get User by email

- **URL:** `http://localhost:8765/users/user`
- **Method:** `GET`
- **Path Variables:**
  - `email` - The email address associated with the crypto wallet.

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

- ### Get Exchange Rate

- **URL:** `http://localhost:8765/currency-exchange`
- **Method:** `GET`
- **Parameters:**
  - `from` (query parameter) - The currency code of the source currency.
  - `to` (query parameter) - The currency code of the target currency.
- **Response:** Returns the exchange rate from the `from` currency to the `to` currency.


- ## BANK ACCOUNT SERVICE

- ### Get All Bank Accounts

- **URL:** `http://localhost:8765/bank-account`
- **Method:** `GET`
- **Response:** Returns a list of all bank accounts.

- ### Get Bank Account by Email

- **URL:** `http://localhost:8765/bank-account/{email}`
- **Method:** `GET`
- **Path Parameters:**
  - `email` - The email of the user whose bank account details are to be fetched.
- **Response:** Returns the bank account details for the specified email.

- ### Create a New Bank Account

- **URL:** `http://localhost:8765/bank-account`
- **Method:** `POST`
- **Headers:** Authorization required.
- **Request Body:** BankAccountDto - The details of the bank account to be created.
- **Response:** Returns the response for the bank account creation request.

- ### Update Bank Account

- **URL:** `http://localhost:8765/bank-account/{email}`
- **Method:** `PUT`
- **Path Parameters:**
  - `email` - The email of the bank account to be updated.
- **Headers:** Authorization required.
- **Request Body:** BankAccountDto - The updated bank account details.
- **Response:** Returns the response for the bank account update request.

- ### Delete Bank Account

- **URL:** `http://localhost:8765/bank-account/{email}`
- **Method:** `DELETE`
- **Path Parameters:**
  - `email` - The email of the bank account to be deleted.
- **Response:** Deletes the bank account for the specified email.

- ### Update Bank Account Balances

- **URL:** `http://localhost:8765/bank-account`
- **Method:** `PUT`
- **Request Parameters:**
  - `email` - The email of the bank account to be updated.
  - `from` - The currency code of the source currency.
  - `to` - The currency code of the target currency.
  - `quantity` - The quantity of currency to be updated.
  - `quantityConverted` - The total amount to be updated.
- **Response:** Returns the response for the bank account balance update request.


- ## CURRENCY CONVERSION SERVICE

- ### Get Currency Conversion

- **URL:** `http://localhost:8765/currency-conversion`
- **Method:** `GET`
- **Request Parameters:**
  - `from` - The currency code of the source currency.
  - `to` - The currency code of the target currency.
  - `quantity` - The amount of source currency to be converted.
- **Response:** Returns the currency conversion details based on the provided parameters without authorization.

- ### Get Currency Conversion with Feign

- **URL:** `http://localhost:8765/currency-conversion-feign`
- **Method:** `GET`
- **Request Parameters:**
  - `from` - The currency code of the source currency.
  - `to` - The currency code of the target currency.
  - `quantity` - The amount of source currency to be converted.
- **Headers:** Authorization required.
- **Response:** Returns the currency conversion details based on the provided parameters and authorization.


- ## CRYPTO WALLET SERVICE

- ### Get All Crypto Wallets

- **URL:** `http://localhost:8765/crypto-wallet`
- **Method:** `GET`
- **Response:** Returns a list of all crypto wallets.

### Get Crypto Wallet by Email

- **URL:** `http://localhost:8765/crypto-wallet/{email}`
- **Method:** `GET`
- **Path Variables:**
  - `email` - The email address associated with the crypto wallet.
- **Response:** Returns the crypto wallet details for the specified email.

### Delete Crypto Wallet

- **URL:** `http://localhost:8765/crypto-wallet/{email}`
- **Method:** `DELETE`
- **Path Variables:**
  - `email` - The email address associated with the crypto wallet.
- **Response:** Deletes the crypto wallet for the specified email.

### Create Crypto Wallet

- **URL:** `http://localhost:8765/crypto-wallet`
- **Method:** `POST`
- **Headers:** Authorization required.
- **Request Body:** `CryptoWalletDto` - Details of the crypto wallet to be created.
- **Response:** Creates a new crypto wallet and returns the result.

### Update Crypto Wallet

- **URL:** `http://localhost:8765/crypto-wallet/{email}`
- **Method:** `PUT`
- **Path Variables:**
  - `email` - The email address associated with the crypto wallet.
- **Headers:** Authorization required.
- **Request Body:** `CryptoWalletDto` - Updated details of the crypto wallet.
- **Response:** Updates the crypto wallet for the specified email and returns the result.

### Get User's Crypto Wallet

- **URL:** `http://localhost:8765/crypto-wallet/user`
- **Method:** `GET`
- **Path Variables:**
  - `email` - The email address associated with the crypto wallet.
- **Headers:** Authorization required.
- **Response:** Returns the crypto wallet details for the currently authenticated user.

### Exchange Crypto

- **URL:** `http://localhost:8765/crypto-wallet`
- **Method:** `PUT`
- **Request Parameters:**
  - `email` - The email address associated with the crypto wallet.
  - `from` - The crypto currency code to be updated.
  - `to` - The crypto currency code to be updated.
  - `quantity` - The quantity of the crypto currency to be updated.
  - `quantityConverted` - The total amount to be updated.
- **Response:** Exchange crypto in Crypto wallet.


## CRYPTO EXCHANGE SERVICE

### Get Crypto Exchange Rate

- **URL:** `http://localhost:8765/crypto-exchange`
- **Method:** `GET`
- **Request Parameters:**
  - `from` - The crypto currency code to exchange from.
  - `to` - The crypto currency code to exchange to.
- **Response:** Returns the exchange rate between the specified crypto currencies.

## CRYPTO CONVERSION SERVICE

### Get Crypto Conversion

- **URL:** `http://localhost:8765/crypto-conversion`
- **Method:** `GET`
- **Request Parameters:**
  - `from` - The crypto currency code to convert from.
  - `to` - The crypto currency code to convert to.
  - `quantity` - The amount of the `from` currency to convert.
- **Headers:**
  - `Authorization` - Required header for authorization.
- **Response:** Returns the result of the conversion from the specified crypto currency to another, including the converted amount.


## TRADE SERVICE

### Trade service

- **URL:** `http://localhost:8765/trade-service`
- **Method:** `GET`
- **Request Parameters:**
  - `from` - The currency or crypto code to trade from.
  - `to` - The currency or crypto code to trade to.
  - `quantity` - The amount of the `from` currency or crypto to trade.
- **Headers:**
  - `Authorization` - Required header for authorization.
- **Response:** Returns the result of the trade operation, including the traded amount and possibly additional details about the transaction.

- ### Trade service Exchange

- **URL:** `http://localhost:8765/trade-service`
- **Method:** `GET`
- **Request Parameters:**
  - `from` - The currency or crypto code to trade from.
  - `to` - The currency or crypto code to trade to.
- **Response:** Returns the result of the trade operation, including the traded amount and possibly additional details about the transaction.
