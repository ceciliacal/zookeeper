package org.apache.zookeeper;

import org.apache.zookeeper.test.ClientBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(value = Parameterized.class)
public class MyZookeeperRemoveWatchesTest extends ClientBase {

    private ZooKeeper zk = null;
    private ClientBase.CountdownWatcher watcher  = new ClientBase.CountdownWatcher();

    private String path;
    private boolean validWatcher;
    private Watcher.WatcherType watcherType;
    private boolean local;
    private boolean expectedRes;

    @Parameterized.Parameters
    public static Collection<?> getParameters(){
        return Arrays.asList(new Object[][] {

                //expectedRes, string path, watch, watcherType, local bool

                {true, "/noWatchPath", true, Watcher.WatcherType.Data, false },
                {true, null, true, Watcher.WatcherType.Data, false },
                {true, "/noWatchPath", false, Watcher.WatcherType.Data, false },
                {false, "/noWatchPath",true, null, false },
                {true, "/noWatchPath", true, Watcher.WatcherType.Children, true },

        });
    }

    public MyZookeeperRemoveWatchesTest (boolean expectedRes,String path, boolean validWatcher, Watcher.WatcherType watcherType, boolean local){

        this.expectedRes=expectedRes;
        this.validWatcher=validWatcher;
        this.watcherType=watcherType;
        this.path=path;
        this.local=local;

    }


    @Before
    public void setup() throws Exception {

        //super.setUp();
        zk = createClient(watcher, hostPort, CONNECTION_TIMEOUT);

    }


    @After
    public void teardown() throws Exception {

        if (zk != null)
            zk.close();
       // super.tearDown();

    }

    //test che prova a rimuovere watcher che non esiste, e quindi il server torna codice d'errore
    @Test
    public void myTest()  {

        boolean res = false;
        System.out.println(" \n-------- STO NEL TEST");

        try {
            watcher.waitForConnected(CONNECTION_TIMEOUT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


        try {

            System.out.println(" \n------NELL' IF -> validWatcher: "+validWatcher);
            if (validWatcher){
                zk.removeWatches(path , watcher, watcherType, local);
            }
            else {
                zk.removeWatches(path , null, watcherType, local);
            }

        } catch (KeeperException e) {   //NoWatcherException perché watcher che matcha i parametri non esiste
            System.out.println(" \n-------- STO NEL CATCH KEEPER (OK)");

            if (e.code().intValue() == KeeperException.Code.NOWATCHER.intValue()) {

                res = true;     //se res = true, vuol dire che ho codice d'errore quindi test è OK

            }

        } catch (IllegalArgumentException e) {     //path non è valido o watcher è null
            System.out.println(" \n-------- STO NEL CATCH ILLEGAL (WATCHER è NULL)");
            res = true;

        } catch ( InterruptedException e ){  //transazione del server viene interrotta quindi test fallisce
            System.out.println(" \n-------- STO NEL CATCH INTERR ");
            res = false;

        } catch (NullPointerException e){
            res = false;
        }

        System.out.println(" \n-------- STO ALLA FINE");
        System.out.println(" \n----- expected ="+expectedRes+"     res="+res);
       // assertTrue(res);
        assertEquals(expectedRes,res);
    }


}



