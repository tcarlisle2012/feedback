import { IDeliveryFeedback, NewDeliveryFeedback } from './delivery-feedback.model';

export const sampleWithRequiredData: IDeliveryFeedback = {
  id: 56386,
};

export const sampleWithPartialData: IDeliveryFeedback = {
  id: 350,
  contactEmail: 'hard',
  driverEmployeeNumber: 'Slovenia',
};

export const sampleWithFullData: IDeliveryFeedback = {
  id: 63390,
  contactName: 'calculating actuating Buckinghamshire',
  contactEmail: 'IB',
  driverEmployeeNumber: 'synthesizing',
};

export const sampleWithNewData: NewDeliveryFeedback = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
