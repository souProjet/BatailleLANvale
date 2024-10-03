
import java.util.Scanner;

import java.io.*;
import java.net.*;

public class BatailleLANvale
{
    //###############################################
    //                SOCKET CONFIG
    //###############################################
    static final String HOST = "xxxxxxxxxx.local";
    static final int PORT = 7777;

    static String[] ASCIIBanner = {
      "                                                                                                                                                                         ",
      "                                                                                                                                                                         ",
      "    ________________________________________________________________________________________________________________________________________________________________     ",
      "                                                                                                                                                                         ",
      "                                                                                                                                                                         ",
      "                                                                                            ,--,                                                                         ",
      "                                                                                         ,---.'|                           ,--.                                          ",
      "    ,---,.                ___                           ,--,    ,--,                     |   | :      ,---,              ,--.'|                        ,--,              ",
      "  ,'  .'  \\             ,--.'|_                ,--,   ,--.'|  ,--.'|                     :   : |     '  .' \\         ,--,:  : |                      ,--.'|              ",
      ",---.' .' |             |  | :,'             ,--.'|   |  | :  |  | :                     |   ' :    /  ;    '.    ,`--.'`|  ' :                      |  | :              ",
      "|   |  |: |             :  : ' :             |  |,    :  : '  :  : '                     ;   ; '   :  :       \\   |   :  :  | |     .---.            :  : '              ",
      ":   :  :  /  ,--.--.  .;__,'  /    ,--.--.   `--'_    |  ' |  |  ' |      ,---.          '   | |__ :  |   /\\   \\  :   |   \\ | :   /.  ./|   ,--.--.  |  ' |      ,---.   ",
      ":   |    ;  /       \\ |  |   |    /       \\  ,' ,'|   '  | |  '  | |     /     \\         |   | :.'||  :  ' ;.   : |   : '  '; | .-' . ' |  /       \\ '  | |     /     \\  ",
      "|   :     \\.--.  .-. |:__,'| :   .--.  .-. | '  | |   |  | :  |  | :    /    /  |        '   :    ;|  |  ;/  \\   \\'   ' ;.    ;/___/ \\: | .--.  .-. ||  | :    /    /  | ",
      "|   |   . | \\__\\/: . .  '  : |__  \\__\\/: . . |  | :   '  : |__'  : |__ .    ' / |        |   |  ./ '  :  | \\  \\ ,'|   | | \\   |.   \\  ' .  \\__\\/: . .'  : |__ .    ' / | ",
      "'   :  '; | ,\" .--.; |  |  | '.'| ,\" .--.; | '  : |__ |  | '.'|  | '.'|'   ;   /|        ;   : ;   |  |  '  '--'  '   : |  ; .' \\   \\   '  ,\" .--.; ||  | '.'|'   ;   /| ",
      "|   |  | ; /  /  ,.  |  ;  :    ;/  /  ,.  | |  | '.'|;  :    ;  :    ;'   |  / |        |   ,/    |  :  :        |   | '`--'    \\   \\    /  /  ,.  |;  :    ;'   |  / | ",
      "|   :   / ;  :   .'   \\ |  ,   /;  :   .'   \\;  :    ;|  ,   /|  ,   / |   :    |        '---'     |  | ,'        '   : |         \\   \\ |;  :   .'   \\  ,   / |   :    | ",
      "|   | ,'  |  ,     .-./  ---`-' |  ,     .-./|  ,   /  ---`-'  ---`-'   \\   \\  /                   `--''          ;   |.'          '---\" |  ,     .-./---`-'   \\   \\  /  ",
      "`----'     `--`---'              `--`---'     ---`-'                     `----'                                   '---'                   `--`---'              `----'   ",
      "                                                                                                                                                                         ",
      "    _________________________________________________________________________________________________________________________________________________________________    ",
      "                                                                                                                                                                         ",
      "                                                                                                                                                                         ",

    };


    static final int GRID_SIZE = 6;

    static DataOutputStream dos;
    static DataInputStream dis;
    static Socket s;
    static boss server;
    static String username;
    static boolean turn = false;
    static int navirePosLigne = 0;
    static int navirePosColonne = 0;
    static int enemyNavirePosLigne = -1;
    static int enemyNavirePosColonne = -1;

    static final String RESET = "\u001B[0m";
    static final String BLUE = "\u001B[1;34m";
    static final String RED = "\u001B[31m";
    static final String MAGENTA = "\u001B[35m";
    static final String CYAN = "\u001B[36m";
    static final String GREEN = "\u001B[32m";
    static final String WHITE = "\u001B[1;37m";

    static char[][] grid = new char[GRID_SIZE][GRID_SIZE];
    static char[][] enemyGrid = new char[GRID_SIZE][GRID_SIZE];
    static Scanner scan = new Scanner(System.in);
    static int choixLigne;
    static int choixColonne;
    static boolean waitBoatPlace;

    static final int TOTAL_CHAR_WIDTH = 237;

    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }  
  
    public static String repeat(int count, String with) {
        return new String(new char[count]).replace("\0", with);
    }

    public static void drawFrame(String text, String color, boolean rounded){
        text = text.toUpperCase();
        final int TEXT_LENGTH = text.length();
        final int PADDING = 4;
        final int FRAME_WIDTH = TEXT_LENGTH+2*PADDING;
        final String MARGIN = repeat((TOTAL_CHAR_WIDTH-FRAME_WIDTH)/2, " ");
        String topBar = MARGIN+(rounded ? "." : "-")+repeat(FRAME_WIDTH, "-")+(rounded ? "." : "-");
        String bottomBar = MARGIN+ (rounded ? "'" : "-")+repeat(FRAME_WIDTH, "-")+(rounded ? "'" : "-");

        System.out.println(color + topBar + RESET);
        System.out.println(color + MARGIN + "|"+repeat(FRAME_WIDTH, " ")+"|" + RESET);
        System.out.println(color + MARGIN + "|"+repeat(PADDING, " ")+text+repeat(PADDING, " ")+"|" + RESET);
        System.out.println(color + MARGIN + "|"+repeat(FRAME_WIDTH, " ")+"|" + RESET);
        System.out.println(color + bottomBar + RESET);
    }

    public static void printText(String text, String color){
        System.out.println();
        System.out.println(color + text.toUpperCase() + RESET);
        System.out.println();
    }
    public static void main(final String args[]) throws IOException
    {   

            String spaceMarginBanner = repeat(Math.round((TOTAL_CHAR_WIDTH - ASCIIBanner[0].length())/2), " ");
            for (String ASCIILine: ASCIIBanner){
                System.out.println(spaceMarginBanner + BLUE + ASCIILine + RESET);
            }
            drawFrame("choisissez votre pseudo :", CYAN, true);


            username = scan.nextLine();
          
            try
            {
                s = new Socket(HOST, PORT);
                dos = new DataOutputStream(s.getOutputStream());
                dis = new DataInputStream(s.getInputStream());

                server = new boss(dis);
                Thread t = new Thread(server);
                t.start();
                printText("En attente de l'adversaire...", CYAN);
            }

            catch(IOException e)
            {
                System.out.println("Le serveur n'est pas disponible. Appuyez sur Ctrl+C pour quitter...");
            }

          
    }

    public static void chooseBoatEmplacement(){
        
        drawFrame("Choisissez l'emplacement de votre bateau !", WHITE, false);

        do {
            drawFrame("Quelle ligne mon capitaine (grille de "+GRID_SIZE+"x"+GRID_SIZE+") ?", CYAN, false);
            navirePosLigne = scan.nextInt()-1;
            if(navirePosLigne < 0 || navirePosLigne > GRID_SIZE-1){
                printText("La ligne doit être un nombre compris entre 1 et "+GRID_SIZE, RED);
            }
        }
        while(navirePosLigne < 0 || navirePosLigne > GRID_SIZE-1);

        do {
            drawFrame("Quelle colonne mon capitaine ?", CYAN, false);

            navirePosColonne = scan.nextInt()-1;

            if(navirePosColonne < 0 || navirePosColonne > GRID_SIZE-1){
                printText("La colonne doit être un nombre compris entre 1 et "+GRID_SIZE, RED);
            }
        }
        while(navirePosColonne < 0 || navirePosColonne > GRID_SIZE-1);

        grid[navirePosLigne][navirePosColonne] = 'X';
        showGrid(true);

        try
        {
            dos.writeUTF(username + ":0:" + navirePosLigne + ':'+ navirePosColonne);
            
        }
        catch(IOException e){}
    }

    public static void oneRound(){
        do {
            drawFrame("Quelle ligne mon capitaine ?", CYAN, false);

            choixLigne = scan.nextInt()-1;
            if(choixLigne < 0 || choixLigne > GRID_SIZE-1){
                printText("La ligne doit être un nombre compris entre 1 et "+GRID_SIZE, RED);
            }    
        }
        while(choixLigne < 0 || choixLigne > GRID_SIZE-1);

        do {
            drawFrame("Quelle colonne mon capitaine ?", CYAN, false);

            choixColonne = scan.nextInt()-1;

            if(choixColonne < 0 || choixColonne > GRID_SIZE-1){
                printText("La colonne doit être un nombre compris entre 1 et "+GRID_SIZE, RED);
            }
            if(enemyGrid[choixLigne][choixColonne] == '#'){
                printText("L'endroit est déjà bombardé mon capitaine !", RED);

            }
        }
        while(choixColonne < 0 || choixColonne > GRID_SIZE-1 || enemyGrid[choixLigne][choixColonne] == '#');
        try
        {   
            clearScreen();
            if(choixLigne == enemyNavirePosLigne && choixColonne == enemyNavirePosColonne) {
                enemyGrid[choixLigne][choixColonne] = '#';
                showGrid(false);
                printText("Boom ! Touché coulé", GREEN);
                drawFrame("Félicitation ! vous avez gagné la partie !", GREEN, true);
                dos.writeUTF(username + ":2:"+ choixLigne + ':'+choixColonne);
                askRestartGame();
                return;
            } else {
                enemyGrid[choixLigne][choixColonne] = '#';
                printText("Plouf ! L'endroit est desert !", BLUE);
                dos.writeUTF(username + ":1:"+choixLigne + ':'+choixColonne);
                showGrid(false);
                printText("à l'adversaire de jouer", BLUE);
                printText("En attente de son coup...", BLUE);
                return;
            }
        }
        catch(IOException e){}
    }


    public static void firstRound(boolean turn){
        drawFrame("La partie peu commencer !", GREEN, false);

        if(turn) {
            printText("à vous de commencer !", GREEN);
            oneRound();
        } else{
            printText("à l'adversaire de jouer", BLUE);
            printText("En attente de son coup...", BLUE);
            return;
        }
    }

    public static void showGrid(boolean fill){
        final byte LINE_LENGTH = GRID_SIZE + 2*GRID_SIZE + (GRID_SIZE+1);
        final String LINE_STRING = repeat(LINE_LENGTH, "-");
        final String LINE_BLANK_STRING = repeat(LINE_LENGTH, " ");
        final String MY_GRID_TEXT = "Ma carte".toUpperCase();
        final String ENEMY_GRID_TEXT = "L'adversaire".toUpperCase();
        final int GAP = 40;
        final String MARGIN = repeat((TOTAL_CHAR_WIDTH - 2*LINE_LENGTH - GAP)/2, " ");
        System.out.println("\n\n\n");

        System.out.println(MARGIN + (LINE_BLANK_STRING.substring(0,(LINE_LENGTH-MY_GRID_TEXT.length())/2))+MY_GRID_TEXT+(LINE_BLANK_STRING.substring(0,(LINE_LENGTH-MY_GRID_TEXT.length())/2))+repeat(GAP, " ")+(LINE_BLANK_STRING.substring(0,(LINE_LENGTH-ENEMY_GRID_TEXT.length())/2))+ENEMY_GRID_TEXT+(LINE_BLANK_STRING.substring(0,(LINE_LENGTH-ENEMY_GRID_TEXT.length())/2))+"\n");
        System.out.println(MARGIN + LINE_STRING + repeat(GAP, " ") + LINE_STRING);
    
        
        for(int x = 0; x<GRID_SIZE; x++) {

            System.out.print(MARGIN.substring(0, MARGIN.length()-1));

            for (int y = 0; y<GRID_SIZE; y++) {

                if(fill && grid[x][y] != 'X'){

                    grid[x][y] = '?';

                }

                System.out.print(" | ");

                System.out.print(grid[x][y] == 'X' ? MAGENTA +"X"+ RESET : (grid[x][y] == '#' ? RED + "#" + RESET : grid[x][y]));	
            }

            System.out.print(" |"+repeat(GAP, " ").substring(0, GAP-1));

            for (int y = 0; y<GRID_SIZE; y++) {

                if(fill){

                    enemyGrid[x][y] = '?';

                }

                System.out.print(" | ");

                System.out.print(enemyGrid[x][y] == '#' ? RED + "#" + RESET : enemyGrid[x][y]);	

            }
            System.out.println(" |");
            System.out.println(MARGIN + LINE_STRING +repeat(GAP, " ")+ LINE_STRING);
        }
        System.out.println("\n\n\n");

    }



    public static void updateMessageArea(String msg)
    {
    
        String[] code = msg.split(":");
        
        String codeUsername = code[0];
       
        
        if(msg.equals("yourturn") || msg.equals("notyourturn")){
            
            chooseBoatEmplacement();

            turn = msg.equals("yourturn") ? true : false;

            if(enemyNavirePosLigne == -1 && enemyNavirePosColonne == -1){
                waitBoatPlace = true;
                printText("L'adversaire n'a pas termine de choisir l'emplacememnt de son bateau...", CYAN);
            } else {
                waitBoatPlace = false;
                firstRound(turn);
            }
        }else{

            
            int codeType = Integer.parseInt(code[1]);
            int codeX = Integer.parseInt(code[2]);
            int codeY = Integer.parseInt(code[3]);


            if(!codeUsername.equals(username)){
                switch (codeType) {
                    case 0:
                        enemyNavirePosLigne = codeX;
                        enemyNavirePosColonne = codeY;

                        if(waitBoatPlace){
                            waitBoatPlace = false;
                            firstRound(turn);
                            break;
                        }

                        break;
                        
                    case 1:
                        clearScreen();
                        printText(codeUsername + " a jouer ligne  "+(codeX+1)+", colonne "+(codeY+1), WHITE);
                        grid[codeX][codeY] = '#';

                        printText("Plouf ! L'endroit est desert !", BLUE);
                        showGrid(false);
                        printText("A vous de jouer hehe", BLUE);
                        oneRound();
                        break;
                        
                    case 2: 
                        clearScreen();
                        System.out.println(codeUsername + " a jouer ligne  "+(codeX+1)+", colonne "+(codeY+1));
                        printText(codeUsername + " a jouer ligne  "+(codeX+1)+", colonne "+(codeY+1), WHITE);
                        printText("Boom ! Touché coulé", MAGENTA);
                        drawFrame("  "+codeUsername+"  ! à gagné la partie !  ", MAGENTA, true);

                        askRestartGame();
                        break;
                        
                    default:
                        break;
                }
            }

        }

    }

    public static void askRestartGame(){
        String reponse = "";
        scan.nextLine();
        try
        { 
        dos.writeUTF("disconnect");
        System.out.println("Voulez-vous rejouer une partie (oui/non) ?");
        reponse = scan.nextLine();
        if(reponse.equals("oui")){
            try
            {
                s = new Socket(HOST, PORT);
                dos = new DataOutputStream(s.getOutputStream());
                dis = new DataInputStream(s.getInputStream());

                server = new boss(dis);
                Thread t = new Thread(server);
                t.start();
                printText("En attente de l'adversaire...", CYAN);
            }

            catch(IOException e)
            {
                System.out.println("Le serveur n'est pas disponible. Appuyez sur Ctrl+C pour quitter...");
            }
        } else{
            System.out.println("#################################################################");
            System.out.println("#######################    Au revoir !    #######################");
            System.out.println("#################################################################");


            try{
                Thread.sleep(1000);
                System.exit(0);
            } catch(InterruptedException e){ }
            
        }
        } catch(IOException e){}
    }


    public static void reconnect()
    {
        try
        {
            s.close();
            s = new Socket(HOST, PORT);
            dos = new DataOutputStream(s.getOutputStream());
            dis = new DataInputStream(s.getInputStream());
            server = new boss(dis);
            Thread newConnection = new Thread(server);
            newConnection.start();

        }
        catch(Exception e)
        {
            System.out.println("Erreur dans reconnect().");
        }
    }

}



class boss extends Thread
{
    DataInputStream disServer;
    String secretCode = new String("46511231dsfdsfsd#@$#$#@^$%#@*$#^");

    public boss(DataInputStream z)
    {
        disServer = z;
    }

    public void run()
    {



        while(true)
        {
            try
            {
                String str = disServer.readUTF();
                BatailleLANvale.updateMessageArea(str);
            }

            catch(IOException e)
            {
                System.out.println("Erreur dans la methode d'execution. Reconnexion.");

                BatailleLANvale.reconnect();
                break;
            }

        }
    }
}