
import java.awt.image.BufferedImage;


public class Bloque extends ElementoGrafico {
    int dureza; //indica la cantidad de coliciones que resiste el bloque
    int puntosPorBloque;
    int color; //1 es Azul,2 es Rosa,3 es Roja,4 es Amarilla
    
    public Bloque(){
        puntosPorBloque=0;
        espacio.setSize(10,7);
        espacioR.setSize(3,7);
        espacioL.setSize(3,7);
        //espacio.setSize(16,7);
    }
    
    public int calcularBonus(){
        return (int)(Math.random()*100);
    }
    
    public void setColor(BufferedImage color){
        this.imagen=color;
    }
    
    public void setNroColor(int nroDelColor){//Metodo nuevo
        this.color=nroDelColor;
    }
    
    public void setDureza(int durezaDelBloque){
                  switch(durezaDelBloque){
                    case 5: this.dureza=2;break;
                    case 6: this.dureza=8750;break;
                   default: this.dureza=1;break;
                  }
                 }
    
    public void setPuntajeXBloque(int color){
        switch(color){ //1 es Azul,2 es Rosa,3 es Rojo,4 es Amarilla
              case 1: this.puntosPorBloque=100;
                      break;
              case 2: this.puntosPorBloque=110;
                      break;
              case 3: this.puntosPorBloque=90;
                      break;
              case 4: this.puntosPorBloque=120;
                      break;
              case 5: this.puntosPorBloque=150;
                      break;
              case 6: this.puntosPorBloque=0;
                      break;
              
          }
    }
    
    public int getPuntajeXBloque(){
        return puntosPorBloque;
    }
    public int getDureza(){
        return this.dureza; //cambio
    }
    
    public void restarDureza(){
        this.dureza-=1;
    }
}



