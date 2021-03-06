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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pt.webdetails.cpk.elements.impl.kettleOutputs;

import org.pentaho.di.core.Result;
import org.pentaho.di.core.row.RowMetaInterface;
import pt.webdetails.cpk.elements.impl.KettleElementType;

/**
 *
 * @author Pedro Alves<pedro.alves@webdetails.pt>
 */
public interface IKettleOutput {

    public void processResult();

    public boolean needsRowListener();

    public void storeRow(Object[] row, RowMetaInterface rowMeta);

    public void setResult(Result r);

    public Result getResult();

    public KettleElementType.KettleType getKettleType();

    public void setKettleType(KettleElementType.KettleType kettleType);
    
    public void setOutputStepName(String stepName);
    
    public String getOutputStepName();
}
