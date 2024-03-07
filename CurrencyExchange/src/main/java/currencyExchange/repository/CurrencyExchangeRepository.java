package currencyExchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import currencyExchange.model.CurrencyExchangeModel;

public interface CurrencyExchangeRepository extends JpaRepository<CurrencyExchangeModel, Integer> {
	
	CurrencyExchangeModel getByFromAndTo(String from, String to);
}
