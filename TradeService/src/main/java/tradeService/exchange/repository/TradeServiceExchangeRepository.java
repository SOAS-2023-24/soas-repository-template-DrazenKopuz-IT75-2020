package tradeService.exchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tradeService.exchange.model.TradeServiceExchangeModel;

public interface TradeServiceExchangeRepository extends JpaRepository<TradeServiceExchangeModel, Integer> {
	
	TradeServiceExchangeModel findByFromAndTo(String from, String to);

}
