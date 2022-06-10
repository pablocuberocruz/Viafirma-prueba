/**
 * 
 */
package com.viafirma.pablocubero.prueba.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.viafirma.pablocubero.prueba.models.Document;
import com.viafirma.pablocubero.prueba.models.Image;
import com.viafirma.pablocubero.prueba.servicios.IImageService;
import com.viafirma.pablocubero.prueba.servicios.IImgToPdfService;
import com.viafirma.pablocubero.prueba.servicios.impl.LogService;
import com.viafirma.pablocubero.prueba.util.Errors;

/**
 * @author Pablo Cubero Cruz
 *
 */
@RestController
public class ImgToPdfController {

	@Autowired
	private IImgToPdfService imgToPdfService;

	@Autowired
	private IImageService imageService;

	@Autowired
	private LogService logService;

	@PostMapping(value = "/img2pdf")
	public String uploadDocument(@RequestBody(required = true) MultipartFile image) {
		String res = null;
		try {
			Image lastImage = this.imageService.getLastImageUploaded();
			long newIdDocument = lastImage == null ? 1 : lastImage.getId() + 1;
			String nombreImagen = image.getOriginalFilename().substring(0, image.getOriginalFilename().length() - 4);
			String namePdf = nombreImagen + "_" + newIdDocument + ".pdf";
			this.imgToPdfService.convertImgToPdf(image, namePdf);
			Image newImage = new Image(newIdDocument, namePdf);
			this.imageService.saveImage(newImage);
			Document document = new Document(newIdDocument);

			ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
			res = mapper.writeValueAsString(document);
		} catch (NullPointerException e) {
			logService.saveLog(String.valueOf(e.getLocalizedMessage()));
			return this.logService.createMessageAndConvertToJson(Errors.ERROR_NO_ADJUNTADA_IMAGEN);
		} catch (Exception e) {
			logService.saveLog(String.valueOf(e.getLocalizedMessage()));
			return this.logService.createMessageAndConvertToJson(Errors.ERROR_NO_ESPERADO);
		}
		return res;
	}
}
