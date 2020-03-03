package edu.immune.cloud.msgraph.util;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;
import javax.naming.OperationNotSupportedException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;

/**
 * Used to access the reloadable properties 
 * 
 * @author Lalit Mehra
 *
 */
public class ReloadableProperties extends Properties {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2821453416454367100L;
	
	private PropertiesConfiguration propertiesConfiguration;

    public ReloadableProperties(PropertiesConfiguration propertiesConfiguration) throws IOException {
        super.load(new FileReader(propertiesConfiguration.getFile()));
        this.propertiesConfiguration = propertiesConfiguration;
    }

    @Override
    public synchronized Object setProperty(String key, String value) {
        propertiesConfiguration.setProperty(key, value);
        return super.setProperty(key, value);
    }

    @Override
    public String getProperty(String key) {
        String val = propertiesConfiguration.getString(key);
        val = val!=null?val:StringUtils.EMPTY;
        super.setProperty(key, val);
        return val;
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        String val = propertiesConfiguration.getString(key, defaultValue);
        super.setProperty(key, val);
        return val;
    }

    @Override
    public synchronized void load(Reader reader) throws IOException {
        throw new RuntimeException(new OperationNotSupportedException());
    }

    @Override
    public synchronized void load(InputStream inStream) throws IOException {
        throw new RuntimeException(new OperationNotSupportedException());
    }

}