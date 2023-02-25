package com.andrin.reservationcalendar;


import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

@SessionScoped
@ManagedBean
public class ReservationBean {
    private ReservationModel reservationModel;
    private RoomModel roomModel;
    private ReservationKey reservationKey;
    private Validator dateValidator;




    public Validator getDateValidator() {
        return dateValidator;
    }

    public void setDateValidator(Validator dateValidator) {
        this.dateValidator = dateValidator;
    }

    public ReservationBean(){
        reservationModel = new ReservationModel();
        roomModel = new RoomModel();
        reservationKey = new ReservationKey();
        dateValidator = (facesContext, uiComponent, o) -> checkDateValidity(facesContext, uiComponent, o);
    }


    public void checkDateValidity(FacesContext context, UIComponent component, Object value){
        String dateStr = (String) value;
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date = format.parse(dateStr);
            if(date.compareTo(new Date()) < 0){
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid date", "Please enter a date in the future"));
            }
        } catch (ParseException e) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid date format", "Please enter a valid date in the format dd.MM.yyyy"));
        }
    }

    public boolean checkRoomAvailability(byte roomNr, String date, LocalTime from, LocalTime to){
        ArrayList<Byte> numbers = new ArrayList<>();

        for(Reservation reservation : reservationModel.getReservations()){
            numbers.add(reservation.getRoom().getRoomNumber());
        }

        //Check for all numbers
        if(arrayContains(numbers, roomNr)){
            //Check for the date
            ArrayList<String> allDates = new ArrayList<>();
            for (Reservation reservation : reservationModel.getReservations()){
                allDates.add(reservation.getDateAsString());
            }

            if(allDates.contains(date)){
                //Finally, check time
                ArrayList<LocalTime> startTimes = new ArrayList<>();
                ArrayList<LocalTime> endTimes = new ArrayList<>();


                for(Reservation reservation : reservationModel.getReservations()){
                    if(reservation.getDateAsString().equals(date)){
                        startTimes.add(LocalTime.parse(reservation.getFrom()));
                        endTimes.add(LocalTime.parse(reservation.getTo()));
                    }
                }

                //Check in startTimes and endTimes
                for(int i = 0; i < startTimes.size(); i++){
                    LocalTime startTime = startTimes.get(i);
                    LocalTime endTime = endTimes.get(i);

                    if(from.isBefore(startTime) && to.isBefore(startTime) || from.isAfter(endTime) && to.isAfter(endTime)){
                        //Time is valid, continue the loop
                    } else{
                        return false;
                    }
                }

            }
        }
        return true;
    }

    public boolean arrayContains(ArrayList<Byte> array, byte number){
        for(byte num : array){
            if(number == num){
                return true;
            }
        }
        return false;
    }

    public boolean checkTimeValidity(String initialTime, String finalTime){
        LocalTime initialLocalTime = LocalTime.parse(initialTime);
        LocalTime finalLocalTime = LocalTime.parse(finalTime);

        if(initialLocalTime.isAfter(finalLocalTime)){
            return false;
        }

        return true;
    }
    public String addReservation(){

        byte roomNumber = reservationModel.getCurrentReservation().getRoom().getRoomNumber();
        String dateAsString = reservationModel.getCurrentReservation().getDateAsString();
        String from = reservationModel.getCurrentReservation().getFrom();
        String to = reservationModel.getCurrentReservation().getTo();

        if (!checkTimeValidity(from, to)) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid time range", null));
            return null;
        }

        try{
            if(reservationModel.getCurrentReservation().getId() == 0){
                if(!checkRoomAvailability(roomNumber, dateAsString, LocalTime.parse(from), LocalTime.parse(to))){
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Room Unavailable", null));
                    return null;
                }
                if(reservationModel.getReservations().size() > 0){
                    reservationModel.getCurrentReservation().setId(reservationModel.getReservations().get(reservationModel.getReservations().size() - 1).getId() + 1);
                    reservationModel.add();
                } else{
                    reservationModel.getCurrentReservation().setId(1);
                    reservationModel.add();
                }

            } else{
                Reservation reservation = reservationModel.findReservationByPrivateKey(reservationKey.getPrivateKey());
                int index = reservationModel.getReservations().indexOf(reservation);
                ArrayList<Reservation> temp;
                temp = reservationModel.getReservations();
                temp.set(index, reservationModel.getCurrentReservation());
                reservationModel.setReservations(temp);
                new Persistence().saveReservations(reservationModel.getReservations());
            }
        } catch (Exception ex){
            try{
                FileWriter writer = new FileWriter(new File("exception.txt"));
                writer.write(ex.getMessage());
                writer.close();
            } catch (IOException iox){
                iox.printStackTrace();
            }
        }

        return "/display.xhtml?faces-redirect=true";

    }

    public String createAndClearReservation(){
        reservationKey = new ReservationKey();
        reservationModel.setCurrentReservation(new Reservation());
        reservationModel.setReadOnly(false);
        return "/reservation.xhtml";
    }
    public RoomModel getRoomModel() {
        return roomModel;
    }

    public void setRoomModel(RoomModel roomModel) {
        this.roomModel = roomModel;
    }


    public ReservationModel getReservationModel() {
        return reservationModel;
    }

    public void setReservationModel(ReservationModel reservationModel) {
        this.reservationModel = reservationModel;
    }

    public String setReservationByPrivateKey(){
        Reservation reservation = reservationModel.findReservationByPrivateKey(reservationKey.getPrivateKey());
        if(reservation.getRemark().equals(reservationModel.EXCEPTION_HANDLER)){
            return "/index.xhtml";
        }
        reservationModel.setCurrentReservation(reservation);
        reservationModel.setReadOnly(false);
        return "/reservation.xhtml";
    }

    public String setReservationByPublicKey(){
        Reservation reservation = reservationModel.findReservationByPublicKey(reservationKey.getPublicKey());
        if(reservation.getRemark().equals(reservationModel.EXCEPTION_HANDLER)){
            return "/index.xhtml";
        }
        reservationModel.setCurrentReservation(reservation);
        reservationModel.setReadOnly(true);
        return "/reservation.xhtml";
    }

    public ReservationKey getReservationKey() {
        return reservationKey;
    }

    public void setReservationKey(ReservationKey reservationKey) {
        this.reservationKey = reservationKey;
    }
}
