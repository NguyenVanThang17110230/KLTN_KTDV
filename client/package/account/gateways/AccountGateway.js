import { AxiosInstance } from "axios";

export default class AccountGateway{
    
    constructor(options) {
        this.restConnector = options.restConnector;
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
      console.log('username',email);
      console.log('password',password);
      const resp = await this.restConnector.post('/user/sign-in',{email,password})
      return resp.data
    }
}