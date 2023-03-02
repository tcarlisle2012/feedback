import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDeliveryFeedback, NewDeliveryFeedback } from '../delivery-feedback.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDeliveryFeedback for edit and NewDeliveryFeedbackFormGroupInput for create.
 */
type DeliveryFeedbackFormGroupInput = IDeliveryFeedback | PartialWithRequiredKeyOf<NewDeliveryFeedback>;

type DeliveryFeedbackFormDefaults = Pick<NewDeliveryFeedback, 'id'>;

type DeliveryFeedbackFormGroupContent = {
  id: FormControl<IDeliveryFeedback['id'] | NewDeliveryFeedback['id']>;
  contactName: FormControl<IDeliveryFeedback['contactName']>;
  contactEmail: FormControl<IDeliveryFeedback['contactEmail']>;
  driverEmployeeNumber: FormControl<IDeliveryFeedback['driverEmployeeNumber']>;
  feedbackResponse: FormControl<IDeliveryFeedback['feedbackResponse']>;
};

export type DeliveryFeedbackFormGroup = FormGroup<DeliveryFeedbackFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DeliveryFeedbackFormService {
  createDeliveryFeedbackFormGroup(deliveryFeedback: DeliveryFeedbackFormGroupInput = { id: null }): DeliveryFeedbackFormGroup {
    const deliveryFeedbackRawValue = {
      ...this.getFormDefaults(),
      ...deliveryFeedback,
    };
    return new FormGroup<DeliveryFeedbackFormGroupContent>({
      id: new FormControl(
        { value: deliveryFeedbackRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      contactName: new FormControl(deliveryFeedbackRawValue.contactName),
      contactEmail: new FormControl(deliveryFeedbackRawValue.contactEmail),
      driverEmployeeNumber: new FormControl(deliveryFeedbackRawValue.driverEmployeeNumber),
      feedbackResponse: new FormControl(deliveryFeedbackRawValue.feedbackResponse),
    });
  }

  getDeliveryFeedback(form: DeliveryFeedbackFormGroup): IDeliveryFeedback | NewDeliveryFeedback {
    return form.getRawValue() as IDeliveryFeedback | NewDeliveryFeedback;
  }

  resetForm(form: DeliveryFeedbackFormGroup, deliveryFeedback: DeliveryFeedbackFormGroupInput): void {
    const deliveryFeedbackRawValue = { ...this.getFormDefaults(), ...deliveryFeedback };
    form.reset(
      {
        ...deliveryFeedbackRawValue,
        id: { value: deliveryFeedbackRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DeliveryFeedbackFormDefaults {
    return {
      id: null,
    };
  }
}
