package io.mosip.admin;

import io.mosip.commons.packet.impl.OnlinePacketCryptoServiceImpl;
import io.mosip.commons.packet.keeper.PacketKeeper;

import javax.validation.Validator;


import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;

@SpringBootApplication
@ComponentScan(value = {"io.mosip.admin.*","io.mosip.commons.*","io.mosip.kernel.biometrics.*",
		 "io.mosip.kernel.idvalidator.rid.*","io.mosip.kernel.dataaccess.*"},excludeFilters = @ComponentScan.Filter(type = FilterType.ASPECTJ,
		pattern = "io.mosip.kernel.lkeymanager.repository.*"))
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

	@Bean
	public Validator validator() {
		return Mockito.mock(Validator.class);
	}
}
