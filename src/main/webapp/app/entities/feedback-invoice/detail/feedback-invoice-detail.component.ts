import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFeedbackInvoice } from '../feedback-invoice.model';

@Component({
  selector: 'jhi-feedback-invoice-detail',
  templateUrl: './feedback-invoice-detail.component.html',
})
export class FeedbackInvoiceDetailComponent implements OnInit {
  feedbackInvoice: IFeedbackInvoice | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feedbackInvoice }) => {
      this.feedbackInvoice = feedbackInvoice;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
