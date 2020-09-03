package org.apache.zookeeper;

import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.test.ClientBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(value = Parameterized.class)

public class MyZookeeperSetDataTest extends ClientBase {

    SimpleWatcherHandler parentWatcher;
    SimpleWatcherHandler childWatcher;

    private ZooKeeper client;
    private List<Watcher.Event.EventType> events;

    private String path;
    private byte[] data;
    private int version;
    private boolean expectedRes;

    public MyZookeeperSetDataTest(boolean expectedRes, String path, byte[] data, int version){

        this.expectedRes=expectedRes;
        this.path=path;
        this.data=data;
        this.version=version;

    }

    @Parameterized.Parameters
    public static Collection<?> getParameters(){
        System.out.println(" \n----STO NEI PARAMETRI");
        return Arrays.asList(new Object[][] {

                //client.setData("/foo", "parent".getBytes(), -1);

                //boolean expectedRes, String path, byte[] data, int version
                {true, "/path01", "myTest".getBytes(), -1},
                {false, "/path50", "myTest".getBytes(), -1},
                {true, "/path01", null, -1},            //non vengono controllati i bytes
                {true, "/path01/path02", "myTest".getBytes(), -1},
                {false, "/path01/path02", "myTest".getBytes(), 1},
                {true, "/path01", "myTest".getBytes(), 0}, //qua non da bad version, quindi da false -> rimetto true


        });


    }

    @Before
    public void setup() throws Exception {

        client = createClient();
        events = new ArrayList<>();

        parentWatcher = new SimpleWatcherHandler(null);
        childWatcher = new SimpleWatcherHandler(null);

        //dopo aver creato i nodi, lascio degli watch su quest'ultimi (con getData)

        client.create("/path01", "parent".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        client.getData("/path01", parentWatcher, null);

        client.create("/path01/path02", "child".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        client.getData("/path01/path02", childWatcher, null);
    }

    @Test
    public void testGetDataSyncWObj()  {

        //setto dati del nodo al path foo. version -1 significa che matcha la versione
        //di qualsiasi nodo

        //se setData ha successo, triggera tutti i watch su quel nodo lasciati da getData
        //il trigger può generarsi per aver settato i dati del nodo o per averlo eliminato

        boolean res=false;
        boolean checkTrigger=false;

        Stat stat;

        try {


            stat = client.setData(path, data, version);
            if (stat!=null){
                res = true;
            }


        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        assertEquals(expectedRes,res);
        events.add(Watcher.Event.EventType.NodeDataChanged);

        //ora per vedere ulteriormente che setData ha avuto successo, vado a verificare che
        //abbia triggerato i watch presenti sui nodi


        System.out.println("\n----expected: "+ events);
        //System.out.println("\n----bytes da getData: "+client.getChildren("/path01",false));

        try {

            if (path.equals("/path01")){
                checkTrigger = parentWatcher.verify(events);

            }else if (path.equals("/path01/path02")) {
                checkTrigger = childWatcher.verify(events);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\n----res: "+checkTrigger);
        assertEquals(checkTrigger,expectedRes);
        //assertTrue(checkTrigger);

        events.clear();


    }
}
