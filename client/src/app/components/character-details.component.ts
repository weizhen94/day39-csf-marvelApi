import { Component, OnDestroy, OnInit } from '@angular/core';
import { MarvelCharacter } from '../models/marvel-character';
import { MarvelService } from '../services/marvel.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-character-details',
  templateUrl: './character-details.component.html',
  styleUrls: ['./character-details.component.css']
})
export class CharacterDetailsComponent implements OnInit, OnDestroy {

  character: MarvelCharacter | null = null;
  characterId: number = 0;
  params$! : Subscription;
  marvelService$! : Subscription;

  constructor(private marvelService: MarvelService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    this.params$ = this.route.params.subscribe((params) => {
    console.log('Params:', params); // test the params passed in
    this.characterId = parseInt(params['id'], 10);
    console.log('Character ID:', this.characterId); // test the characterId
      this.getCharacter();
    });
  }

  getCharacter(): void {
    this.marvelService$ = this.marvelService.getCharacter(this.characterId).subscribe((character) => {
    this.character = character;
    this.marvelService.setCurrentCharacterName(character.name); // Store the character name
    });
  }

  goToComments(): void {
    this.router.navigate(['/character', this.characterId, 'comments']);
  }

  goHome(): void {
    this.router.navigate(['']);
  }

  ngOnDestroy(): void {
    this.params$.unsubscribe();    
    this.marvelService$.unsubscribe(); 
  }
}
