export default class DocumentGateway {
  constructor(options) {
    this.restConnector = options.restConnector;
  }

  async uploadDocument(file) {
    const resp = await this.restConnector.post("/document/upload", file);
    return resp.data;
  }
}
