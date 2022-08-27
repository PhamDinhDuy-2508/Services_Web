package com.Search_Thesis.Search_Thesis.Event;

import com.Search_Thesis.Search_Thesis.Model.Create_category;
import org.springframework.context.ApplicationEvent;
public class Create_Category_Event extends ApplicationEvent {

  private  Create_category create_category ;
    public Create_Category_Event(Object source , Create_category create_category) {
        super(source);
        this.create_category  =  create_category ;
    }

    public Create_category getCreate_category() {
        return create_category;
    }
}
