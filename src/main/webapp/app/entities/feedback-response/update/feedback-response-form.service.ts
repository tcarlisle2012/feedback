import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFeedbackResponse, NewFeedbackResponse } from '../feedback-response.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFeedbackResponse for edit and NewFeedbackResponseFormGroupInput for create.
 */
type FeedbackResponseFormGroupInput = IFeedbackResponse | PartialWithRequiredKeyOf<NewFeedbackResponse>;

type FeedbackResponseFormDefaults = Pick<NewFeedbackResponse, 'id'>;

type FeedbackResponseFormGroupContent = {
  id: FormControl<IFeedbackResponse['id'] | NewFeedbackResponse['id']>;
  minRating: FormControl<IFeedbackResponse['minRating']>;
  maxRating: FormControl<IFeedbackResponse['maxRating']>;
  rating: FormControl<IFeedbackResponse['rating']>;
  tags: FormControl<IFeedbackResponse['tags']>;
  prompt: FormControl<IFeedbackResponse['prompt']>;
  campaign: FormControl<IFeedbackResponse['campaign']>;
  comment: FormControl<IFeedbackResponse['comment']>;
  customerNumber: FormControl<IFeedbackResponse['customerNumber']>;
  salesOrganization: FormControl<IFeedbackResponse['salesOrganization']>;
  distributionChannel: FormControl<IFeedbackResponse['distributionChannel']>;
  division: FormControl<IFeedbackResponse['division']>;
};

export type FeedbackResponseFormGroup = FormGroup<FeedbackResponseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FeedbackResponseFormService {
  createFeedbackResponseFormGroup(feedbackResponse: FeedbackResponseFormGroupInput = { id: null }): FeedbackResponseFormGroup {
    const feedbackResponseRawValue = {
      ...this.getFormDefaults(),
      ...feedbackResponse,
    };
    return new FormGroup<FeedbackResponseFormGroupContent>({
      id: new FormControl(
        { value: feedbackResponseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      minRating: new FormControl(feedbackResponseRawValue.minRating),
      maxRating: new FormControl(feedbackResponseRawValue.maxRating),
      rating: new FormControl(feedbackResponseRawValue.rating),
      tags: new FormControl(feedbackResponseRawValue.tags),
      prompt: new FormControl(feedbackResponseRawValue.prompt),
      campaign: new FormControl(feedbackResponseRawValue.campaign),
      comment: new FormControl(feedbackResponseRawValue.comment, {
        validators: [Validators.required, Validators.maxLength(500)],
      }),
      customerNumber: new FormControl(feedbackResponseRawValue.customerNumber, {
        validators: [Validators.required, Validators.maxLength(40)],
      }),
      salesOrganization: new FormControl(feedbackResponseRawValue.salesOrganization, {
        validators: [Validators.required, Validators.maxLength(10)],
      }),
      distributionChannel: new FormControl(feedbackResponseRawValue.distributionChannel, {
        validators: [Validators.required, Validators.maxLength(10)],
      }),
      division: new FormControl(feedbackResponseRawValue.division, {
        validators: [Validators.required, Validators.maxLength(10)],
      }),
    });
  }

  getFeedbackResponse(form: FeedbackResponseFormGroup): IFeedbackResponse | NewFeedbackResponse {
    return form.getRawValue() as IFeedbackResponse | NewFeedbackResponse;
  }

  resetForm(form: FeedbackResponseFormGroup, feedbackResponse: FeedbackResponseFormGroupInput): void {
    const feedbackResponseRawValue = { ...this.getFormDefaults(), ...feedbackResponse };
    form.reset(
      {
        ...feedbackResponseRawValue,
        id: { value: feedbackResponseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FeedbackResponseFormDefaults {
    return {
      id: null,
    };
  }
}
