package org.example;

import bpmnClases.Caminos;
import bpmnClases.GestionDeProcesos;
import bpmnClases.Proceso;
import bpmnClases.Tarea;
import bpmnFichero.leerFicheroBpmn;
import tiempos.Horas;

import java.util.Objects;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        leerFicheroBpmn leer = new leerFicheroBpmn();

        leer.leer("diagram3");

        for(Tarea i : leer.listaDeObjetos){
            if(i.getNombre().equals("Accion corta")){
                i.establecerCosteTiempo( new Horas(2 , 0 , 0) );
            }else if (i.getNombre().equals("Accion larga")) {
                i.establecerCosteTiempo(new Horas( 10 , 0 , 0 ));
            }else if (i.getNombre().equals("PrimeraAccion Or")) {
                i.establecerCosteTiempo(new Horas( 4 , 0 , 0 ));
            }else if (i.getNombre().equals("SegundaAccion Or")) {
                i.establecerCosteTiempo(new Horas( 1 , 0 , 0 ));
            }
            else{
                i.establecerCosteTiempo(new Horas( 0 , 0 , 0));
            }

            if(i.getNombre().equals("OrPrimera")){
                i.getProbabilidad().add(1.0f);
                i.getProbabilidad().add(0.0f);
            }
            if(i.getNombre().equals("OrSegunda")){
                i.getProbabilidad().add(1.0f);
                i.getProbabilidad().add(1.0f);
            }

        }
        //Aqui vamos a iniciar un proceso
        GestionDeProcesos g = new GestionDeProcesos( leer.listaDeObjetos , 1 , new Horas( 0, 30 , 0),leer.eventoInicio );
        int cont = 50;
        while(cont > 0 && g.getProcesoMenor() != null){

            g.realizarAccion();
            g.ensenar();

            cont -= 1;
        }



        System.out.println("===============================");
        for( Object i : leer.listaDeObjetos ){
            System.out.println(i);
        }
        System.out.println("===============================");
        for( Caminos i : leer.listaCamino ){
            System.out.println(i);
        }
        //System.out.println(leer.eventoInicio);

    }

}