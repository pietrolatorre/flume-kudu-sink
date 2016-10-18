/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.kudu.flume.sink;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.SerializationUtils;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.FlumeException;
import org.apache.kudu.client.Insert;
import org.apache.kudu.client.KuduTable;
import org.apache.kudu.client.Operation;
import org.apache.kudu.client.PartialRow;
import org.quantyca.data.Mapper;
import org.quantyca.data.RigaScontrino;
import org.quantyca.data.TestataScontrino;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class RigaScontrinoKuduOperationsProducer implements KuduOperationsProducer {
  public static final String PAYLOAD_COLUMN_PROP = "payloadColumn";
  public static final String PAYLOAD_COLUMN_DEFAULT = "payload";

  private KuduTable table;
  private String payloadColumn;

  public RigaScontrinoKuduOperationsProducer(){
  }

  public void configure(Context context) {
    payloadColumn = context.getString(PAYLOAD_COLUMN_PROP, PAYLOAD_COLUMN_DEFAULT);
  }

  public void initialize(KuduTable table) {
    this.table = table;
  }
  
    
  
  public List<Operation> getOperations(Event event) throws FlumeException {
    try {
      Insert insert;
      
      String xml= new String(event.getBody());
      
      Document doc =Mapper.getDocumentFromEvent(xml);
      List<RigaScontrino> righeScontrino = Mapper.getRigheScontrinoFromDocument(doc);
      List<Operation> operations = new ArrayList<Operation>();
      
      for(int i=0;i<righeScontrino.size();i++){
    	  RigaScontrino rigaScontrino = righeScontrino.get(i);
    	  insert = table.newInsert();
    	  PartialRow row = insert.getRow();
    	  row.addString("id_scontrino", rigaScontrino.getId_scontrino());
    	  row.addString("id_riga", rigaScontrino.getId_riga());
    	  row.addString("id_prodotto", rigaScontrino.getId_prodotto());
    	  row.addFloat("qta", rigaScontrino.getQta());
    	  row.addFloat("prezzo", rigaScontrino.getPrezzo());
    	  
    	  //System.out.println("******* "+rigaScontrino.getId_scontrino()+"_"+i+") "+(Operation)insert);
    	  
    	  operations.add((Operation) insert);        	  
      }      
        
      return operations;
    } catch (Exception e){
      throw new FlumeException("Failed to create Kudu Insert object", e);
    }
  }

  @Override
  public void close() {
  }
}