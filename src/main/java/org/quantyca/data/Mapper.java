package org.quantyca.data;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.kudu.flume.sink.TestataScontrinoKuduOperationsProducer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class Mapper {

	public static Document getDocumentFromEvent(String xml){
		  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	      DocumentBuilder db = null;
	      Document doc = null;
	      try {
	    	  db = dbf.newDocumentBuilder();	
		      InputSource is = new InputSource();
		      is.setCharacterStream(new StringReader(xml));
			  doc = db.parse(is);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}      
	      catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  return doc;
	  }
	
	public static TestataScontrino getTestataScontrinoFromDocument(Document doc){	  
		  TestataScontrino testataScontrino = new TestataScontrino();
		  
		  doc.getDocumentElement().normalize();
		  String store_id = doc.getElementsByTagName("StoreID").item(0).getTextContent();
		  String business_date = doc.getElementsByTagName("BusinessDate").item(0).getTextContent();	  
		  Float total_amount =  Float.parseFloat(doc.getElementsByTagName("TotalAmount").item(0).getTextContent());
		  String transaction_number = doc.getElementsByTagName("TransactionNumber").item(0).getTextContent();
		  String external_reference_id = doc.getElementsByTagName("ExternalReferenceId").item(0).getTextContent();
		  Integer pos_id = Integer.parseInt(doc.getElementsByTagName("TillID").item(0).getTextContent());
		  
		  testataScontrino.setStore_id(store_id); 
		  testataScontrino.setBusiness_date(business_date);
		  testataScontrino.setTotal_amount(total_amount);
		  testataScontrino.setTransaction_number(transaction_number);
		  testataScontrino.setExternal_reference_id(external_reference_id);
		  testataScontrino.setPos_id(pos_id);	  
	  	  testataScontrino.createIdScontrino();
	  	  
		  return testataScontrino;
	}
	
	public static List<RigaScontrino> getRigheScontrinoFromDocument(Document doc){	  
		  List<RigaScontrino> righeScontrino = new ArrayList<RigaScontrino>();
		  doc.getDocumentElement().normalize();
		  
		  TestataScontrino testataScontrino = getTestataScontrinoFromDocument(doc);
		  
		  NodeList rigaScontrinoNodeList =doc.getElementsByTagName("TransactionDetail");
		  
		  for(int i=0; i<rigaScontrinoNodeList.getLength();i++){		
			  Element rigaScontrinoElement = (Element) rigaScontrinoNodeList.item(i);		  		  
			  RigaScontrino rigaScontrino = new RigaScontrino();
			  String id_riga = rigaScontrinoElement.getElementsByTagName("SequenceNumber").item(0).getTextContent();
			  
			  
			  Element transDetailElement = (Element)rigaScontrinoElement.getElementsByTagName("TransactionDetailGroup").item(0);
			  Element itemElement = (Element)transDetailElement.getElementsByTagName("Item").item(0);
			  
			  //******************* INSERIRE CONTROLLO -> SE MANCA ITEM non creare riga scontrino...
			  
			  String id_prodotto = rigaScontrinoElement.getElementsByTagName("ItemID").item(0).getTextContent();
			  
			  //******************* CORREGGERE E FARE MAPPING CON PREZZO!!!
			  Float prezzo = Float.parseFloat(rigaScontrinoElement.getElementsByTagName("ExtendedAmount").item(0).getTextContent());
			  Float qta = Float.parseFloat(rigaScontrinoElement.getElementsByTagName("Quantity").item(0).getTextContent());
			  
			  rigaScontrino.setId_scontrino(testataScontrino.getId_scontrino());
			  rigaScontrino.setId_riga(id_riga);
			  rigaScontrino.setId_prodotto(id_prodotto);
			  rigaScontrino.setQta(qta);
			  rigaScontrino.setPrezzo(prezzo);
			  
			  righeScontrino.add(rigaScontrino);
		  }
		  
		    	  
		  return righeScontrino;
	  }
}
