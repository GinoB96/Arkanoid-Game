import JGame.*;
import RankingDB.RankingDB;
import java.awt.*;
import java.awt.event.*; //eventos

import java.awt.image.*;  //imagenes
import javax.imageio.*; //imagenes

import java.awt.Graphics2D;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;

import java.util.*;
import java.text.*;
import javax.swing.ImageIcon;



public class Arkanoid extends JGame {
    //extras
    Date dInit = new Date();
    Date dAhora;
    SimpleDateFormat ft = new SimpleDateFormat ("mm:ss");
    //elementos del Update
    final double NAVE_DESPLAZAMIENTO=200.0;
     double BOLA_DESPLAZAMIENTO=100.0;

    boolean chocoTop=false;
    boolean chocoBottom=false;
    boolean chocoL=false;
    boolean chocoR=false;
    boolean chocoNave=true;
    boolean pausarBola=true;
    boolean catchBonu=false;
    boolean enlargeBonu=false;
    boolean duplicate=false;
    boolean bypass=false;
    boolean pausaNivel=false;
    boolean menu=true;
    boolean gano=false;
    boolean gameOver = false;
    boolean ranking=false;
    boolean pararJuego=false;//gino
    
    Vector <String> lista = new Vector();

    int fondoAncho=185;
    int fondoAlto=176;
    int tamanioNave=50;
    
    int posY=30;
    int posYGano=60;//gino

    
    double orientacion=0;
    TipoBonus tipoBonus=new TipoBonus();
    Vector <Bonus> vectorBonus;

    long dateDiff;
    long diffSeconds;
    long diffMinutes;
    long tiempoActual=0;
    double aceleracion=-50;
    double velocidad=0.0;
   
    //elementos graficos
    BufferedImage img_fondo = null;
    BufferedImage fondoMenu = null;
    BufferedImage naveN;
    BufferedImage naveG;
    BufferedImage naveLaser;
    BufferedImage salida;
    BufferedImage titulo;
    BufferedImage startGame;
    BufferedImage play;
    BufferedImage exit;
    Image bucleSalida;
    Image habreSalida;
    Image gifMenu;
    URL url;
    Nave nave;
    private int naveIzq;
    private int naveDer;
    Vector <Bola> vectorBola;

    //arkanoid
    Nivel nivel;
    Jugador jugador;
    RankingDB rdb;
    //AjustesDeJuego aj;//maty
    
    public static void main(String[] args) {
        Arkanoid game = new Arkanoid();
        game.run(1.0 / 60.0);
        System.exit(0);
    }

    public Arkanoid() {
        super("DemoJuego02",800,600);//, 176*2+300, 215*2+25);
        System.out.println(appProperties.stringPropertyNames());
        this.pausar();
    }

    @Override
    public void gameStartup() { //Inicializa todos los elementos graficos que se utilizaran en el juego
        try{
            menu=true; //gino
            nivel=new Nivel();
            jugador=new Jugador();
            rdb=new RankingDB();
            //aj=new AjustesDeJuego();//maty
            
            this.appProperties = new Properties();//maty
            FileInputStream in = new FileInputStream("jgame.properties");
            appProperties.load(in);
            
            naveIzq=(char)(appProperties.getProperty("teclaIzquierda").charAt(0))-32;
            naveDer=(char)(appProperties.getProperty("teclaDerecha").charAt(0)-32);
            System.out.println("naveIzq "+naveIzq+"     "+KeyEvent.VK_Q);
            System.out.println("naveDer "+naveDer+"     "+KeyEvent.VK_E);
            if(naveIzq==28){
              naveIzq=KeyEvent.VK_LEFT;
            }
            if(naveDer==30){
              naveDer=KeyEvent.VK_RIGHT; 
            }//maty
            
            nave=new Nave((fondoAncho / 2)-17,fondoAlto+13);
            this.vectorBonus = new Vector();
            this.vectorBola = new Vector();
            fondoMenu=ImageIO.read(new File("imagenes/Arkanoid/fondoMenu.png"));
            naveG=ImageIO.read(new File("imagenes/Arkanoid/"+(appProperties.getProperty("nave"))+"Grande.png"));
            naveN=ImageIO.read(new File("imagenes/Arkanoid/"+(appProperties.getProperty("nave"))+".png"));
            naveLaser=ImageIO.read(new File("imagenes/Arkanoid/naveLaser.png"));//gino
            startGame=ImageIO.read(new File("imagenes/Arkanoid/vida.png"));//gino
            salida=ImageIO.read(new File("imagenes/Arkanoid/salida1.png"));
            titulo=ImageIO.read(new File("imagenes/Arkanoid/tituloArkanoid.png"));
            play=ImageIO.read(new File("imagenes/Arkanoid/play.png"));//gino
            exit=ImageIO.read(new File("imagenes/Arkanoid/exit.png"));//gino

            System.out.println(getClass());
            
            url = new URL(getClass().getResource("img/salidaBucle.gif").toString());
            bucleSalida = new ImageIcon(url).getImage();
            url = new URL(getClass().getResource("img/salidaConRayo.gif").toString());
            habreSalida=new ImageIcon(url).getImage();
            url = new URL(getClass().getResource("img/fondo_estrellas1.gif").toString());//gino
            gifMenu=new ImageIcon(url).getImage();//gino
            
        }
        catch(Exception e){
            //System.out.println("gameStartup: "+e);
            e.printStackTrace();
        }
    }
    
    public void reset(){
        vectorBonus.clear();
            vectorBola.clear();
            vectorBola.add(new Bola((fondoAncho / 2)-2,fondoAlto+9));
            nave.setPosicion((fondoAncho / 2)-17, fondoAlto+13);
            SistemaDeSonidos.playSonido("inicioMusica");
            aceleracion=0.0;
            catchBonu=false;
            enlargeBonu=false;
            bypass=false;
            duplicate=false;
            nave.setImagen(naveN);
            tamanioNave=50;
            nave.espacioR.setSize(16,7);
            nave.espacioL.setSize(16,7);
            nave.espacio.setSize(32,7);
            this.pausar();
            pausaNivel=true;
    }

    public void nextLevel(){
        try {
            
            nivel.aumentarNivel();
            switch(nivel.getNivelActualArkanoid()){
                case 1:
                        img_fondo=ImageIO.read(new File("imagenes/Arkanoid/Arkanoid Nivel1 (con salida) - Fields.png"));
                        this.reset();
                        break;
                case 2: img_fondo=ImageIO.read(new File("imagenes/Arkanoid/Arkanoid Nivel2 (con salida) - Fields.png"));
                        this.reset();        
                        break;
                case 3: img_fondo=ImageIO.read(new File("imagenes/Arkanoid/Arkanoid Nivel3 (con salida) - Fields.png"));
                        this.reset();
                        break;
                case 4:
                    gano=true;
                    ranking=true;
                    break;
            }
            nivel.loadNivel();
        } catch (IOException e) {
            System.out.println("Error nextLevel: "+e);
        }
    }
    
    public void colisionNave(int i){
       try{
        if (nave.espacioR.intersects(vectorBola.get(i).espacio)){
            SistemaDeSonidos.playSonido("bolaNave");
            if (catchBonu){
                pausarBola=true;
                this.pausar();
            }
            vectorBola.get(i).chocoBottom=true;
            vectorBola.get(i).chocoTop=false;
            vectorBola.get(i).chocoL=true;
            vectorBola.get(i).chocoR=false;
        }
        
        if (nave.espacioL.intersects(vectorBola.get(i).espacio)){
            SistemaDeSonidos.playSonido("bolaNave");
            if (catchBonu){
                pausarBola=true;
                this.pausar();
            }
            vectorBola.get(i).chocoBottom=true;
            vectorBola.get(i).chocoTop=false;
            vectorBola.get(i).chocoL=false;
            vectorBola.get(i).chocoR=true;
        }
       }
       catch(Exception e){
            System.out.println("Problema en ColisionNave "+e);
       }
    }
    
    public void generarBonus(int e,int i){
        (nivel.getObj(e)).restarDureza();
        aceleracion+=0.5;
        if ((nivel.getObj(e)).getDureza()==0){
            if (vectorBola.size()==1&&vectorBonus.isEmpty()){
                int calcular=95;//(nivel.getObj(e)).calcularBonus();
                //System.out.println(calcular);
                if (calcular>=50){
                    Bonus bonus=new Bonus();
                    bonus.setImagen(tipoBonus.loadBonus(bonus.getBonus(calcular)));
                    bonus.setPosicion((nivel.getObj(e)).getX()+3,(nivel.getObj(e)).getY()+5);
                    vectorBonus.add(bonus);
                }
            }
            jugador.aumentarPuntajeJugador((nivel.getObj(e)).getPuntajeXBloque());
            nivel.borrarBloque(e);
        }
        SistemaDeSonidos.playSonido("bolaBloque");
    }
    
    public void colisionBloques(int i){
       try{
        for(int e=0;e<nivel.getCantBloques();e++){
            // CAMBIOOOOOOOOOOO
            if ((nivel.getObj(e)).espacio.intersects(vectorBola.get(i).espacio)){//C
                this.generarBonus(e,i);
                
                if (vectorBola.get(i).chocoBottom){
                    vectorBola.get(i).chocoTop=true;
                    vectorBola.get(i).chocoBottom=false;
                    //System.out.println("abajo");
                }else{
                    vectorBola.get(i).chocoTop=false;
                    vectorBola.get(i).chocoBottom=true;
                    //System.out.println("arriba");

                }
            }
            else{
                if ((nivel.getObj(e)).espacioR.intersects(vectorBola.get(i).espacio)){
                    this.generarBonus(e,i);
                    if (vectorBola.get(i).chocoL){
                        if (vectorBola.get(i).chocoTop){
                            vectorBola.get(i).chocoTop=false;
                            vectorBola.get(i).chocoBottom=true;
                        }
                        else{
                            vectorBola.get(i).chocoTop=true;
                            vectorBola.get(i).chocoBottom=false;
                        }
                    }else{
                        vectorBola.get(i).chocoR=false;
                        vectorBola.get(i).chocoL=true;
                    }
                    //System.out.println("derecha");
                }
                else{
                    if ((nivel.getObj(e)).espacioL.intersects(vectorBola.get(i).espacio)){
                        this.generarBonus(e,i);
                        if (vectorBola.get(i).chocoR){
                            if (vectorBola.get(i).chocoTop){
                                vectorBola.get(i).chocoTop=false;
                                vectorBola.get(i).chocoBottom=true;
                            }
                            else{
                                vectorBola.get(i).chocoTop=true;
                                vectorBola.get(i).chocoBottom=false;
                            }
                        }else{
                            vectorBola.get(i).chocoR=true;
                            vectorBola.get(i).chocoL=false;
                        }
                        //System.out.println("izquierda");
                    }
                }
            }
        }
        if(nivel.getCantidadBloquesVivos()==0){
            System.out.println(nivel.getNivelActualArkanoid());
            this.nextLevel();
         }
       }
       catch(Exception e){
            System.out.println("Problema en ColisionBloques "+e);
       }
    }

    public void colisionBonus(){
       try{
        for (int i=0;i<vectorBonus.size();i++){    
                //if(vectorBonus.get(i).getY()+5==nave.getY()&&vectorBonus.get(i).getX()>nave.getX()-20&&vectorBonus.get(i).getX()<nave.getX()+tamanioNave-20){
                if (vectorBonus.get(i).espacio.intersects(nave.espacio)){
                    switch(vectorBonus.get(i).tipo()){
                        case 1:
                            enlargeBonu=true;
                            tamanioNave=70;
                            nave.espacioR.setSize(24,2);
                            nave.espacioL.setSize(24,2);
                            nave.espacio.setSize(50,7);
                            nave.setImagen(naveG);
                            catchBonu=false;
                            pausarBola=false;
                            break;
                        case 2:
                            if (vectorBola.size()==1){
                                vectorBola.add(new Bola((int)vectorBola.get(0).getX(),(int)vectorBola.get(0).getY()));
                                vectorBola.get(1).chocoBottom=vectorBola.get(0).chocoBottom;
                                vectorBola.get(1).chocoL=(!vectorBola.get(0).chocoL);
                                vectorBola.get(1).chocoR=(!vectorBola.get(0).chocoR);
                                vectorBola.get(1).chocoTop=vectorBola.get(0).chocoTop;
                                
                                vectorBola.add(new Bola((int)vectorBola.get(0).getX(),(int)vectorBola.get(0).getY()));
                                vectorBola.get(2).chocoBottom=vectorBola.get(0).chocoBottom;
                                vectorBola.get(2).chocoL=false;
                                vectorBola.get(2).chocoR=false;
                                vectorBola.get(2).chocoTop=vectorBola.get(0).chocoTop;
                                duplicate=true;
                                catchBonu=false;
                                //pausarBola=false;
                            }
                            break;
                        case 3:
                            catchBonu=true;
                            enlargeBonu=false;
                            nave.espacioR.setSize(16,7);
                            nave.espacioL.setSize(16,7);
                            nave.espacio.setSize(32,7);
                            nave.setImagen(naveN);
                            tamanioNave=50;
                            this.pausar(); // cambio
                            break;
                        case 4:
                            aceleracion=-50;
                            break;
                        case 5:
                            jugador.sumarVida();
                            SistemaDeSonidos.playSonido("extraLife");
                            break;
                        case 6:
                            this.pausar();
                            bypass=true;
                            break;
                    }
                    vectorBonus.remove(i);
                }
                else{
                    if (vectorBonus.get(i).getY()==fondoAlto+50){
                        vectorBonus.remove(i);
                    }
                }
            }
       }
       catch(Exception e){
            System.out.println("Problema en colisionBonus "+e);
       }
    }
    
    
    public void bolaAfuera(int i){
        if(vectorBola.size()>1){
            vectorBola.remove(i);
        }else{
            jugador.restarVida();
            if (jugador.getVidas()==0){
                SistemaDeSonidos.playSonido("gameOver");
                this.pausar();
                gameOver=true;
            }else{
                /*vectorBola.remove(i);
                vectorBonus.clear();
                aceleracion=0.0;
                catchBonu=false;
                enlargeBonu=false;
                bypass=false;
                duplicate=false;
                nave.setImagen(naveN);
                tamanioNave=50;
                nave.espacioR.setSize(16,7);
                nave.espacioL.setSize(16,7);
                nave.espacio.setSize(32,7);
                vectorBola.add(new Bola((fondoAncho / 2)-2,fondoAlto+9));
                nave.setPosicion((fondoAncho / 2)-17, fondoAlto+13);
                this.pausar();
                pausaNivel=true;
                SistemaDeSonidos.playSonido("inicioMusica");*/
                this.reset();
            }
        }
    }
    
    @Override
    public void gameUpdate(double delta) { //UPDATE actualiza frame a frame las acciones de los elementos graficos
        Keyboard keyboard = this.getKeyboard();
        
        // Esc fin del juego
            LinkedList < KeyEvent > keyEvents = keyboard.getEvents();
            for (KeyEvent event: keyEvents) {
                if ((event.getID() == KeyEvent.KEY_PRESSED) &&
                    (event.getKeyCode() == KeyEvent.VK_ESCAPE)) {
                    stop();
                }
            }
            
        // Seguimiento automatico de la bola
        /*
        if (nave.getX()>9&&nave.getX()<fondoAncho-50||bola.getX()>19&&bola.getX()<fondoAncho-30){
            nave.setX( bola.getX()-15);
        }
        */
        // Seguimiento manual de la bola
        SistemaDeSonidos.pararSonidos();
            
        if (vectorBola.isEmpty())return;
        if (!pararJuego){//gino
        if (!pausaNivel){
            if (pausarBola){
                if (keyboard.isKeyPressed(naveIzq)){//maty if (keyboard.isKeyPressed(aj.getTeclaNaveIzq())){
                    if (nave.getX()>9){
                        nave.setX(nave.getX() - NAVE_DESPLAZAMIENTO * delta);
                        vectorBola.get(0).setX((vectorBola.get(0)).getX() - NAVE_DESPLAZAMIENTO * delta);
                    }
                }

                if (keyboard.isKeyPressed(naveDer)){//maty if (keyboard.isKeyPressed(aj.getTeclaNaveDer())){
                    if (nave.getX()<fondoAncho-tamanioNave){
                        nave.setX( nave.getX() + NAVE_DESPLAZAMIENTO * delta);
                        vectorBola.get(0).setX((vectorBola.get(0)).getX() + NAVE_DESPLAZAMIENTO * delta);
                    }
                }

                if(keyboard.isKeyPressed(KeyEvent.VK_X)){
                    pausarBola=false;
                }
                if (diffSeconds>tiempoActual+2){
                    pausarBola=false;
                }
            }
            else{
                if (keyboard.isKeyPressed(naveIzq)){
                    if (nave.getX()>9){
                        nave.setX( nave.getX() - NAVE_DESPLAZAMIENTO * delta);
                    }
                }

                if (keyboard.isKeyPressed(naveDer)){
                    if (!bypass){
                        if (nave.getX()<fondoAncho-tamanioNave){
                            nave.setX( nave.getX() + NAVE_DESPLAZAMIENTO * delta);
                        }
                    }else{
                        if (nave.getX()<fondoAncho-tamanioNave){
                            nave.setX( nave.getX() + NAVE_DESPLAZAMIENTO * delta);
                        }else{
                            this.nextLevel();
                        }
                    }
                }
            }

            nave.espacioL.setLocation((int)nave.getX(), (int)nave.getY());//cambio
                if (enlargeBonu){//cambio
                    nave.espacioR.setLocation((int)nave.getX()+24, (int)nave.getY());
                }else{
                    nave.espacioR.setLocation((int)nave.getX()+16, (int)nave.getY());
                }
                nave.espacio.setLocation((int)nave.getX(), (int)nave.getY());
                if (!vectorBonus.isEmpty()){//cambio
                    for (int e=0;e<vectorBonus.size();e++){
                        vectorBonus.get(e).setPosicion(vectorBonus.get(e).getX(),vectorBonus.get(e).getY()+0.5);
                        vectorBonus.get(e).espacio.setLocation((int)vectorBonus.get(e).getX(),(int)vectorBonus.get(e).getY());
                    }
                }
                this.colisionBonus();

            if (pausarBola){
                return;
            }

            if (velocidad<4.0){
                velocidad=(BOLA_DESPLAZAMIENTO+aceleracion)*delta;
            }else{
                velocidad=4.0;
            }
            
            for (int i=0;i<vectorBola.size();i++){
                if (vectorBola.get(i).getX()<10){//15
                    vectorBola.get(i).chocoL=true;
                    vectorBola.get(i).chocoR=false;
                    aceleracion+=0.5; // cambio
                }
                if (vectorBola.get(i).getX()>fondoAncho-25)//380
                {
                    vectorBola.get(i).chocoR=true;
                    vectorBola.get(i).chocoL=false;
                    aceleracion+=0.5; // cambio

                }
                if (vectorBola.get(i).getY()<20){//35
                    vectorBola.get(i).chocoTop=true;
                    vectorBola.get(i).chocoBottom=false;
                    aceleracion+=0.5; // cambio
                }
                if (vectorBola.get(i).getY()>fondoAlto+50){//447
                    this.bolaAfuera(i);
                    /*vectorBola.get(i).chocoBottom=true;
                    vectorBola.get(i).chocoTop=false;
                    aceleracion+=0.5;*/
                }else{
                     if (vectorBola.get(i).chocoL){
                        vectorBola.get(i).setX( vectorBola.get(i).getX() + velocidad);
                    }
                    if (vectorBola.get(i).chocoR){
                       vectorBola.get(i).setX( vectorBola.get(i).getX() - velocidad); 
                    }
                    if (vectorBola.get(i).chocoTop){
                        vectorBola.get(i).setY( vectorBola.get(i).getY() + velocidad); 
                    }
                    if(vectorBola.get(i).chocoBottom){
                        vectorBola.get(i).setY( vectorBola.get(i).getY() - velocidad);
                    }

                    vectorBola.get(i).espacio.setLocation((int) Math.round(vectorBola.get(i).getX())+2,(int) Math.round(vectorBola.get(i).getY()));

                    /*vectorBola.get(i).top.setLocation((int) Math.round(vectorBola.get(i).getX())+2,(int) Math.round(vectorBola.get(i).getY()));
                    vectorBola.get(i).espacioL.setLocation((int) Math.round(vectorBola.get(i).getX()),(int) Math.round(vectorBola.get(i).getY())+2);
                    vectorBola.get(i).down.setLocation((int) Math.round(vectorBola.get(i).getX())+2,(int) Math.round(vectorBola.get(i).getY())+4);
                    vectorBola.get(i).espacioR.setLocation((int) Math.round(vectorBola.get(i).getX())+4,(int) Math.round(vectorBola.get(i).getY())+2);*/

                    this.colisionBloques(i);
                    this.colisionNave(i);
                }

               
                
            }
        }
    }
        jugador.sumarVidaPuntaje();
    }
    
    public void pausar(){
        tiempoActual=diffSeconds;
    }

    @Override
    public void gameDraw(Graphics2D g){ //DRAW dibuja los elementos graficos Frame a Frame
        
    	dAhora= new Date();
    	dateDiff = dAhora.getTime() - dInit.getTime();
        diffSeconds = dateDiff / 1000 % 60;
        //System.out.println(diffSeconds);
        diffMinutes = dateDiff / (60 * 1000) % 60;
        Keyboard keyboard = this.getKeyboard();
         //Permite escalar el fondo 
        g.scale(2.8,2.8);//gino
        g.setColor(Color.white);//gino
        
        
        if (menu){
            g.drawImage(gifMenu,0,0,null); 
            g.drawImage(titulo,32,10,null);
            g.setColor(Color.white);
            g.drawImage(play,fondoAncho/2+35,fondoAlto/2+20,null); 
            g.drawImage(exit,fondoAncho/2+35,fondoAlto/2+60,null); 
            g.drawImage(startGame,fondoAncho/2+10,fondoAlto/2+posY,null); 
            if (keyboard.isKeyPressed(KeyEvent.VK_UP)){
                posY=30;
            }
            if (keyboard.isKeyPressed(KeyEvent.VK_DOWN)){
                posY=70;
            }
            if (keyboard.isKeyPressed(KeyEvent.VK_ENTER)){
                if (posY==30){
                    pararJuego=false;//gino
                    gano=false;
                    menu=false;
                    jugador.reiniciarJugador();
                    nivel.reiniciarNiveles();
                    this.nextLevel();
                }
                else{
                     stop();
                }
            }
        }else{
            if (ranking){//gino
                if (!pararJuego){//gino
                    System.out.println("juego parado");
                    rdb.insertarJugadorAlRanking(jugador.getNombre(),jugador.getPuntajeJugador(),nivel.getNivelActualArkanoid());//gino
                    lista=rdb.mostrarRanking();//gino
                    System.out.println("lista.size: "+lista.size());
                    pararJuego=true;//gino
                }
            //Scanner entradaEscaner = new Scanner (System.in); //Creación de un objeto Scanner
            //entradaEscaner.nextLine();
            //jugador.setNombre(entradaEscaner); //Invocamos un método sobre un objeto Scanner
                g.drawImage(gifMenu,0,0,null);//gino
                if (gano){//gino
                    
                    g.setFont(new Font("Arkanoid", Font.BOLD, 20));//gino
                    g.drawString("¡Ganaste!",100,30);//gino
                }
                    g.setFont(new Font("Arkanoid", Font.BOLD, 10));//gino
                    for (int i=0;i<lista.size()&&i<40;i=i+4){//gino
                        g.drawString("Jgr"+lista.get(i),70,posYGano);//gino
                        g.drawString("Nivel: "+lista.get(i+1),105,posYGano);//gino
                        g.drawString(lista.get(i+2),150,posYGano);//gino
                        g.drawString(lista.get(i+3),200,posYGano);//gino
                        posYGano+=10;
                    }//gino
                    posYGano=60;
            g.drawString("Espacio para continuar...",90,fondoAlto/2+100);//gino
            if (keyboard.isKeyPressed(KeyEvent.VK_SPACE)){//gino
                ranking=false;//gino
                menu=true;//gino
                SistemaDeSonidos.pararSonidos();
            }//gino
            
        }else
            {
                
        if (pausaNivel){
            if ((tiempoActual+4)>diffSeconds){
                pausaNivel=true;
            }
            else{
                pausaNivel=false;
                this.pausar();
                pausarBola=true;
            }
        }

        
        g.drawImage(img_fondo,0,1,null);// imagen de fondo
        
        
        if (bypass){
            g.drawImage(habreSalida,fondoAncho/2+76,fondoAlto/2+78,null);
            
            //System.out.println(diffSeconds+" "+tiempoActual);
            if (diffSeconds>(tiempoActual+0.5)){
                g.drawImage(bucleSalida,fondoAncho/2+76,fondoAlto/2+78,null);
            }
        }else{
            g.drawImage(salida,fondoAncho/2+76,fondoAlto/2+78,null);
        }
        
    	g.setColor(Color.white);
    	g.drawString("Tiempo: "+diffMinutes+":"+diffSeconds,180,50);
        g.drawString("FPS: "+FPS,180,65);
        g.drawString("Puntaje: "+jugador.getPuntajeJugador(),180,80);
        g.drawString("Vidas: "+jugador.getVidas(),180,95);
        
        if (pausaNivel){
            g.setFont(new Font("Arkanoid", Font.BOLD, 20));
            g.drawString("Nivel "+nivel.getNivelActualArkanoid(),fondoAncho/3,fondoAlto-20);
        }
	
        //System.out.println(g+" "+bola);
        //g.drawString("Posicion X: "+bola.getX(),200,80);
        //g.drawString("Posicion Y: "+bola.getY(),200,90);
        
        nave.draw(g);
        for (int i=0;i<vectorBola.size();i++){
           //vectorBola.get(i).draw(g);
           /*g.setColor(Color.red);
           //g.fillRect((int)vectorBola.get(i).getX()+2, (int)vectorBola.get(i).getY(), 2, 5);
           g.fillRect((int)vectorBola.get(i).getX()+2, (int)vectorBola.get(i).getY(), 1, 1);
           g.setColor(Color.red);
           g.fillRect((int)vectorBola.get(i).getX(), (int)vectorBola.get(i).getY()+2, 1, 1);
           g.fillRect((int)vectorBola.get(i).getX()+2, (int)vectorBola.get(i).getY()+4, 1, 1);
           g.setColor(Color.red);
           g.fillRect((int)vectorBola.get(i).getX()+4, (int)vectorBola.get(i).getY()+2, 1, 1);*/
           vectorBola.get(i).draw(g);
        }
        
        //Disposicion Bloques (1 es Azul,2 es Rosa,3 es Roja,4 es Amarilla)
        for(int i=0;i<nivel.getCantBloques();i++){
            //if (nivel.getObj(i).getX()<=)
            //(nivel.getObj(i)).draw(g);
            /*g.setColor(Color.GREEN);
            g.fillRect((int)nivel.getObj(i).getX(), (int)nivel.getObj(i).getY(), 3, 7);
            g.setColor(Color.BLUE);
            g.fillRect((int)nivel.getObj(i).getX()+3, (int)nivel.getObj(i).getY(), 12, 7);
            g.setColor(Color.GREEN);
            g.fillRect((int)nivel.getObj(i).getX()+12, (int)nivel.getObj(i).getY(), 3, 7);
            g.setColor(Color.BLUE);*/
            //g.fillRect((int)nivel.getObj(i).getX(), (int)nivel.getObj(i).getY()+6, 15, 1);
            (nivel.getObj(i)).draw(g);    
        }
        
        if (!vectorBonus.isEmpty()){
            for (int i=0;i<vectorBonus.size();i++){
                
                //g.setColor(Color.YELLOW);
                //g.fillRect((int)vectorBonus.get(i).getX(), (int)vectorBonus.get(i).getY(), 20, 10);
                vectorBonus.get(i).draw(g);
            }
        }
        if (gameOver){
                pararJuego=true;//gino
                g.setFont(new Font("Arkanoid", Font.BOLD, 20)); //gino
                g.drawString("Game Over",fondoAncho/3-25,fondoAlto-20);
                if ((tiempoActual+5)<diffSeconds){
                    vectorBola.clear();
                    gameOver=false;
                    ranking=true;//gino
                    pararJuego=false;
                    //SistemaDeSonidos.pararSonidos();
                }
                
                /*//rdb.borrarRanking();
                Scanner entradaEscaner = new Scanner (System.in); //Creación de un objeto Scanner
                System.out.println("Fin: Su puntaje es de "+jugador.getPuntajeJugador());
                System.out.println("Ingrese su nombre, por favor");
                jugador.setNombre(entradaEscaner.nextLine()); //Invocamos un método sobre un objeto Scanner
                rdb.insertarJugadorAlRanking(jugador.getNombre(),jugador.getPuntajeJugador(),nivel.getNivelActualArkanoid());
                rdb.mostrarRanking();
                String prueba= entradaEscaner.nextLine();*/
            }
        }
        }
    }

    @Override
    public void gameShutdown() {
       Log.info(getClass().getSimpleName(), "Shutting down game");
    }
}