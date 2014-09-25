/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package epqnemak;

/**
 *
 * @author eduardoriojas
 */
public class Proceso {
    
    String Nombre = "xxx";
    double TiempoCiclo; 
    double PiezasCiclo;
    double Equipos;
    double ETE;
    double CostoPreparacion;
    double OtrosCostos;
    double CostoPieza;
    double TiempoPreparacion;
    
    double HoldingCostPorSeg;
    
    double ConteoPiezas = 0;
    double PiezasProducidasPorSeg;
    double PiezasRealesPorCiclo;
    double TiempoRealCiclo;
    double FlujoReal;
    
    double qOpt;
    double ptoReorden;
    double conteoTemp;
    boolean Productor = false;
    
    public void calcularPiezasProducidasPorSeg()
    {
        HoldingCostPorSeg = OtrosCostos + .12/(12*30*24*60*60) * CostoPieza;
        PiezasRealesPorCiclo = PiezasCiclo * Equipos;
        if(ETE<= 1)
        {
        TiempoRealCiclo = TiempoCiclo / (ETE);
        PiezasProducidasPorSeg = ( 1 / TiempoCiclo) * PiezasCiclo * Equipos * ETE;
        }
        else
        {
        TiempoRealCiclo = TiempoCiclo / (ETE* .01);
        PiezasProducidasPorSeg = ( 1 / TiempoCiclo) * PiezasCiclo * Equipos * ETE * .01;
        }
    }
}
