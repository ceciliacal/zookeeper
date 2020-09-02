package org.apache.zookeeper.common;

import org.apache.zookeeper.test.ClientBase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProvaTest extends ClientBase {
    @Before
    public void setup() throws Exception {
        super.setUp();
    }

    @Test
    public void test(){
        assertEquals(2,2);
    }
}
