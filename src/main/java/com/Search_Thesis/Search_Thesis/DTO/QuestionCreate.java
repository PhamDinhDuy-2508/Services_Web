package com.Search_Thesis.Search_Thesis.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Data
public class QuestionCreate {
    @NotBlank
    String Title;
    @NotBlank
    String Content;
    @NotBlank
    String Category;

    String name_display;
    String email_send;

    @Override
    public String toString() {
        return "QuestionCreate{" +
                "Title='" + Title + '\'' +
                ", Content='" + Content + '\'' +
                ", Category='" + Category + '\'' +
                ", name_display='" + name_display + '\'' +
                ", email_send='" + email_send + '\'' +
                '}';
    }
}
