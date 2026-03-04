package bpmnFichero;


import bpmnClases.Caminos;
import bpmnClases.Tarea;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class leerFicheroBpmn {
    public Tarea eventoInicio;
    public List<Tarea> listaDeObjetos = new ArrayList<>();
    public List<Caminos> listaCamino = new ArrayList<>();
    public void leer(String fichero){
        try {
            File archivo = new File("src/fichero/"+fichero+".bpmn");

            // Crear el DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Parsear el XML
            Document document = builder.parse(archivo);
            //document.getDocumentElement().normalize();

            recorrerNodo(document , false);
            crearAristas();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void recorrerNodo(Node actual , boolean enProceso){
        String titulo = actual.getNodeName();

        if(enProceso){
            if(!actual.getNodeName().equals("#text") &&
                    !actual.getNodeName().equals("outgoing") &&
                    !actual.getNodeName().equals("incoming")) {
                //System.out.println(actual.getNodeName());
                if(
                        actual.getNodeName().equals("startEvent")||
                                actual.getNodeName().equals("task")||
                                actual.getNodeName().equals("sequenceFlow")||
                                actual.getNodeName().equals("endEvent")||
                                actual.getNodeName().equals("exclusiveGateway")||
                                actual.getNodeName().equals("parallelGateway") ||
                                actual.getNodeName().equals("inclusiveGateway")

                ){
                    recopilarEventos(actual);
                }

                NodeList nodoHijos = actual.getChildNodes();
                if (nodoHijos.getLength() > 0) {
                    for (int i = 0; i < nodoHijos.getLength(); i++) {
                        recorrerNodo(nodoHijos.item(i), enProceso);
                    }
                }
            }
        }
        else{
            NodeList nodoHijos = actual.getChildNodes();
            if(nodoHijos.getLength() > 0){
                for(int i = 0 ; i < nodoHijos.getLength() ; i++){
                    if(nodoHijos.item(i).getNodeName().equals("#document") ||
                            nodoHijos.item(i).getNodeName().equals("definitions") ){
                        recorrerNodo( nodoHijos.item(i) , false );
                    }

                    if(nodoHijos.item(i).getNodeName().equals("process")){
                        recorrerNodo( nodoHijos.item(i) , true);
                    }

                }
            }
        }





    }

    private void recopilarEventos(Node actual){

        //Aqui va a estar el evento de incio Solo puede existir 1
        if(actual.getNodeName().equals("startEvent")){
            Tarea nuevoEvento = new Tarea(
                    actual.getNodeName() ,
                    actual.getAttributes().getNamedItem("name").getTextContent() ,
                    actual.getAttributes().getNamedItem("id").getTextContent()
            );
            eventoInicio = nuevoEvento;
            listaDeObjetos.add(nuevoEvento);

            //Aqui van a estar los eventos
        }else if(
                actual.getNodeName().equals("endEvent")
        ){
             Tarea nuevoEvento = new Tarea(
                    actual.getNodeName() ,
                    "END" ,
                    actual.getAttributes().getNamedItem("id").getTextContent()
            );
            listaDeObjetos.add(nuevoEvento);

            //Aqui va a estar los eventos
        }else if(
            actual.getNodeName().equals("task")
        ){
            Tarea nuevoTarea = new Tarea(
                    actual.getNodeName() ,
                    actual.getAttributes().getNamedItem("name").getTextContent() ,
                    actual.getAttributes().getNamedItem("id").getTextContent()
            );
            listaDeObjetos.add(nuevoTarea);

            //Aqui va a estar los caminos
        }else if(
                actual.getNodeName().equals("sequenceFlow")
        ){
            Caminos nuevoCamino = new Caminos(
                    actual.getAttributes().getNamedItem("id").getTextContent(),
                    actual.getAttributes().getNamedItem("sourceRef").getTextContent(),
                    actual.getAttributes().getNamedItem("targetRef").getTextContent()
            );
            listaCamino.add(nuevoCamino);
        }else if(
                actual.getNodeName().equals("parallelGateway")
                //actual.getNodeName().equals("exclusiveGateway")
        ){
            Tarea nuevaPuerta = new Tarea(
                    actual.getNodeName() ,
                    actual.getAttributes().getNamedItem("name").getTextContent() ,
                    actual.getAttributes().getNamedItem("id").getTextContent()
            );
            listaDeObjetos.add(nuevaPuerta);
        }else if(
                actual.getNodeName().equals("exclusiveGateway")
            //actual.getNodeName().equals("exclusiveGateway")
        ){
            Tarea nuevaPuerta = new Tarea(
                    actual.getNodeName() ,
                    actual.getAttributes().getNamedItem("name").getTextContent() ,
                    actual.getAttributes().getNamedItem("id").getTextContent()
            );
            listaDeObjetos.add(nuevaPuerta);
        }else if(
                actual.getNodeName().equals("inclusiveGateway")
            //actual.getNodeName().equals("exclusiveGateway")
        ){
            Tarea nuevaPuerta = new Tarea(
                    actual.getNodeName() ,
                    actual.getAttributes().getNamedItem("name").getTextContent() ,
                    actual.getAttributes().getNamedItem("id").getTextContent()
            );
            listaDeObjetos.add(nuevaPuerta);
        }
    }

    private void crearAristas(){
        Tarea inicio = null;
        Tarea fini = null;
        String idInicio;
        String idFini;
        for( Caminos cami : listaCamino ){
            inicio = null;
            fini = null;
            idInicio = cami.getObjetoInicio();
            idFini = cami.getObjetoFinal();
            for( Tarea proces : listaDeObjetos ){
                if(proces.getNombreId().equals(idInicio)){
                    inicio = proces;
                }
                if(proces.getNombreId().equals(idFini)){
                    fini = proces;
                }
            }
            if( inicio != null && fini != null ){
                inicio.anadirSigienteProceso(fini);
                fini.anadirAnteriorProceso(inicio);
            }
        }

    }
}
