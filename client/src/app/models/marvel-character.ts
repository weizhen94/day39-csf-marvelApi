import { Thumbnail } from "./thumbnail";
import { Comment } from "./comment"; // Add this line

export interface MarvelCharacter {
  id: number;
  name: string;
  description: string;
  modified: string;
  thumbnail: Thumbnail;
  resourceURI: string;
  comments?: Comment[];
}
