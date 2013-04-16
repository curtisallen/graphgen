package com.mis;

import be.datablend.blueprints.impls.mongodb.MongoDBGraph;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.QueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author curtis
 */
@Path("/graphgen")
public class GraphGen {

    private final Logger logger = LoggerFactory.getLogger(GraphGen.class);
    
    private static Graph currentGraph = new MongoDBGraph("localhost", 27017);
    
    @GET
    @Produces("text/plain")
    public Response createGraph(@QueryParam("id") String id) {
        
        Freebaser freebaser = new Freebaser(currentGraph);
        String key = id;// + "?filter=%2Fpeople";
        try {
            freebaser.getAndProcessTopic(key);
        } catch(Exception ex) {
            
        }
        
        return Response.ok("Your awesome").build();
    }
    
    @DELETE
    public void deleteGraph() {
        if (currentGraph != null) {
            logger.info("Clearing the graph");
            for (Edge edge : currentGraph.getEdges()) {
                currentGraph.removeEdge(edge);
            }

            for (Vertex vertex : currentGraph.getVertices()) {
                currentGraph.removeVertex(vertex);
            }
        }
    }
}
