import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SistemaDeSonidos {

    private static SistemaDeSonidos instancia;
    private static Hashtable<String,Clip> sonidoTable;
    private static Clip colisionBolaBloque;
    private static Clip colisionBolaNave;
    private static Clip inicioMusic;
    private static Clip gameOver;
    private static Clip extraLife;
    private static Clip musicaTitulo;
    private static Clip sonidoLaser;
   
    
    private SistemaDeSonidos(int nroMusicaTitulo){
        sonidoTable=new Hashtable<String,Clip>();
        /////
        colisionBolaBloque=loadSound("sonido/BolaBloque.wav");
        sonidoTable.put("bolaBloque", colisionBolaBloque);
        /////
        colisionBolaNave=loadSound("sonido/BolaNave.wav");
        sonidoTable.put("bolaNave", colisionBolaNave);
        /////
        inicioMusic=loadSound("sonido/RoundStart2.wav");
        sonidoTable.put("inicioMusica", inicioMusic);
        /////
        gameOver=loadSound("sonido/gameOver2.wav");
        sonidoTable.put("gameOver", gameOver);
        /////
        extraLife=loadSound("sonido/extraLife.wav");
        sonidoTable.put("extraLife", extraLife);
        /////
        switch(nroMusicaTitulo){
            case 0: musicaTitulo=loadSound("sonido/TitleScreen2.wav"); break;
            case 1: musicaTitulo=loadSound("sonido/TitleScreen.wav"); break;
        }
        musicaTitulo.loop(100);
        sonidoTable.put("musicaTitulo", musicaTitulo);
        /////
        sonidoLaser=loadSound("sonido/sonidoLaser.wav");
        sonidoTable.put("sonidoLaser", sonidoLaser);
    }
    
    private  Clip loadSound(String path) {
        try {
            Clip clip= AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(path)));
            return clip;
            
        } catch (LineUnavailableException ex) {
            Logger.getLogger(SistemaDeSonidos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(SistemaDeSonidos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SistemaDeSonidos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    private static void generarInstancia(){
        if(instancia==null){  
         instancia=new SistemaDeSonidos(0);
        }
    }
    
    public static void generarInstancia(int nroMusicaTitulo){
        if(instancia==null){  
         instancia=new SistemaDeSonidos(nroMusicaTitulo);
        }
    }
    
    public static void playSonido(String keySonido,boolean sonidoState){
        if(sonidoState==true){
            generarInstancia();
            if((sonidoTable.get(keySonido))==gameOver){
                gameOver.setFramePosition(1630000);   
             }
            else{
                (sonidoTable.get(keySonido)).setFramePosition(0);
            }
            (sonidoTable.get(keySonido)).start();
        }
    }
    
    public static void pararMusicaFondo(){
        if((sonidoTable.get("musicaTitulo")).isRunning()==true){
           (sonidoTable.get("musicaTitulo")).stop();
        }
    }
    
    public static void pararSonidos(){
        generarInstancia();
        if((sonidoTable.get("bolaBloque")).getFramePosition()>32500){
        (sonidoTable.get("bolaBloque")).stop();
        }
        if((sonidoTable.get("bolaNave")).getFramePosition()>32500){
        (sonidoTable.get("bolaNave")).stop();
        }
        if((sonidoTable.get("extraLife")).getFramePosition()>40500){
        (sonidoTable.get("extraLife")).stop();
        }
        if((sonidoTable.get("inicioMusica")).getFramePosition()>120000){
        (sonidoTable.get("inicioMusica")).stop();
        }
        if((sonidoTable.get("gameOver")).getFramePosition()>1880000){
           (sonidoTable.get("gameOver")).stop();
        }
        if((sonidoTable.get("sonidoLaser")).getFramePosition()>32500){
           (sonidoTable.get("sonidoLaser")).stop();
        }
    }
    
    public static void eliminarInstancia(){
        instancia=null;
    }
  
}

