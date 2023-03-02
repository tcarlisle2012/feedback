import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FeedbackResponseDetailComponent } from './feedback-response-detail.component';

describe('FeedbackResponse Management Detail Component', () => {
  let comp: FeedbackResponseDetailComponent;
  let fixture: ComponentFixture<FeedbackResponseDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FeedbackResponseDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ feedbackResponse: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FeedbackResponseDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FeedbackResponseDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load feedbackResponse on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.feedbackResponse).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
