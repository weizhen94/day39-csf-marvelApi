import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SearchComponent } from './components/search.component';
import { CharacterListComponent } from './components/character-list.component';
import { CharacterDetailsComponent } from './components/character-details.component';
import { CommentsComponent } from './components/comments.component';

const routes: Routes = [
  { path: '', component: SearchComponent },
  { path: 'characters', component: CharacterListComponent },
  { path: 'character/:id', component: CharacterDetailsComponent },
  { path: 'character/:id/comments', component: CommentsComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
