import java.io.FileReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class LoaderNivelData {
    private  final String filePath = "nivel.json";
    private  int nroBloquesXnivel;
    private int nroBloquesXnivelDestruibles;
    private JSONObject bloque;
   
    public LoaderNivelData(int nroNivel){
       try {
          JSONParser parser = new JSONParser();
          Object cargarJSON = parser.parse(new FileReader(filePath));
          bloque=new JSONObject();
          bloque =(JSONObject)cargarJSON;
          this.nroBloquesXnivel=Integer.parseInt(((JSONObject)bloque.get("cantidadBloquesPorNivel")).get("n"+nroNivel).toString());
          this.nroBloquesXnivelDestruibles=Integer.parseInt(((JSONObject)bloque.get("cantidadBloquesDestruibles")).get("n"+nroNivel).toString());
	  bloque=(JSONObject)bloque.get("nivel"+nroNivel);
        } catch (Exception e) 
        {
          System.out.println("Error en LoaderNivelData-->Constructor:"+e);
        } 
    }
    
    public  int getX(int nroBloque){
        return Integer.parseInt(((JSONObject)bloque.get("b"+nroBloque)).get("x").toString());
    }
   
    public int getY(int nroBloque){
       return Integer.parseInt(((JSONObject)bloque.get("b"+nroBloque)).get("y").toString());
    }
     
    public int getColor(int nroBloque){
        return Integer.parseInt(((JSONObject)bloque.get("b"+nroBloque)).get("color").toString());
    }
    
    public int getCantidadBloques(){
     return this.nroBloquesXnivel;
    }
    
    public int getCantidadBloquesDestruibles(){
        return this.nroBloquesXnivelDestruibles;
    }
}
