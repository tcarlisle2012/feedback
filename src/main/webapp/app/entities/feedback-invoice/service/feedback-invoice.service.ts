import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFeedbackInvoice, NewFeedbackInvoice } from '../feedback-invoice.model';

export type PartialUpdateFeedbackInvoice = Partial<IFeedbackInvoice> & Pick<IFeedbackInvoice, 'id'>;

export type EntityResponseType = HttpResponse<IFeedbackInvoice>;
export type EntityArrayResponseType = HttpResponse<IFeedbackInvoice[]>;

@Injectable({ providedIn: 'root' })
export class FeedbackInvoiceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/feedback-invoices');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(feedbackInvoice: NewFeedbackInvoice): Observable<EntityResponseType> {
    return this.http.post<IFeedbackInvoice>(this.resourceUrl, feedbackInvoice, { observe: 'response' });
  }

  update(feedbackInvoice: IFeedbackInvoice): Observable<EntityResponseType> {
    return this.http.put<IFeedbackInvoice>(`${this.resourceUrl}/${this.getFeedbackInvoiceIdentifier(feedbackInvoice)}`, feedbackInvoice, {
      observe: 'response',
    });
  }

  partialUpdate(feedbackInvoice: PartialUpdateFeedbackInvoice): Observable<EntityResponseType> {
    return this.http.patch<IFeedbackInvoice>(`${this.resourceUrl}/${this.getFeedbackInvoiceIdentifier(feedbackInvoice)}`, feedbackInvoice, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFeedbackInvoice>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFeedbackInvoice[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFeedbackInvoiceIdentifier(feedbackInvoice: Pick<IFeedbackInvoice, 'id'>): number {
    return feedbackInvoice.id;
  }

  compareFeedbackInvoice(o1: Pick<IFeedbackInvoice, 'id'> | null, o2: Pick<IFeedbackInvoice, 'id'> | null): boolean {
    return o1 && o2 ? this.getFeedbackInvoiceIdentifier(o1) === this.getFeedbackInvoiceIdentifier(o2) : o1 === o2;
  }

  addFeedbackInvoiceToCollectionIfMissing<Type extends Pick<IFeedbackInvoice, 'id'>>(
    feedbackInvoiceCollection: Type[],
    ...feedbackInvoicesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const feedbackInvoices: Type[] = feedbackInvoicesToCheck.filter(isPresent);
    if (feedbackInvoices.length > 0) {
      const feedbackInvoiceCollectionIdentifiers = feedbackInvoiceCollection.map(
        feedbackInvoiceItem => this.getFeedbackInvoiceIdentifier(feedbackInvoiceItem)!
      );
      const feedbackInvoicesToAdd = feedbackInvoices.filter(feedbackInvoiceItem => {
        const feedbackInvoiceIdentifier = this.getFeedbackInvoiceIdentifier(feedbackInvoiceItem);
        if (feedbackInvoiceCollectionIdentifiers.includes(feedbackInvoiceIdentifier)) {
          return false;
        }
        feedbackInvoiceCollectionIdentifiers.push(feedbackInvoiceIdentifier);
        return true;
      });
      return [...feedbackInvoicesToAdd, ...feedbackInvoiceCollection];
    }
    return feedbackInvoiceCollection;
  }
}
