import { Account } from '../model/Account';
import { AccountGateway } from './../gateways/AccountGateway';


export class AccountService{
  private accountGateway: AccountGateway;

  constructor(options: {accountGateway: AccountGateway}) {
    this.accountGateway = options.accountGateway;
  }
  public async wellcomeLoginUser(): Promise<Account | null> {
    return this.accountGateway.wellcomeLoginUser();
  }

  public async loginAdmin(body:{
    username?: string;
    password?: string;
  }): Promise<Account> {
    return this.accountGateway.loginAdmin(body);
  }
}