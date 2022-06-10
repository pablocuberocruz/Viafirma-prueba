/**
 * 
 */
package com.viafirma.pablocubero.prueba.models;

/**
 * @author Pablo Cubero Cruz
 *
 */
public class Document {

	private long idDocument;

	/**
	 * @param idDocument
	 */
	public Document(long idDocument) {
		super();
		this.idDocument = idDocument;
	}

	/**
	 * @return the idDocument
	 */
	public long getIdDocument() {
		return idDocument;
	}

	/**
	 * @param idDocument the idDocument to set
	 */
	public void setIdDocument(long idDocument) {
		this.idDocument = idDocument;
	}

}
