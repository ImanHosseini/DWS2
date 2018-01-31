import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by ImanH on 1/28/2018.
 * Seyed Iman Hosseini Zavaraki
 * Github @ https://github.com/ImanHosseini
 * Wordpress @ https://imanhosseini.wordpress.com/
 */
public class World {
    ArrayList<Castle> castles;
    ArrayList<Troop> troops;
    ArrayList<Resource> resources;
    public World(Connection c,MainLoop ml) throws Exception{
        castles=new ArrayList<>();
        troops=new ArrayList<>();
        resources=new ArrayList<>();
        // Resources
        Statement st =c.createStatement();
        String str="SELECT * FROM resource;";
        ResultSet rs =  st.executeQuery(str);
        if(rs!=null){
            while(rs.next()){
                //  System.out.println(rs.getString(5));

                int oid = -1;
                if(rs.getString("r_owner")!=null){
                    oid=Integer.parseInt( rs.getString("r_owner"));
                }
                String type=rs.getString("r_type");
                String pos_string = rs.getString("r_pos");
                Integer val=Integer.parseInt(rs.getString("r_val"));
                String[] pos = pos_string.substring(1,pos_string.length()-1).split(",");
                switch (type){
                    case "Wood": resources.add(new Wood(new Cordinate(pos),val,oid));
                        break;
                    case "Gold": resources.add(new Gold(new Cordinate(pos),val,oid));
                        break;
                }
            }
            rs.close();
        }


        st.close();
        // Now lets do Castles!
         st =c.createStatement();
         str="SELECT * FROM castle;";
         rs =  st.executeQuery(str);
         if(rs!=null){
             while(rs.next()){
                 System.out.println(rs.getString(2));

                 int oid=Integer.parseInt( rs.getString("c_owner"));

                 String pos_string = rs.getString("pos");
                 String[] pos = pos_string.substring(1,pos_string.length()-1).split(",");
                Cordinate co=new Cordinate(pos);
                 castles.add(new Castle(co,oid));
                 ml.widPlayer.get(oid).centre=co;

             }
             rs.close();
         }


        st.close();
        // Now lets do Troops!
        st =c.createStatement();
        str="SELECT * FROM troop;";
        rs =  st.executeQuery(str);
        if(rs!=null){
            while(rs.next()){
                //  System.out.println(rs.getString(5));

                int oid=Integer.parseInt( rs.getString("t_owner"));
                String type=rs.getString("t_type");
                String pos_string = rs.getString("pos");
                String[] pos = pos_string.substring(1,pos_string.length()-1).split(",");
                switch (type){
                    case "FBSD": troops.add(new Fbsd(new Cordinate(pos),oid));
                        break;
                    case "LNX": troops.add(new Lnx(new Cordinate(pos),oid));
                        break;
                }

            }
            rs.close();
        }


        st.close();
    }
}
