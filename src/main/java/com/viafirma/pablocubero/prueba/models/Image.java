/**
 * 
 */
package com.viafirma.pablocubero.prueba.models;

import com.viafirma.pablocubero.prueba.util.Constants;

/**
 * @author Pablo Cubero Cruz
 *
 */
public class Image {

	private long id;

	private String name;

	/**
	 * @param id
	 * @param name
	 */
	public Image(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return id + Constants.SEP_FILE_IMAGES + name;
	}

}
