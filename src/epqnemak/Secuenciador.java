/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package epq;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;

/**
 *
 * @author eduardoriojas
 */
public class Secuenciador {
    double InvMax = 0;
    VentanaOutput ventanao2 = new VentanaOutput();
    
    public void secuenciar(int numPros, String[] nombreDats, double[] tiempoCicloDats, double[] equiposCicloDats, double[] piezasPorCicloDats, double[] eteCicloDats, double[] costoPrepDats, double[] otrosCostosDats, double[] costoPiezaDats, double[] tiempoPrepDats, double demanda) throws BadLocationException, IOException{
    
        int secs = 0;
        double[] Invs = new double[numPros+1];
        double[] InvsFloor = new double[numPros+1];
        double[] count = new double[numPros];
        double[] equiposEfectivos = new double[numPros];
        double[] flujoReal = new double[numPros];
        double[] sobras = new double[numPros+1];
        
        
        NumberFormat nf2 = new DecimalFormat("##.##");
        
        List<Proceso> procesos = new ArrayList<Proceso>();
        
        for (int i= 0; i < numPros; i++){
            procesos.add(new Proceso());
        } 
        
        Proceso temps;
        Proceso tempsAnterior;
        Proceso tempss;
        
        temps = procesos.get(0);
        temps.Nombre = nombreDats[0];
        temps.TiempoCiclo = tiempoCicloDats[0];
        temps.Equipos = equiposCicloDats[0];
        temps.PiezasCiclo = piezasPorCicloDats[0];
        temps.ETE = eteCicloDats[0];
        temps.CostoPreparacion = costoPrepDats[0];
        temps.CostoPieza = costoPiezaDats[0];
        temps.OtrosCostos = otrosCostosDats[0];
        temps.CostoPieza = costoPiezaDats[0];
        temps.TiempoPreparacion = tiempoPrepDats[0];
        
        temps.calcularPiezasProducidasPorSeg();
        double minPPS = temps.PiezasProducidasPorSeg;
        int quienPPS = 0;
        int[] acumuladores = new int[numPros];
        int iAcum = 0;
        
        for (int w = 0; w < procesos.size(); w++){
            temps = procesos.get(w);
            temps.Nombre = nombreDats[w];
            temps.TiempoCiclo = tiempoCicloDats[w];
            temps.Equipos = equiposCicloDats[w];
            temps.PiezasCiclo = piezasPorCicloDats[w];
            temps.ETE = eteCicloDats[w];
            temps.CostoPreparacion = costoPrepDats[w];
            temps.CostoPieza = costoPiezaDats[w];
            temps.OtrosCostos = otrosCostosDats[w];
            temps.CostoPieza = costoPiezaDats[w];
            temps.TiempoPreparacion = tiempoPrepDats[w];
            temps.calcularPiezasProducidasPorSeg();
            
            if(w != 0)
            {
                tempsAnterior = procesos.get(w-1);
                if(temps.PiezasProducidasPorSeg<tempsAnterior.PiezasProducidasPorSeg && temps.PiezasProducidasPorSeg<minPPS)
                {
                    minPPS = temps.PiezasProducidasPorSeg;
                    quienPPS = w;
                    acumuladores[iAcum] = w;
                    iAcum = iAcum + 1;
                    
                }
               
            }
            
            temps.FlujoReal = minPPS;

            System.out.println("---"+temps.Nombre+"---");
            System.out.println(temps.TiempoCiclo);
            System.out.println(temps.Equipos);
            System.out.println(temps.PiezasCiclo);
            System.out.println(temps.ETE);
            System.out.println(temps.PiezasProducidasPorSeg);
            System.out.println(minPPS);
        }

        System.out.println(iAcum);
        
        System.out.println(minPPS);
        
        double tiempoTotalEst = demanda/minPPS;
        
        System.out.println(tiempoTotalEst);
        
        double intervalo = tiempoTotalEst /4000;
        
        int intervaloFloor = (int) Math.floor(intervalo);
        
        double llave = 0;
        
        boolean[] termino = new boolean[numPros];
        boolean[] esperando = new boolean[numPros];
        boolean[] procesando = new boolean[numPros];
        boolean[] procesandoinc = new boolean[numPros];
        boolean[] procesandoincc = new boolean[numPros];
        int[] inicioProceso = new int[numPros];
        
        Invs[0] = demanda;
        
        for(int cc = 0; cc<numPros; cc++)
            {
                termino[cc] = false;
                esperando[cc] = false;
                procesando[cc] = false;
                count[cc] = 1;
            }
        
        while(llave<numPros){
            for(int k = 0; k<procesos.size(); k++){
                
                temps = procesos.get(k);
              
                if(termino[k] == false)
                {
                    if(demanda >= count[k])
                    {
                       if(procesando[k] == false)
                       {
                           if(procesandoinc[k] == false)
                           {
                                if(Invs[k] >= temps.PiezasRealesPorCiclo)
                                {
                                    Invs[k] = Invs[k] - temps.PiezasRealesPorCiclo;
                                    procesando[k] = true;
                                    esperando[k] = false;
                                    inicioProceso[k] = secs;
                                }
                                else 
                                {
                                    if(k != 0) 
                                    {
                                         if(Invs[k] < temps.PiezasCiclo)
                                         {

                                                if(termino[k-1] == false)
                                                {
                                                   esperando[k] = true;
                                                   procesando[k] = false;
                                                   procesandoinc[k] = false;
                                                }
                                                else
                                                {
                                                   termino[k] = true;
                                                   llave = llave + 1.0;
                                                   esperando[k] = false;
                                                   procesando[k] = false;
                                                   procesandoinc[k] = false;
                                                }
                                            
                                           
                                         }
                                         else
                                         {
                                             //PROCESO INCOMPLETOOOOOOO
                                             InvsFloor[k] = Math.floor(Invs[k]);
                                             equiposEfectivos[k] = Math.floor(InvsFloor[k]/temps.PiezasCiclo);
                                             Invs[k] = Invs[k] - equiposEfectivos[k] * temps.PiezasCiclo;
                                             procesandoinc[k] = true;
                                             esperando[k] = false;
                                             inicioProceso[k] = secs;
                                             procesando[k] = false;
                                         }
                                    }
                                    else
                                    {
                                         termino[k] = true;
                                         llave = llave + 1.0;
                                         esperando[k] = false;                          
                                         procesando[k] = false;
                                         procesandoinc[k] = false;
                                    }
                                }
                       
                               
                            }
                            else if(secs - inicioProceso[k] >= temps.TiempoRealCiclo)
                            {
                                procesandoinc[k] = false;
                                esperando[k] = true;
                                Invs[k+1] = Invs[k+1] + equiposEfectivos[k] * temps.PiezasCiclo;
                                count[k] = count[k] + equiposEfectivos[k] * temps.PiezasCiclo;
                            }
                       }
                       else if(secs - inicioProceso[k] >= temps.TiempoRealCiclo)
                       {
                           procesando[k] = false;
                           esperando[k] = true;
                           Invs[k+1] = Invs[k+1] + temps.PiezasRealesPorCiclo;
                           count[k] = count[k] + temps.PiezasRealesPorCiclo;
                       }
                    }
                    else
                    {
                        termino[k] = true;
                        llave = llave + 1.0;
                        esperando[k] = false;
                        procesando[k] = false;
                        procesandoinc[k] = false;
                    }
                }
                       
            if(Invs[k] > InvMax && k != 0)
            {
                InvMax = Invs[k];
            }
            
            }
            
            secs = secs + 1;
            
            if(secs%intervaloFloor  == 0)
            {
               String secString = Integer.toString(secs);
               ventanao2.Mostrar(2, secString + " ");
               
               for(int ii = 0; ii<numPros; ii++)
               {
                   temps = procesos.get(ii);
                   String InvsString = Double.toString((Math.round(Invs[ii+1])));
                   ventanao2.Mostrar(1, InvsString + " ");
               }
               ventanao2.Mostrar(1, "\n");
            }
     
        }
        
        String secString = Integer.toString(secs);
        ventanao2.Mostrar(2, secString + " ");
        
        for(int ii = 0; ii<numPros; ii++)
               {
                   temps = procesos.get(ii);
                   String InvsString = Double.toString((Math.round(Invs[ii+1])));
                   ventanao2.Mostrar(1, InvsString + " ");
               }
        
        ventanao2.Mostrar(1, "\n");
        
 

        
    }
    
}
  
