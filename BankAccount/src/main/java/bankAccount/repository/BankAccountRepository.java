package bankAccount.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bankAccount.model.BankAccountModel;

public interface BankAccountRepository extends JpaRepository<BankAccountModel, Integer> {
	
	BankAccountModel findByEmail(String email);

}
