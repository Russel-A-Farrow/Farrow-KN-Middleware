package com.farrow.knmiddleware.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceARLineVat {
	private String vatCode;
	private BigDecimal vatPercentage;
	private BigDecimal chargeLineVatLcAmount;
	private BigDecimal chargeLineVatFcAmount;
}
