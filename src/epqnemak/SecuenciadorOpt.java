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
import javax.swing.text.BadLocationException;

/**
 *
 * @author eduardoriojas
 */
public class SecuenciadorOpt {
    
    VentanaOutput ventanao2 = new VentanaOutput();
    VentanaOutput2 ventanao3 = new VentanaOutput2();
   
    
    public void secuenciar(int numPros, String[] nombreDats, double[] tiempoCicloDats, double[] equiposCicloDats, double[] piezasPorCicloDats, double[] eteCicloDats, double[] costoPrepDats, double[] otrosCostosDats, double[] costoPiezaDats, double[] tiempoPrepDats, double demanda, double InvMaxPush) throws BadLocationException, IOException{
    
        int secs = 0;
        double[] Invs = new double[numPros+1];
        double[] InvsFloor = new double[numPros+1];
        double[] count = new double[numPros];
        double[] equiposEfectivos = new double[numPros];
        double InvMax = 0;
        
        for(int h = 0; h < numPros+1 ; h++)
        {
            Invs[h] = 0;
            InvsFloor[h] = 0;
        }
        
        for(int hh = 0; hh < numPros ; hh++)
        {
            count[hh] = 0;
            equiposEfectivos[hh] = 0;
        }
        
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
            System.out.println(Invs[w]);
        }
        
        Proceso productor;
        Proceso cliente;
        
        double[] qOptima = new double[iAcum];
        double[] p = new double[iAcum];
        double[] d = new double[iAcum];
        double[] periodo = new double[iAcum];
        double[] holdingCostPorPeriodo = new double[iAcum];
        double[] t = new double[iAcum];
        double[] tIMax = new double[iAcum];
        double[] m = new double[iAcum];
        double[] r = new double[iAcum];
        ventanao3.Mostrar(1, "Analisis EPQ\n");
        
        for (int y = 0; y < iAcum; y++)
        {
            System.out.println("+++");
            System.out.println(acumuladores[y]);
            System.out.println("+++");
            
            productor = procesos.get(acumuladores[y]-1);
            cliente = procesos.get(acumuladores[y]);
            
            productor.Productor = true;
            
            periodo[y] = demanda / cliente.FlujoReal;

            p[y] = periodo[y] * productor.FlujoReal;
            d[y] = periodo[y] * cliente.FlujoReal;
            holdingCostPorPeriodo[y] = productor.HoldingCostPorSeg * periodo[y];
            
            qOptima[y] = Math.sqrt((2*productor.CostoPreparacion*d[y])/(holdingCostPorPeriodo[y]*(1-d[y]/p[y])));
            t[y] = qOptima[y] / d[y];
            tIMax[y] = qOptima[y] * (1-d[y]/p[y]) / d[y];
            m[y] = Math.floor(productor.TiempoPreparacion / t[y]);
            
            productor.qOpt = qOptima[y];
            
            if(productor.TiempoPreparacion < t[y] && productor.TiempoPreparacion < tIMax[y])
                r[y] = productor.TiempoPreparacion * d[y];
            else
                if(productor.TiempoPreparacion < t[y] && productor.TiempoPreparacion > tIMax[y])
                    r[y] = (p[y] - d[y]) * (t[y] - productor.TiempoPreparacion);
                else
                    if(productor.TiempoPreparacion > t[y] && (productor.TiempoPreparacion - m[y] * t[y]) < tIMax[y])
                        r[y] = productor.TiempoPreparacion * d[y] - m[y] * qOptima[y];
                    else
                        r[y] = (p[y] - d[y]) * ((m[y] + 1) * t[y] - productor.TiempoPreparacion);
            
            productor.ptoReorden = r[y];

            System.out.println("***");
            System.out.println(d[y]);
            System.out.println(p[y]);
            System.out.println(productor.FlujoReal);
            System.out.println(cliente.FlujoReal);
            System.out.println(holdingCostPorPeriodo[y]);
            System.out.println(productor.CostoPreparacion);
            System.out.println(qOptima[y]);
            System.out.println(r[y]);
            System.out.println(temps.ptoReorden);
            System.out.println("***");

            String dString = Double.toString(d[y]);
            String pString = Double.toString(p[y]);
            String qString = Double.toString(qOptima[y]);
            String rString = Double.toString(r[y]);
            String yString = Integer.toString(y+1);
            String clienteString = Integer.toString(acumuladores[y] + 1);
            String productorString = Integer.toString(acumuladores[y]);
             
            ventanao3.Mostrar(1, "\n");
            ventanao3.Mostrar(2, "+++++ Cuello de botella " + yString + " +++++\n");
            ventanao3.Mostrar(8,"Cliente: Proceso " + clienteString + " " + cliente.Nombre + "\n");
            ventanao3.Mostrar(4,"Proveedor: Proceso " + productorString+ " " + productor.Nombre +"\n");
            ventanao3.Mostrar(5,"Cantidad óptima: " + qString + "\n");
            ventanao3.Mostrar(5, "Punto de reorden: " + rString + "\n");
            
            
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
        boolean[] prendido = new boolean[numPros];
        int[] inicioProceso = new int[numPros];
  
        
        Invs[0] = demanda;
        
        for(int cc = 0; cc<numPros; cc++)
            {
                termino[cc] = false;
                esperando[cc] = false;
                procesando[cc] = false;
                prendido[cc] = false;
                count[cc] = 1;
                
            }
        
         for (int ww = 0; ww < procesos.size(); ww++){
            temps = procesos.get(ww);
            System.out.println(temps.Nombre);
            System.out.println(temps.Productor);
         }
        
        while(llave<numPros){
            
            for(int k = 0; k<procesos.size(); k++){
                
                temps = procesos.get(k);
              
                if(termino[k] == false)
                {
                if(demanda >= count[k])
                    {
                    if(temps.Productor == true)
                    {
                       if(prendido[k] == true)
                       {
                          if(temps.conteoTemp < temps.qOpt)
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
                                                  InvsFloor[k] = Math.floor(Invs[k]);
                                                  equiposEfectivos[k] = Math.floor(InvsFloor[k]/temps.PiezasCiclo);
                                                  System.out.println(k + " " + equiposEfectivos[k] + " " + InvsFloor[k] + " " + temps.Equipos);
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
                                     temps.conteoTemp = temps.conteoTemp + equiposEfectivos[k] * temps.PiezasCiclo;
                                 }
                            }
                            else if(secs - inicioProceso[k] >= temps.TiempoRealCiclo)
                            {
                                procesando[k] = false;
                                esperando[k] = true;
                                Invs[k+1] = Invs[k+1] + temps.PiezasRealesPorCiclo;
                                count[k] = count[k] + temps.PiezasRealesPorCiclo;
                                temps.conteoTemp = temps.conteoTemp + temps.PiezasRealesPorCiclo;
                            }
                           }
                           else
                           {
                               temps.conteoTemp = 0;
                               prendido[k] = false;
                           }
                       }
                       else
                       {
                           
                           if(Invs[k+1] < temps.ptoReorden)
                           {
                               prendido[k] = true;
                               System.out.println(temps.Nombre + " prendiendo");
                           }
                       }
                    }
                    else
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
                                                  InvsFloor[k] = Math.floor(Invs[k]);
                                                  equiposEfectivos[k] = Math.floor(InvsFloor[k]/temps.PiezasCiclo);
                                                  System.out.println(k + " " + equiposEfectivos[k] + " " + InvsFloor[k] + " " + temps.Equipos);
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
               System.out.println(secs);
               
               String secString = Integer.toString(secs);
               ventanao2.Mostrar(2, secString + " ");
               
               for(int ii = 0; ii<numPros; ii++)
               {
                   temps = procesos.get(ii);
                   String InvsString = Double.toString((Math.round(Invs[ii+1])));
                   System.out.print("  " + InvsString + "  ");              
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
        System.out.println(llave);
        ventanao3.Mostrar(1, "\n");
        ventanao3.Mostrar(1, "\n");
        ventanao3.Mostrar(6, "Inventario Máximo Push: " + InvMaxPush + "\n");
        ventanao3.Mostrar(9, "Inventario Máximo EPQ: " + InvMax + "\n");
        double reduccion = (InvMaxPush-InvMax)/InvMaxPush * 100;
        ventanao3.Mostrar(10, "Reducción de: " + reduccion + "% \n");
        
    
        
    }
    
}
  
