import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by ImanH on 1/28/2018.
 * Seyed Iman Hosseini Zavaraki
 * Github @ https://github.com/ImanHosseini
 * Wordpress @ https://imanhosseini.wordpress.com/
 */
public class MainLoop {
    public DBhandler dbh;
    public NEThandler neth;
    public final long TIMEOUT=(long)(1.0E10);
    public World world;
    public ArrayList<Player> players;
    public boolean running;
    public HashMap<Integer,Player> keyz;
    public HashMap<String,String> userPass;
    public HashMap<String,Player> userPlayer;
    public HashMap<Integer,Player> widPlayer;
    public MainLoop(){
        this.widPlayer=new HashMap<>();
        this.keyz=new HashMap<>();
        this.userPlayer=new HashMap<>();
        this.players=new ArrayList<>();
        this.userPass=new HashMap<>();
        this.running=true;
        dbh=new DBhandler(this);
        neth=new NEThandler(this);
         neth.start();
    }
}
