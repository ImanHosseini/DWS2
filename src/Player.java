import java.util.ArrayList;

/**
 * Created by ImanH on 1/28/2018.
 * Seyed Iman Hosseini Zavaraki
 * Github @ https://github.com/ImanHosseini
 * Wordpress @ https://imanhosseini.wordpress.com/
 */
public class Player {
    public MainLoop ml;
    public int key;
    public String userName;
    public int wID;
    public boolean online;
    public long lastHiDate;
    public String passwd;
    public int credit;
    public Cordinate centre;
    public ArrayList<Msg> msgz;
    public ArrayList<Msg> new_msgz;
    public Player(MainLoop ml,String username,String passwd,int wID){
        msgz=new ArrayList<>();
        new_msgz=new ArrayList<>();
        this.ml=ml;
        this.userName=username;
        this.passwd=passwd;
        this.wID=wID;
        this.online=false;
        lastHiDate=-1L;
        this.key=-1;
        this.credit=0;
    }
    public void setCredit(int cr){
        this.credit=cr;
    }
    public void setCentre(Cordinate c){
        this.centre=c;
    }
    public void addMsg(Msg m){
        msgz.add(m);
        if(!m.sent){
            new_msgz.add(m);
        }
    }
}
