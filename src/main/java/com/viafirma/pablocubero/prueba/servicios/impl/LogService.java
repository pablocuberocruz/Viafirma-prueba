/**
 * 
 */
package com.viafirma.pablocubero.prueba.servicios.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.viafirma.pablocubero.prueba.models.Error;
import com.viafirma.pablocubero.prueba.servicios.ILogService;
import com.viafirma.pablocubero.prueba.util.Constants;

/**
 * @author Pablo Cubero Cruz
 *
 */
@Service
public class LogService implements ILogService {

	@Override
	public void saveLog(String exception) {
		Logger logger = Logger.getLogger("");
		FileHandler fh;

		try {
			String date = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace(":", "");
			String pahtLog = Constants.PATH_SAVE_LOGS + "LOG" + date + ".log";
			fh = new FileHandler(pahtLog, true);
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);

			logger.info(exception);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String createMessageAndConvertToJson(String message) {
		Error mes = null;
		String res = "";

		try {
			mes = new Error(message);
			ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
			res = mapper.writeValueAsString(mes);
		} catch (Exception e) {
			this.saveLog(String.valueOf(e.getLocalizedMessage()));
		}
		return res;
	}
}
