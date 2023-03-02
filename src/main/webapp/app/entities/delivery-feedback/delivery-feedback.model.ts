import { IFeedbackResponse } from 'app/entities/feedback-response/feedback-response.model';

export interface IDeliveryFeedback {
  id: number;
  contactName?: string | null;
  contactEmail?: string | null;
  driverEmployeeNumber?: string | null;
  feedbackResponse?: Pick<IFeedbackResponse, 'id'> | null;
}

export type NewDeliveryFeedback = Omit<IDeliveryFeedback, 'id'> & { id: null };
