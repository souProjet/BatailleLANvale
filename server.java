import java.io.*;
import java.net.*;

public class server
{
    static ServerSocket ss;
    static DataInputStream dis;
    static DataOutputStream dos;
    public static user users[] = new user[20];
    public static int totalClientsOnline=0;
    public static int randomTurn;

    

    public static int randomInt(int Min, int Max)
    {
        return (int) (Math.random()*(Max-Min))+Min;
    }

    public static void main(String args[]) throws Exception
    {
        try
        {
            ss = new ServerSocket(7777);
            System.out.println("[BATAILLE NAVALE] Socket started on port 7777");

            for(int i=0;i<users.length;i++) {
                users[i] = new user(i+1,ss.accept());
                totalClientsOnline++;

                if(totalClientsOnline%2 == 0){
                    randomTurn = randomInt(totalClientsOnline-2, totalClientsOnline);
                    users[randomTurn].sendMessage("yourturn");
                    users[randomTurn%2 == 0 ? randomTurn+1 : randomTurn-1].sendMessage("notyourturn");

                }
            }
        }

        catch(Exception e)
        {
            System.out.println("Exception caught in main due to user connection loss...");
        }
    }


    public void sendMessageToAll(String msg)
    {
        for(int c=0;c<totalClientsOnline;c++)
        {
        try
        {
            
            users[c].sendMessage(msg);
        }

        catch(Exception e){}
        }
    }
}


class user extends Thread
{
    server tirth = new server();
    int userID;
    public Socket userSocket;
    public DataInputStream userDIS;
    public DataOutputStream userDOS;
    public Thread t;
    OutputStream os;


    public user(int id,Socket a)
    {
        try
        {
            userID = id;
            userSocket = a;
            userDIS = new DataInputStream(userSocket.getInputStream());
            userDOS = new DataOutputStream(userSocket.getOutputStream());
            System.out.println("Le joueur "+userID+" a rejoint la partie");
           

            t = new Thread(this);
            t.start();
        }
        catch(Exception e)
              {
                  System.out.println("Exception caught in constructor.");
              }
    }


    public void run()
    {
        String message;
        while(true)
        {
            try
            {
                message = userDIS.readUTF();
                if(message.equals("disconnect")){
                    System.out.println("Deconnexion du joueur "+ userID);
                    userSocket.close();
                } else {
                    tirth.sendMessageToAll(message);
                }
            }
           
            catch(Exception e)
            {

            }

        }
    }


    public void sendMessage(String s)
    {
        try
        {
            userDOS.writeUTF(s);
        }

        catch(Exception e){}
    }
    
}


