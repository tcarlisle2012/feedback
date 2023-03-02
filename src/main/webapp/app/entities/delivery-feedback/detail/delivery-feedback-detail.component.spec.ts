import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DeliveryFeedbackDetailComponent } from './delivery-feedback-detail.component';

describe('DeliveryFeedback Management Detail Component', () => {
  let comp: DeliveryFeedbackDetailComponent;
  let fixture: ComponentFixture<DeliveryFeedbackDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DeliveryFeedbackDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ deliveryFeedback: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DeliveryFeedbackDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DeliveryFeedbackDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load deliveryFeedback on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.deliveryFeedback).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
