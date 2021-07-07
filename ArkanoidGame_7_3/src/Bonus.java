/**
 *
 * @author Gino
 */
public class Bonus extends ElementoGrafico{
    private int idBonus;
    
    public Bonus(){
        espacio.setSize(16,7);
    }
    
    public int getBonus(int nro){
        if (nro>=50&&nro<60){
            idBonus=1;
        }
        else{
            if (nro>=60&&nro<70){
                idBonus=2;
            }else{
                if(nro>=70&&nro<80){
                    idBonus=3;
                }else{
                    if(nro>=80&&nro<90){
                        idBonus=4;
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
        return idBonus;
    }
    
    public int tipo(){
        return idBonus;
    }
}

