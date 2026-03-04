package tiempos;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Calendario {
    private LocalDateTime fechas;
    private List<LocalDate> diasDeVacaciones;
    private HashMap<String , List<Horas>> jornada;

    public Calendario(){
        jornada = new HashMap<>();
        jornada.put( "MONDAY" , new ArrayList<>());
        jornada.put( "TUESDAY" , new ArrayList<>());
        jornada.put( "WEDNESDAY" , new ArrayList<>());
        jornada.put( "THURSDAY" , new ArrayList<>());
        jornada.put( "FRIDAY" , new ArrayList<>());
        jornada.put( "SATURDAY" , new ArrayList<>());
        jornada.put( "SUNDAY" , new ArrayList<>());

        for( int i = 0 ; i < 5 ; i++){
            Horas entrada = new Horas(7 , 0 , 0);
            Horas salida = new Horas( 15 , 0 ,0 );
            ponerJornada( i , entrada , salida );
        }

    }
    public void ponerJornada(Integer dia , Horas entrada , Horas salida){
        if(dia < 7){
            switch (dia){
                case 0:
                    jornada.get("MONDAY").add(entrada);
                    jornada.get("MONDAY").add(salida);
                    break;
                case 1:
                    jornada.get("TUESDAY").add(entrada);
                    jornada.get("TUESDAY").add(salida);
                    break;
                case 2:
                    jornada.get("WEDNESDAY").add(entrada);
                    jornada.get("WEDNESDAY").add(salida);
                    break;
                case 3:
                    jornada.get("THURSDAY").add(entrada);
                    jornada.get("THURSDAY").add(salida);
                    break;
                case 4:
                    jornada.get("FRIDAY").add(entrada);
                    jornada.get("FRIDAY").add(salida);
                    break;
                case 5:
                    jornada.get("SATURDAY").add(entrada);
                    jornada.get("SATURDAY").add(salida);
                    break;
                case 6:
                    jornada.get("SUNDAY").add(entrada);
                    jornada.get("SUNDAY").add(salida);
                    break;
            }
        }
    }
    public void QuitarJornada(Integer dia , Horas entrada , Horas salida){
        if(dia < 7){
            switch (dia){
                case 0:
                    jornada.put("MONDAY", new ArrayList<>());
                    break;
                case 1:
                    jornada.put("TUESDAY",new ArrayList<>());
                    break;
                case 2:
                    jornada.put("WEDNESDAY",new ArrayList<>());
                    break;
                case 3:
                    jornada.put("THURSDAY",new ArrayList<>());
                    break;
                case 4:
                    jornada.put("FRIDAY",new ArrayList<>());
                    break;
                case 5:
                    jornada.put("SATURDAY",new ArrayList<>());
                    break;
                case 6:
                    jornada.put("SUNDAY",new ArrayList<>());
                    break;
            }
        }
    }
}
