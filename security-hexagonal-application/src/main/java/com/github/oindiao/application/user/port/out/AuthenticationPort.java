package com.github.oindiao.application.user.port.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public interface AuthenticationPort {

	@Builder
    @AllArgsConstructor
    @Getter
    class Input {
		
		@NotNull
        @NotEmpty
        @Email
        private final String email;
		
		@NotNull
        @NotEmpty
        private final String password;
						
	}
	
	@Builder
    @AllArgsConstructor
    @Getter
    class Output {
		
		private final Boolean authenticated;
		
	}
	
	
}
