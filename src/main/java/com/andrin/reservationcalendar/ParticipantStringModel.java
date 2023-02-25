package com.andrin.reservationcalendar;

import java.io.Serializable;

public class ParticipantStringModel implements Serializable {
    private String participantString;

    public ParticipantStringModel(){

    }
    public String getParticipantString() {
        return participantString;
    }

    public void setParticipantString(String participantString) {
        this.participantString = participantString;
    }
}
