package com.mis;

import be.datablend.blueprints.impls.mongodb.MongoDBGraph;
import com.tinkerpop.blueprints.Graph;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author curtis
 */
public class FreebaserTest {
    
    private static Graph currentGraph;
    
    public FreebaserTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        currentGraph = new MongoDBGraph("localhost", 27017);
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getAndProcessTopic method, of class Freebaser.
     */
    @Test
    public void testGetTopic() throws Exception {
        System.out.println("getTopic");
        String key = "/en/christopher_nolan";
        Freebaser instance = new Freebaser(currentGraph);
        
        String result = instance.getAndProcessTopic(key);
        
    }
}
