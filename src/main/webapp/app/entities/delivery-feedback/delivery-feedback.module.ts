import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DeliveryFeedbackComponent } from './list/delivery-feedback.component';
import { DeliveryFeedbackDetailComponent } from './detail/delivery-feedback-detail.component';
import { DeliveryFeedbackUpdateComponent } from './update/delivery-feedback-update.component';
import { DeliveryFeedbackDeleteDialogComponent } from './delete/delivery-feedback-delete-dialog.component';
import { DeliveryFeedbackRoutingModule } from './route/delivery-feedback-routing.module';

@NgModule({
  imports: [SharedModule, DeliveryFeedbackRoutingModule],
  declarations: [
    DeliveryFeedbackComponent,
    DeliveryFeedbackDetailComponent,
    DeliveryFeedbackUpdateComponent,
    DeliveryFeedbackDeleteDialogComponent,
  ],
})
export class DeliveryFeedbackModule {}
