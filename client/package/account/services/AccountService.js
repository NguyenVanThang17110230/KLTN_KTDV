export default class AccountService{

  constructor(options) {
    this.accountGateway = options.accountGateway;
  }

  async wellcomeLoginUser() {
    return this.accountGateway.wellcomeLoginUser();
  }

  async loginAdmin({email,password}) {
    return this.accountGateway.loginAdmin({email,password});
  }
}