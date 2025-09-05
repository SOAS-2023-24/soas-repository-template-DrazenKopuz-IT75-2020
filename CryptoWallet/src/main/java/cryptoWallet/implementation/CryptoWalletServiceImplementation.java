package cryptoWallet.implementation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.decoder.HeaderDecoder;
import api.dtos.CryptoWalletDto;
import api.dtos.UserDto;
import api.feignProxies.UsersServiceProxy;
import api.services.CryptoWalletService;
import cryptoWallet.model.CryptoWalletModel;
import cryptoWallet.repository.CryptoWalletRepository;

@RestController
public class CryptoWalletServiceImplementation implements CryptoWalletService {
	
	@Autowired CryptoWalletRepository repo;
	
	@Autowired UsersServiceProxy usersProxy;
	
	@Autowired HeaderDecoder headerDecoder;

	@Override
	public ResponseEntity<?> getCryptoWallets(String authorization) {
		String email = headerDecoder.decodeHeader(authorization);
		UserDto user = usersProxy.getUserByEmail(email);
		if (user.getRole().equals("ADMIN")) {
			List<CryptoWalletModel> models = repo.findAll();
			List<CryptoWalletDto> dtos = convertModelsToDtos(models);
			return ResponseEntity.ok(dtos);
		}
		
		if (user.getRole().equals("USER")) {
			CryptoWalletModel model = repo.findByEmail(email);
			CryptoWalletDto dto = convertModelToDto(model);
			return ResponseEntity.ok(dto);
		}
		
		return ResponseEntity.status(400).body("Bad request");
	}

	@Override
	public CryptoWalletDto getCryptoWallet(String email) {
		CryptoWalletModel model = repo.findByEmail(email);
		CryptoWalletDto dto = convertModelToDto(model);
		return dto;
	}

	@Override
	public ResponseEntity<?> createCryptoWallet(CryptoWalletDto dto) {
		if(repo.findByEmail(dto.getEmail()) == null) {
			CryptoWalletModel model = convertDtoToModel(dto);
			return ResponseEntity.status(201).body(repo.save(model));
		}
		return ResponseEntity.status(409).body("Crypto wallet with forwarded email already exists");
	}

	@Override
	public ResponseEntity<?> updateCryptoWallet(String email, CryptoWalletDto dto) {
		CryptoWalletModel model = repo.findByEmail(email);
		if (model != null) {
			return ResponseEntity.status(200).body(repo.save(setCurrenciesForCryptoWallet(model, dto)));
		}
		return ResponseEntity.status(404).body("Crypto wallet with forwarded email doesn't exist");
	}

	@Override
	public ResponseEntity<?> exchangeCrypto(String email, String from, String to, BigDecimal quantity,
			BigDecimal quantityConverted) {
		CryptoWalletModel cryptoWallet = repo.findByEmail(email);
		
		switch(from.toUpperCase()) {
		case "BTC":
			cryptoWallet.setBtc(cryptoWallet.getBtc().subtract(quantity));
			break;
		case "ETH":
			cryptoWallet.setEth(cryptoWallet.getEth().subtract(quantity));
			break;
		case "XRP":
			cryptoWallet.setXrp(cryptoWallet.getXrp().subtract(quantity));
			break;
		}
		
		switch(to.toUpperCase()) {
		case "BTC":
			cryptoWallet.setBtc(cryptoWallet.getBtc().add(quantityConverted));
			break;
		case "ETH":
			cryptoWallet.setEth(cryptoWallet.getEth().add(quantityConverted));
			break;
		case "XRP":
			cryptoWallet.setXrp(cryptoWallet.getXrp().add(quantityConverted));
			break;
		}
		
		return ResponseEntity.status(200).body(repo.save(cryptoWallet));
	}

	@Override
	public ResponseEntity<?> deleteCryptoWallet(String email) {
		if(repo.findByEmail(email) != null) {
			CryptoWalletModel model = repo.findByEmail(email);
			repo.deleteById(model.getId());
			return ResponseEntity.status(200).body("Crypto wallet with forwarded email is successfully deleted!");
		}
		return ResponseEntity.status(404).body("Crypto wallet with forwarded email doesn't exist");
	}
	
	private List<CryptoWalletDto> convertModelsToDtos(List<CryptoWalletModel> models) {
		List<CryptoWalletDto> dtos = new ArrayList<CryptoWalletDto>();
		for(CryptoWalletModel model : models) {
			CryptoWalletDto dto = new CryptoWalletDto(model.getEmail(), model.getBtc(), model.getEth(), model.getXrp());
			dtos.add(dto);
		}
		
		return dtos;
	}
	
	private CryptoWalletDto convertModelToDto(CryptoWalletModel model) {
		CryptoWalletDto dto = new CryptoWalletDto(model.getEmail(), model.getBtc(), model.getEth(), model.getXrp());
		return dto;
	}
	
	private CryptoWalletModel convertDtoToModel(CryptoWalletDto dto) {
		CryptoWalletModel model = new CryptoWalletModel(dto.getEmail(), dto.getBtc(), dto.getEth(), dto.getXrp());
		return model;
	}
	
	private CryptoWalletModel setCurrenciesForCryptoWallet(CryptoWalletModel model, CryptoWalletDto dto) {
		model.setBtc(dto.getBtc());
		model.setEth(dto.getEth());
		model.setXrp(dto.getXrp());
		
		return model;
	}

}
