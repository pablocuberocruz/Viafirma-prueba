/**
 * 
 */
package com.viafirma.pablocubero.prueba.servicios;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Pablo Cubero Cruz
 *
 */
public interface IImgToPdfService {

	public void convertImgToPdf(MultipartFile image, String name) throws IOException;

}
