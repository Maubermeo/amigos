package com.unitec.amigos;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ControladorUsuario {
    //Aquí va un método que representa cada uno de los estados que vamos a transferir
    //es decir, va un GET, POST, PUT y DELETE. como mínimo.

    //Aquí viene ya el uso de la inversión de control
    @Autowired RepositorioUsuario repoUsuario;


    //Implementamos el código para guardar un usuario MongoDB
    @PostMapping("/usuario")
    public Estatus guardar(@RequestBody String json)throws Exception{
        //Primero leemos y convertimos el objeto a json a objeto Java
        ObjectMapper mapper=new ObjectMapper();
        Usuario u=mapper.readValue(json,Usuario.class);
        //Este usuario, ya en formato json lo guardamos en mongoDB
        repoUsuario.save(u);
        //Creamos un objeto de tipo status y este objeto lo retornamos al cliente (android o postman)
        Estatus estatus=new Estatus();
        estatus.setSuccess(true);
        estatus.setMensaje("¡TU USUARIO SE GUARDÓ CON ÉXITO!");
        return estatus;

    }

    @GetMapping("/usuario/{id}")
    public Usuario obtenerPorId(@PathVariable String id){
        //Leemos un usuario con el método FindById pasándole como argumento el id (email) que queremos
        //apoyándonos del repo usuario
        Usuario u= repoUsuario.findById(id).get();
        return u;
    }

    @GetMapping("/usuario")
    public List<Usuario> buscarTodos(){
        return repoUsuario.findAll();
    }

    //Metodo para actualizar
    @PutMapping("/usuario")
    public Estatus actualizar(@RequestBody String json)throws Exception{
        //Primero debemos verificar que exista, por lo tanto primero buscamos
        ObjectMapper mapper= new ObjectMapper();
        Usuario u=mapper.readValue(json, Usuario.class);
        Estatus e=new Estatus();
        if(repoUsuario.findById(u.getEmail()).isPresent()){
            //Lo volvemos a guardar
            repoUsuario.save(u);
            e.setMensaje("Usuario actualizó con éxito");
            e.setSuccess(true);
        }else{
            e.setMensaje("Este usuario no existe, no se actualizará");
            e.setSuccess(false);
        }
        return e;
    }

    @DeleteMapping("/usuario({id}")
    public Estatus borrar(@PathVariable String id){
        Estatus estatus= new Estatus();
        if(repoUsuario.findById(id).isPresent()){
            repoUsuario.deleteById(id);
            estatus.setSuccess(true);
            estatus.setMensaje("Usuario borrado con éxito");
        }else{
            estatus.setSuccess(false);
            estatus.setMensaje("Ese usuario no existe");
        }
        return estatus;
    }
    
}
