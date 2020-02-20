import { Component } from '@angular/core';
import { IModalDialog, IModalDialogOptions, IModalDialogButton } from '@preeco-privacy/ngx-modal-dialog';
import { ComponentRef } from '@angular/core';

@Component({
  selector: 'app-confirmation-modal',
  templateUrl: './confirmation-modal.component.html',
  styleUrls: ['./confirmation-modal.component.scss']
})
export class ConfirmationModalComponent implements IModalDialog  {
  actionButtons: IModalDialogButton[];
  confirmAction: Function;

  constructor() {
    this.actionButtons = [
        { text: 'Dismiss', buttonClass: "btn btn-danger ",
        onAction: () => true},
        { text: 'Confirm', buttonClass: "btn btn-success", onAction: () => this.confirmAction() },
    ];
  }

  dialogInit(reference: ComponentRef<IModalDialog>, options: Partial<IModalDialogOptions<any>>) {
    this.confirmAction = options.data;
    options.title = 'Confirmation dialog';
    options.settings= {
      bodyClass: 'text-left pl-4',
    }
  }

}
