package bpmnClases;

import tiempos.Horas;

import java.util.*;

public class Proceso {
    private Tarea tareaActual;
    private Integer cola;
    private Tarea tareaAnterior;
    private Integer id;
    private List<Tarea>  puertasPasadas;
    private List<HistorialPuertaOr> puertasOrPasadas;
    /*
    Cola = 0 significa que se esta procesando
    Cola > 0 es que esta en espera (Porque un proceso esta ocupado
    Cola < 0 significa que estamos en un condicional siendo ese numero negativo el numero de procesos que necesitamos esperar

     */
    private Horas tiempoDeRealizacion;

    public Proceso(Tarea tareaActual, Integer cola, Horas tiempoDeRealizacion , Integer id , List<HistorialPuertaOr> puertasOr) {
        this.tareaActual = tareaActual;
        this.cola = cola;
        this.tiempoDeRealizacion = tiempoDeRealizacion;
        this.id = id;
        this.puertasPasadas = new ArrayList<>();
        this.puertasOrPasadas = puertasOr;
    }

    public Integer getId() {
        return id;
    }

    public Tarea getTareaAnterior() {
        return tareaAnterior;
    }

    public Tarea getTareaActual() {
        return tareaActual;
    }

    public Integer getCola() {
        return cola;
    }

    public Horas getTiempoDeRealizacion() {
        return tiempoDeRealizacion;
    }
    public void ejecutarProceso( List<Proceso> lsProceso){

        for( Proceso i : lsProceso ){
            if( !i.equals(this) && i.cola == 0 ){
                i.tiempoDeRealizacion = i.tiempoDeRealizacion.restaHoras(this.tiempoDeRealizacion);
            }
        }


        if(this.tareaActual.getTipo().equals("startEvent")) inicioTarea(lsProceso);
        else if(this.tareaActual.getTipo().equals("parallelGateway")){
            puertaAND(
                    lsProceso
            );
        }else if(this.tareaActual.getTipo().equals("task")) proceseTarea(lsProceso);
        else if (this.tareaActual.getTipo().equals("endEvent")) tareaFinal(lsProceso);
        else if(this.tareaActual.getTipo().equals("exclusiveGateway")) puertaXOR(lsProceso);
        else if(this.tareaActual.getTipo().equals("inclusiveGateway")) puertaOr(lsProceso);

//exclusiveGateway


    }
    private void inicioTarea(List<Proceso> todosProceso){
        //Movemos la tarea
        mover(todosProceso);
        tiempoDeRealizacion = tareaActual.getTiempoTarda();

    }
    private void puertaAND(List<Proceso> todosProceso){

        //Dicho rapido que se hace
        //Se coge todas los procesos ya guardado + el proceso actual
        //se mira sus tareas anteriores, si estas tienen el mimso tamaño se procede a mover. si no se procece a darle cola -1
        //se espera a que llegue otro proceso para volver a hacer la comparacion
        Set<Tarea> compo = new HashSet<>();
        List<Proceso> procesoCompro = new ArrayList<>();
        List<Tarea> listaEntrada = this.tareaActual.getAnteriorTarea();
        List<Proceso> procesoId = new ArrayList<>(this.tareaActual.getProcesosDentro().stream()
                .filter(c -> c.id.equals(this.id)).toList());

        List<Tarea> listaCondicionEntrada = procesoId.stream()
                .map(c -> c.tareaAnterior).toList();
        if( listaEntrada.size() == listaCondicionEntrada.size() ){
            for( Proceso j : procesoId ){
                todosProceso.remove(j); //Eliminamos el actual de la lista
                tareaActual.eliminarProceso(j); //Eliminamos el actual de la tarea
            }
            Proceso procesoAux;
            for(Tarea i : tareaActual.getSigienteTarea()){
                //Creamos los nuevos procesos
                procesoAux = new Proceso(
                        i,
                        i.asignarCola(),
                        i.getTiempoTarda(),
                        this.id,
                        this.puertasOrPasadas

                );
                //Guardamos su tarea anterior
                procesoAux.tareaAnterior = tareaActual;
                i.anadirProceso(procesoAux);

                //Lo metemos en la lista
                todosProceso.add(procesoAux);


            }

        }else{
            this.cola = -1;
        }
        //mover(todosProceso);




    }
    private void puertaXOR(List<Proceso> todosProcesos){
        float random = (float) Math.random();
        float acum = 0.0f;
        int resultado = -1;
        for(int i = 0 ; i < this.tareaActual.getProbabilidad().size() ; i++){
            acum += this.tareaActual.getProbabilidad().get(i);

            if(random <= acum){
                resultado = i;
                break;
            }
        }
        if(resultado == -1) resultado = this.tareaActual.getProbabilidad().size()-1;

        moverUno(todosProcesos , resultado);


    }
    private void puertaOr(List<Proceso> todosProcesos){

        if(this.tareaActual.getSigienteTarea().size() > 1){
            todosProcesos.remove(this); //Eliminamos el actual de la lista
            tareaActual.eliminarProceso(this); //Eliminamos el actual de la tarea
            float azar;
            List<Tarea> listaTareas = new ArrayList<>();
            List<HistorialPuertaOr> lsh;
            for(int i = 0; i < this.tareaActual.getSigienteTarea().size() ; i++){
                azar = (float) Math.random();
                if( azar <= this.tareaActual.getProbabilidad().get(i) ){
                    listaTareas.add( this.tareaActual.getSigienteTarea().get(i) );
                }
            }
            HistorialPuertaOr h = new HistorialPuertaOr(this.tareaActual , listaTareas);
            for(int i = 0 ; i < listaTareas.size();i++){
                lsh = new ArrayList<>(puertasOrPasadas);
                lsh.add( h.elegir( listaTareas.get(i) ) );
                moverUnoSinEliminarOr(todosProcesos,listaTareas.get(i),lsh);
            }



        }else{
            List<Proceso> procesosId = this.getTareaActual().getProcesosDentro().stream()
                    .filter(c -> c.id.equals(this.id))
                    .toList();
            HistorialPuertaOr puertaOrHist = puertasOrPasadas.getLast();
            if(puertaOrHist.listaDeTareasPost.size() == procesosId.size()){

                for(Proceso prop : procesosId){
                    todosProcesos.remove(prop); //Eliminamos el actual de la lista
                    tareaActual.eliminarProceso(prop); //Eliminamos el actual de la tarea
                }
                moverSinBorrar(todosProcesos);
                puertasOrPasadas.removeLast();


            }else{
                this.cola = -1;
            }

        }
        //Ahora nos toca programar el siguiente caso el de esperar a que se resuelva

        //Aqui toca el calcular la probabilidad de casa salida
        //crear y añadir el hostioral
        //Mandar a las nuevas salidas
        //comporbar cuando acaba
    }
    private void proceseTarea(List<Proceso> todosProceso){
        //Movemos la tarea
        mover(todosProceso);


        tiempoDeRealizacion = tareaActual.getTiempoTarda();
    }
    private void tareaFinal(List<Proceso> todosProceso){
        todosProceso.remove(this);
        tareaActual.eliminarProceso(this);
    }

    private void mover(List<Proceso> todosProceso){
        todosProceso.remove(this); //Eliminamos el actual de la lista
        tareaActual.eliminarProceso(this); //Eliminamos el actual de la tarea
        //Con esto esta tarea esta eliminada
        Proceso procesoAux;
        for(Tarea i : tareaActual.getSigienteTarea()){
            //Creamos los nuevos procesos
            procesoAux = new Proceso(
                    i,
                    i.asignarCola(),
                    i.getTiempoTarda(),
                    this.id,
                    puertasOrPasadas

            );
            //Guardamos su tarea anterior
            procesoAux.tareaAnterior = tareaActual;
            i.anadirProceso(procesoAux);

            //Lo metemos en la lista
            todosProceso.add(procesoAux);


        }
    }

    private void moverSinBorrar(List<Proceso> todosProceso){
        //Con esto esta tarea esta eliminada
        Proceso procesoAux;
        for(Tarea i : tareaActual.getSigienteTarea()){
            //Creamos los nuevos procesos
            procesoAux = new Proceso(
                    i,
                    i.asignarCola(),
                    i.getTiempoTarda(),
                    this.id,
                    puertasOrPasadas

            );
            //Guardamos su tarea anterior
            procesoAux.tareaAnterior = tareaActual;
            i.anadirProceso(procesoAux);

            //Lo metemos en la lista
            todosProceso.add(procesoAux);


        }
    }


    private void moverUno(List<Proceso> todosProceso , int posi){
        todosProceso.remove(this); //Eliminamos el actual de la lista
        tareaActual.eliminarProceso(this); //Eliminamos el actual de la tarea
        //Con esto esta tarea esta eliminada
        Proceso procesoAux;
        Tarea i = tareaActual.getSigienteTarea().get(posi);
        //Creamos los nuevos procesos
        procesoAux = new Proceso(
                i,
                i.asignarCola(),
                i.getTiempoTarda(),
                this.id,
                puertasOrPasadas

        );
        //Guardamos su tarea anterior
        procesoAux.tareaAnterior = tareaActual;
        i.anadirProceso(procesoAux);

        //Lo metemos en la lista
        todosProceso.add(procesoAux);


    }
    private void moverUnoSinEliminarOr(List<Proceso> todosProceso , Tarea i , List<HistorialPuertaOr> lsOr){
        //Con esto esta tarea esta eliminada
        Proceso procesoAux;
        //Creamos los nuevos procesos
        procesoAux = new Proceso(
                i,
                i.asignarCola(),
                i.getTiempoTarda(),
                this.id,
                lsOr

        );
        //Guardamos su tarea anterior
        procesoAux.tareaAnterior = tareaActual;
        i.anadirProceso(procesoAux);

        //Lo metemos en la lista
        todosProceso.add(procesoAux);


    }


}
