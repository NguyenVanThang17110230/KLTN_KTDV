import { AxiosInstance } from "axios";
import Cookies from "js-cookie";

const AUTHORIZATION_HEADER = 'Authorization';

export const ACCESS_TOKEN_COOKIE = 'jwt';

export default class AccountGateway{
    
    constructor(options) {
        this.restConnector = options.restConnector;
        this.jwt = null;
    }

    async wellcomeLoginUser(){
        try {
          const resp = await this.restConnector.get('/user/welcome');          
          return resp.data;
        } catch (e) {
          return null;
        }
    }

    async loginAdmin({email,password}) {
      const resp = await this.restConnector.post('/user/sign-in',{email,password})
      return resp.data
    }

    async setAccessToken(token){
      if(token){
        this.jwt = token
        Cookies.set(ACCESS_TOKEN_COOKIE, token)
        this.restConnector.defaults.headers[
          AUTHORIZATION_HEADER
        ] = `Bearer ${token}`;
      }else {
        this.jwt = null;
        Cookies.remove(ACCESS_TOKEN_COOKIE);
        delete this.restConnector.defaults.headers[AUTHORIZATION_HEADER];
      }
    }

    async getUserAfterLogin(){
      const resp = await this.restConnector.get('/user/info')
      return resp.data
    }
}