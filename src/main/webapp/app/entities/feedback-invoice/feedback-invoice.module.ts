import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FeedbackInvoiceComponent } from './list/feedback-invoice.component';
import { FeedbackInvoiceDetailComponent } from './detail/feedback-invoice-detail.component';
import { FeedbackInvoiceUpdateComponent } from './update/feedback-invoice-update.component';
import { FeedbackInvoiceDeleteDialogComponent } from './delete/feedback-invoice-delete-dialog.component';
import { FeedbackInvoiceRoutingModule } from './route/feedback-invoice-routing.module';

@NgModule({
  imports: [SharedModule, FeedbackInvoiceRoutingModule],
  declarations: [
    FeedbackInvoiceComponent,
    FeedbackInvoiceDetailComponent,
    FeedbackInvoiceUpdateComponent,
    FeedbackInvoiceDeleteDialogComponent,
  ],
})
export class FeedbackInvoiceModule {}
