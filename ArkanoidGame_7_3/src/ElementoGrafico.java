
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.*;

public class ElementoGrafico{

    BufferedImage imagen;
    private Point2D.Double posicion;
    Rectangle espacio,espacioR,espacioL;//gino
    
    public ElementoGrafico(){
        this.posicion  = new Point2D.Double();
        this.espacio=new Rectangle();
        this.espacioR=new Rectangle();
        this.espacioL=new Rectangle();
    }

    public void setImagen(BufferedImage img){
        this.imagen=img;

    }

    public void setPosicion(double x, double y){
        posicion.setLocation(x, y);
    }

    public void setX(double x){
        posicion.x=x;
    }

    public void setY(double y){
        posicion.y=y;
    }
    public double getX(){
        return posicion.getX(); 
    }

    public double getY(){
        return posicion.getY(); 
    }
    public void update(double delta){

    }

    public void draw(Graphics2D g){
        g.drawImage(imagen,(int)posicion.getX(),(int)posicion.getY(),null);
    }
}
