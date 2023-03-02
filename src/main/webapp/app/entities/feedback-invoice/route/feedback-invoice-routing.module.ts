import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FeedbackInvoiceComponent } from '../list/feedback-invoice.component';
import { FeedbackInvoiceDetailComponent } from '../detail/feedback-invoice-detail.component';
import { FeedbackInvoiceUpdateComponent } from '../update/feedback-invoice-update.component';
import { FeedbackInvoiceRoutingResolveService } from './feedback-invoice-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const feedbackInvoiceRoute: Routes = [
  {
    path: '',
    component: FeedbackInvoiceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FeedbackInvoiceDetailComponent,
    resolve: {
      feedbackInvoice: FeedbackInvoiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FeedbackInvoiceUpdateComponent,
    resolve: {
      feedbackInvoice: FeedbackInvoiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FeedbackInvoiceUpdateComponent,
    resolve: {
      feedbackInvoice: FeedbackInvoiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(feedbackInvoiceRoute)],
  exports: [RouterModule],
})
export class FeedbackInvoiceRoutingModule {}
