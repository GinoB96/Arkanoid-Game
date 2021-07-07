
import java.awt.Rectangle;
import java.io.File;
import javax.imageio.ImageIO;


public class Bola extends ElementoGrafico {
    
    boolean chocoTop=false;
    boolean chocoBottom=false;
    boolean chocoL=false;
    boolean chocoR=false;
    Rectangle top,down;
    
    public Bola(int posInicialAncho,int posInicialAlto){
        espacio.setSize(2,5);
        /*top.setSize(1,1);
        down.setSize(1,1);
        espacioR.setSize(1,1);
        espacioL.setSize(1,1);*/
        try{
           setImagen(ImageIO.read(new File("build/classes/Arkanoid/bola.png")));
           setPosicion(posInicialAncho,posInicialAlto);
        }
        catch(Exception e){
            System.out.println("gameStartup-->Constructor Bola: "+e);
        }
    }
    
}
