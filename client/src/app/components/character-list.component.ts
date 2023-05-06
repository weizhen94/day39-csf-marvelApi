import { Component, OnInit } from '@angular/core';
import { MarvelCharacter } from '../models/marvel-character';
import { MarvelService } from '../services/marvel.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-character-list',
  templateUrl: './character-list.component.html',
  styleUrls: ['./character-list.component.css']
})
export class CharacterListComponent implements OnInit {
  characters: MarvelCharacter[] = [];
  nameStartsWith: string = '';

  constructor(private marvelService: MarvelService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.nameStartsWith = params['nameStartsWith'] || '';
      this.getCharacters();
    });
  }

  getCharacters(): void {
    this.marvelService.searchCharacters(this.nameStartsWith).subscribe((characters) => {
      this.characters = characters;
    });
  }

  goHome(): void {
    this.router.navigate(['']);
  }
}
