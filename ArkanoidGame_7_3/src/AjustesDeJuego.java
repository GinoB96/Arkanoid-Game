import java.awt.event.KeyEvent;
import java.util.Properties;


public class AjustesDeJuego {
    protected Properties appProperties;
    private int naveIzq;
    private int naveDer;
    
  public AjustesDeJuego(){
      this.appProperties = new Properties();
      naveIzq=Integer.parseInt(appProperties.getProperty("teclaIzquierda"));
      naveDer=Integer.parseInt(appProperties.getProperty("teclaDerecha"));
      if(naveIzq==60){
        naveIzq=KeyEvent.VK_LEFT;
      }
      if(naveDer==62){
        naveDer=KeyEvent.VK_RIGHT; 
      }
    }
  
  public int getTeclaNaveIzq(){
      return this.naveIzq;
  }
  
  public int getTeclaNaveDer(){
      return this.naveDer;
  }
}
