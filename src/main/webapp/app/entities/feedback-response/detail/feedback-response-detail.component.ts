import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFeedbackResponse } from '../feedback-response.model';

@Component({
  selector: 'jhi-feedback-response-detail',
  templateUrl: './feedback-response-detail.component.html',
})
export class FeedbackResponseDetailComponent implements OnInit {
  feedbackResponse: IFeedbackResponse | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feedbackResponse }) => {
      this.feedbackResponse = feedbackResponse;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
