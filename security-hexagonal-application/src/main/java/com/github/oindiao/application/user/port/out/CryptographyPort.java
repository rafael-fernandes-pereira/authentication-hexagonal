package com.github.oindiao.application.user.port.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface CryptographyPort {

	Optional<Output> encrypth(Input input);

	@Builder
    @AllArgsConstructor
    @Getter
    class Input {
		
		 @NotNull
	     private final String info;
		
	}
	
	@Builder
    @AllArgsConstructor
    @Getter
    class Output {
		@NotNull
		private final String infoEncrypt;
	}
	

}
