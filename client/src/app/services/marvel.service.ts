import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MarvelCharacter } from '../models/marvel-character';
import { Comment } from '../models/comment';


@Injectable({
  providedIn: 'root'
})
export class MarvelService {
  
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  searchCharacters(nameStartsWith: string): Observable<MarvelCharacter[]> {
    return this.http.get<MarvelCharacter[]>(`${this.baseUrl}/characters?nameStartsWith=${nameStartsWith}`);
  }

  getCharacter(id: number): Observable<MarvelCharacter> {
    return this.http.get<MarvelCharacter>(`${this.baseUrl}/character/${id}`);
  }

  addComment(characterId: number, comment: Comment): Observable<Comment> {
    return this.http.post<Comment>(`${this.baseUrl}/character/${characterId}/comments`, comment);
  }

  currentCharacterName: string = '';

  setCurrentCharacterName(name: string): void {
    this.currentCharacterName = name;
  }

}
