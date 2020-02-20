import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';

import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { JwtTokenInterceptor } from './auth/interceptors/jwt.token.interceptor';

import { AppComponent } from './app.component';
import { LandingPageComponent } from './pages/landing-page/landing-page.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { AboutComponent } from './pages/about/about.component';
import { AuthorisedLayoutComponent } from './layout/authorised/authorised-layout/authorised-layout.component';
import { AuthorisedSideNavTogglerComponent } from './layout/authorised/authorised-side-nav-toggler/authorised-side-nav-toggler.component';
import { AuthorisedTopNavComponent } from './layout/authorised/authorised-top-nav/authorised-top-nav.component';
import { GuestLayoutComponent } from './layout/guest/guest-layout/guest-layout.component';
import { AuthorisedSideNavComponent } from './layout/authorised/authorised-side-nav/authorised-side-nav.component';
import { GuestNavComponent } from './layout/guest/guest-nav/guest-nav.component';
import { GuestFooterComponent } from './layout/guest/guest-footer/guest-footer.component';
import { PageContentComponent } from './layout/page-content/page-content.component';
import { ProjectDashboardCardsComponent } from './projects/components/project-dashboard-cards/project-dashboard-cards.component';
import { ProjectDetailsComponent } from './projects/pages/project-details/project-details.component';
import { ProjectFormComponent } from './projects/pages/project-form/project-form.component';
import { ProjectsListComponent } from './projects/pages/projects-list/projects-list.component';
import { ProjectsTableComponent } from './projects/components/data-table/projects-table/projects-table.component';
import { ProjectsTableActionsComponent } from './projects/components/data-table/projects-table-actions/projects-table-actions.component';
import { ProjectsTableHeaderComponent } from './projects/components/data-table/projects-table-header/projects-table-header.component';
import { CustomPaginationComponent } from './pagination/components/custom-pagination/custom-pagination.component';
import { ConfirmationModalComponent } from './modals/confirmation-modal/confirmation-modal.component';
import { TaskFormComponent } from './tasks/pages/task-form/task-form.component';
import { TasksTableComponent } from './tasks/components/tasks-table/tasks-table.component';
import { TasksTableActionsComponent } from './tasks/components/tasks-table-actions/tasks-table-actions.component';
import { TasksTableHeaderComponent } from './tasks/components/tasks-table-header/tasks-table-header.component';
import { TaskDetailsComponent } from './tasks/pages/task-details/task-details.component';
import { LoginComponent } from './auth/components/login/login.component';
import { SortableHeadersComponent } from './sorting/components/sortable-headers/sortable-headers.component';
import { ModalDialogModule } from "@preeco-privacy/ngx-modal-dialog";

@NgModule({
  declarations: [
    AppComponent,
    LandingPageComponent,
    DashboardComponent,
    AboutComponent,
    AuthorisedLayoutComponent,
    AuthorisedSideNavTogglerComponent,
    AuthorisedTopNavComponent,
    GuestLayoutComponent,
    AuthorisedSideNavComponent,
    GuestNavComponent,
    GuestFooterComponent,
    PageContentComponent,
    ProjectDashboardCardsComponent,
    ProjectDetailsComponent,
    ProjectFormComponent,
    ProjectsListComponent,
    ProjectsTableHeaderComponent,
    ProjectsTableComponent,
    ProjectsTableActionsComponent,
    CustomPaginationComponent,
    ConfirmationModalComponent,
    TaskFormComponent,
    TasksTableComponent,
    TasksTableActionsComponent,
    TasksTableHeaderComponent,
    TaskDetailsComponent,
    LoginComponent,
    SortableHeadersComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ModalDialogModule.forRoot()
  ],
  schemas: [],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtTokenInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent],
  entryComponents: [ConfirmationModalComponent]
})
export class AppModule { }
