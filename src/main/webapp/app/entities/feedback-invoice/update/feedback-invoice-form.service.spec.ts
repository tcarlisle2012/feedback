import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../feedback-invoice.test-samples';

import { FeedbackInvoiceFormService } from './feedback-invoice-form.service';

describe('FeedbackInvoice Form Service', () => {
  let service: FeedbackInvoiceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FeedbackInvoiceFormService);
  });

  describe('Service methods', () => {
    describe('createFeedbackInvoiceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFeedbackInvoiceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            invoiceNumber: expect.any(Object),
            deliveryFeedback: expect.any(Object),
          })
        );
      });

      it('passing IFeedbackInvoice should create a new form with FormGroup', () => {
        const formGroup = service.createFeedbackInvoiceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            invoiceNumber: expect.any(Object),
            deliveryFeedback: expect.any(Object),
          })
        );
      });
    });

    describe('getFeedbackInvoice', () => {
      it('should return NewFeedbackInvoice for default FeedbackInvoice initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFeedbackInvoiceFormGroup(sampleWithNewData);

        const feedbackInvoice = service.getFeedbackInvoice(formGroup) as any;

        expect(feedbackInvoice).toMatchObject(sampleWithNewData);
      });

      it('should return NewFeedbackInvoice for empty FeedbackInvoice initial value', () => {
        const formGroup = service.createFeedbackInvoiceFormGroup();

        const feedbackInvoice = service.getFeedbackInvoice(formGroup) as any;

        expect(feedbackInvoice).toMatchObject({});
      });

      it('should return IFeedbackInvoice', () => {
        const formGroup = service.createFeedbackInvoiceFormGroup(sampleWithRequiredData);

        const feedbackInvoice = service.getFeedbackInvoice(formGroup) as any;

        expect(feedbackInvoice).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFeedbackInvoice should not enable id FormControl', () => {
        const formGroup = service.createFeedbackInvoiceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFeedbackInvoice should disable id FormControl', () => {
        const formGroup = service.createFeedbackInvoiceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
