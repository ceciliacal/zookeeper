package org.apache.zookeeper.test;

import org.apache.zookeeper.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(value = Parameterized.class)

public class MyZookeeperMainCreateQuotaTest  extends ClientBase{

    /**
     * this method creates a quota node for the path
     * @param zk the ZooKeeper client
     * @param path the path for which quota needs to be created
     * @param bytes the limit of bytes on this path
     * @param numNodes the limit of number of nodes on this path
     * @return true if its successful and false if not.
     */

    private ZooKeeper zk;

    private boolean zkIsValid;
    private String path;
    private long bytes;
    private int numNodes;
    private boolean expectedRes;


    public MyZookeeperMainCreateQuotaTest(boolean expectedRes, boolean zk, String path, long bytes, int numNodes){
        //METI CASO IN CUI BYTE = -1L, NUMNODES = -1 (ANCHE -2)

        //path che non esiste : valid, non valid
        //long : -2, -1, 0, 1, 10 [-1,0,1]
        //zk: valid, null
        //byte= -1L, 0, 1L


        this.expectedRes=expectedRes;
        this.zkIsValid=zk;
        this.path=path;
        this.bytes=bytes;
        this.numNodes=numNodes;


    }

    @Parameterized.Parameters
    public static Collection<?> getParameters(){
        System.out.println(" \n----STO NEI PARAMETRI");
        return Arrays.asList(new Object[][] {

                //boolean expectedRes, ZooKeeper zk, String path, long bytes, int numNodes

                {true, true, "/path_01/path_02", 1L, 1},       //POI METTI 1L !!!
                {true, true, "/path_01/path_02", -1L, 1},     //byte non vengono controllati effettivamente, quindi da false ->rimetto true
                {false, true, "/path_04", 1L, 10},
                {true, true, "/path_01/path_02", 1L, -1},      //numNodes non viene controllato effettivamente, quindi da false ->rimetto true
                {true, true, "/path_01/path_02", 0, -1},
                {false, false, "/path_01/path_02", 0, -1},
                {true, true, "/path_01/path_02", 5L, 0},
        });


    }



    @Before
    public void setup() throws KeeperException, InterruptedException, IOException {

        zk = createClient();

        zk.setData("/", "myTest".getBytes(), -1);
        zk.create("/path_01", "myTest".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);

        zk.create("/path_01/path_02", "myTest".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);

        zk.create("/path_01/path_02/path_03", "myTest".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);


    }



    @Test
    public void testQuota() {

        boolean res=false;

        try{
            if (zkIsValid){
                res = ZooKeeperMain.createQuota(zk, path, bytes, numNodes);
            }
            else {
                res = ZooKeeperMain.createQuota(null, path, bytes, numNodes);

            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }

        assertEquals(expectedRes,res);


    }
}
