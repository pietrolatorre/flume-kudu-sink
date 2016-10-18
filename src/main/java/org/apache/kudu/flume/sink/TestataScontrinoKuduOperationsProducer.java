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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TestataScontrinoKuduOperationsProducer implements KuduOperationsProducer {
  public static final String PAYLOAD_COLUMN_PROP = "payloadColumn";
  public static final String PAYLOAD_COLUMN_DEFAULT = "payload";

  private KuduTable table;
  private String payloadColumn;

  public TestataScontrinoKuduOperationsProducer(){
  }

  public void configure(Context context) {
    payloadColumn = context.getString(PAYLOAD_COLUMN_PROP, PAYLOAD_COLUMN_DEFAULT);
  }

  public void initialize(KuduTable table) {
    this.table = table;
  }
    

  public List<Operation> getOperations(Event event) throws FlumeException {
    try {
      Insert insert = table.newInsert();
      PartialRow row = insert.getRow();
      String xml= new String(event.getBody());
      
      
      Document doc = Mapper.getDocumentFromEvent(xml);      
      TestataScontrino testataScontrino = Mapper.getTestataScontrinoFromDocument(doc);
      
      if(testataScontrino.getId_scontrino() !=null){    	  
	      row.addString("id_scontrino", testataScontrino.getId_scontrino());
	      row.addString("store_id", testataScontrino.getStore_id());
	      row.addString("business_date", testataScontrino.getBusiness_date());
	      row.addInt("pos_id", testataScontrino.getPos_id());
	      row.addString("transaction_number", testataScontrino.getTransaction_number());
	      row.addString("external_reference_id", testataScontrino.getExternal_reference_id());
	      row.addFloat("total_amount", testataScontrino.getTotal_amount());
      }
      
	  //System.out.println("******* "+testataScontrino.getId_scontrino()+") "+(Operation)insert);
      
      return Collections.singletonList((Operation) insert);
    } catch (Exception e){
      throw new FlumeException("Failed to create Kudu Insert object", e);
    }
  }

  @Override
  public void close() {
  }
}