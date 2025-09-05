insert into crypto_wallet(id, email, btc_amount, eth_amount, xrp_amount)
values (nextval('CRYPTO_WALLET_SEQ'), 'user@uns.ac.rs', 3, 5, 1000000),
	   (nextval('CRYPTO_WALLET_SEQ'), 'admin@uns.ac.rs', 20, 10, 1000000),
       (nextval('CRYPTO_WALLET_SEQ'), 'kopuz.it75.2020@uns.ac.rs', 10, 20, 123400),
       (nextval('CRYPTO_WALLET_SEQ'), 'kukricar.it65.2020@uns.ac.rs', 10, 30, 1000000);