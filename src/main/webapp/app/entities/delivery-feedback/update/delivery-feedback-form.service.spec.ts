import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../delivery-feedback.test-samples';

import { DeliveryFeedbackFormService } from './delivery-feedback-form.service';

describe('DeliveryFeedback Form Service', () => {
  let service: DeliveryFeedbackFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DeliveryFeedbackFormService);
  });

  describe('Service methods', () => {
    describe('createDeliveryFeedbackFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDeliveryFeedbackFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            contactName: expect.any(Object),
            contactEmail: expect.any(Object),
            driverEmployeeNumber: expect.any(Object),
            feedbackResponse: expect.any(Object),
          })
        );
      });

      it('passing IDeliveryFeedback should create a new form with FormGroup', () => {
        const formGroup = service.createDeliveryFeedbackFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            contactName: expect.any(Object),
            contactEmail: expect.any(Object),
            driverEmployeeNumber: expect.any(Object),
            feedbackResponse: expect.any(Object),
          })
        );
      });
    });

    describe('getDeliveryFeedback', () => {
      it('should return NewDeliveryFeedback for default DeliveryFeedback initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDeliveryFeedbackFormGroup(sampleWithNewData);

        const deliveryFeedback = service.getDeliveryFeedback(formGroup) as any;

        expect(deliveryFeedback).toMatchObject(sampleWithNewData);
      });

      it('should return NewDeliveryFeedback for empty DeliveryFeedback initial value', () => {
        const formGroup = service.createDeliveryFeedbackFormGroup();

        const deliveryFeedback = service.getDeliveryFeedback(formGroup) as any;

        expect(deliveryFeedback).toMatchObject({});
      });

      it('should return IDeliveryFeedback', () => {
        const formGroup = service.createDeliveryFeedbackFormGroup(sampleWithRequiredData);

        const deliveryFeedback = service.getDeliveryFeedback(formGroup) as any;

        expect(deliveryFeedback).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDeliveryFeedback should not enable id FormControl', () => {
        const formGroup = service.createDeliveryFeedbackFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDeliveryFeedback should disable id FormControl', () => {
        const formGroup = service.createDeliveryFeedbackFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
