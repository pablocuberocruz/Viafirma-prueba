/**
 * 
 */
package com.viafirma.pablocubero.prueba.controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viafirma.pablocubero.prueba.models.Image;
import com.viafirma.pablocubero.prueba.servicios.IImageService;
import com.viafirma.pablocubero.prueba.util.Constants;
import com.viafirma.pablocubero.prueba.util.Errors;

/**
 * @author Pablo Cubero Cruz
 *
 */
@RestController
@RequestMapping("/pdf")
public class DownloadPdfController {

	Logger logger = LoggerFactory.getLogger(DownloadPdfController.class);

	private final IImageService imageService;

	@Autowired
	public DownloadPdfController(IImageService imageService) {
		this.imageService = imageService;
	}

	@GetMapping(value = "/{idDocument}")
	public String downloadImage(HttpServletResponse response, @PathVariable String idDocument) {
		File file = null;
		String pathPdf = "";
		Image image = null;

		try {
			image = this.imageService.getImageById(idDocument);
			if (image != null) {
				pathPdf = Constants.PATH_SAVE_PDF + image.getName();
				file = new File(pathPdf);
			} else {
				logger.error(Errors.ERROR_DOCUMENT_NOT_EXIST);
				return this.imageService.createMessageAndConvertToJson(Errors.ERROR_DOCUMENT_NOT_EXIST);
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return this.imageService.createMessageAndConvertToJson(Errors.ERROR_NOT_EXPECTED);
		}

		if (file != null && image != null) {
			try (PDDocument document = PDDocument.load(file);) {
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				document.save(byteArrayOutputStream);

				response.setContentType("application/force-download");
				response.setContentLength((int) file.length());
				response.setHeader("Content-Transfer-Encoding", "binary");
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + image.getName().replace("_" + idDocument, ""));
				response.getOutputStream().write(byteArrayOutputStream.toByteArray());
				return "";
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage());
				return this.imageService.createMessageAndConvertToJson(Errors.ERROR_NOT_EXPECTED);
			}
		}
		return "";
	}
}
