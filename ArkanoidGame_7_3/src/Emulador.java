import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.List;
import java.awt.Button;
import java.awt.Label;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.net.*;
import java.awt.Image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import static jdk.nashorn.internal.objects.NativeDebug.getClass;

//--------------definicion de clase--------------
public class Emulador extends JFrame implements ItemListener,ActionListener{
//--------------atributos--------------
private JPanel panelJuego;
private JPanel panelLista;
private List juegos;
private JPanel panelImagen;
private JPanel panelAccion;
private JButton iniciar;
private BorderLayout borderImagen;
private JButton configuracion;
CardLayout cardlayout;

//--------------constructor--------------
public Emulador() throws IOException{
	super("Emulador");
	//---------------------crear objetos
	panelJuego = new JPanel();
	panelLista = new JPanel();
	juegos = new List(10, false);
	juegos.add("Arkanoid");
	juegos.add("Contra");
	juegos.add("Doom");
	juegos.add("Duke Nukem 3D");
	juegos.add("Paper Boy");
        juegos.select(0);
	panelImagen = new JPanel();
	panelAccion = new JPanel();
	iniciar = new JButton("Iniciar");
	borderImagen = new BorderLayout();
	configuracion = new JButton("Ajustes");
	juegos.addItemListener(this);
	configuracion.addActionListener(this);
        iniciar.addActionListener(this);

	//---------------------asignar layouts y agregar objetos a contenedores
	
	panelJuego.setLayout(borderImagen);
	cardlayout=new CardLayout();
	panelJuego.add(panelImagen,BorderLayout.CENTER);
	panelJuego.add(panelAccion,BorderLayout.SOUTH);
	panelLista.add(juegos);
	panelAccion.add(iniciar);
	panelAccion.add(configuracion);
	panelImagen.setLayout(cardlayout);
	panelImagen.add("Arkanoid",new Diapositiva("arkanoid.jpg"));
	panelImagen.add("Contra",new Diapositiva("contra.jpg"));
	panelImagen.add("Doom",new Diapositiva("doom.jpg"));
	panelImagen.add("Duke Nukem 3D",new Diapositiva("duke-nukem-3d.jpg"));
	panelImagen.add("Paper Boy",new Diapositiva("paperboy.jpg"));
	//---------------------asignar layout y agregar objetos a 'this'
	this.add(panelLista,BorderLayout.WEST);
	this.add(panelJuego,BorderLayout.EAST);
	//---------------------capturar evento para cerrar la aplicacion
	this.addWindowListener(new java.awt.event.WindowAdapter(){
	    public void windowClosing(java.awt.event.WindowEvent e){
	        System.exit(0);
	    }
	});
	//set automatic size and show
	this.pack();
	this.setVisible(true);
        this.setLocationRelativeTo(null);
}//end constructor

public void iniciar(String juego){
    if (juego.equals("Arkanoid")){
        System.out.print("iniciar juego");
        Arkanoid game = new Arkanoid();
        Thread t = new Thread() {
            public void run() {
                game.run(1.0 / 60.0);
            }
        };
        t.start();
    }
    else{
        
    }
}

public void itemStateChanged(ItemEvent e){
	cardlayout.show(panelImagen,(String)this.juegos.getSelectedItem());
}

public static void main(String a[]) throws IOException{
    Emulador instance = new Emulador();
}

public void actionPerformed(ActionEvent evt){
	if (evt.getActionCommand()==configuracion.getActionCommand()){
            Configuracion instance = new Configuracion();
	}
	if (evt.getActionCommand()==iniciar.getActionCommand()){
		this.iniciar(this.juegos.getSelectedItem());
	}
}

}//end class

class Diapositiva extends JPanel{
	JLabel lbl_texto;

	public Diapositiva(String archivo) throws IOException{
		lbl_texto = new JLabel();

		ImageIcon image = new ImageIcon(getClass().getResource(archivo));
		Image image2=image.getImage();
		Image newImg=image2.getScaledInstance(640,480, java.awt.Image.SCALE_SMOOTH);
		image = new ImageIcon(newImg);

		if (image != null){
			lbl_texto.setIcon(image);
			//lbl_texto.setText(archivo);
		}else{
			lbl_texto.setText("Imposible mostrar la imagen");
		}
		this.add(lbl_texto);
	}

}