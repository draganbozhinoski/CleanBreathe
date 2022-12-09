package com.example.cleanbreathejava.controller;

import com.example.cleanbreathejava.model.CurrentView;
import com.example.cleanbreathejava.model.Pm;
import com.example.cleanbreathejava.repository.DataRepository;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class HomeController {
    private final DataRepository dataRepository;

    public HomeController(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }
    @GetMapping("/findDailyAverage")
    public CurrentView findDailyAveragePm10() {
        List<Pm> pmsList = dataRepository.findAll().stream().filter(pm -> Integer.parseInt(pm.getLocalDateTime().split("-")[0]) == LocalDateTime.now().getDayOfMonth() && pm.getType().equals("pm10")).toList();
        List<Pm> pmsList25 = dataRepository.findAll().stream().filter(pm -> Integer.parseInt(pm.getLocalDateTime().split("-")[0]) == LocalDateTime.now().getDayOfMonth() && pm.getType().equals("pm25")).toList();
        List<Pm> pmsListNoise = dataRepository.findAll().stream().filter(pm -> Integer.parseInt(pm.getLocalDateTime().split("-")[0]) == LocalDateTime.now().getDayOfMonth() && pm.getType().equals("noise")).toList();
        double sum10 = pmsList.stream().mapToDouble(Pm::getValue).sum();
        double sum25 = pmsList25.stream().mapToDouble(Pm::getValue).sum();
        double sumNoise = pmsListNoise.stream().mapToDouble(Pm::getValue).sum();
        DecimalFormat df = new DecimalFormat("0.00");
        sum10 = Double.parseDouble(df.format(sum10));
        sum25 = Double.parseDouble(df.format(sum25));
        sumNoise = Double.parseDouble(df.format(sumNoise));
        if(pmsList.size() == 0 || pmsList25.size() == 0 || pmsListNoise.size() == 0)
            return new CurrentView(-1D,-1D,-1D,"undefined");
        return new CurrentView(sum10/pmsList.size(),sum25/pmsList25.size(),sumNoise/pmsListNoise.size(),pmsListNoise.get(0).getLocalDateTime());
    }
    @GetMapping("/pm10")
    public List<Pm> findAllPm10s() {
        List<Pm> pmsList = new java.util.ArrayList<>(dataRepository.findAll().stream().filter(pm -> pm.getType().equals("pm10")).toList());
        Collections.reverse(pmsList);
        return pmsList;
    }
    @GetMapping("/pm25")
    public List<Pm> findAllPm25s() {
        List<Pm> pmsList = new java.util.ArrayList<>(dataRepository.findAll().stream().filter(pm -> pm.getType().equals("pm25")).toList());
        Collections.reverse(pmsList);
        return pmsList;
    }
    @GetMapping("/noises")
    public List<Pm> findAllNoises() {
        List<Pm> pmsList = new java.util.ArrayList<>(dataRepository.findAll().stream().filter(pm -> pm.getType().equals("noise")).toList());
        Collections.reverse(pmsList);
        return pmsList;
    }
    @GetMapping("/currentPms")
    public CurrentView getCurrentPms() {
        List<Pm> pm10s = dataRepository.findAll().stream().filter(pm -> pm.getType().equals("pm10")).toList();
        List<Pm> pm25s = dataRepository.findAll().stream().filter(pm -> pm.getType().equals("pm25")).toList();
        List<Pm> noises = dataRepository.findAll().stream().filter(pm -> pm.getType().equals("noise")).toList();
        if(pm10s.size() >= 1 && pm25s.size() >= 1 && noises.size() >= 1) {
            Double pm10 = pm10s.get(pm10s.size()-1).getValue();
            Double pm25 = pm25s.get(pm25s.size()-1).getValue();
            Double noise = noises.get(noises.size()-1).getValue();
            return new CurrentView(pm10,pm25,noise,pm10s.get(0).getLocalDateTime());
        }
        return new CurrentView(-1D,-1D,-1D,"undefined");
    }
    @PostMapping("/save")
    public String save(@RequestParam String pm10,
                     @RequestParam String pm25,
                       @RequestParam String noise) {
        System.out.println("Saved pm10: " + pm10 + " pm25: "+pm25 + " noise: " + noise);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formatted = now.format(formatter);
        dataRepository.save(new Pm("pm10",Double.parseDouble(pm10), formatted));
        dataRepository.save(new Pm("pm25",Double.parseDouble(pm25), formatted));
        dataRepository.save(new Pm("noise",Double.parseDouble(noise), formatted));
        return "Success";
    }
}
