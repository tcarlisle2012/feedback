import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFeedbackInvoice } from '../feedback-invoice.model';
import { FeedbackInvoiceService } from '../service/feedback-invoice.service';

@Injectable({ providedIn: 'root' })
export class FeedbackInvoiceRoutingResolveService implements Resolve<IFeedbackInvoice | null> {
  constructor(protected service: FeedbackInvoiceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFeedbackInvoice | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((feedbackInvoice: HttpResponse<IFeedbackInvoice>) => {
          if (feedbackInvoice.body) {
            return of(feedbackInvoice.body);
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
