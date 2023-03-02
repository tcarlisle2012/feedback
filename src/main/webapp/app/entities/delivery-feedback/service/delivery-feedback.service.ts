import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDeliveryFeedback, NewDeliveryFeedback } from '../delivery-feedback.model';

export type PartialUpdateDeliveryFeedback = Partial<IDeliveryFeedback> & Pick<IDeliveryFeedback, 'id'>;

export type EntityResponseType = HttpResponse<IDeliveryFeedback>;
export type EntityArrayResponseType = HttpResponse<IDeliveryFeedback[]>;

@Injectable({ providedIn: 'root' })
export class DeliveryFeedbackService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/delivery-feedbacks');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(deliveryFeedback: NewDeliveryFeedback): Observable<EntityResponseType> {
    return this.http.post<IDeliveryFeedback>(this.resourceUrl, deliveryFeedback, { observe: 'response' });
  }

  update(deliveryFeedback: IDeliveryFeedback): Observable<EntityResponseType> {
    return this.http.put<IDeliveryFeedback>(
      `${this.resourceUrl}/${this.getDeliveryFeedbackIdentifier(deliveryFeedback)}`,
      deliveryFeedback,
      { observe: 'response' }
    );
  }

  partialUpdate(deliveryFeedback: PartialUpdateDeliveryFeedback): Observable<EntityResponseType> {
    return this.http.patch<IDeliveryFeedback>(
      `${this.resourceUrl}/${this.getDeliveryFeedbackIdentifier(deliveryFeedback)}`,
      deliveryFeedback,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDeliveryFeedback>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDeliveryFeedback[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDeliveryFeedbackIdentifier(deliveryFeedback: Pick<IDeliveryFeedback, 'id'>): number {
    return deliveryFeedback.id;
  }

  compareDeliveryFeedback(o1: Pick<IDeliveryFeedback, 'id'> | null, o2: Pick<IDeliveryFeedback, 'id'> | null): boolean {
    return o1 && o2 ? this.getDeliveryFeedbackIdentifier(o1) === this.getDeliveryFeedbackIdentifier(o2) : o1 === o2;
  }

  addDeliveryFeedbackToCollectionIfMissing<Type extends Pick<IDeliveryFeedback, 'id'>>(
    deliveryFeedbackCollection: Type[],
    ...deliveryFeedbacksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const deliveryFeedbacks: Type[] = deliveryFeedbacksToCheck.filter(isPresent);
    if (deliveryFeedbacks.length > 0) {
      const deliveryFeedbackCollectionIdentifiers = deliveryFeedbackCollection.map(
        deliveryFeedbackItem => this.getDeliveryFeedbackIdentifier(deliveryFeedbackItem)!
      );
      const deliveryFeedbacksToAdd = deliveryFeedbacks.filter(deliveryFeedbackItem => {
        const deliveryFeedbackIdentifier = this.getDeliveryFeedbackIdentifier(deliveryFeedbackItem);
        if (deliveryFeedbackCollectionIdentifiers.includes(deliveryFeedbackIdentifier)) {
          return false;
        }
        deliveryFeedbackCollectionIdentifiers.push(deliveryFeedbackIdentifier);
        return true;
      });
      return [...deliveryFeedbacksToAdd, ...deliveryFeedbackCollection];
    }
    return deliveryFeedbackCollection;
  }
}
