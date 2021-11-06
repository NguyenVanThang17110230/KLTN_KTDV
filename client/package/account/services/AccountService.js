export default class AccountService{

  constructor(options) {
    this.accountGateway = options.accountGateway;
  }

  async wellcomeLoginUser() {
    return this.accountGateway.wellcomeLoginUser();
  }

  async loginAdmin({email,password}) {
    const token = await this.accountGateway.loginAdmin({email,password});
    this.accountGateway.setAccessToken(token.data.jwt);
    return this.accountGateway.getUserAfterLogin();
  }
  async signUpUser(values) {
    return this.accountGateway.signUpUser(values);
  }
}