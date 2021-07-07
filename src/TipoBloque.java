
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;


public class TipoBloque { /*El objetivo de esta clase es trabajar con los archivos de las imagenes de cada bloque
    una unica vez, permitiendo asi la reutilizacion de las mismas*/
    private BufferedImage azul;
    private BufferedImage rosa;
    private BufferedImage rojo;
    private BufferedImage amarillo;
    private BufferedImage plateado;
    private BufferedImage dorado;
   
    public TipoBloque(){
        try{
            azul=(ImageIO.read(new File("imagenes/Arkanoid/bloques/azul.png")));
            rosa=(ImageIO.read(new File("imagenes/Arkanoid/bloques/rosa.png")));
            rojo=(ImageIO.read(new File("imagenes/Arkanoid/bloques/rojo.png")));
            amarillo=(ImageIO.read(new File("imagenes/Arkanoid/bloques/amarillo.png")));
            plateado=(ImageIO.read(new File("imagenes/Arkanoid/bloques/plateado.png")));
            dorado=(ImageIO.read(new File("imagenes/Arkanoid/bloques/dorado.png")));
        }
        catch(Exception e){
            System.out.println("Inicializacion de imagenes en Class TipoBloque: "+e);
        }
    }
    
    public BufferedImage loadImage(int color){ 
        BufferedImage rta=null;
        switch(color){ //1 es Azul,2 es Rosa,3 es Rojo,4 es Amarilla
              case 1: rta=azul;
                      break;
              case 2: rta=rosa;
                      break;
              case 3: rta=rojo;
                      break;
              case 4: rta=amarillo;
                      break;
              case 5: rta=plateado;
                      break;
              case 6: rta=dorado;
                      break;
              
          }
        return rta;
    }
}
