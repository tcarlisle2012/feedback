import { IFeedbackResponse, NewFeedbackResponse } from './feedback-response.model';

export const sampleWithRequiredData: IFeedbackResponse = {
  id: 51254,
  comment: 'revolutionary Market',
  customerNumber: 'next-generation Regional',
  salesOrganization: 'calculatin',
  distributionChannel: 'transmit S',
  division: 'content Or',
};

export const sampleWithPartialData: IFeedbackResponse = {
  id: 41690,
  minRating: 20452,
  tags: 'supply-chains',
  comment: 'Designer',
  customerNumber: 'Money system project',
  salesOrganization: 'Account',
  distributionChannel: 'hacking',
  division: 'Soap',
};

export const sampleWithFullData: IFeedbackResponse = {
  id: 37591,
  minRating: 18064,
  maxRating: 87970,
  rating: 17693,
  tags: 'Industrial Grocery',
  prompt: 'Hampshire',
  campaign: 'Neck',
  comment: 'administration',
  customerNumber: 'Home Small engage',
  salesOrganization: 'cutting-ed',
  distributionChannel: 'Profound',
  division: 'Car compre',
};

export const sampleWithNewData: NewFeedbackResponse = {
  comment: 'transmitter Mouse',
  customerNumber: 'parse strategic',
  salesOrganization: 'Card syste',
  distributionChannel: 'Associate',
  division: 'definition',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
