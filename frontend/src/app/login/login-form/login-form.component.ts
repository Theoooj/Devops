import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { UserServiceService as UserService } from '../../services/user/user-service.service';
import { Observable } from 'rxjs';
import { User } from '../../interfaces/user';

@Component({
  selector: 'app-login-form',
  imports: [CommonModule],
  templateUrl: './login-form.component.html',
  styleUrl: './login-form.component.css'
})
export class LoginFormComponent implements OnInit{
  mots: string= "ecriture de test"
  boolean: boolean= false;
  users : User[] = [];

  constructor(private userService: UserService){
  }

  ngOnInit(): void {
    this.userService.getAllUsers().subscribe((data: User[])=>{
      this.users = data
      console.log(this.users)
    })
  }
}
