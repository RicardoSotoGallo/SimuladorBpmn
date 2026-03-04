package bpmnClases;

import tiempos.Horas;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GestionDeProcesos {
    public static final String RESET = "\u001B[0m";
    public static final String ROJO = "\u001B[31m";
    public static final String VERDE = "\u001B[32m";
    public static final String AMARILLO = "\u001B[33m";
    public static final String AZUL = "\u001B[34m";
    Tarea tareaInicial;
    List<Proceso> listaDeProcesos;
    List<Tarea> listaTareas;
    Proceso procesoMenor;
    Integer procesosMaximos;
    Horas tiempoDeSalida;
    Integer procesoid;
    public GestionDeProcesos(List<Tarea> listaDeTareas , Integer numeroDeProcesos , Horas frecuenciaDeProceso , Tarea tareaInicial){
        this.listaTareas = listaDeTareas;
        this.procesosMaximos = numeroDeProcesos;
        this.tiempoDeSalida = frecuenciaDeProceso;
        this.tareaInicial = tareaInicial;
        listaDeProcesos = new ArrayList<>();
        procesoid = 0;
        Proceso procesoAux;
        if( procesosMaximos > 0 ){
            procesoAux = new Proceso( tareaInicial , 0 , new Horas(0,0,0 ) ,procesoid , new ArrayList<>());
            listaDeProcesos.add( procesoAux );
            tareaInicial.anadirProceso(procesoAux);
            procesoid +=1;
            if(procesosMaximos >= 1){
                procesoAux = new Proceso( tareaInicial , 0 , tiempoDeSalida ,procesoid , new ArrayList<>());
                listaDeProcesos.add( procesoAux );
                tareaInicial.anadirProceso(procesoAux);
                procesoid+=1;
            }
        }
        eleguirMenor();
    }

    private void eleguirMenor(){
        //Esto en algun moemnto habra que cambiarlo a que la lista siempre este ordenada pero me parece bien asi
        procesoMenor = listaDeProcesos.stream().filter( c -> c.getCola() == 0 )
                .min(Comparator.comparing(Proceso::getTiempoDeRealizacion)  ).orElse(null);
    }
    public void realizarAccion(){
        procesoMenor.ejecutarProceso(listaDeProcesos);
        eleguirMenor();
    }
    public void ensenar(){
        System.out.println("================== Enseñar ======================");
        for(Proceso p : listaDeProcesos){
            System.out.println("Tareaid: "+VERDE+p.getId() +RESET);
            System.out.println("Tarea  : "+ROJO+p.getTareaActual().getNombre() +RESET);
            if(p.getTareaAnterior() != null) System.out.println("Tarea A: "+ROJO+p.getTareaAnterior().getNombre() +RESET);
            System.out.println("Cola   : "+AZUL+p.getCola() +RESET);
            System.out.println("Tiempo : "+AMARILLO+p.getTiempoDeRealizacion() +RESET);
            System.out.println("=================================================");
        }
        System.out.println("================== Fin vi ======================\n");
        if(procesoMenor != null){
            System.out.println(VERDE+"================== Tarea Minima ======================"+RESET);
            System.out.println("Tareaid: "+VERDE+procesoMenor.getId() +RESET);
            System.out.println("Tarea  : "+ROJO+procesoMenor.getTareaActual().getNombre() +RESET);
            if(procesoMenor.getTareaAnterior() != null) System.out.println("Tarea A: "+ROJO+procesoMenor.getTareaAnterior().getNombre() +RESET);
            System.out.println("Cola   : "+AZUL+procesoMenor.getCola() +RESET);
            System.out.println("Tiempo : "+AMARILLO+procesoMenor.getTiempoDeRealizacion() +RESET);
            System.out.println(ROJO+"----------------- Fin eseñar -----------------------"+RESET);
        }else{
            System.out.println("Final del ejercicio");
        }


    }

    public List<Proceso> getListaDeProcesos() {
        return listaDeProcesos;
    }

    public Proceso getProcesoMenor() {
        return procesoMenor;
    }
}
