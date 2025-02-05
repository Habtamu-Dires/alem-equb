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

import { cancelInvitation } from '../fn/users/cancel-invitation';
import { CancelInvitation$Params } from '../fn/users/cancel-invitation';
import { createUser } from '../fn/users/create-user';
import { CreateUser$Params } from '../fn/users/create-user';
import { deleteUser } from '../fn/users/delete-user';
import { DeleteUser$Params } from '../fn/users/delete-user';
import { getInvitedUsersInEkubAndNotJoined } from '../fn/users/get-invited-users-in-ekub-and-not-joined';
import { GetInvitedUsersInEkubAndNotJoined$Params } from '../fn/users/get-invited-users-in-ekub-and-not-joined';
import { getPageOfUsers } from '../fn/users/get-page-of-users';
import { GetPageOfUsers$Params } from '../fn/users/get-page-of-users';
import { getUserById } from '../fn/users/get-user-by-id';
import { GetUserById$Params } from '../fn/users/get-user-by-id';
import { IdResponse } from '../models/id-response';
import { inviteUserToEkub } from '../fn/users/invite-user-to-ekub';
import { InviteUserToEkub$Params } from '../fn/users/invite-user-to-ekub';
import { PageResponseUserResponse } from '../models/page-response-user-response';
import { searchUsersToInvite } from '../fn/users/search-users-to-invite';
import { SearchUsersToInvite$Params } from '../fn/users/search-users-to-invite';
import { updatePassword } from '../fn/users/update-password';
import { UpdatePassword$Params } from '../fn/users/update-password';
import { updateProfile } from '../fn/users/update-profile';
import { UpdateProfile$Params } from '../fn/users/update-profile';
import { updateUser } from '../fn/users/update-user';
import { UpdateUser$Params } from '../fn/users/update-user';
import { uploadIdCardImage } from '../fn/users/upload-id-card-image';
import { UploadIdCardImage$Params } from '../fn/users/upload-id-card-image';
import { uploadProfilePicture } from '../fn/users/upload-profile-picture';
import { UploadProfilePicture$Params } from '../fn/users/upload-profile-picture';
import { UserResponse } from '../models/user-response';

@Injectable({ providedIn: 'root' })
export class UsersService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `getPageOfUsers()` */
  static readonly GetPageOfUsersPath = '/users';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getPageOfUsers()` instead.
   *
   * This method doesn't expect any request body.
   */
  getPageOfUsers$Response(params?: GetPageOfUsers$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseUserResponse>> {
    return getPageOfUsers(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getPageOfUsers$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getPageOfUsers(params?: GetPageOfUsers$Params, context?: HttpContext): Observable<PageResponseUserResponse> {
    return this.getPageOfUsers$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseUserResponse>): PageResponseUserResponse => r.body)
    );
  }

  /** Path part for operation `updateUser()` */
  static readonly UpdateUserPath = '/users';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateUser()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateUser$Response(params: UpdateUser$Params, context?: HttpContext): Observable<StrictHttpResponse<IdResponse>> {
    return updateUser(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateUser$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateUser(params: UpdateUser$Params, context?: HttpContext): Observable<IdResponse> {
    return this.updateUser$Response(params, context).pipe(
      map((r: StrictHttpResponse<IdResponse>): IdResponse => r.body)
    );
  }

  /** Path part for operation `createUser()` */
  static readonly CreateUserPath = '/users';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createUser()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  createUser$Response(params?: CreateUser$Params, context?: HttpContext): Observable<StrictHttpResponse<IdResponse>> {
    return createUser(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `createUser$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  createUser(params?: CreateUser$Params, context?: HttpContext): Observable<IdResponse> {
    return this.createUser$Response(params, context).pipe(
      map((r: StrictHttpResponse<IdResponse>): IdResponse => r.body)
    );
  }

  /** Path part for operation `updateProfile()` */
  static readonly UpdateProfilePath = '/users/profile';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateProfile()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateProfile$Response(params: UpdateProfile$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return updateProfile(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateProfile$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateProfile(params: UpdateProfile$Params, context?: HttpContext): Observable<void> {
    return this.updateProfile$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `updatePassword()` */
  static readonly UpdatePasswordPath = '/users/password/{user-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updatePassword()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updatePassword$Response(params: UpdatePassword$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return updatePassword(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updatePassword$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updatePassword(params: UpdatePassword$Params, context?: HttpContext): Observable<void> {
    return this.updatePassword$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `cancelInvitation()` */
  static readonly CancelInvitationPath = '/users/cancel-invitation/{user-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `cancelInvitation()` instead.
   *
   * This method doesn't expect any request body.
   */
  cancelInvitation$Response(params: CancelInvitation$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return cancelInvitation(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `cancelInvitation$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  cancelInvitation(params: CancelInvitation$Params, context?: HttpContext): Observable<void> {
    return this.cancelInvitation$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `uploadProfilePicture()` */
  static readonly UploadProfilePicturePath = '/users/profile-picture';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `uploadProfilePicture()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  uploadProfilePicture$Response(params: UploadProfilePicture$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return uploadProfilePicture(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `uploadProfilePicture$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  uploadProfilePicture(params: UploadProfilePicture$Params, context?: HttpContext): Observable<void> {
    return this.uploadProfilePicture$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `inviteUserToEkub()` */
  static readonly InviteUserToEkubPath = '/users/invite/{user-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `inviteUserToEkub()` instead.
   *
   * This method doesn't expect any request body.
   */
  inviteUserToEkub$Response(params: InviteUserToEkub$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return inviteUserToEkub(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `inviteUserToEkub$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  inviteUserToEkub(params: InviteUserToEkub$Params, context?: HttpContext): Observable<void> {
    return this.inviteUserToEkub$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `uploadIdCardImage()` */
  static readonly UploadIdCardImagePath = '/users/id-card-image';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `uploadIdCardImage()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  uploadIdCardImage$Response(params: UploadIdCardImage$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return uploadIdCardImage(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `uploadIdCardImage$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  uploadIdCardImage(params: UploadIdCardImage$Params, context?: HttpContext): Observable<void> {
    return this.uploadIdCardImage$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `getUserById()` */
  static readonly GetUserByIdPath = '/users/{user-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getUserById()` instead.
   *
   * This method doesn't expect any request body.
   */
  getUserById$Response(params: GetUserById$Params, context?: HttpContext): Observable<StrictHttpResponse<UserResponse>> {
    return getUserById(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getUserById$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getUserById(params: GetUserById$Params, context?: HttpContext): Observable<UserResponse> {
    return this.getUserById$Response(params, context).pipe(
      map((r: StrictHttpResponse<UserResponse>): UserResponse => r.body)
    );
  }

  /** Path part for operation `deleteUser()` */
  static readonly DeleteUserPath = '/users/{user-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `deleteUser()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteUser$Response(params: DeleteUser$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return deleteUser(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `deleteUser$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteUser(params: DeleteUser$Params, context?: HttpContext): Observable<void> {
    return this.deleteUser$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `searchUsersToInvite()` */
  static readonly SearchUsersToInvitePath = '/users/search-to-invite/{ekub-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `searchUsersToInvite()` instead.
   *
   * This method doesn't expect any request body.
   */
  searchUsersToInvite$Response(params: SearchUsersToInvite$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<UserResponse>>> {
    return searchUsersToInvite(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `searchUsersToInvite$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  searchUsersToInvite(params: SearchUsersToInvite$Params, context?: HttpContext): Observable<Array<UserResponse>> {
    return this.searchUsersToInvite$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<UserResponse>>): Array<UserResponse> => r.body)
    );
  }

  /** Path part for operation `getInvitedUsersInEkubAndNotJoined()` */
  static readonly GetInvitedUsersInEkubAndNotJoinedPath = '/users/invited-users/{ekub-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getInvitedUsersInEkubAndNotJoined()` instead.
   *
   * This method doesn't expect any request body.
   */
  getInvitedUsersInEkubAndNotJoined$Response(params: GetInvitedUsersInEkubAndNotJoined$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<UserResponse>>> {
    return getInvitedUsersInEkubAndNotJoined(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getInvitedUsersInEkubAndNotJoined$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getInvitedUsersInEkubAndNotJoined(params: GetInvitedUsersInEkubAndNotJoined$Params, context?: HttpContext): Observable<Array<UserResponse>> {
    return this.getInvitedUsersInEkubAndNotJoined$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<UserResponse>>): Array<UserResponse> => r.body)
    );
  }

}
