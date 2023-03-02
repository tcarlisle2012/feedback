import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DeliveryFeedbackFormService, DeliveryFeedbackFormGroup } from './delivery-feedback-form.service';
import { IDeliveryFeedback } from '../delivery-feedback.model';
import { DeliveryFeedbackService } from '../service/delivery-feedback.service';
import { IFeedbackResponse } from 'app/entities/feedback-response/feedback-response.model';
import { FeedbackResponseService } from 'app/entities/feedback-response/service/feedback-response.service';

@Component({
  selector: 'jhi-delivery-feedback-update',
  templateUrl: './delivery-feedback-update.component.html',
})
export class DeliveryFeedbackUpdateComponent implements OnInit {
  isSaving = false;
  deliveryFeedback: IDeliveryFeedback | null = null;

  feedbackResponsesCollection: IFeedbackResponse[] = [];

  editForm: DeliveryFeedbackFormGroup = this.deliveryFeedbackFormService.createDeliveryFeedbackFormGroup();

  constructor(
    protected deliveryFeedbackService: DeliveryFeedbackService,
    protected deliveryFeedbackFormService: DeliveryFeedbackFormService,
    protected feedbackResponseService: FeedbackResponseService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareFeedbackResponse = (o1: IFeedbackResponse | null, o2: IFeedbackResponse | null): boolean =>
    this.feedbackResponseService.compareFeedbackResponse(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deliveryFeedback }) => {
      this.deliveryFeedback = deliveryFeedback;
      if (deliveryFeedback) {
        this.updateForm(deliveryFeedback);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deliveryFeedback = this.deliveryFeedbackFormService.getDeliveryFeedback(this.editForm);
    if (deliveryFeedback.id !== null) {
      this.subscribeToSaveResponse(this.deliveryFeedbackService.update(deliveryFeedback));
    } else {
      this.subscribeToSaveResponse(this.deliveryFeedbackService.create(deliveryFeedback));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeliveryFeedback>>): void {
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

  protected updateForm(deliveryFeedback: IDeliveryFeedback): void {
    this.deliveryFeedback = deliveryFeedback;
    this.deliveryFeedbackFormService.resetForm(this.editForm, deliveryFeedback);

    this.feedbackResponsesCollection = this.feedbackResponseService.addFeedbackResponseToCollectionIfMissing<IFeedbackResponse>(
      this.feedbackResponsesCollection,
      deliveryFeedback.feedbackResponse
    );
  }

  protected loadRelationshipsOptions(): void {
    this.feedbackResponseService
      .query({ filter: 'deliveryfeedback-is-null' })
      .pipe(map((res: HttpResponse<IFeedbackResponse[]>) => res.body ?? []))
      .pipe(
        map((feedbackResponses: IFeedbackResponse[]) =>
          this.feedbackResponseService.addFeedbackResponseToCollectionIfMissing<IFeedbackResponse>(
            feedbackResponses,
            this.deliveryFeedback?.feedbackResponse
          )
        )
      )
      .subscribe((feedbackResponses: IFeedbackResponse[]) => (this.feedbackResponsesCollection = feedbackResponses));
  }
}
