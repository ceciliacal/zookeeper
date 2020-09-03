package org.apache.zookeeper;

import org.apache.zookeeper.cli.CliException;
import org.apache.zookeeper.test.ClientBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(value = Parameterized.class)
public class MyZookeeperGetChildrenTest extends ClientBase {

    private ZooKeeper zk;

    private boolean expectedRes;
    private String path;
    private boolean watch;

    public MyZookeeperGetChildrenTest(boolean expectedRes, String path, boolean watch){

        this.expectedRes=expectedRes;
        this.path=path;
        this.watch=watch;

    }

    @Before
    public void setup() throws Exception {

        zk = createClient();
        System.out.println(" \n----STO NEL BEFORE");

        //creazione znode

        zk.setData("/", "myTest".getBytes(), -1);
        zk.create("/path_01", "myTest".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        zk.create("/path_01/path_02", "myTest".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        zk.create("/path_01/path_02/path_03", "myTest".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        zk.create("/path_01/path_04", "myTest".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        zk.create("/path_01/path_05", "myTest".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        zk.create("/anotherPath_01", "myTest".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        zk.create("/anotherPath_01/anotherPath_02", "myTest".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        zk.create("/anotherPath_01/anotherPath_03", "myTest".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);

    }

    @Parameterized.Parameters
    public static Collection<?> getParameters(){
        System.out.println(" \n----STO NEI PARAMETRI");
        return Arrays.asList(new Object[][] {

                //expectedRes bool, string path, watch
                {true, "/anotherPath_01", false},
                {false, "/anotherPath01", false},
                {true, "/path_01", true},
                {false, null, true},

        });


    }

    @Test
    public void testDeleteRecursive() throws IOException, InterruptedException, CliException, KeeperException {

        System.out.println(" \n----STO NEL TEST");

        boolean res = true;
        List<String> children = null;


        System.out.println(" \n------path: "+path);

        try {

            children = zk.getChildren(path, watch);

        } catch (KeeperException e) {       //path errato
            System.out.println(" \n-------- STO DENTRO TRY CATCH");
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
        }


        if  (expectedRes){


            if (path.equals("/path_01")) {

                if ( (children.contains("path_02"))  && (children.contains("path_04")) && (children.contains("path_05")) ){

                    System.out.println(" \n--------path: "+path+"     children: "+children);
                    assertEquals(children.size(),3);

                }

            }
            else if (path.equals("/anotherPath_01")) {

                if ( (children.contains("anotherPath_02")) && (children.contains("anotherPath_03")) ){

                    System.out.println(" \n--------path: "+path+"     children: "+children);
                    assertEquals(children.size(),2);
                }

            }

            ;System.out.println(" \n-------- STO ALLA FINE");
            assertEquals(expectedRes,res);

        }


    }

    @After
    public void teardown() {
        if (zk!=null){
            try {
                zk.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
