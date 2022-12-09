import { Component, OnInit } from '@angular/core';
import { CurrentPms } from '../currentPms';
import { DataService } from '../data.service';
import { Pm } from '../pm';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  pm10s: Pm[] = []
  pm25s: Pm[] = []
  noises: Pm[] = []
  currentPms: CurrentPms|undefined = undefined;
  dailyPms:CurrentPms|undefined = undefined;
  constructor(private dataService:DataService) { }
  ngOnInit(): void {
    this.dataService.findAllPm10s().subscribe(data => this.pm10s=data);
    this.dataService.findAllPm25s().subscribe(data => this.pm25s=data);
    this.dataService.findAllNoises().subscribe(data => this.noises=data);
    this.dataService.findCurrent().subscribe(data => this.currentPms = data);
    this.dataService.findDaily().subscribe(data => this.dailyPms = data);
  }
}
