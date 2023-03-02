import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFeedbackInvoice, NewFeedbackInvoice } from '../feedback-invoice.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFeedbackInvoice for edit and NewFeedbackInvoiceFormGroupInput for create.
 */
type FeedbackInvoiceFormGroupInput = IFeedbackInvoice | PartialWithRequiredKeyOf<NewFeedbackInvoice>;

type FeedbackInvoiceFormDefaults = Pick<NewFeedbackInvoice, 'id'>;

type FeedbackInvoiceFormGroupContent = {
  id: FormControl<IFeedbackInvoice['id'] | NewFeedbackInvoice['id']>;
  invoiceNumber: FormControl<IFeedbackInvoice['invoiceNumber']>;
  deliveryFeedback: FormControl<IFeedbackInvoice['deliveryFeedback']>;
};

export type FeedbackInvoiceFormGroup = FormGroup<FeedbackInvoiceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FeedbackInvoiceFormService {
  createFeedbackInvoiceFormGroup(feedbackInvoice: FeedbackInvoiceFormGroupInput = { id: null }): FeedbackInvoiceFormGroup {
    const feedbackInvoiceRawValue = {
      ...this.getFormDefaults(),
      ...feedbackInvoice,
    };
    return new FormGroup<FeedbackInvoiceFormGroupContent>({
      id: new FormControl(
        { value: feedbackInvoiceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      invoiceNumber: new FormControl(feedbackInvoiceRawValue.invoiceNumber),
      deliveryFeedback: new FormControl(feedbackInvoiceRawValue.deliveryFeedback),
    });
  }

  getFeedbackInvoice(form: FeedbackInvoiceFormGroup): IFeedbackInvoice | NewFeedbackInvoice {
    return form.getRawValue() as IFeedbackInvoice | NewFeedbackInvoice;
  }

  resetForm(form: FeedbackInvoiceFormGroup, feedbackInvoice: FeedbackInvoiceFormGroupInput): void {
    const feedbackInvoiceRawValue = { ...this.getFormDefaults(), ...feedbackInvoice };
    form.reset(
      {
        ...feedbackInvoiceRawValue,
        id: { value: feedbackInvoiceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FeedbackInvoiceFormDefaults {
    return {
      id: null,
    };
  }
}
