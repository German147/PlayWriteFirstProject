package org.example.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.util.Properties;

public class TestProperties {
    private Logger log = LogManager.getLogger();

    private Properties prop;

    /**
     * Constructor - Load the config properties for Test
     */

    public TestProperties() {
        this.prop = new Properties();
        try(FileInputStream fileInputStream = new FileInputStream("./src/main/resources/config.properties")){
            prop.load(fileInputStream);
        }catch(Exception e){
            log.error("Error while reading properties file ", e);
        }
    }

    public String getProperty(String key){
        return prop.getProperty(key) != null ? prop.getProperty(key).trim() : null;
    }
}
