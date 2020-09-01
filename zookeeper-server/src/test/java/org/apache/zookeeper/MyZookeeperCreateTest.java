package org.apache.zookeeper;

import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.test.ClientBase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MyZookeeperCreateTest extends ClientBase {

    private ZooKeeper zk;
    /*
    Every node in a ZooKeeper tree is refered to as a znode.
    Znodes maintain a stat structure that includes version numbers for data changes,
     acl changes. The stat structure also has timestamps.
     The version number, together with the timestamp allow ZooKeeper
      to validate the cache and to coordinate updates.

    testo creazione di un zk sia con stat che senza stat
     */

    @Before
    public void setUp() throws Exception {
        super.setUp();
        zk = createClient();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        zk.close();
    }

    @Test
    public void stupidTest(){
        assertEquals(2,2);
    }
    /*
    @Test
    public void testCreateWithNullStat2()
            throws IOException, KeeperException, InterruptedException {
        String name = "/foo";
        Assert.assertNull(zk.exists(name, false));

        Stat stat = null;
        // If a null Stat object is passed the create should still
        // succeed, but no Stat info will be returned.
        String path = zk.create(name, name.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, stat);
        Assert.assertNull(stat);
        Assert.assertNotNull(zk.exists(name, false));
    }

    @Test(timeout = 30000)
    public void testCreateWithNullStat()
            throws KeeperException, InterruptedException {
        final String name = "/foo";
        Assert.assertNull(zk.exists(name, false));

        Stat stat = null;
        // If a null Stat object is passed the create should still
        // succeed, but no Stat info will be returned.
        zk.create(name, name.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.CONTAINER, stat);
        Assert.assertNull(stat);
        Assert.assertNotNull(zk.exists(name, false));
    }
    */

}
