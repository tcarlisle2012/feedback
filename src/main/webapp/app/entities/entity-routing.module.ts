import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'feedback-response',
        data: { pageTitle: 'FeedbackResponses' },
        loadChildren: () => import('./feedback-response/feedback-response.module').then(m => m.FeedbackResponseModule),
      },
      {
        path: 'delivery-feedback',
        data: { pageTitle: 'DeliveryFeedbacks' },
        loadChildren: () => import('./delivery-feedback/delivery-feedback.module').then(m => m.DeliveryFeedbackModule),
      },
      {
        path: 'feedback-invoice',
        data: { pageTitle: 'FeedbackInvoices' },
        loadChildren: () => import('./feedback-invoice/feedback-invoice.module').then(m => m.FeedbackInvoiceModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
