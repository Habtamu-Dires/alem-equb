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

import { getEkubRounds } from '../fn/rounds/get-ekub-rounds';
import { GetEkubRounds$Params } from '../fn/rounds/get-ekub-rounds';
import { getLastRound } from '../fn/rounds/get-last-round';
import { GetLastRound$Params } from '../fn/rounds/get-last-round';
import { getUserPendingPayments } from '../fn/rounds/get-user-pending-payments';
import { GetUserPendingPayments$Params } from '../fn/rounds/get-user-pending-payments';
import { LastRoundResponse } from '../models/last-round-response';
import { RoundResponse } from '../models/round-response';
import { UserPendingPaymentResponse } from '../models/user-pending-payment-response';

@Injectable({ providedIn: 'root' })
export class RoundsService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `getEkubRounds()` */
  static readonly GetEkubRoundsPath = '/rounds/{ekub-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getEkubRounds()` instead.
   *
   * This method doesn't expect any request body.
   */
  getEkubRounds$Response(params: GetEkubRounds$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<RoundResponse>>> {
    return getEkubRounds(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getEkubRounds$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getEkubRounds(params: GetEkubRounds$Params, context?: HttpContext): Observable<Array<RoundResponse>> {
    return this.getEkubRounds$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<RoundResponse>>): Array<RoundResponse> => r.body)
    );
  }

  /** Path part for operation `getLastRound()` */
  static readonly GetLastRoundPath = '/rounds/{ekub-id}/{version}/{round-no}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getLastRound()` instead.
   *
   * This method doesn't expect any request body.
   */
  getLastRound$Response(params: GetLastRound$Params, context?: HttpContext): Observable<StrictHttpResponse<LastRoundResponse>> {
    return getLastRound(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getLastRound$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getLastRound(params: GetLastRound$Params, context?: HttpContext): Observable<LastRoundResponse> {
    return this.getLastRound$Response(params, context).pipe(
      map((r: StrictHttpResponse<LastRoundResponse>): LastRoundResponse => r.body)
    );
  }

  /** Path part for operation `getUserPendingPayments()` */
  static readonly GetUserPendingPaymentsPath = '/rounds/user/pending-payments/{user-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getUserPendingPayments()` instead.
   *
   * This method doesn't expect any request body.
   */
  getUserPendingPayments$Response(params: GetUserPendingPayments$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<UserPendingPaymentResponse>>> {
    return getUserPendingPayments(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getUserPendingPayments$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getUserPendingPayments(params: GetUserPendingPayments$Params, context?: HttpContext): Observable<Array<UserPendingPaymentResponse>> {
    return this.getUserPendingPayments$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<UserPendingPaymentResponse>>): Array<UserPendingPaymentResponse> => r.body)
    );
  }

}
