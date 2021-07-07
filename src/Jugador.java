public class Jugador {
    private int puntajeJugador;
    private int vidas;
    private int cantidadBloquesDestruidos;
    private int subirVida;
    private String jugadorNombre;
    
    public Jugador(){
       this.puntajeJugador=0;
       this.vidas=3;
       this.cantidadBloquesDestruidos=0;
       this.subirVida=7000;
       this.jugadorNombre="";
    }
    
    public void aumentarPuntajeJugador(int cantidad){
        this.puntajeJugador=this.puntajeJugador+cantidad;
        this.cantidadBloquesDestruidos++;
    }
    
    public int getPuntajeJugador(){
        return this.puntajeJugador;
    }
    
    public String getNombre(){
        return this.jugadorNombre;
    }
    
    public void setNombre(String nombre){
        this.jugadorNombre=nombre;
    }
    
    public int getCantidadBloquesDestruidos(){
        return this.cantidadBloquesDestruidos;
    }
    public void sumarVidaPuntaje(){
        if (puntajeJugador>=subirVida){
            vidas+=1;
            subirVida+=subirVida;
        }
    }
    
    public void sumarVida(){
        vidas+=1;
    }
    
    public void restarVida(){
        this.vidas-=1;
    }
    
    public int getVidas(){
        return vidas;
    }
    
    public void reiniciarJugador(){
        this.jugadorNombre="";
        this.puntajeJugador=0;
        this.vidas=3;
    }
}