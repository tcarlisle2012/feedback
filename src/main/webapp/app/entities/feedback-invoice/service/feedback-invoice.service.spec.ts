import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFeedbackInvoice } from '../feedback-invoice.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../feedback-invoice.test-samples';

import { FeedbackInvoiceService } from './feedback-invoice.service';

const requireRestSample: IFeedbackInvoice = {
  ...sampleWithRequiredData,
};

describe('FeedbackInvoice Service', () => {
  let service: FeedbackInvoiceService;
  let httpMock: HttpTestingController;
  let expectedResult: IFeedbackInvoice | IFeedbackInvoice[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FeedbackInvoiceService);
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

    it('should create a FeedbackInvoice', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const feedbackInvoice = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(feedbackInvoice).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FeedbackInvoice', () => {
      const feedbackInvoice = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(feedbackInvoice).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FeedbackInvoice', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FeedbackInvoice', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FeedbackInvoice', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFeedbackInvoiceToCollectionIfMissing', () => {
      it('should add a FeedbackInvoice to an empty array', () => {
        const feedbackInvoice: IFeedbackInvoice = sampleWithRequiredData;
        expectedResult = service.addFeedbackInvoiceToCollectionIfMissing([], feedbackInvoice);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(feedbackInvoice);
      });

      it('should not add a FeedbackInvoice to an array that contains it', () => {
        const feedbackInvoice: IFeedbackInvoice = sampleWithRequiredData;
        const feedbackInvoiceCollection: IFeedbackInvoice[] = [
          {
            ...feedbackInvoice,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFeedbackInvoiceToCollectionIfMissing(feedbackInvoiceCollection, feedbackInvoice);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FeedbackInvoice to an array that doesn't contain it", () => {
        const feedbackInvoice: IFeedbackInvoice = sampleWithRequiredData;
        const feedbackInvoiceCollection: IFeedbackInvoice[] = [sampleWithPartialData];
        expectedResult = service.addFeedbackInvoiceToCollectionIfMissing(feedbackInvoiceCollection, feedbackInvoice);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(feedbackInvoice);
      });

      it('should add only unique FeedbackInvoice to an array', () => {
        const feedbackInvoiceArray: IFeedbackInvoice[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const feedbackInvoiceCollection: IFeedbackInvoice[] = [sampleWithRequiredData];
        expectedResult = service.addFeedbackInvoiceToCollectionIfMissing(feedbackInvoiceCollection, ...feedbackInvoiceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const feedbackInvoice: IFeedbackInvoice = sampleWithRequiredData;
        const feedbackInvoice2: IFeedbackInvoice = sampleWithPartialData;
        expectedResult = service.addFeedbackInvoiceToCollectionIfMissing([], feedbackInvoice, feedbackInvoice2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(feedbackInvoice);
        expect(expectedResult).toContain(feedbackInvoice2);
      });

      it('should accept null and undefined values', () => {
        const feedbackInvoice: IFeedbackInvoice = sampleWithRequiredData;
        expectedResult = service.addFeedbackInvoiceToCollectionIfMissing([], null, feedbackInvoice, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(feedbackInvoice);
      });

      it('should return initial array if no FeedbackInvoice is added', () => {
        const feedbackInvoiceCollection: IFeedbackInvoice[] = [sampleWithRequiredData];
        expectedResult = service.addFeedbackInvoiceToCollectionIfMissing(feedbackInvoiceCollection, undefined, null);
        expect(expectedResult).toEqual(feedbackInvoiceCollection);
      });
    });

    describe('compareFeedbackInvoice', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFeedbackInvoice(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFeedbackInvoice(entity1, entity2);
        const compareResult2 = service.compareFeedbackInvoice(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFeedbackInvoice(entity1, entity2);
        const compareResult2 = service.compareFeedbackInvoice(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFeedbackInvoice(entity1, entity2);
        const compareResult2 = service.compareFeedbackInvoice(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
