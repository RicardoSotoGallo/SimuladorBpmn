package tiempos;

import com.sun.source.tree.BreakTree;

import java.util.Objects;

public class Horas implements Comparable<Horas> {
    private Integer hora;
    private Integer minutos;
    private Integer segundos;

    public Horas(Integer segundosTotal) {
        int resto;
        this.hora = segundosTotal/3600;
        resto = segundosTotal%3600;
        this.minutos = resto/60;
        this.segundos = resto%60;
    }

    public Horas(Integer hora, Integer minutos, Integer segundos) {
        this.hora = hora;
        this.minutos = minutos;
        this.segundos = segundos;
    }

    public Horas sumaHoras(Horas suma){
        int Hs;
        int Ms;
        int Ss;
        int llevar;
        Ss = suma.getSegundos() + this.segundos;
        if(Ss >= 60){
            Ss = Ss%60;
            llevar = 1;
        }else{
            llevar = 0;
        }
        Ms = suma.getMinutos() + this.minutos + llevar;

        if(Ms >= 60){
            Ms = Ms%60;
            llevar = 1;
        }else {
            llevar = 0;
        }
        Hs = suma.getHora() + this.hora + llevar;
        return new Horas(Hs,Ms,Ss);
    }

    public Horas restaHoras(Horas suma){
        int Hs;
        int Ms;
        int Ss;
        int llevar;
        Ss = this.segundos -suma.getSegundos() ;
        if(Ss < 0){
            Ss = Ss + 60;
            llevar = 1;
        }else{
            llevar = 0;
        }
        Ms =   this.minutos - suma.getMinutos() - llevar;

        if(Ms < 0){
            Ms = Ms+60;
            llevar = 1;
        }else {
            llevar = 0;
        }
        Hs = this.hora - suma.getHora() - llevar;
        return new Horas(Hs,Ms,Ss);
    }

    public Integer getHora() {
        return hora;
    }

    public Integer getMinutos() {
        return minutos;
    }

    public Integer getSegundos() {
        return segundos;
    }


    @Override
    public String toString() {
        return "Horas{" +
                "hora=" + hora +
                ", minutos=" + minutos +
                ", segundos=" + segundos +
                '}';
    }

    @Override
    public int compareTo(Horas com){
        int res = 0;
        if(this.hora != com.getHora()){
            if(this.hora > com.getHora()){
                res = 1;
            }else {
                res = -1;
            }
        }else if(this.minutos != com.minutos){
            if(this.minutos > com.getMinutos()){
                res = 1;
            }else{
                res = -1;
            }
        } else if (this.segundos != com.segundos) {
            if(this.segundos > com.segundos){
                res = 1;
            }else{
                res = -1;
            }
        }
        return res;
    }

}
