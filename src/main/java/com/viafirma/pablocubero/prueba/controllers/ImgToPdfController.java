/**
 * 
 */
package com.viafirma.pablocubero.prueba.controllers;

import java.io.FileWriter;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viafirma.pablocubero.prueba.models.Document;
import com.viafirma.pablocubero.prueba.models.Image;
import com.viafirma.pablocubero.prueba.servicios.IImageService;
import com.viafirma.pablocubero.prueba.servicios.IImgToPdfService;
import com.viafirma.pablocubero.prueba.util.Constants;
import com.viafirma.pablocubero.prueba.util.Errors;

/**
 * @author Pablo Cubero Cruz
 *
 */
@RestController
public class ImgToPdfController {

	Logger logger = LoggerFactory.getLogger(ImgToPdfController.class);

	private final IImgToPdfService imgToPdfService;

	private final IImageService imageService;

	private static final ObjectMapper mapper = new ObjectMapper();

	@Autowired
	public ImgToPdfController(IImgToPdfService imgToPdfService, IImageService imageService) {
		this.imgToPdfService = imgToPdfService;
		this.imageService = imageService;
	}

	@PostMapping(value = "/img2pdf")
	public String uploadDocument(@RequestBody(required = true) MultipartFile image) {
		String res = null;
		try {
			if (image != null && !image.getOriginalFilename().isEmpty()) {
				String newIdDocument = UUID.randomUUID().toString();
				String nombreImagen = FilenameUtils.removeExtension(image.getOriginalFilename());
				String namePdf = nombreImagen + "_" + newIdDocument + ".pdf";
				this.imgToPdfService.convertImgToPdf(image, namePdf);
				Image newImage = new Image(newIdDocument, namePdf);
				this.saveImage(newImage);
				Document document = new Document(newIdDocument);

				res = mapper.writeValueAsString(document);
			} else {
				logger.error(Errors.ERROR_IMAGE_NOT_ATTACHED);
				return this.imageService.createMessageAndConvertToJson(Errors.ERROR_IMAGE_NOT_ATTACHED);
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return this.imageService.createMessageAndConvertToJson(Errors.ERROR_NOT_EXPECTED);
		}
		return res;
	}

	private void saveImage(Image image) {
		try (FileWriter fichero = new FileWriter(Constants.PATH_FILE_IMAGES, true);) {
			fichero.write(image.toString());
			fichero.append("\n");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
	}
}
