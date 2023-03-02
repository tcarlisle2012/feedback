import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { FeedbackResponseFormService, FeedbackResponseFormGroup } from './feedback-response-form.service';
import { IFeedbackResponse } from '../feedback-response.model';
import { FeedbackResponseService } from '../service/feedback-response.service';

@Component({
  selector: 'jhi-feedback-response-update',
  templateUrl: './feedback-response-update.component.html',
})
export class FeedbackResponseUpdateComponent implements OnInit {
  isSaving = false;
  feedbackResponse: IFeedbackResponse | null = null;

  editForm: FeedbackResponseFormGroup = this.feedbackResponseFormService.createFeedbackResponseFormGroup();

  constructor(
    protected feedbackResponseService: FeedbackResponseService,
    protected feedbackResponseFormService: FeedbackResponseFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feedbackResponse }) => {
      this.feedbackResponse = feedbackResponse;
      if (feedbackResponse) {
        this.updateForm(feedbackResponse);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const feedbackResponse = this.feedbackResponseFormService.getFeedbackResponse(this.editForm);
    if (feedbackResponse.id !== null) {
      this.subscribeToSaveResponse(this.feedbackResponseService.update(feedbackResponse));
    } else {
      this.subscribeToSaveResponse(this.feedbackResponseService.create(feedbackResponse));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFeedbackResponse>>): void {
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

  protected updateForm(feedbackResponse: IFeedbackResponse): void {
    this.feedbackResponse = feedbackResponse;
    this.feedbackResponseFormService.resetForm(this.editForm, feedbackResponse);
  }
}
