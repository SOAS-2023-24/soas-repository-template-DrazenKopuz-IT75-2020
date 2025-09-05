package api.feignProxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import api.dtos.TradeServiceExchangeDto;

@FeignClient("trade-service")
public interface TradeServiceExchangeProxy {
	
	@GetMapping("/trade-service-exchange")
	ResponseEntity<TradeServiceExchangeDto> getExchange(@RequestParam(value = "from") String from, @RequestParam(value = "to") String to);
}
