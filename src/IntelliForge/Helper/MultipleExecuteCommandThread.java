package IntelliForge.Helper;

/**
 * Created by mincrmatt12. Do not copy this or you will have to face
 * our legal team. (dmf444)
 */
public class MultipleExecuteCommandThread extends Thread {

    ExecuteCommandThread[] threads;

    Runnable r;

    public MultipleExecuteCommandThread(ExecuteCommandThread... thread){
        threads = thread;
    }

    public MultipleExecuteCommandThread(Runnable onEnd, ExecuteCommandThread...threads){

        this.threads = threads;
        r = onEnd;

    }

    public void run(){
        for (ExecuteCommandThread t : threads){
            t.run();
        }
        r.run();
    }


}
