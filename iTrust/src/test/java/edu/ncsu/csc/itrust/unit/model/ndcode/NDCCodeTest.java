package edu.ncsu.csc.itrust.unit.model.ndcode;

import org.junit.Assert;

import edu.ncsu.csc.itrust.model.ndcode.NDCCode;
import junit.framework.TestCase;

public class NDCCodeTest extends TestCase {
    
    public void testEverything(){
        NDCCode nd = new NDCCode();
        nd.setCode("test");
        Assert.assertEquals("test", nd.getCode());
        
        nd.setDescription("test2");
        Assert.assertEquals("test2", nd.getDescription());
    }
}
