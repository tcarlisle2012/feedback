import { IFeedbackInvoice, NewFeedbackInvoice } from './feedback-invoice.model';

export const sampleWithRequiredData: IFeedbackInvoice = {
  id: 26374,
};

export const sampleWithPartialData: IFeedbackInvoice = {
  id: 32145,
  invoiceNumber: 'Integration Triple-buffered',
};

export const sampleWithFullData: IFeedbackInvoice = {
  id: 57290,
  invoiceNumber: 'compress',
};

export const sampleWithNewData: NewFeedbackInvoice = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
