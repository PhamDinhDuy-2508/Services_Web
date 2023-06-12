package com.Search_Thesis.Search_Thesis.Event.Event;

import com.Search_Thesis.Search_Thesis.DTO.Create_category;
import org.springframework.context.ApplicationEvent;
public class CreateCategoryEvent extends ApplicationEvent {

  private  Create_category create_category ;
    public CreateCategoryEvent(Object source , Create_category create_category) {
        super(source);
        this.create_category  =  create_category ;
    }

    public Create_category getCreate_category() {
        return create_category;
    }
}
