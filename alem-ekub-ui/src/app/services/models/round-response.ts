/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { UserGuaranteeResponse } from '../models/user-guarantee-response';
import { UserResponse } from '../models/user-response';
export interface RoundResponse {
  createdDate?: string;
  ekubName?: string;
  endDate?: string;
  id?: string;
  paid?: boolean;
  roundNumber?: number;
  totalAmount?: number;
  userGuarantees?: Array<UserGuaranteeResponse>;
  version?: number;
  winner?: UserResponse;
}
