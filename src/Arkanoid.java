import JGame.*;
import RankingDB.RankingDB;
import java.awt.*;
import java.awt.event.*;

import java.awt.image.*;
import javax.imageio.*;

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
    private Date dInit = new Date();
    private Date dAhora;
    private SimpleDateFormat ft = new SimpleDateFormat ("mm:ss");
    //elementos del Update
    private final double NAVE_DESPLAZAMIENTO=200.0;
    private double BOLA_DESPLAZAMIENTO=100.0;
    private boolean pausarBola=true;
    private boolean catchBonu=false;
    private boolean enlargeBonu=false;
    private boolean bypass=false;
    private boolean pausaNivel=false;
    private boolean menu=true;
    private boolean gano=false;
    private boolean gameOver = false;
    private boolean ranking=false;
    private boolean pararJuego=false;
    private boolean tituloMusica=false;
    private boolean sonidoState=true;
    private boolean laser=false;
    private boolean puedeDisparar=false;
    private Integer musicaChoice=0;
    private Vector <String> lista = new Vector();
    private int fondoAncho=185;
    private int fondoAlto=176;
    private int tamanioNave=50;
    private int posY=30;
    private int posYGano=60;
    private TipoBonus tipoBonus=new TipoBonus();
    private Vector <Bonus> vectorBonus;
    private Vector <Laser> vectorLaser=new Vector();
    private long dateDiff;
    private long diffSeconds;
    private long diffMinutes;
    private long segundosCorriendo;
    private Date reiniciarTiempo;
    private double aceleracion=-50;
    private double velocidad=0.0;
    //elementos graficos
    private BufferedImage img_fondo = null;
    private BufferedImage naveN;
    private BufferedImage naveG;
    private BufferedImage naveLaser;
    private BufferedImage salida;
    private BufferedImage titulo;
    private BufferedImage startGame;
    private BufferedImage play;
    private BufferedImage exit;
    private Image bucleSalida;
    private Image habreSalida;
    private Image gifMenu;
    private Image naveGif;
    private URL url;
    private Nave nave;
    private int naveIzq;
    private int naveDer;
    private int naveAccion;
    private Vector <Bola> vectorBola;
    //arkanoid
    private Nivel nivel;
    private Jugador jugador;
    private RankingDB rdb;
    
    public static void main(String[] args) {
        Arkanoid game = new Arkanoid();
        game.run(1.0 / 60.0);
        System.exit(0);
    }

    public Arkanoid() {
        super("Arkanoid",800,600);
        this.pausar();
    }

    @Override
    public void gameStartup() { //Inicializa todos los elementos graficos que se utilizaran en el juego
        try{
            menu=true; 
            nivel=new Nivel();
            jugador=new Jugador();
            rdb=new RankingDB();
            reiniciarTiempo=new Date();//gino
            this.appProperties = new Properties();
            FileInputStream in = new FileInputStream("jgame.properties");
            appProperties.load(in);
            naveIzq=(char)(appProperties.getProperty("teclaIzquierda").charAt(0))-32;
            naveDer=(char)(appProperties.getProperty("teclaDerecha").charAt(0)-32);
            sonidoState=Boolean.valueOf(appProperties.getProperty("sonidoChoice"));
            musicaChoice=Integer.parseInt(appProperties.getProperty("musica"));
            SistemaDeSonidos.generarInstancia(musicaChoice);
            if (appProperties.getProperty("teclaAccion").equals("space")){
                naveAccion=KeyEvent.VK_SPACE;
            }else{
                naveAccion=(char)(appProperties.getProperty("teclaAccion").charAt(0)-32);
            }
            if(naveIzq==28){
              naveIzq=KeyEvent.VK_LEFT;
            }
            if(naveIzq==30){
              naveIzq=KeyEvent.VK_RIGHT;
            }
            if(naveDer==30){
              naveDer=KeyEvent.VK_RIGHT;
            }
            if(naveDer==28){
              naveDer=KeyEvent.VK_LEFT; 
            }
            if(naveAccion==28){
              naveAccion=KeyEvent.VK_LEFT;
            }
            if(naveAccion==30){
              naveAccion=KeyEvent.VK_RIGHT;
            }
            
            nave=new Nave((fondoAncho / 2)-17,fondoAlto+13);
            this.vectorBonus = new Vector();
            this.vectorBola = new Vector();
            naveG=ImageIO.read(new File("imagenes/Arkanoid/"+(appProperties.getProperty("nave"))+"Grande.png"));
            naveN=ImageIO.read(new File("imagenes/Arkanoid/"+(appProperties.getProperty("nave"))+".png"));
            url = new URL(getClass().getResource("img/nave.gif").toString());
            naveGif= new ImageIcon(url).getImage();
            naveLaser=ImageIO.read(new File("imagenes/Arkanoid/naveLaser.png"));//gino
            startGame=ImageIO.read(new File("imagenes/Arkanoid/vida.png"));
            salida=ImageIO.read(new File("imagenes/Arkanoid/salida1.png"));
            titulo=ImageIO.read(new File("imagenes/Arkanoid/tituloArkanoid.png"));
            play=ImageIO.read(new File("imagenes/Arkanoid/play.png"));
            exit=ImageIO.read(new File("imagenes/Arkanoid/exit.png"));
            url = new URL(getClass().getResource("img/salidaBucle.gif").toString());
            bucleSalida = new ImageIcon(url).getImage();
            url = new URL(getClass().getResource("img/salidaConRayo.gif").toString());
            habreSalida=new ImageIcon(url).getImage();
            url = new URL(getClass().getResource("img/fondo_estrellas1.gif").toString());
            gifMenu=new ImageIcon(url).getImage();
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reset(){
        vectorBonus.clear();
        vectorBola.clear();
        vectorLaser.clear();
        vectorBola.add(new Bola((fondoAncho / 2)-2,fondoAlto+9));
        nave.setPosicion((fondoAncho / 2)-17, fondoAlto+13);
        SistemaDeSonidos.playSonido("inicioMusica",sonidoState);
        aceleracion=0.0;
        catchBonu=false;
        enlargeBonu=false;
        bypass=false;
        laser=false;//gino
        puedeDisparar=false;//gino
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
    
    public void colisionNave(int indiceVectorBola){
       try{
        if (nave.espacioR.intersects(vectorBola.get(indiceVectorBola).espacio)){
            SistemaDeSonidos.playSonido("bolaNave",sonidoState);
            if (catchBonu){
                pausarBola=true;
                this.pausar();
            }
            vectorBola.get(indiceVectorBola).chocoBottom=true;
            vectorBola.get(indiceVectorBola).chocoTop=false;
            vectorBola.get(indiceVectorBola).chocoL=true;
            vectorBola.get(indiceVectorBola).chocoR=false;
        }
        
        if (nave.espacioL.intersects(vectorBola.get(indiceVectorBola).espacio)){
            SistemaDeSonidos.playSonido("bolaNave",sonidoState);
            if (catchBonu){
                pausarBola=true;
                this.pausar();
            }
            vectorBola.get(indiceVectorBola).chocoBottom=true;
            vectorBola.get(indiceVectorBola).chocoTop=false;
            vectorBola.get(indiceVectorBola).chocoL=false;
            vectorBola.get(indiceVectorBola).chocoR=true;
        }
       }
       catch(Exception e){
            System.out.println("Problema en ColisionNave "+e);
       }
    }
    
    public void restarDurezaBloque(int indiceBloque){
        (nivel.getObj(indiceBloque)).restarDureza();
        aceleracion+=0.5;
        SistemaDeSonidos.playSonido("bolaBloque",sonidoState);
        this.eliminarBloque(indiceBloque);
    }
    
    public void eliminarBloque(int indiceBloque){
        if ((nivel.getObj(indiceBloque)).getDureza()==0){
            if (vectorBola.size()==1&&vectorBonus.isEmpty()){
                int calcular=(nivel.getObj(indiceBloque)).calcularBonus();
                if (calcular>=50){
                   this.generarBonus(calcular,indiceBloque);
                }
            }
            jugador.aumentarPuntajeJugador((nivel.getObj(indiceBloque)).getPuntajeXBloque());
            nivel.borrarBloque(indiceBloque);
        }
    }
    
    public void generarBonus(int calcular,int indiceBloque){
        Bonus bonus=new Bonus();
        bonus.setImagen(tipoBonus.loadBonus(bonus.getBonus(calcular)));
        bonus.setPosicion((nivel.getObj(indiceBloque)).getX()+3,(nivel.getObj(indiceBloque)).getY()+5);
        vectorBonus.add(bonus);
    }
    
    
    public void colisionBloques(int indiceVectorBola){
       try{
        for(int e=0;e<nivel.getCantBloques();e++){
            if ((nivel.getObj(e)).espacio.intersects(vectorBola.get(indiceVectorBola).espacio)){//C
                this.restarDurezaBloque(e);
                
                if (vectorBola.get(indiceVectorBola).chocoBottom){
                    vectorBola.get(indiceVectorBola).chocoTop=true;
                    vectorBola.get(indiceVectorBola).chocoBottom=false; 
                }else{
                    vectorBola.get(indiceVectorBola).chocoTop=false;
                    vectorBola.get(indiceVectorBola).chocoBottom=true; 
                }
            }
            else{
                if ((nivel.getObj(e)).espacioR.intersects(vectorBola.get(indiceVectorBola).espacio)){
                    this.restarDurezaBloque(e);
                    if (vectorBola.get(indiceVectorBola).chocoL){
                        if (vectorBola.get(indiceVectorBola).chocoTop){
                            vectorBola.get(indiceVectorBola).chocoTop=false;
                            vectorBola.get(indiceVectorBola).chocoBottom=true;
                        }
                        else{
                            vectorBola.get(indiceVectorBola).chocoTop=true;
                            vectorBola.get(indiceVectorBola).chocoBottom=false;
                        }
                    }else{
                        vectorBola.get(indiceVectorBola).chocoR=false;
                        vectorBola.get(indiceVectorBola).chocoL=true;
                    }
                }
                else{
                    if ((nivel.getObj(e)).espacioL.intersects(vectorBola.get(indiceVectorBola).espacio)){
                        this.restarDurezaBloque(e);
                        if (vectorBola.get(indiceVectorBola).chocoR){
                            if (vectorBola.get(indiceVectorBola).chocoTop){
                                vectorBola.get(indiceVectorBola).chocoTop=false;
                                vectorBola.get(indiceVectorBola).chocoBottom=true;
                            }
                            else{
                                vectorBola.get(indiceVectorBola).chocoTop=true;
                                vectorBola.get(indiceVectorBola).chocoBottom=false;
                            }
                        }else{
                            vectorBola.get(indiceVectorBola).chocoR=true;
                            vectorBola.get(indiceVectorBola).chocoL=false;
                        }
                    }
                }
            }
        }
        if(nivel.getCantidadBloquesVivos()==0){
            this.nextLevel();
         }
       }
       catch(Exception e){
            System.out.println("Problema en ColisionBloques "+e);
       }
    }

    public void colisionBonus(){
       try{
        for (int indiceVectorBonus=0;indiceVectorBonus<vectorBonus.size();indiceVectorBonus++){    
                if (vectorBonus.get(indiceVectorBonus).espacio.intersects(nave.espacio)){
                    switch(vectorBonus.get(indiceVectorBonus).tipo()){
                        case 1:
                            enlargeBonu=true;
                            tamanioNave=70;
                            nave.espacioR.setSize(24,2);
                            nave.espacioL.setSize(24,2);
                            nave.espacio.setSize(50,7);
                            nave.setImagen(naveG);
                            catchBonu=false;
                            pausarBola=false;
                            laser=false;//gino
                            puedeDisparar=false;//gino
                            break;
                        case 2:
                            if (vectorBola.size()==1){
                                nave.espacioR.setSize(16,7);
                                nave.espacioL.setSize(16,7);
                                nave.espacio.setSize(32,7);
                                nave.setImagen(naveN);
                                tamanioNave=50;
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
                                catchBonu=false;
                                laser=false;//gino
                                puedeDisparar=false;//gino
                            }
                            break;
                        case 3:
                            catchBonu=true;
                            enlargeBonu=false;
                            laser=false;//gino
                            puedeDisparar=false;//gino
                            nave.espacioR.setSize(16,7);
                            nave.espacioL.setSize(16,7);
                            nave.espacio.setSize(32,7);
                            nave.setImagen(naveN);
                            tamanioNave=50;
                            this.pausar();
                            break;
                        case 4:
                            aceleracion=-50;
                            break;
                        case 5:
                            jugador.sumarVida();
                            SistemaDeSonidos.playSonido("extraLife",sonidoState);
                            break;
                        case 6:
                            this.pausar();
                            bypass=true;
                            break;
                        case 7://gino
                            catchBonu=false;
                            enlargeBonu=false;
                            nave.espacioR.setSize(16,7);
                            nave.espacioL.setSize(16,7);
                            nave.espacio.setSize(32,7);
                            nave.setImagen(naveLaser);
                            tamanioNave=50;
                            laser=true;
                            puedeDisparar=true;
                            break;
                    }
                    vectorBonus.remove(indiceVectorBonus);
                }
                else{
                    if (vectorBonus.get(indiceVectorBonus).getY()==fondoAlto+50){
                        vectorBonus.remove(indiceVectorBonus);
                    }
                }
            }
       }
       catch(Exception e){
            System.out.println("Problema en colisionBonus "+e);
       }
    }
    
    
    public void bolaAfuera(int indiceVectorBola){
        if(vectorBola.size()>1){
            vectorBola.remove(indiceVectorBola);
        }else{
            jugador.restarVida();
            if (jugador.getVidas()==0){
                SistemaDeSonidos.playSonido("gameOver",sonidoState);
                this.pausar();
                gameOver=true;
            }else{
                this.reset();
            }
        }
    }
    
    public void colisionLaser(int indiceLaser){//gino
        try{
        for(int indiceBloque=0;indiceBloque<nivel.getCantBloques()&&!vectorLaser.isEmpty();indiceBloque++){
            if (vectorLaser.get(indiceLaser).espacio.intersects((nivel.getObj(indiceBloque)).espacio)){
                vectorLaser.remove(indiceLaser);
                this.restarDurezaBloque(indiceBloque);
            }else{
                if (vectorLaser.get(indiceLaser).espacio.intersects((nivel.getObj(indiceBloque)).espacioR)){
                    vectorLaser.remove(indiceLaser);
                    this.restarDurezaBloque(indiceBloque);
                }else{
                    if (vectorLaser.get(indiceLaser).espacio.intersects((nivel.getObj(indiceBloque)).espacioL)){
                        vectorLaser.remove(indiceLaser);
                        this.restarDurezaBloque(indiceBloque);
                    }else{
                        if (vectorLaser.get(indiceLaser).getY()<20){
                            vectorLaser.remove(indiceLaser);
                        }
                    }
                }
            }
        }
        }catch(Exception e){
            indiceLaser--;
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
                    SistemaDeSonidos.pararSonidos();
                    SistemaDeSonidos.pararMusicaFondo();
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
                if (keyboard.isKeyPressed(naveIzq)){
                    if (nave.getX()>9){
                        nave.setX(nave.getX() - NAVE_DESPLAZAMIENTO * delta);
                        vectorBola.get(0).setX((vectorBola.get(0)).getX() - NAVE_DESPLAZAMIENTO * delta);
                    }
                }

                if (keyboard.isKeyPressed(naveDer)){
                    if (nave.getX()<fondoAncho-tamanioNave){
                        nave.setX( nave.getX() + NAVE_DESPLAZAMIENTO * delta);
                        vectorBola.get(0).setX((vectorBola.get(0)).getX() + NAVE_DESPLAZAMIENTO * delta);
                    }
                }

                if(keyboard.isKeyPressed(naveAccion)){
                    pausarBola=false;
                }
                if (segundosCorriendo>2){
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
            
            if (laser){//gino
                if(keyboard.isKeyPressed(naveAccion)){
                    if (puedeDisparar){
                        SistemaDeSonidos.playSonido("sonidoLaser", sonidoState);//gino
                        vectorLaser.add(new Laser(nave.getX()+5));
                        vectorLaser.add(new Laser(nave.getX()+23));
                        puedeDisparar=false;
                        this.pausar();
                    }
                    if (segundosCorriendo>0.1){
                        puedeDisparar=true;
                    }
                }
            }

            nave.espacioL.setLocation((int)nave.getX(), (int)nave.getY());
                if (enlargeBonu){
                    nave.espacioR.setLocation((int)nave.getX()+24, (int)nave.getY());
                }else{
                    nave.espacioR.setLocation((int)nave.getX()+16, (int)nave.getY());
                }
                nave.espacio.setLocation((int)nave.getX(), (int)nave.getY());
                if (!vectorBonus.isEmpty()){
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
            
            for (int indiceVectorBola=0;indiceVectorBola<vectorBola.size();indiceVectorBola++){
                if (vectorBola.get(indiceVectorBola).getX()<10){
                    vectorBola.get(indiceVectorBola).chocoL=true;
                    vectorBola.get(indiceVectorBola).chocoR=false;
                    aceleracion+=0.5;
                }
                if (vectorBola.get(indiceVectorBola).getX()>fondoAncho-25)
                {
                    vectorBola.get(indiceVectorBola).chocoR=true;
                    vectorBola.get(indiceVectorBola).chocoL=false;
                    aceleracion+=0.5;

                }
                if (vectorBola.get(indiceVectorBola).getY()<20){
                    vectorBola.get(indiceVectorBola).chocoTop=true;
                    vectorBola.get(indiceVectorBola).chocoBottom=false;
                    aceleracion+=0.5;
                }
                if (vectorBola.get(indiceVectorBola).getY()>fondoAlto+50){
                    this.bolaAfuera(indiceVectorBola);
                }else{
                     if (vectorBola.get(indiceVectorBola).chocoL){
                        vectorBola.get(indiceVectorBola).setX(vectorBola.get(indiceVectorBola).getX() + velocidad);
                    }
                    if (vectorBola.get(indiceVectorBola).chocoR){
                       vectorBola.get(indiceVectorBola).setX(vectorBola.get(indiceVectorBola).getX() - velocidad); 
                    }
                    if (vectorBola.get(indiceVectorBola).chocoTop){
                        vectorBola.get(indiceVectorBola).setY(vectorBola.get(indiceVectorBola).getY() + velocidad); 
                    }
                    if(vectorBola.get(indiceVectorBola).chocoBottom){
                        vectorBola.get(indiceVectorBola).setY(vectorBola.get(indiceVectorBola).getY() - velocidad);
                    }

                    vectorBola.get(indiceVectorBola).espacio.setLocation((int) Math.round(vectorBola.get(indiceVectorBola).getX())+2,(int) Math.round(vectorBola.get(indiceVectorBola).getY()));

                    this.colisionBloques(indiceVectorBola);
                    this.colisionNave(indiceVectorBola);
                }
                for (int indiceLaser=0;indiceLaser<vectorLaser.size();indiceLaser++){//gino
                    try{
                        vectorLaser.get(indiceLaser).setY(vectorLaser.get(indiceLaser).getY()-(150*delta));
                        vectorLaser.get(indiceLaser).espacio.setLocation((int)vectorLaser.get(indiceLaser).getX(),(int)(vectorLaser.get(indiceLaser).getY()-(150*delta)));
                        this.colisionLaser(indiceLaser);
                    }catch(Exception e){
                        indiceLaser--;
                    }
                }
            }
        }
    }
        jugador.sumarVidaPuntaje();
    }
    
    public void pausar(){
        segundosCorriendo=0;
        reiniciarTiempo=new Date();
    }

    @Override
    public void gameDraw(Graphics2D g){ //DRAW dibuja los elementos graficos Frame a Frame
        
    	dAhora= new Date();
    	dateDiff = dAhora.getTime() - dInit.getTime();
        diffSeconds = dateDiff / 1000 % 60;
        segundosCorriendo=(dAhora.getTime() - reiniciarTiempo.getTime())/1000%60;
        diffMinutes = dateDiff / (60 * 1000) % 60;
        Keyboard keyboard = this.getKeyboard();
         //Permite escalar el fondo 
        g.scale(2.8,2.8);
        g.setColor(Color.white);
        
        if (menu){
            if(tituloMusica==false){
                SistemaDeSonidos.playSonido("musicaTitulo",sonidoState);
                tituloMusica=true;
            }
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
                    pararJuego=false;
                    gano=false;
                    menu=false;
                    jugador.reiniciarJugador();
                    nivel.reiniciarNiveles();
                    this.nextLevel();
                    SistemaDeSonidos.pararSonidos();
                    SistemaDeSonidos.pararMusicaFondo();
                }
                else{
                     SistemaDeSonidos.pararMusicaFondo();
                     SistemaDeSonidos.eliminarInstancia();
                     stop();
                }
            }
        }else{
            if (ranking){
                if (!pararJuego){
                    rdb.insertarJugadorAlRanking(jugador.getNombre(),jugador.getPuntajeJugador(),nivel.getNivelActualArkanoid());//gino
                    lista=rdb.mostrarRanking();
                    pararJuego=true;
                }
                g.drawImage(gifMenu,0,0,null);
                if (gano){
                    
                    g.setFont(new Font("Arkanoid", Font.BOLD, 20));
                    g.drawString("¡Ganaste!",100,30);
                }
                    tituloMusica=false;
                    g.setFont(new Font("Arkanoid", Font.BOLD, 10));
                    for (int i=0;i<lista.size()&&i<40;i=i+4){
                        g.drawString("Jgr"+lista.get(i),70,posYGano);
                        g.drawString("Nivel: "+lista.get(i+1),105,posYGano);
                        g.drawString(lista.get(i+2),150,posYGano);
                        g.drawString(lista.get(i+3),200,posYGano);
                        posYGano+=10;
                    }
                    posYGano=60;
            g.drawString("Espacio para continuar...",90,fondoAlto/2+100);
            if (keyboard.isKeyPressed(KeyEvent.VK_SPACE)){
                ranking=false;
                menu=true;
                SistemaDeSonidos.pararSonidos();
            }
            
        }else
            {
                
        if (pausaNivel){
            if (4>segundosCorriendo){
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
            if (segundosCorriendo>0.5){
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

        
        
        for (int i=0;i<vectorBola.size();i++){
           vectorBola.get(i).draw(g);
        }
        nave.draw(g);
        //Disposicion Bloques (1 es Azul,2 es Rosa,3 es Roja,4 es Amarilla)
        for(int i=0;i<nivel.getCantBloques();i++){
            (nivel.getObj(i)).draw(g);    
        }
        
        if (!vectorBonus.isEmpty()){
            for (int i=0;i<vectorBonus.size();i++){
                vectorBonus.get(i).draw(g);
            }
        }
        
        for (int i=0;i<vectorLaser.size();i++){//gino
            vectorLaser.get(i).draw(g);
        }
        
        if (gameOver){
                pararJuego=true;
                g.setFont(new Font("Arkanoid", Font.BOLD, 20));
                g.drawString("Game Over",fondoAncho/3-25,fondoAlto-20);
                if (5<segundosCorriendo){
                    vectorBola.clear();
                    gameOver=false;
                    ranking=true;
                    pararJuego=false;
                }
            }
        }
        }
    }

    @Override
    public void gameShutdown() {
       Log.info(getClass().getSimpleName(), "Shutting down game");
    }
}