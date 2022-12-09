package com.example.cleanbreathejava.model;

import com.example.cleanbreathejava.model.Pm;
import lombok.Data;

@Data
public class CurrentView {
    Double currentPm10;
    Double currentPm25;
    Double currentNoise;
    String date;

    public CurrentView(Double currentPm10, Double currentPm25, Double currentNoise, String date) {
        this.currentPm10 = currentPm10;
        this.currentPm25 = currentPm25;
        this.currentNoise = currentNoise;
        this.date = date;
    }
}
