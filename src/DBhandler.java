import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

/**
 * Created by ImanH on 1/28/2018.
 * Seyed Iman Hosseini Zavaraki
 * Github @ https://github.com/ImanHosseini
 * Wordpress @ https://imanhosseini.wordpress.com/
 */
public class DBhandler {
    MainLoop ml;
    Connection cA;
    Connection cW;
    Connection cM;
    int msgsize;
    public DBhandler(MainLoop ml){
        this.ml=ml;
        cA = null;
        try {
            Class.forName("org.postgresql.Driver");
            cA = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/accounts",
                            "postgres", "iman1995");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened Accounts database successfully");
        // Set players
        try {
            Statement st = cA.createStatement();
            String str = "SELECT username,passwd,id FROM player;";
            ResultSet rs = st.executeQuery(str);
            if(rs!=null){
                while (rs.next()) {
                    String usr = rs.getString("username");
                    int id = Integer.parseInt(rs.getString("id"));
                    String pass = rs.getString("passwd");
                    Player p = new Player(ml,usr,pass,id);
                    ml.players.add(p);
                    ml.userPlayer.put(usr,p);
                    ml.userPass.put(usr,pass);
                    ml.widPlayer.put(id,p);
                }
                rs.close();
            }

            st.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }

        // Connect to world
        cW = null;
        try {
            Class.forName("org.postgresql.Driver");
            cW = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/world1",
                            "postgres", "iman1995");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened World1 database successfully");
        try{
           ml.world=new World(cW,ml);
            Gson gson = new GsonBuilder().create();
            gson.toJson(ml.world, System.out);
        }catch (Exception e){
            System.out.println(e.toString());
        }

        // Set Player credits
        try {
            Statement st = cW.createStatement();
            String str = "SELECT username,credit FROM player;";
            ResultSet rs = st.executeQuery(str);
            if(rs!=null){
                while (rs.next()) {
                    String usr = rs.getString("username");
                    int credit = Integer.parseInt(rs.getString("credit"));
                    ml.userPlayer.get(usr).setCredit(credit);
                }
                rs.close();
            }

            st.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
        // Get chats
        cM=null;
        try {
            Class.forName("org.postgresql.Driver");
            cM = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/chat",
                            "postgres", "iman1995");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
       // System.out.println("Opened chat database successfully");
        try {
            Statement st = cM.createStatement();
            String str = "SELECT * FROM msgs;";
            ResultSet rs = st.executeQuery(str);
            if(rs!=null){
                while (rs.next()) {
                    int sender_id = Integer.parseInt(rs.getString("sender_id"));
                    int rec_id = Integer.parseInt(rs.getString("rec_id"));
                    String txt = rs.getString("txt");
                    String date = rs.getString("datentime");
                    Player sender = ml.widPlayer.get(sender_id);
                    Player receiver = ml.widPlayer.get(rec_id);
                    sender.msgz.add(new Msg(sender_id,rec_id,txt,date,true));
                    receiver.msgz.add(new Msg(sender_id,rec_id,txt,date,false));
                   // System.out.println(sender+"\n"+rec+"\n"+txt+"\n"+date);
                }
                rs.close();
            }
            st=cM.createStatement();
            str = "SELECT COUNT(*) FROM msgs;";
            rs=st.executeQuery(str);
           // System.out.println(rs);
            if(rs.next()){

                msgsize=Integer.parseInt(rs.getString(1));
                System.out.println("number of msgs :"+Integer.toString(msgsize));
            }

            st.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }

    }

    public void addMsg(Msg m){
        try {
            Statement st = cM.createStatement();
            msgsize++;
            java.sql.Timestamp t = new java.sql.Timestamp(new Date().getTime());
            String str = "INSERT INTO msgs VALUES\n" +
                    "    ("+Integer.toString(msgsize)+","+Integer.toString(m.from)+","+Integer.toString(m.to)+","+"'"+m.txt+"'"+","+"'"+t.toString()+"'"+");";

            ResultSet rs = st.executeQuery(str);
            System.out.println("RS : "+rs);
            if(rs!=null){
                while (rs.next()) {
                    int sender_id = Integer.parseInt(rs.getString("sender_id"));
                    int rec_id = Integer.parseInt(rs.getString("rec_id"));
                    String txt = rs.getString("txt");
                    String date = rs.getString("datentime");
                    Player sender = ml.widPlayer.get(sender_id);
                    Player receiver = ml.widPlayer.get(rec_id);
                    sender.msgz.add(new Msg(sender_id,rec_id,txt,date,true));
                    receiver.msgz.add(new Msg(sender_id,rec_id,txt,date,false));
                    // System.out.println(sender+"\n"+rec+"\n"+txt+"\n"+date);
                }
                rs.close();
            }

            st.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }
    public String getHscore(){
        String ans="";
        try {
            Statement st = cW.createStatement();
            String str = "SELECT * FROM highscore;";
            ResultSet rs = st.executeQuery(str);
            if(rs!=null){
                while (rs.next()) {
                    ans+=rs.getString(1)+":"+rs.getString(2)+"\n";
                }
                rs.close();
            }


            st.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return ans;
    }
}
