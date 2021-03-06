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

package pt.webdetails.cpf;

import java.io.OutputStream;

import org.pentaho.platform.api.engine.IParameterProvider;

public interface RestRequestHandler {
  
  public enum HttpMethod {

    GET, POST, PUT, DELETE, HEAD, TRACE, OPTIONS, CONNECT, PATCH
}

  public boolean canHandle(HttpMethod method, String path);
  
  public void route(HttpMethod method, String path, OutputStream out, IParameterProvider pathParams, IParameterProvider requestParams);
  
  public String getResponseMimeType();
  
}
