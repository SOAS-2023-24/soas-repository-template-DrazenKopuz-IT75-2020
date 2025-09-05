package cryptoWallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cryptoWallet.model.CryptoWalletModel;

public interface CryptoWalletRepository extends JpaRepository<CryptoWalletModel, Integer> {
	
	CryptoWalletModel findByEmail(String email);

}
