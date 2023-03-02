import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeliveryFeedback } from '../delivery-feedback.model';

@Component({
  selector: 'jhi-delivery-feedback-detail',
  templateUrl: './delivery-feedback-detail.component.html',
})
export class DeliveryFeedbackDetailComponent implements OnInit {
  deliveryFeedback: IDeliveryFeedback | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deliveryFeedback }) => {
      this.deliveryFeedback = deliveryFeedback;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
