package cryptoExchange.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.CryptoExchangeDto;
import api.services.CryptoExchangeService;
import cryptoExchange.model.CryptoExchangeModel;
import cryptoExchange.repository.CryptoExchangeRepository;

@RestController
public class CryptoExchangeServiceImplementation implements CryptoExchangeService {
	
	@Autowired
	private CryptoExchangeRepository cryptoExchangeRepo;

	@Override
	public ResponseEntity<?> getExchange(String from, String to) {
		CryptoExchangeModel model = cryptoExchangeRepo.findByFromAndTo(from, to);
		if (model == null) {
			return ResponseEntity.status(404).body("Requested Exchange pair {" + from + " into " + to + "} could not be found");
		}
		return ResponseEntity.ok(convertModelToDto(model));
	}
	
	private CryptoExchangeDto convertModelToDto(CryptoExchangeModel model) {
		CryptoExchangeDto dto = new CryptoExchangeDto(model.getFrom(), model.getTo(), model.getExchangeValue());
		return dto;
	}

}
