package bpmnClases;

import tiempos.Horas;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Tarea {
    private String tipo;
    private String nombre;
    private String nombreId;
    private List<Tarea> sigienteTarea;
    private List<Tarea> anteriorTarea;
    private Horas tiempoTarda;
    private List<Proceso> procesosDentro;
    private List<Float> probabilidad;

    public Tarea(String tipo, String nombre, String nombreId) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.nombreId = nombreId;
        this.sigienteTarea = new ArrayList<>();
        this.anteriorTarea = new ArrayList<>();
        procesosDentro = new ArrayList<>();
        probabilidad = new ArrayList<>();
    }

    public List<Float> getProbabilidad() {
        return probabilidad;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNombreId() {
        return nombreId;
    }

    public List<Tarea> getsigienteProceso() {
        return sigienteTarea;
    }

    public void anadirProceso(Proceso p){
        procesosDentro.add(p);
    }
    public void eliminarProceso(Proceso p){
        procesosDentro.remove(p);
    }

    public List<Proceso> getProcesosDentro() {
        return procesosDentro;
    }

    public void anadirSigienteProceso(Tarea p){
        this.sigienteTarea.add(p);
    }
    public void anadirAnteriorProceso(Tarea p){
        this.anteriorTarea.add(p);
    }

    public List<Tarea> getAnteriorTarea() {
        return anteriorTarea;
    }

    public void establecerCosteTiempo(Horas h){
        this.tiempoTarda = h;
    }

    public Horas getTiempoTarda() {
        return tiempoTarda;
    }

    public List<Tarea> getSigienteTarea() {
        return sigienteTarea;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tarea puertas = (Tarea) o;
        return Objects.equals(nombreId, puertas.nombreId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nombreId);
    }

    @Override
    public String toString() {
        return "Tarea{" +
                "tipo='" + tipo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", nombreId='" + nombreId + '\'' +
                ", sigienteProceso=" + sigienteTarea.stream().map(c -> c.nombreId).toList() +
                ", anteriorProceso=" + anteriorTarea.stream().map(c -> c.nombreId).toList() +
                ", Coste tiempo="+tiempoTarda+ '\'' +
                '}';
    }
    public Integer asignarCola(){
        return 0;
    }
}
