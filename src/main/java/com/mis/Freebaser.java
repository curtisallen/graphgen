package com.mis;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Queries freebase given id
 * @author curtis
 */
public class Freebaser {
    private final Logger logger = LoggerFactory.getLogger(Freebaser.class);
    private final String API_KEY = "AIzaSyBQcaeTrGRDWOIbWa3QyNi33jTR2aSM-pw";
    private Graph graph = null;
    private String searchId = null;
    private Vertex parentObject = null;
    /**
     * Create a freebaser graph
     * @param graphInstance 
     */
    public Freebaser(Graph graphInstance) {
        this.graph = graphInstance;
    }
    
    public String getAndProcessTopic(String key) throws IOException {
        
        Client client = Client.create();
        
        WebResource webResource = client.resource("https://www.googleapis.com/freebase/v1/topic" + key);
        webResource.accept(MediaType.APPLICATION_JSON);
       
        MultivaluedMap multiMap = new MultivaluedMapImpl();
        multiMap.add("key", API_KEY);
        
        String response = webResource.queryParams(multiMap).get(String.class);
//        logger.info("RAW: " + response);
        
        // start crawling the object tree
        ObjectMapper om = new ObjectMapper();
        
        Map<String, Object> resultMap = om.readValue(response, Map.class);
        logger.debug(response);
        
        searchId = (String) resultMap.get("id");
        // create parent object
        Vertex existingObject = graph.getVertex(searchId);
        if(existingObject == null) {
            // create object
            parentObject = graph.addVertex(searchId);
        }
        
        for(String mapKey: resultMap.keySet()) {
            logger.info("key: " + mapKey);
            Object value = resultMap.get(mapKey);
            
            if(value instanceof String) {
                logger.info("value: " + value);
            } else if(value instanceof LinkedHashMap) {
                processMaps(resultMap, 1, 10);

            }
        }
        
        return response;
    }
    
    /**
     * Recursive method that process maps
     * @param inputMap 
     */
    private void processMaps(final Map<String, Object> inputMap, final int count, final int maxDepth) {
        if(count <= maxDepth) {
//            logger.info("Count: {}, maxDepth: {}, Processing map: {}", count, maxDepth, inputMap.toString());
            for(String mapKey: inputMap.keySet()) {
                logger.info("key: " + mapKey);
                Object value = inputMap.get(mapKey);
                if(value instanceof String) {
                    logger.info("value: {}", value);
                } else if (value instanceof LinkedHashMap) {
                    // lets only process maps of type object
                    Map<String, Object> valueMap = (Map<String, Object>) value;
                    String valuetype = (String) valueMap.get("valuetype");
                    if(valuetype != null && valuetype.equals("object")) {
                        int c = count + 1;
//                        logger.info("Found object type");
                        // process object
                        addObject( (ArrayList<Map<String, Object>>)valueMap.get("values"));
                        processMaps(valueMap, c, maxDepth);
                    } else {
                        processMaps(valueMap, count, maxDepth);
                    }
                    
                }
            }
            
        }
    }

    private void addObject(ArrayList<Map<String, Object>> valuesList) {
        
        for(Map<String, Object> valueMap : valuesList) {
            String id = (String) valueMap.get("id"); // get the freebase id

            Vertex object = graph.getVertex(id);
            
            if (object == null) {
                // object desn't exist create it
                Vertex obj = graph.addVertex(id);
                obj.setProperty("freebaseId", id);
                
                // add all properties now
                for (String key : valueMap.keySet()) {
                    if (!key.equals("id")) {
                        obj.setProperty(key, valueMap.get(key).toString());
                    }
                }

                // add a edge to the parent
                graph.addEdge(UUID.randomUUID().toString(), parentObject, obj, "related");

            }    
        }
        
        
    }
}
