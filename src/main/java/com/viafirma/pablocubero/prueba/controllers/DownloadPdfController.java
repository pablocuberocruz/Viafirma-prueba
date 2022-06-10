/**
 * 
 */
package com.viafirma.pablocubero.prueba.controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viafirma.pablocubero.prueba.models.Image;
import com.viafirma.pablocubero.prueba.servicios.IImageService;
import com.viafirma.pablocubero.prueba.servicios.ILogService;
import com.viafirma.pablocubero.prueba.util.Constants;
import com.viafirma.pablocubero.prueba.util.Errors;

/**
 * @author Pablo Cubero Cruz
 *
 */
@RestController
@RequestMapping("/pdf")
public class DownloadPdfController {

	@Autowired
	private IImageService imageService;

	@Autowired
	private ILogService logService;

	@GetMapping(value = "/{idDocument}")
	public String downloadImage(HttpServletResponse response, @PathVariable String idDocument) {
		PDDocument document = null;
		try {
			Long id = Long.valueOf(idDocument);
			Image image = this.imageService.getImageById(id);
			if (image != null) {
				String pathPdf = Constants.PATH_SAVE_PDF + image.getName();
				File file = new File(pathPdf);
				document = PDDocument.load(file);
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				document.save(byteArrayOutputStream);

				response.setContentType("application/force-download");
				response.setContentLength((int) file.length());
				response.setHeader("Content-Transfer-Encoding", "binary");
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + image.getName().replace("_" + idDocument, ""));
				response.getOutputStream().write(byteArrayOutputStream.toByteArray());
				return "";
			} else {
				return this.logService.createMessageAndConvertToJson(Errors.ERROR_DOCUMENTO_NO_EXISTE);
			}
		} catch (NumberFormatException e) {
			logService.saveLog(String.valueOf(e.getLocalizedMessage()));
			return this.logService.createMessageAndConvertToJson(Errors.ERROR_NO_NUMERICO);
		} catch (Exception e) {
			logService.saveLog(String.valueOf(e.getLocalizedMessage()));
			return this.logService.createMessageAndConvertToJson(Errors.ERROR_NO_ESPERADO);
		} finally {
			try {
				if (document != null) {
					document.close();
				}
			} catch (Exception e2) {
				logService.saveLog(String.valueOf(e2.getLocalizedMessage()));
			}
		}
	}
}
