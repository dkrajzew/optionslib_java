package de.dks.utils.options;

import java.io.IOException;

/**
 * @class OptionsTypedFileIO
 * @brief A base class for loading/saving options from/to files
 * @author Daniel Krajzewicz (daniel@krajzewicz.de)
 * @copyright Eclipse Public License v2.0 (EPL v2.0), (c) Daniel Krajzewicz 2021-
 */
public abstract class OptionsTypedFileIO {
	
	/** @brief Loads parameters from a configuration file
	 * 
	 * This method calls the protected member  _loadConfiguration(OptionsCont, String)
	 * within a loop that realises hierarchical configurations.
	 * 
	 * @see _loadConfiguration(OptionsCont, String)
	 * @param into The options container to fill
	 * @param configFileName The name of the option to retrieve the file name from
	 * @return Whether options could be loaded
	 * @throws IOException If the file cannot be read
	 */
	public boolean loadConfiguration(OptionsCont into, String configFileName) throws IOException {
		String parentName = into.getParentConfigurationName();
		boolean ok = true;
		do {
			if(parentName!=null && !"".equals(parentName)) {
				into.remarkUnset(parentName);
			}
			ok &= _loadConfiguration(into, configFileName);
			if(parentName!=null && !"".equals(parentName) && into.isSet(parentName)) {
				configFileName = into.getString(parentName);
			} else {
				configFileName = null;
			}
		} while (ok && configFileName!=null);
		return ok;
	}
	
	
	
    /** @brief Writes the set options as configuration file
     * 
     * @param configName The name of the file to write the configuration to
     * @param options The options container that includes the (set/parsed) options to write 
     * @throws IOException If the file cannot be written
     */
	public abstract boolean writeConfiguration(String configName, OptionsCont options) throws IOException;

    
    /** @brief Writes the a template for a configuration file
     * 
     * @param configName The name of the file to write the template to
     * @param options The options container to write a template for 
     * @throws IOException If the file cannot be written
     */
    public abstract boolean writeTemplate(String configName, OptionsCont options) throws IOException;
    
    
    
	/** @brief Loads parameters from a configuration file
	 * @param into The options container to fill
	 * @param configOptionName The name of the option to retrieve the file name from
	 * @return Whether options could be loaded
	 * @throws IOException If the file cannot be read
	 */
    protected abstract boolean _loadConfiguration(OptionsCont into, String configFileName) throws IOException;

}
