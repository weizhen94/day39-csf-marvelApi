import { Component, Input, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MarvelService } from '../services/marvel.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Comment } from '../models/comment';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.css']
})
export class CommentsComponent implements OnInit{

  @Input() characterId: number = 0;

  commentForm!: FormGroup;

  characterName: string = '';
  
  constructor(private fb: FormBuilder, private marvelService: MarvelService, private snackBar: MatSnackBar, private route: ActivatedRoute, private router: Router) { }

  private createComment(): FormGroup { 
    return this.fb.group({
    characterId: this.characterId, 
    text: this.fb.control<string>('', [ Validators.required ]),
    timestamp: new Date().toISOString(),
    })}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.characterId = parseInt(params['id'], 10);
      this.characterName = this.marvelService.currentCharacterName; // Get the character name from the service
      this.commentForm = this.createComment(); 
    });
  }

  onSubmit(): void {
    const commentItem: Comment = this.commentForm.value;
  
    this.marvelService.addComment(this.characterId, commentItem).subscribe(
      (_) => {
        this.snackBar.open('Comment added!', 'Close', {
          duration: 3000,
        });
        this.commentForm.reset();
        this.router.navigate(['/character', this.characterId]); // Redirect to the character details page
      },
      (error) => {
        this.snackBar.open('Failed to submit comment.', 'Close', {
          duration: 3000,
        });
        console.error('Error:', error);
      }
    );
  }

  // Add this method to navigate back to the character details page
  cancel(): void {
  this.router.navigate(['/character', this.characterId]);
  }

}

