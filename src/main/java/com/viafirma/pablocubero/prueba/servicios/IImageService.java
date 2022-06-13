package com.viafirma.pablocubero.prueba.servicios;

import com.viafirma.pablocubero.prueba.models.Image;

public interface IImageService {

	public Image getImageById(String id);

	String createMessageAndConvertToJson(String message);
}
