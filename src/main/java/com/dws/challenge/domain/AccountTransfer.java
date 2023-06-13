package com.dws.challenge.domain;

import java.math.BigDecimal;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AccountTransfer {

	@NotNull
	@NotEmpty
	private String accountFromId;

	@NotNull
	@NotEmpty
	private String accountToId;
	
	@NotNull
	@Min(value = 1, message = "Transfer Amount must be positive.")
	private BigDecimal amountToTransfer;
}
