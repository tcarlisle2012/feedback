import { IDeliveryFeedback } from 'app/entities/delivery-feedback/delivery-feedback.model';

export interface IFeedbackInvoice {
  id: number;
  invoiceNumber?: string | null;
  deliveryFeedback?: Pick<IDeliveryFeedback, 'id' | 'contactEmail'> | null;
}

export type NewFeedbackInvoice = Omit<IFeedbackInvoice, 'id'> & { id: null };
