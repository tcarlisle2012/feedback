import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDeliveryFeedback } from '../delivery-feedback.model';
import { DeliveryFeedbackService } from '../service/delivery-feedback.service';

@Injectable({ providedIn: 'root' })
export class DeliveryFeedbackRoutingResolveService implements Resolve<IDeliveryFeedback | null> {
  constructor(protected service: DeliveryFeedbackService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDeliveryFeedback | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((deliveryFeedback: HttpResponse<IDeliveryFeedback>) => {
          if (deliveryFeedback.body) {
            return of(deliveryFeedback.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
