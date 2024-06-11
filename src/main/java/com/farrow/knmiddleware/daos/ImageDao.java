package com.farrow.knmiddleware.daos;

import java.io.IOException;

import com.farrow.knmiddleware.dto.ImageFile;

public interface ImageDao {
	public ImageFile downloadInvoiceImage(int clientNo, int referenceNo, int invoiceNo) throws IOException;
}
