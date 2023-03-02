import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDeliveryFeedback } from '../delivery-feedback.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../delivery-feedback.test-samples';

import { DeliveryFeedbackService } from './delivery-feedback.service';

const requireRestSample: IDeliveryFeedback = {
  ...sampleWithRequiredData,
};

describe('DeliveryFeedback Service', () => {
  let service: DeliveryFeedbackService;
  let httpMock: HttpTestingController;
  let expectedResult: IDeliveryFeedback | IDeliveryFeedback[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DeliveryFeedbackService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a DeliveryFeedback', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const deliveryFeedback = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(deliveryFeedback).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DeliveryFeedback', () => {
      const deliveryFeedback = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(deliveryFeedback).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DeliveryFeedback', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DeliveryFeedback', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DeliveryFeedback', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDeliveryFeedbackToCollectionIfMissing', () => {
      it('should add a DeliveryFeedback to an empty array', () => {
        const deliveryFeedback: IDeliveryFeedback = sampleWithRequiredData;
        expectedResult = service.addDeliveryFeedbackToCollectionIfMissing([], deliveryFeedback);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deliveryFeedback);
      });

      it('should not add a DeliveryFeedback to an array that contains it', () => {
        const deliveryFeedback: IDeliveryFeedback = sampleWithRequiredData;
        const deliveryFeedbackCollection: IDeliveryFeedback[] = [
          {
            ...deliveryFeedback,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDeliveryFeedbackToCollectionIfMissing(deliveryFeedbackCollection, deliveryFeedback);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DeliveryFeedback to an array that doesn't contain it", () => {
        const deliveryFeedback: IDeliveryFeedback = sampleWithRequiredData;
        const deliveryFeedbackCollection: IDeliveryFeedback[] = [sampleWithPartialData];
        expectedResult = service.addDeliveryFeedbackToCollectionIfMissing(deliveryFeedbackCollection, deliveryFeedback);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deliveryFeedback);
      });

      it('should add only unique DeliveryFeedback to an array', () => {
        const deliveryFeedbackArray: IDeliveryFeedback[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const deliveryFeedbackCollection: IDeliveryFeedback[] = [sampleWithRequiredData];
        expectedResult = service.addDeliveryFeedbackToCollectionIfMissing(deliveryFeedbackCollection, ...deliveryFeedbackArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const deliveryFeedback: IDeliveryFeedback = sampleWithRequiredData;
        const deliveryFeedback2: IDeliveryFeedback = sampleWithPartialData;
        expectedResult = service.addDeliveryFeedbackToCollectionIfMissing([], deliveryFeedback, deliveryFeedback2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deliveryFeedback);
        expect(expectedResult).toContain(deliveryFeedback2);
      });

      it('should accept null and undefined values', () => {
        const deliveryFeedback: IDeliveryFeedback = sampleWithRequiredData;
        expectedResult = service.addDeliveryFeedbackToCollectionIfMissing([], null, deliveryFeedback, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deliveryFeedback);
      });

      it('should return initial array if no DeliveryFeedback is added', () => {
        const deliveryFeedbackCollection: IDeliveryFeedback[] = [sampleWithRequiredData];
        expectedResult = service.addDeliveryFeedbackToCollectionIfMissing(deliveryFeedbackCollection, undefined, null);
        expect(expectedResult).toEqual(deliveryFeedbackCollection);
      });
    });

    describe('compareDeliveryFeedback', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDeliveryFeedback(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDeliveryFeedback(entity1, entity2);
        const compareResult2 = service.compareDeliveryFeedback(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDeliveryFeedback(entity1, entity2);
        const compareResult2 = service.compareDeliveryFeedback(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDeliveryFeedback(entity1, entity2);
        const compareResult2 = service.compareDeliveryFeedback(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
