/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { RoundResponse } from '../models/round-response';
import { UserResponse } from '../models/user-response';
export interface PaymentResponse {
  amount?: number;
  createdDate?: string;
  ekubName?: string;
  id?: string;
  paymentMethod?: string;
  remark?: string;
  round?: RoundResponse;
  type?: string;
  user?: UserResponse;
}
