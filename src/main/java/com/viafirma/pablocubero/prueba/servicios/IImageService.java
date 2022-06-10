package com.viafirma.pablocubero.prueba.servicios;

import java.io.IOException;

import com.viafirma.pablocubero.prueba.models.Image;

public interface IImageService {

	public Image getLastImageUploaded() throws IOException;

	public void saveImage(Image image);

	public Image getImageById(long id);
}
