/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { RoundResponse } from '../../models/round-response';

export interface GetRoundByEkubAndRoundNo$Params {
  'ekub-id': string;
  version: number;
  'round-no': number;
}

export function getRoundByEkubAndRoundNo(http: HttpClient, rootUrl: string, params: GetRoundByEkubAndRoundNo$Params, context?: HttpContext): Observable<StrictHttpResponse<RoundResponse>> {
  const rb = new RequestBuilder(rootUrl, getRoundByEkubAndRoundNo.PATH, 'get');
  if (params) {
    rb.path('ekub-id', params['ekub-id'], {});
    rb.path('version', params.version, {});
    rb.path('round-no', params['round-no'], {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<RoundResponse>;
    })
  );
}

getRoundByEkubAndRoundNo.PATH = '/rounds/{ekub-id}/{version}/{round-no}';
