package bpmnClases;

import java.util.List;

public class HistorialPuertaOr {
    Tarea puertaOr;
    Tarea tareaElegida;
    List<Tarea> listaDeTareasPost;

    public HistorialPuertaOr(Tarea puertaOr , List<Tarea> listaTareas) {
        this.puertaOr = puertaOr;
        this.listaDeTareasPost = listaTareas;
    }

    public HistorialPuertaOr elegir(Tarea escogida){
        HistorialPuertaOr res = new HistorialPuertaOr(puertaOr,listaDeTareasPost);
        res.tareaElegida = escogida;
        return res;
    }
}
