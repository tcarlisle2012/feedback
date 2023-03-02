export interface IFeedbackResponse {
  id: number;
  minRating?: number | null;
  maxRating?: number | null;
  rating?: number | null;
  tags?: string | null;
  prompt?: string | null;
  campaign?: string | null;
  comment?: string | null;
  customerNumber?: string | null;
  salesOrganization?: string | null;
  distributionChannel?: string | null;
  division?: string | null;
}

export type NewFeedbackResponse = Omit<IFeedbackResponse, 'id'> & { id: null };
