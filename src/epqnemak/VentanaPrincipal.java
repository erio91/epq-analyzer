/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package epqnemak;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;

/**
 *
 * @author eduardoriojas
 */
public class VentanaPrincipal extends JFrame implements ActionListener, ItemListener {
    
    String NumeroProcesosS = JOptionPane.showInputDialog("Numero de procesos en linea?"); 
    public int NumeroProcesos;
    
    String DemandaGlobalS = JOptionPane.showInputDialog("Demanda global?");
    public double DemandaGlobal = Integer.parseInt(DemandaGlobalS);
    public double interes = .12;
    
    double[] Invs;
    
    String[] nombreDat;
    double[] tiempoCicloDat;
    double[] piezasPorCicloDat;
    double[] equiposCicloDat;
    double[] eteCicloDat;
    double[] costoPreparacionDat;
    double[] otrosCostosInvDat;
    double[] costoPiezaDat;
    double[] tiempoPreparacionDat;
    
    int intervalo = 50;
    
    JComboBox menuProcesos = new JComboBox();
    JLabel etiquetaProcesos = new JLabel("Proceso:");
    
    JButton botonOk = new JButton("OK");
    JButton botonGuardar = new JButton("Guardar");
    
    JTextField tfNombre = new JTextField(15);
    JTextField tfTiempoCiclo = new JTextField(15);
    JTextField tfPiezasPorCiclo = new JTextField(15);
    JTextField tfEquipos = new JTextField(15);
    JTextField tfETE = new JTextField(15);
    JTextField tfCostoPrep = new JTextField(15);
    JTextField tfOtrosCostos = new JTextField(15);
    JTextField tfCostoPieza = new JTextField(15);
    JTextField tfTiempoPrep = new JTextField(15);
    
    JTextField procesoGuardado = new JTextField(25);
       
    JLabel lblNombre = new JLabel("Nombre:",JLabel.LEFT);
    JLabel lblTiempoCiclo = new JLabel("C/T:",JLabel.LEFT);
    JLabel lblPiezasPorCiclo = new JLabel("Piezas/ciclo:",JLabel.LEFT);
    JLabel lblEquipos = new JLabel("Equipos:",JLabel.LEFT);
    JLabel lblETE = new JLabel("ETE %:",JLabel.LEFT);
    JLabel lblCostoPrep = new JLabel("A ($/corrida):",JLabel.LEFT);
    JLabel lblOtrosCostos = new JLabel("w ($/s/pza):",JLabel.LEFT);
    JLabel lblCostoPieza = new JLabel("c ($/pza):",JLabel.LEFT);
    JLabel lblTiempoPrep = new JLabel("tao (s):",JLabel.LEFT);
    JLabel lblStatus = new JLabel("Status:",JLabel.LEFT);
    
    JPanel arriba = new JPanel();
    JPanel abajo1 = new JPanel();
    JPanel abajo2 = new JPanel();
    JPanel abajo3 = new JPanel();
    JPanel abajo4 = new JPanel();
    JPanel abajo5 = new JPanel();
    JPanel abajo6 = new JPanel();
    JPanel abajo7 = new JPanel();
    JPanel abajo8 = new JPanel();
    JPanel abajo9 = new JPanel();
    JPanel abajo10 = new JPanel();
    JPanel abajo11 = new JPanel();
    
    String[] nombreDef = {"SOP", "SIM","REB","IMP","SIE","ESC","DES","HTT","PON","CE1","CE2","SPL","FAK","INS"};       
    double[] tiempoCicloDef = {60. , 325. , 59. , 130. , 36. , 34. , 30. , 400. , 33. , 39. , 23. , 30. , 35. , 49.};
    double[] equipoDef = {     2.8 , 2.,    2.,   2.,    1.,   1.,   1.,   1.,   1.,   1.,   1.,   1.,   1.,   1.};
    double[] piezasPorCicloDef = {3.,5. , 1., 4., 1.,1.,1.,5.,1.,1.,1.,1.,1.,2.};
    double[] eteDef = {58,68,75,73,70,85,85,75,85,85,85,85,85,85};
    
    VentanaPrincipal(int numPro){
        super();
        
        if (NumeroProcesosS.equals("demo"))
        {
            NumeroProcesos = 14;
            System.out.println("x");
        }
        else
        {
            System.out.println("xx");
            NumeroProcesos = Integer.parseInt(NumeroProcesosS);
        }
        
        Invs = new double[NumeroProcesos];
        
        nombreDat = new String[NumeroProcesos];
        tiempoCicloDat = new double[NumeroProcesos];
        piezasPorCicloDat = new double[NumeroProcesos];
        equiposCicloDat = new double[NumeroProcesos];
        eteCicloDat = new double[NumeroProcesos];
        costoPreparacionDat = new double[NumeroProcesos];
        otrosCostosInvDat = new double [NumeroProcesos];
        costoPiezaDat = new double [NumeroProcesos];
        tiempoPreparacionDat = new double[NumeroProcesos];
        
        numPro = NumeroProcesos;
        int numeroProcesos = NumeroProcesos;
 
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,540);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        
        for(int i = 0; i < numeroProcesos; i++)
        {
            menuProcesos.addItem(i+1);
        }
        
                
        if(NumeroProcesosS.equals("demo"))
        {
        for(int i = 0; i < numeroProcesos; i++)
            {
                nombreDat[i] = nombreDef[i];
                tiempoCicloDat[i] = tiempoCicloDef[i];
                equiposCicloDat[i] = equipoDef[i];
                piezasPorCicloDat[i] = piezasPorCicloDef[i];
                eteCicloDat[i] = eteDef[i];
                costoPreparacionDat[i] = 100;
                otrosCostosInvDat[i] = .0000005;
                costoPiezaDat[i] = 1000;
                tiempoPreparacionDat[i] = 100;
            }
        }
        
        arriba.add(etiquetaProcesos);
        arriba.add(menuProcesos);
        add(arriba);
        
        abajo1.add(lblNombre);
        abajo1.add(tfNombre);
        abajo2.add(lblTiempoCiclo);
        abajo2.add(tfTiempoCiclo);
        abajo3.add(lblEquipos);
        abajo3.add(tfEquipos);
        abajo4.add(lblPiezasPorCiclo);
        abajo4.add(tfPiezasPorCiclo);
        abajo5.add(lblETE);
        abajo5.add(tfETE);
        abajo6.add(lblCostoPrep);
        abajo6.add(tfCostoPrep);
        abajo7.add(lblOtrosCostos);
        abajo7.add(tfOtrosCostos);
        abajo8.add(lblCostoPieza);
        abajo8.add(tfCostoPieza);
        abajo9.add(lblTiempoPrep);
        abajo9.add(tfTiempoPrep);
        abajo10.add(lblStatus);
        abajo10.add(procesoGuardado);
        abajo11.add(botonOk);
        abajo11.add(botonGuardar);
        
        procesoGuardado.setEditable(false);
        procesoGuardado.setBackground(Color.green);
        
        add(abajo1);
        add(abajo2);
        add(abajo3);
        add(abajo4);
        add(abajo5);
        add(abajo6);
        add(abajo7);
        add(abajo8);
        add(abajo9);
        add(abajo11);    
        add(abajo10);
           
        menuProcesos.addItemListener(this);
        menuProcesos.addActionListener(this);
        botonOk.addActionListener(this);
        botonGuardar.addActionListener(this);
        
        tfNombre.setText(nombreDat[0]);
        String currentTiempoCiclo = Double.toString(tiempoCicloDat[0]);
        tfTiempoCiclo.setText(currentTiempoCiclo);
        String currentEquipos = Double.toString(equiposCicloDat[0]);
        tfEquipos.setText(currentEquipos);
        String currentPiezasPorCiclo = Double.toString(piezasPorCicloDat[0]);
        tfPiezasPorCiclo.setText(currentPiezasPorCiclo);
        String currentEte = Double.toString(eteCicloDat[0]);
        tfETE.setText(currentEte);
        String currentCostoPrep = Double.toString(costoPreparacionDat[0]);
        tfCostoPrep.setText(currentCostoPrep);
        String currentOtrosCostos = Double.toString(otrosCostosInvDat[0]);
        tfOtrosCostos.setText(currentOtrosCostos);
        String currentCostoPieza = Double.toString(costoPiezaDat[0]);
        tfCostoPieza.setText(currentCostoPieza);
        String currentTiempoPrep = Double.toString(tiempoPreparacionDat[0]);
        tfTiempoPrep.setText(currentTiempoPrep);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {           
        String command = ae.getActionCommand();
        int temp = menuProcesos.getSelectedIndex();
        int numPro2 = this.NumeroProcesos;
        
        if (command.equals("Guardar"))
        {
            System.out.println("Guardando datos de proceso "+temp);
            procesoGuardado.setBackground(Color.black);
            procesoGuardado.setForeground(Color.yellow);
            procesoGuardado.setSelectedTextColor(Color.red);
            procesoGuardado.setText("Datos de proceso "+(temp + 1)+" guardados!");
            
            this.nombreDat[temp] = tfNombre.getText();
            tiempoCicloDat[temp] = Double.parseDouble(tfTiempoCiclo.getText());
            equiposCicloDat[temp] = Double.parseDouble(tfEquipos.getText());
            piezasPorCicloDat[temp] = Double.parseDouble(tfPiezasPorCiclo.getText());
            eteCicloDat[temp] = Double.parseDouble(tfETE.getText());
            costoPreparacionDat[temp] = Double.parseDouble(tfCostoPrep.getText());
            otrosCostosInvDat[temp] = Double.parseDouble(tfOtrosCostos.getText());
            costoPiezaDat[temp] = Double.parseDouble(tfCostoPieza.getText());
            tiempoPreparacionDat[temp] = Double.parseDouble(tfTiempoPrep.getText());
        }
        if (command.equals("OK"))
        {
            Secuenciador secuenciador1 = new Secuenciador();
            
            try {
                secuenciador1.secuenciar(numPro2, nombreDat, tiempoCicloDat, equiposCicloDat, piezasPorCicloDat, eteCicloDat, costoPreparacionDat, otrosCostosInvDat, costoPiezaDat, tiempoPreparacionDat, DemandaGlobal );
            } catch (BadLocationException ex) {
                Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            SecuenciadorOpt secuenciador2 = new SecuenciadorOpt();

            try {
                secuenciador2.secuenciar(numPro2, nombreDat, tiempoCicloDat, equiposCicloDat, piezasPorCicloDat, eteCicloDat, costoPreparacionDat, otrosCostosInvDat, costoPiezaDat, tiempoPreparacionDat, DemandaGlobal, secuenciador1.InvMax );
            } catch (BadLocationException ex) {
                Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @Override
    public void itemStateChanged(ItemEvent ie) {
        Object cual = ie.getItem();
        String seleccionS = cual.toString();
        int seleccion = Integer.parseInt(seleccionS);
        tfNombre.setText(nombreDat[seleccion-1]);
        String currentTiempoCiclo = Double.toString(tiempoCicloDat[seleccion-1]);
        tfTiempoCiclo.setText(currentTiempoCiclo);
        String currentEquipos = Double.toString(equiposCicloDat[seleccion-1]);
        tfEquipos.setText(currentEquipos);
        String currentPiezasPorCiclo = Double.toString(piezasPorCicloDat[seleccion-1]);
        tfPiezasPorCiclo.setText(currentPiezasPorCiclo);
        String currentEte = Double.toString(eteCicloDat[seleccion-1]);
        tfETE.setText(currentEte);
        String currentCostoPrep = Double.toString(costoPreparacionDat[seleccion-1]);
        tfCostoPrep.setText(currentCostoPrep);
        String currentOtrosCostos = Double.toString(otrosCostosInvDat[seleccion-1]);
        tfOtrosCostos.setText(currentOtrosCostos);
        String currentCostoPieza = Double.toString(costoPiezaDat[seleccion-1]);
        tfCostoPieza.setText(currentCostoPieza);
        String currentTiempoPrep = Double.toString(tiempoPreparacionDat[seleccion-1]);
        tfTiempoPrep.setText(currentTiempoPrep);
        
        
    }
    

    
}
