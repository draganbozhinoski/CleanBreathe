import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Pm } from './pm';
import {HttpClient} from '@angular/common/http'
import { CurrentPms } from './currentPms';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  
  constructor(private http:HttpClient) { }

  findAllPm10s():Observable<Pm[]> {
    return this.http.get<Pm[]>(`/api/pm10`);
  }
  findAllPm25s():Observable<Pm[]> {
    return this.http.get<Pm[]>(`/api/pm25`);
  }
  findAllNoises():Observable<Pm[]> {
    return this.http.get<Pm[]>(`/api/noises`);
  }
  findCurrent():Observable<CurrentPms> {
    return this.http.get<CurrentPms>(`/api/currentPms`)
  }
  findDaily():Observable<CurrentPms> {
    return this.http.get<CurrentPms>(`/api/findDailyAverage`);
  }

}
