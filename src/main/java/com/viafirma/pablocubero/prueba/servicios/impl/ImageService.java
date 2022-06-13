/**
 * 
 */
package com.viafirma.pablocubero.prueba.servicios.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.viafirma.pablocubero.prueba.models.Error;
import com.viafirma.pablocubero.prueba.models.Image;
import com.viafirma.pablocubero.prueba.servicios.IImageService;
import com.viafirma.pablocubero.prueba.util.Constants;

/**
 * @author Pablo Cubero Cruz
 *
 */
@Service
public class ImageService implements IImageService {

	Logger logger = LoggerFactory.getLogger(ImageService.class);

	@Override
	public Image getImageById(String id) {
		Image res = null;

		try {
			List<String> lines = this.getLinesOfFile(Constants.PATH_FILE_IMAGES);
			String line = lines.stream().filter(x -> x.split(Constants.SEP_FILE_IMAGES)[0].equals(id)).findFirst()
					.orElse(null);
			if (line != null && !line.isEmpty()) {
				String[] parts = line.split(Constants.SEP_FILE_IMAGES);
				res = new Image(parts[0], parts[1]);
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return res;
	}

	private List<String> getLinesOfFile(String path) {

		List<String> lines = new ArrayList<String>();
		File archivo = new File(path);
		try (FileReader fr = new FileReader(archivo); BufferedReader br = new BufferedReader(fr);) {

			lines = br.lines().collect(Collectors.toList());
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return lines;
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
			logger.error(e.getLocalizedMessage());
		}
		return res;
	}

}
