/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { createPayment } from '../fn/payments/create-payment';
import { CreatePayment$Params } from '../fn/payments/create-payment';
import { getPageOfPayments } from '../fn/payments/get-page-of-payments';
import { GetPageOfPayments$Params } from '../fn/payments/get-page-of-payments';
import { getPaymentsOfCurrentRound } from '../fn/payments/get-payments-of-current-round';
import { GetPaymentsOfCurrentRound$Params } from '../fn/payments/get-payments-of-current-round';
import { getPaymentsOfEkubRound } from '../fn/payments/get-payments-of-ekub-round';
import { GetPaymentsOfEkubRound$Params } from '../fn/payments/get-payments-of-ekub-round';
import { getUserPayments } from '../fn/payments/get-user-payments';
import { GetUserPayments$Params } from '../fn/payments/get-user-payments';
import { getUserRoundPayments } from '../fn/payments/get-user-round-payments';
import { GetUserRoundPayments$Params } from '../fn/payments/get-user-round-payments';
import { PageResponsePaymentResponse } from '../models/page-response-payment-response';
import { PaymentResponse } from '../models/payment-response';
import { UserRoundPaymentResponse } from '../models/user-round-payment-response';

@Injectable({ providedIn: 'root' })
export class PaymentsService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `getPageOfPayments()` */
  static readonly GetPageOfPaymentsPath = '/payments';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getPageOfPayments()` instead.
   *
   * This method doesn't expect any request body.
   */
  getPageOfPayments$Response(params?: GetPageOfPayments$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponsePaymentResponse>> {
    return getPageOfPayments(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getPageOfPayments$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getPageOfPayments(params?: GetPageOfPayments$Params, context?: HttpContext): Observable<PageResponsePaymentResponse> {
    return this.getPageOfPayments$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponsePaymentResponse>): PageResponsePaymentResponse => r.body)
    );
  }

  /** Path part for operation `createPayment()` */
  static readonly CreatePaymentPath = '/payments';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createPayment()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createPayment$Response(params: CreatePayment$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return createPayment(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `createPayment$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createPayment(params: CreatePayment$Params, context?: HttpContext): Observable<void> {
    return this.createPayment$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `getPaymentsOfEkubRound()` */
  static readonly GetPaymentsOfEkubRoundPath = '/payments/{ekub-id}/{round-no}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getPaymentsOfEkubRound()` instead.
   *
   * This method doesn't expect any request body.
   */
  getPaymentsOfEkubRound$Response(params?: GetPaymentsOfEkubRound$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<PaymentResponse>>> {
    return getPaymentsOfEkubRound(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getPaymentsOfEkubRound$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getPaymentsOfEkubRound(params?: GetPaymentsOfEkubRound$Params, context?: HttpContext): Observable<Array<PaymentResponse>> {
    return this.getPaymentsOfEkubRound$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<PaymentResponse>>): Array<PaymentResponse> => r.body)
    );
  }

  /** Path part for operation `getUserPayments()` */
  static readonly GetUserPaymentsPath = '/payments/user/{user-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getUserPayments()` instead.
   *
   * This method doesn't expect any request body.
   */
  getUserPayments$Response(params: GetUserPayments$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<PaymentResponse>>> {
    return getUserPayments(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getUserPayments$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getUserPayments(params: GetUserPayments$Params, context?: HttpContext): Observable<Array<PaymentResponse>> {
    return this.getUserPayments$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<PaymentResponse>>): Array<PaymentResponse> => r.body)
    );
  }

  /** Path part for operation `getUserRoundPayments()` */
  static readonly GetUserRoundPaymentsPath = '/payments/user/round/{ekub-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getUserRoundPayments()` instead.
   *
   * This method doesn't expect any request body.
   */
  getUserRoundPayments$Response(params: GetUserRoundPayments$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<UserRoundPaymentResponse>>> {
    return getUserRoundPayments(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getUserRoundPayments$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getUserRoundPayments(params: GetUserRoundPayments$Params, context?: HttpContext): Observable<Array<UserRoundPaymentResponse>> {
    return this.getUserRoundPayments$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<UserRoundPaymentResponse>>): Array<UserRoundPaymentResponse> => r.body)
    );
  }

  /** Path part for operation `getPaymentsOfCurrentRound()` */
  static readonly GetPaymentsOfCurrentRoundPath = '/payments/current-round/{ekub-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getPaymentsOfCurrentRound()` instead.
   *
   * This method doesn't expect any request body.
   */
  getPaymentsOfCurrentRound$Response(params?: GetPaymentsOfCurrentRound$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<PaymentResponse>>> {
    return getPaymentsOfCurrentRound(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getPaymentsOfCurrentRound$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getPaymentsOfCurrentRound(params?: GetPaymentsOfCurrentRound$Params, context?: HttpContext): Observable<Array<PaymentResponse>> {
    return this.getPaymentsOfCurrentRound$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<PaymentResponse>>): Array<PaymentResponse> => r.body)
    );
  }

}
