/**
 * 
 */
package de.fuberlin.csw.aood.owlapi;

import java.util.Properties;

/**
 * @author ralph
 *
 */
public class Config extends Properties {
	
	
	private static final Config instance = new Config();
	
	public static Config instance() {
		return instance;
	}
	
	private Config() {
		// TODO do not hardwire this
		put("expandModules", true);
	}
	
}
