package de.dks.utils.options;

import java.io.IOException;

/**
 * @class OptionsTypedFileIO
 * @brief A base class for loading/saving options from/to files
 * @author Daniel Krajzewicz (daniel@krajzewicz.de)
 * @copyright Eclipse Public License v2.0 (EPL v2.0), (c) Daniel Krajzewicz 2021-
 */
public interface OptionsTypedFileIO {
	
	/** @brief Loads parameters from a configuration file
	 * @param into The options container to fill
	 * @param configOptionName The name of the option to retrieve the file name from
	 * @return Whether options could be loaded
	 * @throws IOException If the file cannot be read
	 */
	public boolean loadConfiguration(OptionsCont into, String configOptionName) throws IOException;
	
	
	
    /** @brief Writes the set options as configuration file
     * 
     * @param configName The name of the file to write the configuration to
     * @param options The options container that includes the (set/parsed) options to write 
     * @throws IOException If the file cannot be written
     */
	public void writeConfiguration(String configName, OptionsCont options) throws IOException;

    
    /** @brief Writes the a template for a configuration file
     * 
     * @param configName The name of the file to write the template to
     * @param options The options container to write a template for 
     * @throws IOException If the file cannot be written
     */
    public void writeTemplate(String configName, OptionsCont options) throws IOException;
	
}
