import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Laser extends ElementoGrafico{
    public Laser(Double posX){
        try {
            espacio.setSize(3,9);
            imagen=ImageIO.read(new File("imagenes/Arkanoid/disparo.png"));
            this.setPosicion(posX, 185);
        } catch (IOException e) {
            System.out.println("Error constructor Laser: "+e);
        }
    }
}
