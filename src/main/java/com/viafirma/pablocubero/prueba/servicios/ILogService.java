/**
 * 
 */
package com.viafirma.pablocubero.prueba.servicios;

/**
 * @author Pablo Cubero Cruz
 *
 */
public interface ILogService {

	public void saveLog(String exception);

	public String createMessageAndConvertToJson(String message);

}
