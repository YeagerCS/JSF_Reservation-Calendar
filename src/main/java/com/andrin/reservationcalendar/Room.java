package com.andrin.reservationcalendar;

import java.io.Serializable;
import java.util.Date;

public class Room implements Serializable {
    private byte roomNumber;


    public Room(byte roomNumber){
        this.roomNumber = roomNumber;
    }

    public Room(){

    }
    public byte getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(byte roomNumber) {
        this.roomNumber = roomNumber;
    }

}
