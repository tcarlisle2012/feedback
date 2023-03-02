import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeliveryFeedback } from '../delivery-feedback.model';
import { DeliveryFeedbackService } from '../service/delivery-feedback.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './delivery-feedback-delete-dialog.component.html',
})
export class DeliveryFeedbackDeleteDialogComponent {
  deliveryFeedback?: IDeliveryFeedback;

  constructor(protected deliveryFeedbackService: DeliveryFeedbackService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deliveryFeedbackService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
