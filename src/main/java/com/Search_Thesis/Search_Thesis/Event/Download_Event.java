package com.Search_Thesis.Search_Thesis.Event;

import org.springframework.context.ApplicationEvent;

public class Download_Event extends ApplicationEvent {

    private  String ID ;
    private  String filename ;

    public Download_Event(Object source, String ID, String filename) {
        super(source);
        this.ID = ID;
        this.filename = filename;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
