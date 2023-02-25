package com.andrin.reservationcalendar;

import java.io.File;
import java.util.ArrayList;

public class ReservationModel {
    private ArrayList<Reservation> reservations;
    private Reservation currentReservation;
    private boolean isReadOnly;
    public final String EXCEPTION_HANDLER = "NO_SUCH_PRIVATE_OR_PUBLIC_KEY_FOUND";



    public boolean isReadOnly() {
        return isReadOnly;
    }

    public void setReadOnly(boolean readOnly) {
        isReadOnly = readOnly;
    }

    public ReservationModel(){
        File dir = new File("C:\\ReservationSaves");
        if(!dir.exists()){
            dir.mkdir();
        }

        if(new File("C:\\ReservationSaves\\reservations.json").exists()){
            reservations = new Persistence().loadReservations();
        } else{
            reservations = new ArrayList<>();
        }

        currentReservation = new Reservation();
    }

    public Reservation getCurrentReservation() {
        return currentReservation;
    }

    public void add(){
        reservations.add(currentReservation);
        new Persistence().saveReservations(reservations);
        currentReservation = new Reservation();
    }

    public String setCurrent(){
        return currentReservation.getPublicKey();
    }

    public void setCurrentReservation(Reservation currentReservation) {
        this.currentReservation = currentReservation;
    }

    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    public Reservation findReservationByPrivateKey(String privateKey) {
        for (Reservation reservation : reservations) {
            if (reservation.getPrivateKey().equals(privateKey)) {
                return reservation;
            }
        }
        return new Reservation(EXCEPTION_HANDLER);
    }

    public Reservation findReservationByPublicKey(String publicKey){
        for(Reservation reservation : reservations){
            if(reservation.getPublicKey().equals(publicKey)){
                return reservation;
            }
        }
        return new Reservation(EXCEPTION_HANDLER);
    }



    public void setReservations(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
    }
}
