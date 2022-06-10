/**
 * 
 */
package com.viafirma.pablocubero.prueba.servicios.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.viafirma.pablocubero.prueba.models.Image;
import com.viafirma.pablocubero.prueba.servicios.IImageService;
import com.viafirma.pablocubero.prueba.util.Constants;

/**
 * @author Pablo Cubero Cruz
 *
 */
@Service
public class ImageService implements IImageService {

	@Override
	public void saveImage(Image image) {
		FileWriter fichero = null;

		try {
			fichero = new FileWriter(Constants.PATH_FILE_IMAGES, true);
			fichero.write(image.toString());
			fichero.append("\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fichero != null) {
					fichero.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	@Override
	public Image getLastImageUploaded() throws IOException {
		Image image = null;
		try {
			List<String> lines = this.getLinesOfFile(Constants.PATH_FILE_IMAGES);
			if (!lines.isEmpty()) {
				String lastLine = lines.get(lines.size() - 1);
				String[] parts = lastLine.split(Constants.SEP_FILE_IMAGES);
				image = new Image(Integer.parseInt(parts[0]), parts[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return image;
	}

	@Override
	public Image getImageById(long id) {
		Image res = null;

		try {
			List<String> lines = this.getLinesOfFile(Constants.PATH_FILE_IMAGES);
			String line = lines.stream().filter(x -> x.split(Constants.SEP_FILE_IMAGES)[0].equals(String.valueOf(id)))
					.findFirst().orElse(null);
			if (line != null && !line.isEmpty()) {
				String[] parts = line.split(Constants.SEP_FILE_IMAGES);
				res = new Image(Integer.valueOf(parts[0]), parts[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	private List<String> getLinesOfFile(String path) {
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;

		List<String> lines = new ArrayList<String>();
		try {
			archivo = new File(path);
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);

			lines = br.lines().collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fr != null) {
					fr.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return lines;
	}

}
