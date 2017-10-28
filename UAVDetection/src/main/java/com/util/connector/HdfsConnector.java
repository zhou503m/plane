package com.util.connector;

import javax.xml.datatype.DatatypeConfigurationException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by WangRupeng on 2017/4/19.
 */
public class HdfsConnector implements FileConnector {
    private static final String PREFIX = "hdfs";
    private File localLocation;
    private ExtensionFilter filter = new ExtensionFilter();

    @Override
    public void prepare(Map stormConf) throws DatatypeConfigurationException {

    }

    /**
     * Specify the valid extensions to use by this FileLocation (used by list).
     * If no extensions are set all files will match
     *
     * @param extensions
     * @return itself
     */
    @Override
    public FileConnector setExtensions(String[] extensions) {
        return null;
    }

    /**
     * Moves the remote location to the specified point
     *
     * @param location
     * @return
     */
    @Override
    public void moveTo(String location) throws IOException {

    }

    /**
     * Copies the specified local file to the currently set (remote) location.
     * If the remote location is a 'directory' it will copy the file into the directory.
     * If the remote location points to a file the remote file will likely be overwritten
     * (or an exception is thrown).
     *
     * @param localFile
     * @param delete    indicates if the localFile must be deleted after a successful copy
     * @throws IOException
     */
    @Override
    public void copyFile(File localFile, boolean delete) throws IOException {

    }

    /**
     * List all 'files' within the current location which match the provided extensions.
     * If the extensions is null than all files will be returned.
     *
     * @return
     */
    @Override
    public List<String> list() {
        return null;
    }

    /**
     * Returns the FileLocation's prefix (ftp, s3, file, ...)
     *
     * @return
     */
    @Override
    public String getProtocol() {
        return null;
    }

    /**
     * Gets the current location as a file. If the location is remote this will trigger a download
     *
     * @return
     */
    @Override
    public File getAsFile() throws IOException {
        return null;
    }

    /**
     * Makes a deep copy of this object
     *
     * @return
     */
    @Override
    public FileConnector deepCopy() {
        return null;
    }

    private class ExtensionFilter implements Serializable, FilenameFilter {

        private static final long serialVersionUID = 7715776887121995584L;
        private String[] extensions;

        @Override
        public boolean accept(File file, String name) {
            if(extensions == null) return true;
            for(String ext : extensions){
                if(name.endsWith(ext)) return true;
            }
            return false;
        }

        public void setExtensions(String[] extensions) {
            this.extensions = extensions;
        }
    }
}
