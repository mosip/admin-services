package io.mosip.admin;

import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import io.mosip.commons.packet.impl.OnlinePacketCryptoServiceImpl;
import io.mosip.commons.packet.keeper.PacketKeeper;

@SpringBootApplication(scanBasePackages= {"io.mosip.admin.*", "io.mosip.commons.*", "io.mosip.commons.packet.spi.*" })
public class TestBootApplication {
	public static void main(String[] args) {
		SpringApplication.run(TestBootApplication.class, args);
	}

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

}
