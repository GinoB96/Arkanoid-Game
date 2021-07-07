
public class Bonus extends ElementoGrafico{
    private int idBonus;
    
    public Bonus(){
        espacio.setSize(16,7);
    }
    
    public int getBonus(int nro){
        if (nro>=50&&nro<58){
            idBonus=1;
        }
        else{
            if (nro>=58&&nro<66){
                idBonus=2;
            }else{
                if(nro>=66&&nro<74){
                    idBonus=3;
                }else{
                    if(nro>=74&&nro<82){
                        idBonus=4;
                    }else{
                        if (nro>=82&&nro<90){
                            idBonus=7;
                        }else{
                            if(nro>=90&&nro<95){
                                idBonus=5;
                            }else{
                                idBonus=6;
                            }
                        }
                    }
                }
            }
        }
        return idBonus;
    }
    
    public int tipo(){
        return idBonus;
    }
}

