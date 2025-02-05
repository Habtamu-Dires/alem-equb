/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';


export interface InviteUserToEkub$Params {
  'user-id': string;
  'ekub-id': string;
}

export function inviteUserToEkub(http: HttpClient, rootUrl: string, params: InviteUserToEkub$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
  const rb = new RequestBuilder(rootUrl, inviteUserToEkub.PATH, 'post');
  if (params) {
    rb.path('user-id', params['user-id'], {});
    rb.query('ekub-id', params['ekub-id'], {});
  }

  return http.request(
    rb.build({ responseType: 'text', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return (r as HttpResponse<any>).clone({ body: undefined }) as StrictHttpResponse<void>;
    })
  );
}

inviteUserToEkub.PATH = '/users/invite/{user-id}';
