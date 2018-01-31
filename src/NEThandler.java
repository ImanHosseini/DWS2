/**
 * Created by ImanH on 1/28/2018.
 * Seyed Iman Hosseini Zavaraki
 * Github @ https://github.com/ImanHosseini
 * Wordpress @ https://imanhosseini.wordpress.com/
 */
public class NEThandler extends Thread{
    MainLoop ml;
    public NEThandler(MainLoop ml){
        this.ml=ml;
    }
    public void run(){
        MultiThreadedServer server = new MultiThreadedServer(1234,ml);
        new Thread(server).start();
    }
}
