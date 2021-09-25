import { AxiosInstance } from "axios";
import { Account } from "../model/Account";


export class AccountGateway{
    private restConnector: AxiosInstance;

    constructor(options: {restConnector: AxiosInstance}) {
        this.restConnector = options.restConnector;
    }

    public async getLoginUser(): Promise<Account | null> {
        try {
          const resp = await this.restConnector.get('/user/welcome');          
          return resp.data;
        } catch (e) {
          return null;
        }
    }

    public async loginAdmin(body:{
      username?:string;
      password?:string;
    }): Promise<Account> {
      
      const resp = await this.restConnector.post('/user/sign-in',body)
      return resp.data
    }
}