import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FeedbackInvoiceDetailComponent } from './feedback-invoice-detail.component';

describe('FeedbackInvoice Management Detail Component', () => {
  let comp: FeedbackInvoiceDetailComponent;
  let fixture: ComponentFixture<FeedbackInvoiceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FeedbackInvoiceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ feedbackInvoice: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FeedbackInvoiceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FeedbackInvoiceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load feedbackInvoice on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.feedbackInvoice).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
