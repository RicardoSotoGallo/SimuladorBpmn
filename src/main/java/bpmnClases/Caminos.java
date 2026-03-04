package bpmnClases;

import java.util.Objects;

public class Caminos {
    String nombreId;
    String objetoInicio;
    String objetoFinal;

    public Caminos(String nombreId , String objetoInicio , String objetoFinal) {
        this.nombreId = nombreId;
        this.objetoInicio = objetoInicio;
        this.objetoFinal = objetoFinal;
    }

    public String getNombreId() {
        return nombreId;
    }

    public String getObjetoInicio() {
        return objetoInicio;
    }

    public String getObjetoFinal() {
        return objetoFinal;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Caminos caminos = (Caminos) o;
        return Objects.equals(nombreId, caminos.nombreId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nombreId);
    }

    @Override
    public String toString() {
        return "Caminos{" +
                "nombreId='" + nombreId + '\'' +
                ", objetoInicio=" + objetoInicio +
                ", objetoFinal=" + objetoFinal +
                '}';
    }
}
