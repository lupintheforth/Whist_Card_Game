/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                          Team 12                          *
 * Chen Fu(987369), Yizhou Zhu(1034676), Shengqi Zhou(893295)*
 *                   last update: 2020.6.6                   *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package whist;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertyManagement { // Singleton design pattern

	// only one PropertyManagement object
	private static final PropertyManagement propertyManagement = new PropertyManagement();
	private Properties whistProperties;

	// set the constructor private
	private PropertyManagement() {

	}

	// get only one PropertyManagement object
	public static PropertyManagement getInstance() {
		return propertyManagement;
	}

	public void setWhistProperties(String propertyFileName) throws IOException {
		if (this.whistProperties == null) {
			this.whistProperties = new Properties();
		}
		// Default properties: same as the original.properties
		this.whistProperties.setProperty("seed", "30006");
		this.whistProperties.setProperty("nbInteractivePlayer", "1");
		this.whistProperties.setProperty("nbRandomNPC", "3");
		this.whistProperties.setProperty("nbLegalNPC", "0");
		this.whistProperties.setProperty("nbSmartNPC", "0");
		this.whistProperties.setProperty("nbStartCards", "13");
		this.whistProperties.setProperty("winningScore", "11");
		this.whistProperties.setProperty("enforceRules", "false");
		// Read properties
		try {
			FileReader inStream = new FileReader(propertyFileName);
			this.whistProperties.load(inStream);
			inStream.close();
			System.out.println("Using the property file: " + propertyFileName);
		} catch (FileNotFoundException e) {
			System.out.println("Property file \"whist.properties\" is not found:\nUsing default properties:");
			System.out.println("seed=30006\n" + "nbInteractivePlayer=1\n" + "nbRandomNPC=3\n" + "nbLegalNPC=0\n"
					+ "nbSmartNPC=0\n" + "nbStartCards=13\n" + "winningScore=11\n" + "enforceRules=false");
		}
	}

	public String getWhistProperties(String para) {
		return this.whistProperties.getProperty(para);
	}

}
