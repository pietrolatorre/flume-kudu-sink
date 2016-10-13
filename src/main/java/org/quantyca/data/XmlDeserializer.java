package org.quantyca.data;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.SerializationUtils;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.serialization.EventDeserializer;
import org.apache.flume.serialization.ResettableInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class XmlDeserializer implements EventDeserializer {
	private static final Logger logger = LoggerFactory.getLogger(XmlDeserializer.class);

    private final ResettableInputStream in;
    private XMLStreamReader xmlStreamReader;
    private final Charset outputCharset;
    private final String nullData;
    private volatile boolean isOpen;
    private DateFormat dateFormat;
    public static final String OUT_CHARSET_KEY = "outputCharset";
    public static final String CHARSET_DFLT = "UTF-8";
    
    public static final String NULL_DATA = "nullData";
    public static final String NULL_DATA_DFLT = "\\N";
    
    public XmlDeserializer(Context context, ResettableInputStream in) {
    	this.dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        this.in = in;
        this.isOpen = true;
        this.outputCharset = Charset.forName(context.getString(OUT_CHARSET_KEY, CHARSET_DFLT));
        this.nullData = context.getString(NULL_DATA, NULL_DATA_DFLT);

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        try {
            xmlStreamReader = inputFactory.createXMLStreamReader(new FlumeInputStream(in));
            
        } catch (XMLStreamException e) {
            logger.error("createXMLStreamReader failed: " + e.getMessage());
            this.isOpen = false;
        }
    }
    public void mark() throws IOException {
        ensureOpen();
        in.mark();
    }

    public void reset() throws IOException {
        ensureOpen();
        in.reset();
    }

    public void close() throws IOException {
        if (isOpen) {
            reset();
            in.close();
            try {
                xmlStreamReader.close();
            } catch (XMLStreamException e) {
                logger.error("XML close error: " + e.getMessage());
            }
            isOpen = false;
        }
    }

    private Map<String, String> generateHeaders(List<String> keys, List<String> values) {
        Map<String, String> map = Maps.newHashMap();
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).equals("mts")) {
                Date d;
                try {
                    d = (new SimpleDateFormat("yyyyMMddHHmmssX").parse(values.get(i)));
                    map.put("timestamp", String.valueOf(d.getTime()));
                } catch (ParseException e) {
                    logger.error("Parse timestamp error: " + e.getMessage());
                }
            } else {
                map.put(keys.get(i), values.get(i));
            }
        }
        return map;
    }

    private void ensureOpen() {
        if (!isOpen) {
            throw new IllegalStateException("Serializer has been closed");
        }
    }

    public static class Builder implements EventDeserializer.Builder {
        public EventDeserializer build(Context context, ResettableInputStream in) {
            return new XmlDeserializer(context, in);
        }
    }

    public Event readEvent() throws IOException {
        logger.error("Reading a single event from a XML stream is not supported! Try readEvents()!");
        return null;
    }

    public List<Event> readEvents(int numEvents) throws IOException {    	
    	List<Event> events = Lists.newLinkedList();    	
    	Event objEvent;
    	TestataScontrino testataScontrino = new TestataScontrino();
        ensureOpen();
        boolean started=false;
        try {        	
            while (xmlStreamReader.hasNext()) {
            	int eventType = xmlStreamReader.next();

                switch (eventType) {
            	                
                case XMLStreamConstants.START_ELEMENT:
                    String currentElementName = xmlStreamReader.getName().getLocalPart();  
                    
                    if(!started){
                    	Date xmlParseStartDateTime = new Date();
                    	//testataScontrino.setXmlParseStartDateTime(dateFormat.format(xmlParseStartDateTime));
                    	started=true;
                    }
                    
                	if(currentElementName.equalsIgnoreCase("StoreID")) {	
                		String currentElementText = xmlStreamReader.getElementText();
                		testataScontrino.setStore_id(currentElementText);       			
					}
                	else if(currentElementName.equalsIgnoreCase("BusinessDate")) {	
                		String currentElementText = xmlStreamReader.getElementText();
                		testataScontrino.setBusiness_date(currentElementText);       			
					}
                	else if(currentElementName.equalsIgnoreCase("TotalAmount")) {	
                		String currentElementText = xmlStreamReader.getElementText();
                		testataScontrino.setTotal_amount(Float.parseFloat(currentElementText));      			
					}
                	else if(currentElementName.equalsIgnoreCase("TransactionNumber")) {	
                		String currentElementText = xmlStreamReader.getElementText();
                		testataScontrino.setTransaction_number(currentElementText);      			
					}
                	else if(currentElementName.equalsIgnoreCase("ExternalReferenceId")) {	
                		String currentElementText = xmlStreamReader.getElementText();
                		testataScontrino.setExternal_reference_id(currentElementText);      			
					}
                	else if(currentElementName.equalsIgnoreCase("TillID")) {	
                		String currentElementText = xmlStreamReader.getElementText();
                		testataScontrino.setPos_id(Integer.parseInt(currentElementText));      			
					}
                	
                    break;
                    
                case XMLStreamConstants.END_DOCUMENT:
                	System.out.println(testataScontrino);                    
	                Date xmlParseEndDateTime = new Date();
                	//testataScontrino.setXmlParseEndDateTime(dateFormat.format(xmlParseEndDateTime));
	            	testataScontrino.createIdScontrino();
                    objEvent = EventBuilder.withBody(SerializationUtils.serialize(testataScontrino));
                    events.add(objEvent);                  	
                	break;    
                }

            
                if (events.size() >= numEvents) {
                    break;
                }
                
                 
           }

    		            
        } catch (XMLStreamException e) {
            logger.error(e.getMessage());
    }

        return events;
   }

}