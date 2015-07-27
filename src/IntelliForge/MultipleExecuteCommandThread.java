package IntelliForge;

/**
 * Created by mincrmatt12. Do not copy this or you will have to face
 * our legal team. (dmf444)
 */
public class MultipleExecuteCommandThread extends Thread {

    ExecuteCommandThread[] threads;

    public MultipleExecuteCommandThread(ExecuteCommandThread... thread){
        threads = thread;
    }

    public void run(){
        for (ExecuteCommandThread t : threads){
            t.run();
        }
    }


}
