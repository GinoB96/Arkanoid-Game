
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TipoBonus{
    private BufferedImage enlarge;
    private BufferedImage bonusCatch;
    private BufferedImage bypass;
    private BufferedImage duplicate;
    private BufferedImage laser;
    private BufferedImage bonusPlayer;
    private BufferedImage slow;
    
    public TipoBonus(){
        try {
            bonusCatch=(ImageIO.read(new File("imagenes/Arkanoid/bonus/catch.png")));
            bypass=(ImageIO.read(new File("imagenes/Arkanoid/bonus/bypass.png")));
            duplicate=(ImageIO.read(new File("imagenes/Arkanoid/bonus/duplicate.png")));
            enlarge=(ImageIO.read(new File("imagenes/Arkanoid/bonus/enlarge.png")));
            bonusPlayer=(ImageIO.read(new File("imagenes/Arkanoid/bonus/player.png")));
            slow=(ImageIO.read(new File("imagenes/Arkanoid/bonus/slow.png")));
            laser=(ImageIO.read(new File("imagenes/Arkanoid/bonus/laser.png")));//gino
        } catch (IOException ex) {
            System.out.println("error cargar bonus "+ex);
        }
    }
    
    public BufferedImage loadBonus(int id){
        BufferedImage rta=null;
        switch(id){
            case 1:
                rta=enlarge;
                break;
            case 2:
                rta=duplicate;
                break;
            case 3:
                rta=bonusCatch;
                break;
            case 4:
                rta=slow;
                break;
            case 5:
                rta=bonusPlayer;
                break;
            case 6:
                rta=bypass;
                break;
            case 7://gino
                rta=laser;
                break;
        }
        return rta;
    }
    
}
