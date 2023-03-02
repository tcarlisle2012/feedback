import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FeedbackInvoiceFormService } from './feedback-invoice-form.service';
import { FeedbackInvoiceService } from '../service/feedback-invoice.service';
import { IFeedbackInvoice } from '../feedback-invoice.model';
import { IDeliveryFeedback } from 'app/entities/delivery-feedback/delivery-feedback.model';
import { DeliveryFeedbackService } from 'app/entities/delivery-feedback/service/delivery-feedback.service';

import { FeedbackInvoiceUpdateComponent } from './feedback-invoice-update.component';

describe('FeedbackInvoice Management Update Component', () => {
  let comp: FeedbackInvoiceUpdateComponent;
  let fixture: ComponentFixture<FeedbackInvoiceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let feedbackInvoiceFormService: FeedbackInvoiceFormService;
  let feedbackInvoiceService: FeedbackInvoiceService;
  let deliveryFeedbackService: DeliveryFeedbackService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FeedbackInvoiceUpdateComponent],
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
      .overrideTemplate(FeedbackInvoiceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FeedbackInvoiceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    feedbackInvoiceFormService = TestBed.inject(FeedbackInvoiceFormService);
    feedbackInvoiceService = TestBed.inject(FeedbackInvoiceService);
    deliveryFeedbackService = TestBed.inject(DeliveryFeedbackService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call DeliveryFeedback query and add missing value', () => {
      const feedbackInvoice: IFeedbackInvoice = { id: 456 };
      const deliveryFeedback: IDeliveryFeedback = { id: 56363 };
      feedbackInvoice.deliveryFeedback = deliveryFeedback;

      const deliveryFeedbackCollection: IDeliveryFeedback[] = [{ id: 81107 }];
      jest.spyOn(deliveryFeedbackService, 'query').mockReturnValue(of(new HttpResponse({ body: deliveryFeedbackCollection })));
      const additionalDeliveryFeedbacks = [deliveryFeedback];
      const expectedCollection: IDeliveryFeedback[] = [...additionalDeliveryFeedbacks, ...deliveryFeedbackCollection];
      jest.spyOn(deliveryFeedbackService, 'addDeliveryFeedbackToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ feedbackInvoice });
      comp.ngOnInit();

      expect(deliveryFeedbackService.query).toHaveBeenCalled();
      expect(deliveryFeedbackService.addDeliveryFeedbackToCollectionIfMissing).toHaveBeenCalledWith(
        deliveryFeedbackCollection,
        ...additionalDeliveryFeedbacks.map(expect.objectContaining)
      );
      expect(comp.deliveryFeedbacksSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const feedbackInvoice: IFeedbackInvoice = { id: 456 };
      const deliveryFeedback: IDeliveryFeedback = { id: 45888 };
      feedbackInvoice.deliveryFeedback = deliveryFeedback;

      activatedRoute.data = of({ feedbackInvoice });
      comp.ngOnInit();

      expect(comp.deliveryFeedbacksSharedCollection).toContain(deliveryFeedback);
      expect(comp.feedbackInvoice).toEqual(feedbackInvoice);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackInvoice>>();
      const feedbackInvoice = { id: 123 };
      jest.spyOn(feedbackInvoiceFormService, 'getFeedbackInvoice').mockReturnValue(feedbackInvoice);
      jest.spyOn(feedbackInvoiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackInvoice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedbackInvoice }));
      saveSubject.complete();

      // THEN
      expect(feedbackInvoiceFormService.getFeedbackInvoice).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(feedbackInvoiceService.update).toHaveBeenCalledWith(expect.objectContaining(feedbackInvoice));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackInvoice>>();
      const feedbackInvoice = { id: 123 };
      jest.spyOn(feedbackInvoiceFormService, 'getFeedbackInvoice').mockReturnValue({ id: null });
      jest.spyOn(feedbackInvoiceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackInvoice: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedbackInvoice }));
      saveSubject.complete();

      // THEN
      expect(feedbackInvoiceFormService.getFeedbackInvoice).toHaveBeenCalled();
      expect(feedbackInvoiceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackInvoice>>();
      const feedbackInvoice = { id: 123 };
      jest.spyOn(feedbackInvoiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackInvoice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(feedbackInvoiceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDeliveryFeedback', () => {
      it('Should forward to deliveryFeedbackService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(deliveryFeedbackService, 'compareDeliveryFeedback');
        comp.compareDeliveryFeedback(entity, entity2);
        expect(deliveryFeedbackService.compareDeliveryFeedback).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
