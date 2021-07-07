import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Choice;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

//--------------definicion de clase--------------
public class Configuracion extends Frame implements ActionListener{
//--------------atributos--------------
    private Panel teclas;
    private GridLayout lasTeclas;
    private Label sonido;
    private Choice sonidoChoice;
    private Label accion;
    private Label tipoNave;
    private Choice nave;
    private Choice pista;
    private Label pistaMusical;
    private Panel guardarReset;
    private Button guardar;
    private Button reset;
    private Label izquierda;
    private Button teclaIzquierda;
    private Label derecha;
    private Button teclaDerecha;
    private Label pantalla;
    private Choice estiloPantalla;
    private Button teclaAccion;
    private Properties appProperties = new Properties();
//--------------constructor--------------
    public Configuracion(){
	super("Configuracion");
        //---------------------crear objetos
        try{
            FileInputStream in = new FileInputStream("jgame.properties");
            appProperties.load(in);
            in.close();
        }catch(IOException e){
            System.out.println("Error en metodo PropertiesFile: "+e);
	}
	teclas = new Panel();
        guardarReset = new Panel();
        
	lasTeclas = new GridLayout(7,2);
        
	sonido = new Label("Sonido",Label.LEFT);
        accion = new Label("Dispara lasers / captura y lanza bola",Label.LEFT);
        pistaMusical = new Label("Musica",Label.LEFT);
        izquierda = new Label("Izquierda",Label.LEFT);
	derecha = new Label("Derecha",Label.LEFT);
        pantalla = new Label("Tamaño Pantalla",Label.LEFT);
        tipoNave= new Label("Apariencia de la nave",Label.LEFT);
	
        nave = new Choice();
        nave.add("Roja");
        nave.add("Azul");
        
	pista = new Choice();
	pista.add("Original");
	pista.add("Opcional");
        
        sonidoChoice = new Choice();
        sonidoChoice.add("Activado");
        sonidoChoice.add("Desactivado");
	
	guardar = new Button("Guardar");
	reset = new Button("Reset");
        teclaAccion = new Button(appProperties.getProperty("teclaAccion"));
        
	teclaIzquierda = new Button(appProperties.getProperty("teclaIzquierda"));
	teclaDerecha = new Button(appProperties.getProperty("teclaDerecha"));
        
	estiloPantalla = new Choice();
	estiloPantalla.add("Ventana");
	estiloPantalla.add("Pantalla completa");
        
        if (appProperties.getProperty("nave").equals("nave")){
            nave.select(0);
        }else{
            nave.select(1);
        }
        
        if (appProperties.getProperty("fullScreen").equals("true")){
            estiloPantalla.select(1);
        }else{
          estiloPantalla.select(0);
        }
        
        if (appProperties.getProperty("sonidoChoice").equals("true")){
            sonidoChoice.select(0);
        }else{
            sonidoChoice.select(1);
        }
        
        switch (appProperties.getProperty("musica")){
            case "0":
              pista.select(0);
              break;
            case "1":
              pista.select(1);
              break;
            case "2":
              pista.select(2);
              break;
            case "3":
              pista.select(3);
              break;
        }

	guardar.addActionListener(this);
	reset.addActionListener(this);	
	teclaIzquierda.addActionListener(this);
	teclaDerecha.addActionListener(this);
	teclaAccion.addActionListener(this);
	teclaIzquierda.addKeyListener(new KeyAdapter()
	{
   			public void keyPressed(KeyEvent e)
   			{
                            if (e.getKeyCode()==KeyEvent.VK_LEFT){
                                teclaIzquierda.setLabel("<");
                            }
                            else{
                                if (e.getKeyCode()==KeyEvent.VK_RIGHT){
                                    teclaIzquierda.setLabel(">");
                                }else{
                                    teclaIzquierda.setLabel(Character.toString(e.getKeyChar()));
                                }
                            }
			}
	});
	teclaDerecha.addKeyListener(new KeyAdapter()
	{
   			public void keyPressed(KeyEvent e)
   			{
                            if (e.getKeyCode()==KeyEvent.VK_LEFT){
                                teclaDerecha.setLabel("<");
                            }
                            else{
                                if (e.getKeyCode()==KeyEvent.VK_RIGHT){
                                    teclaDerecha.setLabel(">");
                                }else{
                                    teclaDerecha.setLabel(Character.toString(e.getKeyChar()));
                                }
                            }
			}
	});
        teclaAccion.addKeyListener(new KeyAdapter()
	{
   			public void keyPressed(KeyEvent e)
   			{
                            if (e.getKeyCode()==KeyEvent.VK_LEFT){
                                teclaAccion.setLabel("<");
                            }
                            else{
                                if (e.getKeyCode()==KeyEvent.VK_RIGHT){
                                    teclaAccion.setLabel(">");
                                }else{
                                    if (e.getKeyCode()==KeyEvent.VK_SPACE){
                                        teclaAccion.setLabel("space");
                                    }else{
                                        teclaAccion.setLabel(Character.toString(e.getKeyChar()));
                                    }
                                }
                            }
			}
	});
	teclas.setLayout(lasTeclas);
	teclas.add(pantalla);
	teclas.add(estiloPantalla);
	teclas.add(pistaMusical);
	teclas.add(pista);
        teclas.add(tipoNave);
        teclas.add(nave);
	teclas.add(sonido);
        teclas.add(sonidoChoice);
	teclas.add(izquierda);
	teclas.add(teclaIzquierda);
	teclas.add(derecha);
	teclas.add(teclaDerecha);
	teclas.add(accion);
	teclas.add(teclaAccion);
	guardarReset.add(guardar);
	guardarReset.add(reset);
	//---------------------asignar layout y agregar objetos a 'this'
	this.add(teclas,BorderLayout.CENTER);
	this.add(guardarReset,BorderLayout.SOUTH);
	//---------------------capturar evento para cerrar la aplicacion
	this.addWindowListener(new java.awt.event.WindowAdapter(){
	    public void windowClosing(java.awt.event.WindowEvent e){
	        cerrarVentana();
	    }
	});
        
	this.pack();
	this.setVisible(true);
        this.setLocationRelativeTo(null);
}

public void cerrarVentana(){
	this.dispose();
}
public static void main(String a[]){
	Configuracion instance = new Configuracion();
}

public void actionPerformed(ActionEvent evt){
	if (evt.getActionCommand()==guardar.getActionCommand()){
            if (nave.getSelectedItem​().equals("Roja")){
                appProperties.setProperty("nave","nave");
            }else{
                appProperties.setProperty("nave","naveAzul");
            }
            
            if (estiloPantalla.getSelectedItem​().equals("Pantalla completa")){
                appProperties.setProperty("fullScreen","true");
            }else{
                appProperties.setProperty("fullScreen","false");
            }
            
            if (sonidoChoice.getSelectedItem​().equals("Activado")){
                    appProperties.setProperty("sonidoChoice","true");
            }else{
                    appProperties.setProperty("sonidoChoice","false");
            }
            if (pista.getSelectedItem​().equals("Original")){
                appProperties.setProperty("musica","0");
            }else{
                appProperties.setProperty("musica","1");
            }
            appProperties.setProperty("teclaIzquierda",teclaIzquierda.getLabel​());
            appProperties.setProperty("teclaDerecha",teclaDerecha.getLabel​());
            appProperties.setProperty("teclaAccion",teclaAccion.getLabel​());
            try{
                FileOutputStream out=new FileOutputStream("jgame.properties");
                appProperties.store(out,"jgame.properties");
                out.close();
            }catch(Exception e){
                System.out.print("Error guardar Properties");
            }
            this.cerrarVentana();
	}
	if (evt.getActionCommand()==reset.getActionCommand()){
           teclaIzquierda.setLabel("<");
           teclaDerecha.setLabel(">");
           teclaAccion.setLabel("space");
           nave.select(0);
           pista.select(0);
           sonidoChoice.select(0);
           estiloPantalla.select(0);
	}
}

}//end class
