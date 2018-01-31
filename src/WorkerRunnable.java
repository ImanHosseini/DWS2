/**
 * Created by ImanH on 12/3/2017.
 * Seyed Iman Hosseini Zavaraki
 * Github @ https://github.com/ImanHosseini
 * Wordpress @ https://imanhosseini.wordpress.com/
 */



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

/**

 */
public class WorkerRunnable implements Runnable{

    protected Socket clientSocket = null;
    protected String serverText   = null;
    MainLoop ml;

    public WorkerRunnable(Socket clientSocket, String serverText,MainLoop ml) {
        this.ml=ml;
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
        System.out.println("ADDRESS IS:"+  clientSocket.getInetAddress());
    }

    public void run() {
        System.out.println("hi");
        try {
            Scanner input =
                    new Scanner(clientSocket.getInputStream());
            String inputString=input.nextLine();
            // ml.dbQueue=inputString;
            System.out.println("input:"+inputString);
            if (inputString.startsWith("GET")){
                try {

                   String content = "";
                   content=ml.dbh.getHscore();
                    PrintWriter output =
                            new PrintWriter(
                                    clientSocket.getOutputStream(), true);
                    System.out.println(content);
                    output.print(content);

                    output.close();
                }catch (Exception e){
                    System.out.println(e.toString());
                }
                input.close();
            }
            else if(inputString.split("\\*").length>0){
                handle(inputString.split("\\*")[0],inputString);
                input.close();
            }
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }
    public void handle(String action,String input){
        switch (action){
            case "SIGNIN":
                handleSignIn(input);
                break;
            case "MAP":
                handleMapReq(Integer.parseInt(input.split("\\*")[1]));
                break;
            case "UPD":
                handleUpd(Integer.parseInt(input.split("\\*")[1]));
                break;
            case "MSG":
                handleMsgReq(input);
                break;
        }
    }
    public void handleMsgReq(String input){
        String[] splitz=input.split("\\*");
        Player sender = ml.keyz.get(Integer.parseInt(splitz[1]));
        Player receiver = ml.widPlayer.get(Integer.parseInt(splitz[2]));
        java.sql.Timestamp t = new java.sql.Timestamp(new Date().getTime());
        Msg mSender = new Msg(sender.wID,receiver.wID,splitz[3],t.toString(),true);
        Msg mRec= new Msg(sender.wID,receiver.wID,splitz[3],t.toString(),false);
        sender.addMsg(mSender);
        receiver.addMsg(mRec);
        ml.dbh.addMsg(mSender);
    }
    public void handleUpd(int key){
        if (!ml.keyz.containsKey(key)) return;
        Gson gson = new GsonBuilder().create();
        Player p = ml.keyz.get(key);
        String ans = "UPD$"+p.credit+"$"+gson.toJson(ml.world)+"$";

        // System.out.println("ANS:"+ans);
        for(Msg m:p.new_msgz){
            String dat=(m.sent) ? Integer.toString(m.to) : Integer.toString(m.from);
            dat+=(m.sent) ? "%S" : "%R";
            dat+="%"+m.txt+"%";
            // System.out.println(m.dtime.split("\\.")[0]);
            dat+=m.dtime.split("\\.")[0]+"*";
            ans+=dat;
        }
        p.new_msgz.clear();
        //ans+="$"+ml.keyz.get(key).credit;
        try {
            PrintWriter output =
                    new PrintWriter(
                            clientSocket.getOutputStream(), true);

            output.println(ans);
            ml.keyz.get(key).lastHiDate=System.nanoTime();
            output.close();
            System.out.println("Request processed: UpdReq");
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public void handleSignIn(String input) {
        try {
            PrintWriter output =
                    new PrintWriter(
                            clientSocket.getOutputStream(), true);
            String[] inputsp = input.split("\\*");
            if(ml.userPass.containsKey(inputsp[1]) && ml.userPass.get(inputsp[1]).equals(inputsp[2])){
                int nkey=new Random().nextInt();
                while(ml.keyz.containsKey(nkey)){
                    nkey=new Random().nextInt();
                }
                ml.keyz.put(nkey,ml.userPlayer.get(inputsp[1]));
                Player p=ml.userPlayer.get(inputsp[1]);
                String ans="OK*" + nkey+"*"+p.credit+"*"+p.centre.toString()+"$";
                System.out.println(ans);
                for(Msg m:p.msgz){
                    String dat=(m.sent) ? Integer.toString(m.to) : Integer.toString(m.from);
                    dat+=(m.sent) ? "%S" : "%R";
                    dat+="%"+m.txt+"%";
                   // System.out.println(m.dtime.split("\\.")[0]);
                    dat+=m.dtime.split("\\.")[0]+"*";
                    ans+=dat;
                }
                output.println(ans);
            } else {
                output.println("FAIL");
            }

            output.close();
            System.out.println("Request processed: SignIn");
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public void handleMapReq(int key){
        if (!ml.keyz.containsKey(key)) return;
        Gson gson = new GsonBuilder().create();
        String ans = "MAP$"+gson.toJson(ml.world)+"$";
        System.out.println("ANS:"+ans);
        for(Integer k : ml.widPlayer.keySet()){
            String usname= ml.widPlayer.get(k).userName;
            ans+=k.toString()+"-"+usname+"*";
        }
        //ans+="$"+ml.keyz.get(key).credit;
        try {
            PrintWriter output =
                    new PrintWriter(
                            clientSocket.getOutputStream(), true);

            output.println(ans);
            ml.keyz.get(key).lastHiDate=System.nanoTime();
            output.close();
            System.out.println("Request processed: MapReq");
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

}