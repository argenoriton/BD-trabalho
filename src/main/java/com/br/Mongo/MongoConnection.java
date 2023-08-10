package com.br.Mongo;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.bson.Document;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
public class MongoConnection {

    public static void main(String[] args) {
        SpringApplication.run(MongoConnection.class, args);
    }

    String uri = "mongodb+srv://argenoriton:root@meubanco.pejwh2a.mongodb.net/?retryWrites=true&w=majority";
    MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(uri))
            .build();

    MongoClient mongoClient = MongoClients.create(settings);

    MongoDatabase database = mongoClient.getDatabase("MeuBanco");
    MongoCollection<Document> ford = database.getCollection("FORD");


    @GetMapping("/")
    public Document retornarPrimeiro() {
        return ford.find().first();
    }

    @PostMapping("/")
    public ResponseEntity criarDocumento(@RequestBody Car car){

        ford.insertOne(new Document("Modelo", car.getModel())
                .append("Ano", car.getYear())
                .append("Valor", car.getPrice()));

        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    @GetMapping("/lista")
    public ResponseEntity<List<Car>> retornarLista(){
        List<Car> documents = new ArrayList<>();

        FindIterable<Document> iterable = ford.find();
        for (Document doc : iterable) {
            Car car = new Car();
            car.setModel(doc.getString("Modelo"));
            car.setYear(doc.getString("Ano"));
            car.setPrice(doc.getString("Valor"));
            documents.add(car);
        }
              return new ResponseEntity<>(documents,HttpStatus.OK);
    }

}
