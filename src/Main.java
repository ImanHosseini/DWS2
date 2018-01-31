/**
 * Created by ImanH on 1/28/2018.
 * Seyed Iman Hosseini Zavaraki
 * Github @ https://github.com/ImanHosseini
 * Wordpress @ https://imanhosseini.wordpress.com/
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

public class Main {
    public static void main(String[] args){
        MainLoop mainloop= new MainLoop();
        java.sql.Timestamp t = new java.sql.Timestamp(new Date().getTime());
        mainloop.dbh.addMsg(new Msg(1,2,"hi", t.toString(),true));
        while(mainloop.running){

        }
    }
}
