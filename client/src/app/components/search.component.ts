import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MarvelService } from '../services/marvel.service';
import { MarvelCharacter } from '../models/marvel-character';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})

export class SearchComponent implements OnInit {
  nameStartsWith: string = '';

  constructor(private router: Router, private marvelService: MarvelService) {}

  ngOnInit(): void {}

  search(): void {
    this.marvelService.searchCharacters(this.nameStartsWith).subscribe((characters: MarvelCharacter[]) => {
      if (characters.length > 0) {
        this.router.navigate(['/characters'], { queryParams: { nameStartsWith: this.nameStartsWith } });
      } else {
        alert('No results found');
      }
    });
  }
}
