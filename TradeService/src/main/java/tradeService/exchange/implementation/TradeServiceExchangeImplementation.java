package tradeService.exchange.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.TradeServiceExchangeDto;
import api.services.TradeServiceExchangeService;
import tradeService.exchange.model.TradeServiceExchangeModel;
import tradeService.exchange.repository.TradeServiceExchangeRepository;

@RestController
public class TradeServiceExchangeImplementation implements TradeServiceExchangeService {
	
	@Autowired
	private TradeServiceExchangeRepository repo;

	@Override
	public ResponseEntity<?> getExchange(String from, String to) {
		TradeServiceExchangeModel model = repo.findByFromAndTo(from, to);
		if (model == null) {
			return ResponseEntity.status(404).body("Requested Exchange pair {" + from + " into " + to + "} could not be found");
		}
		return ResponseEntity.ok(convertModelToDto(model));
	}
	
	private TradeServiceExchangeDto convertModelToDto(TradeServiceExchangeModel model) {
		TradeServiceExchangeDto dto = new TradeServiceExchangeDto(model.getFrom(), model.getTo(), model.getExchangeValue());
		return dto;
	}
	
}
