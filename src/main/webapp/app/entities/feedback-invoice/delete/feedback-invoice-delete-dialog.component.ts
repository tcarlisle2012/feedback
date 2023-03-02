import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFeedbackInvoice } from '../feedback-invoice.model';
import { FeedbackInvoiceService } from '../service/feedback-invoice.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './feedback-invoice-delete-dialog.component.html',
})
export class FeedbackInvoiceDeleteDialogComponent {
  feedbackInvoice?: IFeedbackInvoice;

  constructor(protected feedbackInvoiceService: FeedbackInvoiceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.feedbackInvoiceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
