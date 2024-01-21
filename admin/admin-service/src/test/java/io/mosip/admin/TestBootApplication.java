package io.mosip.admin;

import io.mosip.commons.packet.impl.OnlinePacketCryptoServiceImpl;
import io.mosip.commons.packet.keeper.PacketKeeper;

import javax.validation.Validator;

import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages= {"io.mosip.admin.*", "io.mosip.commons.*", "io.mosip.kernel.idvalidator.rid.*"})
public class TestBootApplication {
	public static void main(String[] args) {
		SpringApplication.run(TestBootApplication.class, args);
	}


	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public RestTemplate selfTokenRestTemplate() { return new RestTemplate(); }

	@Bean
	@Primary
	public OnlinePacketCryptoServiceImpl onlineCrypto() {
		return Mockito.mock(OnlinePacketCryptoServiceImpl.class);
	}

	@Bean
	@Primary
	public PacketKeeper packetKeeper() {
		return Mockito.mock(PacketKeeper.class);
	}

	@Bean
	public Validator validator() {
		return Mockito.mock(Validator.class);
	}
}
