import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FeedbackResponseFormService } from './feedback-response-form.service';
import { FeedbackResponseService } from '../service/feedback-response.service';
import { IFeedbackResponse } from '../feedback-response.model';

import { FeedbackResponseUpdateComponent } from './feedback-response-update.component';

describe('FeedbackResponse Management Update Component', () => {
  let comp: FeedbackResponseUpdateComponent;
  let fixture: ComponentFixture<FeedbackResponseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let feedbackResponseFormService: FeedbackResponseFormService;
  let feedbackResponseService: FeedbackResponseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FeedbackResponseUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FeedbackResponseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FeedbackResponseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    feedbackResponseFormService = TestBed.inject(FeedbackResponseFormService);
    feedbackResponseService = TestBed.inject(FeedbackResponseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const feedbackResponse: IFeedbackResponse = { id: 456 };

      activatedRoute.data = of({ feedbackResponse });
      comp.ngOnInit();

      expect(comp.feedbackResponse).toEqual(feedbackResponse);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackResponse>>();
      const feedbackResponse = { id: 123 };
      jest.spyOn(feedbackResponseFormService, 'getFeedbackResponse').mockReturnValue(feedbackResponse);
      jest.spyOn(feedbackResponseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackResponse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedbackResponse }));
      saveSubject.complete();

      // THEN
      expect(feedbackResponseFormService.getFeedbackResponse).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(feedbackResponseService.update).toHaveBeenCalledWith(expect.objectContaining(feedbackResponse));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackResponse>>();
      const feedbackResponse = { id: 123 };
      jest.spyOn(feedbackResponseFormService, 'getFeedbackResponse').mockReturnValue({ id: null });
      jest.spyOn(feedbackResponseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackResponse: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedbackResponse }));
      saveSubject.complete();

      // THEN
      expect(feedbackResponseFormService.getFeedbackResponse).toHaveBeenCalled();
      expect(feedbackResponseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackResponse>>();
      const feedbackResponse = { id: 123 };
      jest.spyOn(feedbackResponseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackResponse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(feedbackResponseService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
