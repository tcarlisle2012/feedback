import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FeedbackInvoiceFormService, FeedbackInvoiceFormGroup } from './feedback-invoice-form.service';
import { IFeedbackInvoice } from '../feedback-invoice.model';
import { FeedbackInvoiceService } from '../service/feedback-invoice.service';
import { IDeliveryFeedback } from 'app/entities/delivery-feedback/delivery-feedback.model';
import { DeliveryFeedbackService } from 'app/entities/delivery-feedback/service/delivery-feedback.service';

@Component({
  selector: 'jhi-feedback-invoice-update',
  templateUrl: './feedback-invoice-update.component.html',
})
export class FeedbackInvoiceUpdateComponent implements OnInit {
  isSaving = false;
  feedbackInvoice: IFeedbackInvoice | null = null;

  deliveryFeedbacksSharedCollection: IDeliveryFeedback[] = [];

  editForm: FeedbackInvoiceFormGroup = this.feedbackInvoiceFormService.createFeedbackInvoiceFormGroup();

  constructor(
    protected feedbackInvoiceService: FeedbackInvoiceService,
    protected feedbackInvoiceFormService: FeedbackInvoiceFormService,
    protected deliveryFeedbackService: DeliveryFeedbackService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDeliveryFeedback = (o1: IDeliveryFeedback | null, o2: IDeliveryFeedback | null): boolean =>
    this.deliveryFeedbackService.compareDeliveryFeedback(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feedbackInvoice }) => {
      this.feedbackInvoice = feedbackInvoice;
      if (feedbackInvoice) {
        this.updateForm(feedbackInvoice);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const feedbackInvoice = this.feedbackInvoiceFormService.getFeedbackInvoice(this.editForm);
    if (feedbackInvoice.id !== null) {
      this.subscribeToSaveResponse(this.feedbackInvoiceService.update(feedbackInvoice));
    } else {
      this.subscribeToSaveResponse(this.feedbackInvoiceService.create(feedbackInvoice));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFeedbackInvoice>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(feedbackInvoice: IFeedbackInvoice): void {
    this.feedbackInvoice = feedbackInvoice;
    this.feedbackInvoiceFormService.resetForm(this.editForm, feedbackInvoice);

    this.deliveryFeedbacksSharedCollection = this.deliveryFeedbackService.addDeliveryFeedbackToCollectionIfMissing<IDeliveryFeedback>(
      this.deliveryFeedbacksSharedCollection,
      feedbackInvoice.deliveryFeedback
    );
  }

  protected loadRelationshipsOptions(): void {
    this.deliveryFeedbackService
      .query()
      .pipe(map((res: HttpResponse<IDeliveryFeedback[]>) => res.body ?? []))
      .pipe(
        map((deliveryFeedbacks: IDeliveryFeedback[]) =>
          this.deliveryFeedbackService.addDeliveryFeedbackToCollectionIfMissing<IDeliveryFeedback>(
            deliveryFeedbacks,
            this.feedbackInvoice?.deliveryFeedback
          )
        )
      )
      .subscribe((deliveryFeedbacks: IDeliveryFeedback[]) => (this.deliveryFeedbacksSharedCollection = deliveryFeedbacks));
  }
}
