package com.andrin.reservationcalendar;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

public class Reservation {
    private String privateKey;
    private String publicKey;
    private ParticipantStringModel participants;
    private Room room;
    private String dateAsString;
    private String from;
    private String to;
    private String remark;
    private int id;

    public String getDateAsString() {
        return dateAsString;
    }

    public void setDateAsString(String dateAsString) {
        this.dateAsString = dateAsString;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String md5Hash(String string){
        String hash = "";
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(string.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            hash = sb.toString();
        } catch (NoSuchAlgorithmException ex){
            ex.printStackTrace();
        }
        return hash;
    }
    public String getKey() {
        Random random = new Random();
        String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        StringBuilder keySb = new StringBuilder();
        for(int i = 0; i < 5; i++){
            int randomIndex = random.nextInt(alphabet.length - 1);
            keySb.append(alphabet[randomIndex]);
        }
        String key = md5Hash(keySb.toString());
        key = key.substring(0, 10);
        return key;
    }

    public String createPublicKey(){
        return "PU-" + getKey();
    }

    public String createPrivateKey(){
        return "PR-" + getKey();
    }

    public Reservation(){
        room = new Room();
        participants = new ParticipantStringModel();
        this.privateKey = createPrivateKey();
        this.publicKey = createPublicKey();
    }

    public Reservation(String remark){
        this.remark = remark;
    }
    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }


    public ParticipantStringModel getParticipants() {
        return participants;
    }

    public void setParticipants(ParticipantStringModel participants) {
        this.participants = participants;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
