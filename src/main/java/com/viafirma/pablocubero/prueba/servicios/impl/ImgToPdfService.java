/**
 * 
 */
package com.viafirma.pablocubero.prueba.servicios.impl;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.viafirma.pablocubero.prueba.servicios.IImgToPdfService;
import com.viafirma.pablocubero.prueba.util.Constants;

/**
 * @author Pablo Cubero Cruz
 *
 */
@Service
public class ImgToPdfService implements IImgToPdfService {

	@Autowired
	public ImgToPdfService() {
	}

	@Override
	public void convertImgToPdf(MultipartFile image, String name) throws IOException {
		try (PDDocument pdf = new PDDocument()) {
			PDImageXObject img = PDImageXObject.createFromByteArray(pdf, image.getBytes(), name);
			PDPage page = new PDPage(new PDRectangle(img.getWidth(), img.getHeight()));
			pdf.addPage(page);

			PDPageContentStream contentStream = new PDPageContentStream(pdf, page);

			contentStream.drawImage(img, 0, 0);

			contentStream.close();

			pdf.save(Constants.PATH_SAVE_PDF + name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
