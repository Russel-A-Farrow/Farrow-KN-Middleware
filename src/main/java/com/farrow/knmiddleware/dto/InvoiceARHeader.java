package com.farrow.knmiddleware.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InvoiceARHeader {
	private String companyCode;
	private String generatedFrom;
	private String debtorCode;
	private String itemType;
	private String itemNumber;
	private LocalDate issueDate;
	private LocalDate dueDate;

}
