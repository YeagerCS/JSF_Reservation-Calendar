package com.andrin.reservationcalendar;

import java.util.ArrayList;

public class RoomModel {
    private ArrayList<Room> rooms;

    public RoomModel(){
        rooms = new ArrayList<>();
        rooms.add(new Room((byte) 101));
        rooms.add(new Room((byte) 102));
        rooms.add(new Room((byte) 103));
        rooms.add(new Room((byte) 104));
        rooms.add(new Room((byte) 105));
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }
}
