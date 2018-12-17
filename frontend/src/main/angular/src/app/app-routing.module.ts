import { NgModule } from '@angular/core';
import { RouterModule, Routes} from '@angular/router';

import { AuthGuard } from './auth/guards/auth.guard';

import { GuestLayoutComponent } from './layout/guest/guest-layout/guest-layout.component';
import { AuthorisedLayoutComponent } from './layout/authorised/authorised-layout/authorised-layout.component';
import { LandingPageComponent } from './pages/landing-page/landing-page.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { AboutComponent } from './pages/about/about.component';
import { ProjectDetailsComponent } from './projects/pages/project-details/project-details.component';
import { ProjectFormComponent } from './projects/pages/project-form/project-form.component';
import { ProjectsListComponent } from './projects/pages/projects-list/projects-list.component';
import { TaskFormComponent } from './tasks/pages/task-form/task-form.component';
import { TaskDetailsComponent } from './tasks/pages/task-details/task-details.component';
import { LoginComponent } from 'src/app/auth/components/login/login.component';

const routes: Routes = [
  {
    path: '',
    component: GuestLayoutComponent,
    children: [
      { path: '', component: LandingPageComponent, pathMatch: 'full'},
      { path: 'about', component: AboutComponent },
      { path: 'login', component: LoginComponent },
    ]
  },
  {
    path: '',
    component: AuthorisedLayoutComponent,
    canActivate: [AuthGuard],
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'projects', component: ProjectsListComponent },
      { path: 'new-project', component: ProjectFormComponent },
      { path: 'project-details/:id', component: ProjectDetailsComponent },
      { path: 'edit-project/:id', component: ProjectFormComponent },
      { path: 'projects/:project-id/new-task', component: TaskFormComponent },
      { path: 'task-details/:id', component: TaskDetailsComponent },
      { path: 'edit-task/:id', component: TaskFormComponent },
    ]
  },
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule { }
