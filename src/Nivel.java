
import java.util.ArrayList;

public class Nivel {
    private ArrayList<Bloque> conjBloques;
    private final TipoBloque imagePNG; /*Cuando sea necesario cargarle una imagen al bloque, este objeto retornara
    la imagen al objeto bloque*/
    private int nivelActualArkanoid;
    private int cantidadBloquesVivos;
    private LoaderNivelData nivelData;//Se encarga de proveer los datos del JSON
    
    public Nivel(){
        //Se debe llamar a un metodo que lea el JSON para obtener la distribuccion del nivel correspondiente
        //a modo de ejemplo cargo el elemento a mano
        this.imagePNG=new TipoBloque();
        this.nivelActualArkanoid=0;
        //tipobonus
        //loadNivel();
    }
   
    public void loadNivel(){ /*carga el ArrayList con objetos de tipo bloque y a su vez les va cargando el color 
        y la posicion del mismo*/
        this.nivelData=new LoaderNivelData(this.nivelActualArkanoid);
        this.conjBloques=new ArrayList<Bloque>();
            for(int i=1;i<nivelData.getCantidadBloques();i++){
                Bloque bloque=new Bloque();
                bloque.setX((double)nivelData.getX(i));
                bloque.setY((double)nivelData.getY(i));
                bloque.setColor(imagePNG.loadImage(nivelData.getColor(i)));
                bloque.setNroColor(nivelData.getColor(i));//cambio
                bloque.setDureza(nivelData.getColor(i));
                bloque.setPuntajeXBloque(nivelData.getColor(i));
                bloque.espacio.setLocation((int)Math.round(bloque.getX())+3, (int)Math.round(bloque.getY()));
                bloque.espacioR.setLocation((int)Math.round(bloque.getX())+13, (int)Math.round(bloque.getY()));
                bloque.espacioL.setLocation((int)Math.round(bloque.getX()), (int)Math.round(bloque.getY()));
                this.conjBloques.add(bloque);
            }
            this.cantidadBloquesVivos=this.nivelData.getCantidadBloquesDestruibles()-1;
    }
    public void borrarBloque(int pos){
        this.conjBloques.remove(pos);
        this.cantidadBloquesVivos--;
    }
    
    public int getCantBloques(){
        return conjBloques.size();
    }
    
    public Bloque getObj(int posicion){
       try{
        return(conjBloques.get(posicion));
       }
       catch(Exception e){
            return new Bloque();
        }
    }
    
    public void reiniciarNiveles(){
        this.nivelActualArkanoid=0;
    }
    
    public void aumentarNivel(){
        this.nivelActualArkanoid++;
    }
    public int getNivelActualArkanoid(){
        return this.nivelActualArkanoid;
    }
    public int getCantidadBloquesVivos(){
        return this.cantidadBloquesVivos;
    }
    
}
