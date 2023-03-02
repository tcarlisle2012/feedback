import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DeliveryFeedbackFormService } from './delivery-feedback-form.service';
import { DeliveryFeedbackService } from '../service/delivery-feedback.service';
import { IDeliveryFeedback } from '../delivery-feedback.model';
import { IFeedbackResponse } from 'app/entities/feedback-response/feedback-response.model';
import { FeedbackResponseService } from 'app/entities/feedback-response/service/feedback-response.service';

import { DeliveryFeedbackUpdateComponent } from './delivery-feedback-update.component';

describe('DeliveryFeedback Management Update Component', () => {
  let comp: DeliveryFeedbackUpdateComponent;
  let fixture: ComponentFixture<DeliveryFeedbackUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deliveryFeedbackFormService: DeliveryFeedbackFormService;
  let deliveryFeedbackService: DeliveryFeedbackService;
  let feedbackResponseService: FeedbackResponseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DeliveryFeedbackUpdateComponent],
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
      .overrideTemplate(DeliveryFeedbackUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeliveryFeedbackUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deliveryFeedbackFormService = TestBed.inject(DeliveryFeedbackFormService);
    deliveryFeedbackService = TestBed.inject(DeliveryFeedbackService);
    feedbackResponseService = TestBed.inject(FeedbackResponseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call feedbackResponse query and add missing value', () => {
      const deliveryFeedback: IDeliveryFeedback = { id: 456 };
      const feedbackResponse: IFeedbackResponse = { id: 88698 };
      deliveryFeedback.feedbackResponse = feedbackResponse;

      const feedbackResponseCollection: IFeedbackResponse[] = [{ id: 14997 }];
      jest.spyOn(feedbackResponseService, 'query').mockReturnValue(of(new HttpResponse({ body: feedbackResponseCollection })));
      const expectedCollection: IFeedbackResponse[] = [feedbackResponse, ...feedbackResponseCollection];
      jest.spyOn(feedbackResponseService, 'addFeedbackResponseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deliveryFeedback });
      comp.ngOnInit();

      expect(feedbackResponseService.query).toHaveBeenCalled();
      expect(feedbackResponseService.addFeedbackResponseToCollectionIfMissing).toHaveBeenCalledWith(
        feedbackResponseCollection,
        feedbackResponse
      );
      expect(comp.feedbackResponsesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const deliveryFeedback: IDeliveryFeedback = { id: 456 };
      const feedbackResponse: IFeedbackResponse = { id: 51387 };
      deliveryFeedback.feedbackResponse = feedbackResponse;

      activatedRoute.data = of({ deliveryFeedback });
      comp.ngOnInit();

      expect(comp.feedbackResponsesCollection).toContain(feedbackResponse);
      expect(comp.deliveryFeedback).toEqual(deliveryFeedback);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeliveryFeedback>>();
      const deliveryFeedback = { id: 123 };
      jest.spyOn(deliveryFeedbackFormService, 'getDeliveryFeedback').mockReturnValue(deliveryFeedback);
      jest.spyOn(deliveryFeedbackService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deliveryFeedback });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deliveryFeedback }));
      saveSubject.complete();

      // THEN
      expect(deliveryFeedbackFormService.getDeliveryFeedback).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(deliveryFeedbackService.update).toHaveBeenCalledWith(expect.objectContaining(deliveryFeedback));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeliveryFeedback>>();
      const deliveryFeedback = { id: 123 };
      jest.spyOn(deliveryFeedbackFormService, 'getDeliveryFeedback').mockReturnValue({ id: null });
      jest.spyOn(deliveryFeedbackService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deliveryFeedback: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deliveryFeedback }));
      saveSubject.complete();

      // THEN
      expect(deliveryFeedbackFormService.getDeliveryFeedback).toHaveBeenCalled();
      expect(deliveryFeedbackService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeliveryFeedback>>();
      const deliveryFeedback = { id: 123 };
      jest.spyOn(deliveryFeedbackService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deliveryFeedback });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deliveryFeedbackService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFeedbackResponse', () => {
      it('Should forward to feedbackResponseService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(feedbackResponseService, 'compareFeedbackResponse');
        comp.compareFeedbackResponse(entity, entity2);
        expect(feedbackResponseService.compareFeedbackResponse).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
