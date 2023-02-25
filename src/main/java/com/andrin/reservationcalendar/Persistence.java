package com.andrin.reservationcalendar;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

public class Persistence {
    private Gson gson;

    public Persistence() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void saveReservations(ArrayList<Reservation> reservations) {
        try{
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(reservations);

            try (FileWriter writer = new FileWriter("C:\\ReservationSaves\\reservations.json")) {
                writer.write(json);
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public ArrayList<Reservation> loadReservations() {
        try{
            Gson gson = new Gson();

            String json = new String(Files.readAllBytes(Paths.get("C:\\ReservationSaves\\reservations.json")));

            return gson.fromJson(json, new TypeToken<ArrayList<Reservation>>(){}.getType());
        } catch (IOException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
