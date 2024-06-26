package com.farrow.knmiddleware.queuerunners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.farrow.knmiddleware.converters.AccrualConverter;
import com.farrow.knmiddleware.dto.AccrualDTO;
import com.farrow.knmiddleware.dto.DataType;
import com.farrow.knmiddleware.dto.QueueFile;
import com.farrow.knmiddleware.dto.QueueItem;
import com.farrow.knmiddleware.dto.SourceSystem;
import com.farrow.knmiddleware.exceptions.UnsupportedSourceSystemException;
import com.kn.services.accuredexpenses.AccruedExpenses;

@Component
public class AccrualQueueRunner extends QueueRunner {

	@Autowired private AccrualConverter accrualConverter;
	
	@Override
	public DataType getType() {
		return DataType.ACCRUAL;
	}

	@Override
	public void convertData(QueueItem item) throws Exception {
		if(SourceSystem.TSB.equals(item.getSourceSystem())) {

			AccrualDTO accrualDetails = mapper.readValue(item.getInputFile().getFile(), AccrualDTO.class);
			AccruedExpenses expenses = accrualConverter.convertToKNObject(accrualDetails);
			byte[] xmlFile = accrualConverter.generateXml(expenses);
			QueueFile xmlQueueFile = new QueueFile();
			xmlQueueFile.setFile(xmlFile);
			item.setOutputXml(xmlQueueFile);
			queueDao.saveOutputFile(item.getId(),item.getOutputXml());
			
			
		}
		else if(SourceSystem.TM.equals(item.getSourceSystem())) {
			//TODO
		}
		else if(SourceSystem.PL.equals(item.getSourceSystem())) {
			//TODO
		}
		else {
			throw new UnsupportedSourceSystemException(item.getSourceSystem()+" is unsupported for "+item.getDataType());
		}

	}

	@Override
	public void transmitData(QueueItem item) throws Exception {
		genericTransmitData(item,"/pub/inbound/accrual");

	}

}
