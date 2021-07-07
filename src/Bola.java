
import java.awt.Rectangle;
import java.io.File;
import javax.imageio.ImageIO;


public class Bola extends ElementoGrafico {
    
    protected boolean chocoTop=false;
    protected boolean chocoBottom=false;
    protected boolean chocoL=false;
    protected boolean chocoR=false;
    Rectangle top,down;
    
    public Bola(int posInicialAncho,int posInicialAlto){
        espacio.setSize(2,5);
        try{
           setImagen(ImageIO.read(new File("build/classes/Arkanoid/bola.png")));
           setPosicion(posInicialAncho,posInicialAlto);
        }
        catch(Exception e){
            System.out.println("gameStartup-->Constructor Bola: "+e);
        }
    }
    
}
