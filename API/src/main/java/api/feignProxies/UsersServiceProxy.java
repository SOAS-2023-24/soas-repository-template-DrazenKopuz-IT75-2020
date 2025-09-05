package api.feignProxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import api.dtos.UserDto;

@FeignClient(name = "users-service")
public interface UsersServiceProxy {
	
	@GetMapping("/users/user")
	UserDto getUserByEmail(@RequestParam(value = "email") String email);

}
