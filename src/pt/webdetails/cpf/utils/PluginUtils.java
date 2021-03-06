/*!
* Copyright 2002 - 2013 Webdetails, a Pentaho company.  All rights reserved.
* 
* This software was developed by Webdetails and is provided under the terms
* of the Mozilla Public License, Version 2.0, or any later version. You may not use
* this file except in compliance with the license. If you need a copy of the license,
* please go to  http://mozilla.org/MPL/2.0/. The Initial Developer is Webdetails.
*
* Software distributed under the Mozilla Public License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
* the license for the specific language governing your rights and limitations.
*/

package pt.webdetails.cpf.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.pentaho.platform.api.engine.IMimeTypeListener;
import org.pentaho.platform.api.engine.IParameterProvider;
import org.pentaho.platform.api.engine.IPluginResourceLoader;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import pt.webdetails.cpf.Util;

/**
 *
 * @author Pedro Alves<pedro.alves@webdetails.pt>
 */
public class PluginUtils {

    private static PluginUtils _instance;
    protected Log logger = LogFactory.getLog(this.getClass());
    private String pluginName;
    private File pluginDirectory;

    public File getPluginDirectory() {
        return pluginDirectory;
    }

    public void setPluginDirectory(File pluginDirectory) {
        this.pluginDirectory = pluginDirectory;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public PluginUtils() {
        try {
            // init
             initialize();
        } catch (Exception e) {
            logger.error("Can't initialize PluginUtils: " + Util.getExceptionDescription(e));
        }

    }

    public static PluginUtils getInstance() {

        if (_instance == null) {
            _instance = new PluginUtils();
        }

        return _instance;
    }

    private void initialize() throws IOException, DocumentException {

        // We need to get the plugin name
        IPluginResourceLoader resLoader = PentahoSystem.get(IPluginResourceLoader.class, null);
        List<URL> pluginResource = resLoader.findResources(this.getClass(), "plugin.xml");

        if (pluginResource.size() < 1) {
            throw new IOException("plugin.xml required but not found");
        }

        /*
         * Verify if the index 0 is actually the file we want!
         */
        URL pluginUrl = pluginResource.get(0);

        // Parent file holds the name
        File pluginDir = new File(pluginUrl.getFile()).getParentFile();
        setPluginName(pluginDir.getName());
        setPluginDirectory(pluginDir);

        logger.debug("Found resource? " + pluginResource.size());

    }

    /**
     * Calls out for resources in the plugin, on the specified path
     *
     * @param elementPath Relative to the plugin directory
     * @param recursive Do we want to enable recursivity?
     * @param pattern regular expression to filter the files
     * @return Files found
     */
    public Collection<File> getPluginResources(String elementPath, Boolean recursive, String pattern) {

        IOFileFilter fileFilter = TrueFileFilter.TRUE;

        if (pattern != null && !pattern.equals("")) {
            fileFilter = new RegexFileFilter(pattern);


        }

        IOFileFilter dirFilter = recursive.equals(Boolean.TRUE) ? TrueFileFilter.TRUE : null;

        // Get directory name. We need to make sure we're not allowing this to fetch other resources
        String basePath = FilenameUtils.normalize(getPluginDirectory().getAbsolutePath());
        String elementFullPath = FilenameUtils.normalize(basePath + File.separator + elementPath);

        if (!elementFullPath.startsWith(basePath)) {
            logger.warn("PluginUtils.getPluginResources is trying to access a parent path - denied : " + elementFullPath);
            return null;
        }

        File dir = new File(elementFullPath);
        if (!dir.exists() || !dir.isDirectory()) {
            return null;
        }

        return FileUtils.listFiles(dir, fileFilter, dirFilter);


    }

    /**
     * From a full path, returns the relative path
     *
     * @param fullPath
     * @param includePluginDir
     * @return The relative path
     */
    public String getPluginRelativeDirectory(String fullPath, boolean includePluginDir) throws FileNotFoundException {


        // Get directory name. We need to make sure we're not allowing this to fetch other resources
        File pluginDir = getPluginDirectory();
        if (includePluginDir) {
            pluginDir = pluginDir.getParentFile();
        }

        String basePath = FilenameUtils.normalize(pluginDir.getAbsolutePath());
        String elementFullPath = FilenameUtils.getFullPath(FilenameUtils.normalize(fullPath));

        if (elementFullPath.indexOf(basePath) < 0) {
            throw new FileNotFoundException("Can't extract relative path from file " + fullPath);
        }

        return elementFullPath.substring(basePath.length());


    }

    /**
     * Calls out for resources in the plugin, on the specified path
     *
     * @param elementPath Relative to the plugin directory
     * @param recursive Do we want to enable recursivity?
     * @return Files found
     */
    public Collection<File> getPluginResources(String elementPath, Boolean recursive) {
        return getPluginResources(elementPath, recursive, null);
    }

    /**
     * Calls out for resources in the plugin, on the specified path. Not
     * recursive
     *
     * @param elementPath Relative to the plugin directory
     * @param pattern regular expression to filter the files
     * @return Files found
     */
    public Collection<File> getPluginResources(String elementPath, String pattern) {
        return getPluginResources(elementPath, false, pattern);
    }

    public void setResponseHeaders(Map<String, IParameterProvider> parameterProviders, final String mimeType) {
        setResponseHeaders(parameterProviders, mimeType, 0, null, 0);
    }

    public void setResponseHeaders(Map<String, IParameterProvider> parameterProviders, final String mimeType, final String attachmentName) {
        setResponseHeaders(parameterProviders, mimeType, 0, attachmentName, 0);
    }
    
    public void setResponseHeaders(Map<String, IParameterProvider> parameterProviders, final String mimeType, final String attachmentName, long attachmentSize){
        setResponseHeaders(parameterProviders, mimeType, 0, attachmentName, attachmentSize);
        
    }

    public void setResponseHeaders(Map<String, IParameterProvider> parameterProviders, final String mimeType, final int cacheDuration, final String attachmentName, long attachmentSize) {
        // Make sure we have the correct mime type

        /* 
         * This code is part of the content generator. Since we want to simplify,
         * I'll remove this from here and directly set the Content-Type header on the response
         * 
         final IMimeTypeListener mimeTypeListener = outputHandler.getMimeTypeListener();
         if (mimeTypeListener != null) {
         mimeTypeListener.setMimeType(mimeType);
         }
         */


        final HttpServletResponse response = PluginUtils.getInstance().getResponse(parameterProviders);

        if (response == null) {
            logger.warn("Parameter 'httpresponse' not found!");
            return;
        }

        if (mimeType != null) {
            response.setHeader("Content-Type", mimeType);
        }

        if (attachmentName != null) {
            response.setHeader("content-disposition", "attachment; filename=" + attachmentName);
        } // Cache?
        
        if (attachmentSize > 0){
            response.setHeader("Content-Length", String.valueOf(attachmentSize));
        }

        if (cacheDuration > 0) {
            response.setHeader("Cache-Control", "max-age=" + cacheDuration);
        } else {
            response.setHeader("Cache-Control", "max-age=0, no-store");
        }
    }

    /**
     * Copies the parameters from the IParameterProvider to a Map
     *
     * @param params
     * @param provider
     */
    public void copyParametersFromProvider(Map<String, Object> params, IParameterProvider provider) {
        @SuppressWarnings("unchecked")
        Iterator<String> paramNames = provider.getParameterNames();
        while (paramNames.hasNext()) {
            String paramName = paramNames.next();
            params.put(paramName, provider.getParameter(paramName));
        }
    }

    public void redirect(Map<String, IParameterProvider> parameterProviders, String url) {

        final HttpServletResponse response = getResponse(parameterProviders);

        if (response == null) {
            logger.error("response not found");
            return;
        }
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            logger.error("could not redirect", e);
        }
    }

    public HttpServletRequest getRequest(Map<String, IParameterProvider> parameterProviders) {
        return (HttpServletRequest) parameterProviders.get("path").getParameter("httprequest");
    }

    public HttpServletResponse getResponse(Map<String, IParameterProvider> parameterProviders) {
        return (HttpServletResponse) parameterProviders.get("path").getParameter("httpresponse");
    }

    public IParameterProvider getRequestParameters(Map<String, IParameterProvider> parameterProviders) {
        return parameterProviders.get("request");
    }

    public IParameterProvider getPathParameters(Map<String, IParameterProvider> parameterProviders) {
        return parameterProviders.get("path");
    }

    public OutputStream getResponseOutputStream(Map<String, IParameterProvider> parameterProviders) throws IOException {

        return getResponse(parameterProviders).getOutputStream();
    }
}
