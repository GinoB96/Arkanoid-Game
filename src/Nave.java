
import java.io.File;
import javax.imageio.ImageIO;

class Nave extends ElementoGrafico{
    
    public Nave(int posInicialAncho,int posInicialAlto){
        try{
           espacioR.setSize(16,2);
           espacioL.setSize(16,2);
           espacio.setSize(32,7);
           setImagen(ImageIO.read(new File("imagenes/Arkanoid/nave.png")));
           setPosicion(posInicialAncho,posInicialAlto);
        }
        catch(Exception e){
            System.out.println("gameStartup-->Constructor Nave: "+e);
        }
    }
}
