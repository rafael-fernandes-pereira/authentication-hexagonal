package com.github.oindiao.application.user.port.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public interface ProviderCachePort {

	@Builder
    @AllArgsConstructor
    @Getter
    class Input {
		
		@NotNull
        @NotEmpty
        private final String token;
						
	}
	
	@Builder
    @AllArgsConstructor
    @Getter
    class Output {
		
		private final Boolean send;
		
	}
	
}
