import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DeliveryFeedbackComponent } from '../list/delivery-feedback.component';
import { DeliveryFeedbackDetailComponent } from '../detail/delivery-feedback-detail.component';
import { DeliveryFeedbackUpdateComponent } from '../update/delivery-feedback-update.component';
import { DeliveryFeedbackRoutingResolveService } from './delivery-feedback-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const deliveryFeedbackRoute: Routes = [
  {
    path: '',
    component: DeliveryFeedbackComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DeliveryFeedbackDetailComponent,
    resolve: {
      deliveryFeedback: DeliveryFeedbackRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DeliveryFeedbackUpdateComponent,
    resolve: {
      deliveryFeedback: DeliveryFeedbackRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DeliveryFeedbackUpdateComponent,
    resolve: {
      deliveryFeedback: DeliveryFeedbackRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(deliveryFeedbackRoute)],
  exports: [RouterModule],
})
export class DeliveryFeedbackRoutingModule {}
