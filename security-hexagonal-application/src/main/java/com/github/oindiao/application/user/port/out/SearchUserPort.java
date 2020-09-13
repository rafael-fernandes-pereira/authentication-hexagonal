package com.github.oindiao.application.user.port.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Optional;

public interface SearchUserPort {
	
	Optional<Output> buscar(Input input);
	
	@Builder
    @AllArgsConstructor
    @Getter
    class Input {
		
		@NotNull
        @NotEmpty
        @Email
        private final String email;
						
	}
	
	@Builder
    @AllArgsConstructor
    @Getter
    class Output {
		
		@NotNull
	    @Email
	    private final String email;

	    @NotNull
	    private final String password;

	    @NotNull
	    private final String profile;

		@NotNull
		private LocalDate expirationPasswordDate;

		@NotNull
		private Boolean active;
	}
		
}
